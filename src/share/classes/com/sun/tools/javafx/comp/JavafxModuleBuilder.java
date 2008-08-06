/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.comp;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.TypeTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import static com.sun.tools.javac.code.Flags.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javafx.tree.JavafxTag.*;
import static com.sun.tools.javafx.code.JavafxFlags.SCRIPT_LEVEL_SYNTH_STATIC;
import com.sun.tools.javafx.util.MsgSym;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.tools.FileObject;

public class JavafxModuleBuilder {
    protected static final Context.Key<JavafxModuleBuilder> javafxModuleBuilderKey =
        new Context.Key<JavafxModuleBuilder>();

    private final JavafxDefs defs;
    private Table names;
    private JavafxTreeMaker fxmake;
    private Log log;
    private JavafxSymtab syms;
    private Set<Name> reservedTopLevelNamesSet;
    private Name pseudoFile;
    private Name pseudoDir;
    private Name commandLineArgs;
    
    private static final boolean debugBadPositions = Boolean.getBoolean("JavafxModuleBuilder.debugBadPositions");

    public static JavafxModuleBuilder instance(Context context) {
        JavafxModuleBuilder instance = context.get(javafxModuleBuilderKey);
        if (instance == null)
            instance = new JavafxModuleBuilder(context);
        return instance;
    }

    protected JavafxModuleBuilder(Context context) {
        defs = JavafxDefs.instance(context);
        names = Table.instance(context);
        fxmake = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        pseudoFile = names.fromString("__FILE__");
        pseudoDir = names.fromString("__DIR__");
        commandLineArgs = names.fromString("__ARGS__");
    }

    public void preProcessJfxTopLevel(JFXUnit module) {
        Name moduleClassName = moduleName(module);
        
        if (debugBadPositions) {
            checkForBadPositions(module);
        }

        ListBuffer<JFXTree> scriptClassDefs = new ListBuffer<JFXTree>();
        ListBuffer<JFXExpression> stats = new ListBuffer<JFXExpression>();

        // check for references to pseudo variables and if found, declare them
        final boolean[] usesFile = new boolean[1];
        final boolean[] usesDir = new boolean[1];
        final DiagnosticPosition[] diagPos = new DiagnosticPosition[1];
        new JavafxTreeScanner() {
            @Override
            public void visitIdent(JFXIdent id) {
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
            void markPosition(JFXTree tree) {
                if (diagPos[0] == null) { // want the first only
                    diagPos[0] = tree.pos();
                }
            }
        }.scan(module.defs);
        //debugPositions(module);
        addPseudoVariables(diagPos[0], moduleClassName, module, scriptClassDefs, usesFile[0], usesDir[0]);

        // Divide module defs between run method body, Java compilation unit, and module class
        ListBuffer<JFXTree> topLevelDefs = new ListBuffer<JFXTree>();
        JFXClassDeclaration moduleClass = null;
        JFXExpression value = null;
        for (JFXTree tree : module.defs) {
            if (value != null) {
                stats.append(value);
                value = null;
            }
            switch (tree.getFXTag()) {
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
                    decl.mods.flags |= STATIC | SCRIPT_LEVEL_SYNTH_STATIC;
                    scriptClassDefs.append(tree);
                }
                break;
            }
            case FUNCTION_DEF: {
                JFXFunctionDefinition decl =
                    (JFXFunctionDefinition)tree;
                decl.mods.flags |= STATIC | SCRIPT_LEVEL_SYNTH_STATIC;
                Name name = decl.name;
                checkName(tree.pos, name);
                scriptClassDefs.append(tree);
                //stats.append(decl);
                break;
            }
            case VAR_DEF: { //TODO: deal with var value
                JFXVar decl = (JFXVar) tree;
                checkName(tree.pos, decl.getName());
                if ((decl.getModifiers().flags & (Flags.PUBLIC | Flags.PRIVATE | JavafxFlags.PUBLIC_READABLE)) != 0L) {
                    // externally visible, so needs to be a static on the script class
                    // we can't handle this in the lazy conversion since attribution will barf on a var with these flags
                    // note that protected is an error, but we will let attribution handle that
                    decl.mods.flags |= STATIC | SCRIPT_LEVEL_SYNTH_STATIC;
                    scriptClassDefs.append(tree);
                } else {
                    // otherwise lazily see is it can be local to the run method
                    stats.append(decl);
                }
                break;
            }
            default:
                value = (JFXExpression) tree;
                break;
            }
        }
                
        // Add run() method... If the class can be a module class.
        JFXFunctionDefinition runMethod = makeRunFunction(stats.toList(), value);
        scriptClassDefs.prepend(runMethod);

        if (moduleClass == null) {
            moduleClass =  fxmake.ClassDeclaration(
                fxmake.Modifiers(PUBLIC),   //public access needed for applet initialization 
                moduleClassName, 
                List.<JFXExpression>nil(),             // no supertypes
                scriptClassDefs.toList());
        } else {
            moduleClass.setMembers(scriptClassDefs.appendList(moduleClass.getMembers()).toList());
        }
        moduleClass.isModuleClass = true;
        moduleClass.runMethod = runMethod;

        topLevelDefs.append(moduleClass);
        
        module.defs = topLevelDefs.toList();

        reservedTopLevelNamesSet = null;
    }
    
    private void debugPositions(final JFXUnit module) {
        new JavafxTreeScanner() {

            @Override
            public void scan(JFXTree tree) {
                super.scan(tree);
                if (tree != null) {
                    System.out.println("[" + tree.getStartPosition() + "," + tree.getEndPosition(module.endPositions) + "]  " + tree.toString());
                }
            }
        }.scan(module);

    }
    
    private void addPseudoVariables(DiagnosticPosition diagPos, Name moduleClassName, JFXUnit module,
            ListBuffer<JFXTree> stats, boolean usesFile, boolean usesDir) {
        if (usesFile || usesDir) {
            // java.net.URL __FILE__ = Util.get__FILE__(moduleClass);
            JFXExpression moduleClassFQN = module.pid != null ?
                fxmake.at(diagPos).Select(module.pid, moduleClassName) : fxmake.at(diagPos).Ident(moduleClassName);
            JFXExpression getFile = fxmake.at(diagPos).Identifier("com.sun.javafx.runtime.Util.get__FILE__");
            JFXExpression forName = fxmake.at(diagPos).Identifier("java.lang.Class.forName");
            List<JFXExpression> args = List.<JFXExpression>of(fxmake.at(diagPos).Literal(moduleClassFQN.toString()));
            JFXExpression loaderCall = fxmake.at(diagPos).Apply(List.<JFXExpression>nil(), forName, args);
            args = List.<JFXExpression>of(loaderCall);
            JFXExpression getFileURL = fxmake.at(diagPos).Apply(List.<JFXExpression>nil(), getFile, args);
            JFXExpression fileVar =
                fxmake.at(diagPos).Var(pseudoFile, getURLType(diagPos), 
                         fxmake.at(diagPos).Modifiers(FINAL|STATIC|SCRIPT_LEVEL_SYNTH_STATIC),
                         false, getFileURL, JavafxBindStatus.UNBOUND, null);

            // java.net.URL __DIR__;
            if (usesDir) {
                JFXExpression getDir = fxmake.at(diagPos).Identifier("com.sun.javafx.runtime.Util.get__DIR__");
                args = List.<JFXExpression>of(fxmake.at(diagPos).Ident(pseudoFile));
                JFXExpression getDirURL = fxmake.at(diagPos).Apply(List.<JFXExpression>nil(), getDir, args);
                stats.prepend(
                    fxmake.at(diagPos).Var(pseudoDir, getURLType(diagPos), 
                             fxmake.at(diagPos).Modifiers(FINAL|STATIC|SCRIPT_LEVEL_SYNTH_STATIC),
                             false, getDirURL, JavafxBindStatus.UNBOUND, null));
            }

            stats.prepend(fileVar);  // must come before __DIR__ call
        }
    }
    
    private JFXType getURLType(DiagnosticPosition diagPos) {
        JFXExpression urlFQN = fxmake.at(diagPos).Identifier("java.net.URL");
        return fxmake.at(diagPos).TypeClass(urlFQN, TypeTree.Cardinality.SINGLETON);
    }

    private JFXFunctionDefinition makeRunFunction(List<JFXExpression> stats, JFXExpression value) {
        JFXVar mainArgs = fxmake.Param(commandLineArgs, 
                fxmake.TypeClass(fxmake.Ident(Name.fromString(names, "String")), TypeTree.Cardinality.ANY));
        List<JFXVar> argsVarList = List.<JFXVar>of(mainArgs);
        JFXBlockExpression body = fxmake.BlockExpression(0, stats, value);
        JFXExpression rettree = fxmake.Type(syms.objectType);
        rettree.type = syms.objectType;
        return fxmake.FunctionDefinition(
                fxmake.Modifiers(PUBLIC | STATIC | SCRIPT_LEVEL_SYNTH_STATIC | SYNTHETIC),
                defs.runMethodName,
                fxmake.TypeClass(rettree, JFXType.Cardinality.SINGLETON),
                argsVarList,
                body);        
    }
    
    private Name moduleName(JFXUnit tree) {
        String fileObjName = null;

        FileObject fo = tree.getSourceFile();
        URI uri = fo.toUri();
        String path = uri.toString();
        int i = path.lastIndexOf('/') + 1;
        fileObjName = path.substring(i);
        int lastDotIdx = fileObjName.lastIndexOf('.');
        if (lastDotIdx != -1) {
            fileObjName = fileObjName.substring(0, lastDotIdx);
        }

        return Name.fromString(names, fileObjName);
    }

    private void checkName(int pos, Name name) {
        if (reservedTopLevelNamesSet == null) {
            reservedTopLevelNamesSet = new HashSet<Name>();
            
            // make sure no one tries to declare these reserved names
            reservedTopLevelNamesSet.add(pseudoFile);
            reservedTopLevelNamesSet.add(pseudoDir);
            reservedTopLevelNamesSet.add(commandLineArgs);
        }
        
        if (reservedTopLevelNamesSet.contains(name)) {
            log.error(pos, MsgSym.MESSAGE_JAVAFX_RESERVED_TOP_LEVEL_SCRIPT_MEMBER, name.toString());
        }
    }
    
    private void checkForBadPositions(JFXUnit testTree) {
        final Map<JCTree, Integer> endPositions = testTree.endPositions;  
        new JavafxTreeScanner() {

            @Override
            public void scan(JFXTree tree) {
                super.scan(tree);
                
                // A Modifiers instance with no modifier tokens and no annotations
                // is defined as having no position.
                if (tree instanceof JFXModifiers) {
                    JFXModifiers mods = (JFXModifiers)tree;
                    if (mods.getFlags().isEmpty() || 
                        (mods.flags & Flags.SYNTHETIC) != 0)
                        return;
                }
                
                // TypeUnknown trees have no associated tokens.
                if (tree instanceof JFXTypeUnknown)
                    return; 
                
                if (tree != null) {
                    if (tree.pos <= 0) {
                        String where = tree.getClass().getSimpleName();
                        try {
                            where = where + ": " + tree.toString();
                        } catch (Throwable exc) {
                            //ignore
                        }
                        System.err.println("Position of " +
                                           tree.pos +
                                           " in ---" +
                                           where);
                    }
                    if (tree.getEndPosition(endPositions) <= 0) {
                        String where = tree.getClass().getSimpleName();
                        try {
                            where = where + ": " + tree.toString();
                        } catch (Throwable exc) {
                            //ignore
                        }
                        System.err.println("End position of " +
                                           tree.getEndPosition(endPositions) +
                                           " in ---" +
                                           where);
                    }
                }
            }
        }.scan(testTree);
    }

}
