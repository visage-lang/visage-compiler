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

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.tools.FileObject;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.SyntheticTree.SynthType;
import com.sun.javafx.api.tree.TypeTree;
import com.sun.javafx.api.tree.TypeTree.Cardinality;
import com.sun.tools.javac.code.Flags;
import static com.sun.tools.javac.code.Flags.*;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javafx.code.JavafxFlags;
import static com.sun.tools.javafx.code.JavafxFlags.SCRIPT_LEVEL_SYNTH_STATIC;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.util.MsgSym;

public class JavafxScriptClassBuilder {
    protected static final Context.Key<JavafxScriptClassBuilder> javafxModuleBuilderKey =
        new Context.Key<JavafxScriptClassBuilder>();

    private final JavafxDefs defs;
    private Table names;
    private JavafxTreeMaker fxmake;
    private Log log;
    private JavafxSymtab syms;
    private Set<Name> reservedTopLevelNamesSet;
    private Name pseudoFile;
    private Name pseudoDir;
    private Name defaultRunArgName;
    
    private static final boolean debugBadPositions = Boolean.getBoolean("JavafxModuleBuilder.debugBadPositions");

    public static JavafxScriptClassBuilder instance(Context context) {
        JavafxScriptClassBuilder instance = context.get(javafxModuleBuilderKey);
        if (instance == null)
            instance = new JavafxScriptClassBuilder(context);
        return instance;
    }

    protected JavafxScriptClassBuilder(Context context) {
        defs = JavafxDefs.instance(context);
        names = Table.instance(context);
        fxmake = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        pseudoFile = names.fromString("__FILE__");
        pseudoDir = names.fromString("__DIR__");
        defaultRunArgName = names.fromString("<ARGS>");
    }

    public void convertAccessFlags(JFXScript script) {
        new JavafxTreeScanner() {
            
            void convertFlags(JFXModifiers mods) {
                long flags = mods.flags;
                long access = flags & (Flags.AccessFlags | JavafxFlags.PACKAGE_ACCESS);
                if (access == 0L) {
                    flags |= JavafxFlags.SCRIPT_PRIVATE;
                }
                mods.flags = flags;
            }

            @Override
            public void visitClassDeclaration(JFXClassDeclaration tree) {
                super.visitClassDeclaration(tree);
                convertFlags(tree.getModifiers());
            }

            @Override
            public void visitFunctionDefinition(JFXFunctionDefinition tree) {
                super.visitFunctionDefinition(tree);
                convertFlags(tree.getModifiers());
            }

            @Override
            public void visitVar(JFXVar tree) {
                super.visitVar(tree);
                convertFlags(tree.getModifiers());
            }
        }.scan(script);
    }
    
    private void checkAndNormalizeUserRunFunction(JFXFunctionDefinition runFunc) {
        JFXFunctionValue fval = runFunc.operation;
        List<JFXVar> params = fval.funParams;
        switch (params.size()) {
            case 0: {
                // no parameter specified, fill it in
                fval.funParams = makeRunFunctionArgs(defaultRunArgName);
                break;
            }
            case 1: {
                JFXType paramType = params.head.getJFXType();
                if (paramType.getCardinality() == Cardinality.ANY &&
                        paramType instanceof JFXTypeClass) {
                    JFXExpression cnExp = ((JFXTypeClass) paramType).getClassName();
                    if (cnExp instanceof JFXIdent) {
                        Name cName = ((JFXIdent)cnExp).getName();
                        if (cName == syms.stringTypeName) {
                            break;
                        }
                    }
                }
                // not well-formed, fall-through
            }
            default: {
                // bad arguments
                log.error(runFunc.pos(), MsgSym.MESSAGE_JAVAFX_RUN_FUNCTION_PARAM);
                fval.funParams = makeRunFunctionArgs(defaultRunArgName);
            }
        }
        
        //TODO: check specified return type
        
        // set return type
        fval.rettype = makeRunFunctionType();
    }


    public void preProcessJfxTopLevel(JFXScript module) {
        Name moduleClassName = scriptName(module);
        
        if (debugBadPositions) {
            checkForBadPositions(module);
        }

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

        ListBuffer<JFXTree> scriptTops = ListBuffer.<JFXTree>lb();
        scriptTops.appendList( pseudoVariables(diagPos[0], moduleClassName, module, usesFile[0], usesDir[0]) );
        scriptTops.appendList(module.defs);

        // Determine if this is a library script
        boolean externalAccessFound = false;
        JFXFunctionDefinition userRunFunction = null;
        final long EXTERNALIZING_FLAGS = Flags.PUBLIC | Flags.PROTECTED | JavafxFlags.PACKAGE_ACCESS | JavafxFlags.PUBLIC_READ | JavafxFlags.PUBLIC_INIT;
        for (JFXTree tree : scriptTops) {
            switch (tree.getFXTag()) {
                case CLASS_DEF: {
                    JFXClassDeclaration decl = (JFXClassDeclaration) tree;
                    if ((decl.getModifiers().flags & EXTERNALIZING_FLAGS) != 0) {
                        externalAccessFound = true;
                    }
                    break;
                }
                case FUNCTION_DEF: {
                    JFXFunctionDefinition decl =
                            (JFXFunctionDefinition) tree;
                    Name name = decl.name;
                    if (name == defs.userRunFunctionName) {
                        if (userRunFunction == null) {
                            checkAndNormalizeUserRunFunction(decl);
                            userRunFunction = decl;
                        } else {
                            log.error(decl.pos(), MsgSym.MESSAGE_JAVAFX_RUN_FUNCTION_SINGLE);
                        }
                    }
                    if ((decl.getModifiers().flags & EXTERNALIZING_FLAGS) != 0) {
                        externalAccessFound = true;
                    }
                    break;
                }
                case VAR_DEF: { 
                    JFXVar decl = (JFXVar) tree;
                    if ((decl.getModifiers().flags & EXTERNALIZING_FLAGS) != 0) {
                        externalAccessFound = true;
                    }
                    break;
                }
            }
        }
        final boolean isLibrary = externalAccessFound || (userRunFunction != null);
        module.isLibrary = isLibrary;
        ListBuffer<JFXTree> scriptClassDefs = new ListBuffer<JFXTree>();
        ListBuffer<JFXExpression> stats = new ListBuffer<JFXExpression>();
        JFXExpression value = null;
       
        // Divide module defs between internsl run function body, Java compilation unit, and module class
        ListBuffer<JFXTree> topLevelDefs = new ListBuffer<JFXTree>();
        JFXClassDeclaration moduleClass = null;
        boolean looseExpressionErrorShown = false;
        for (JFXTree tree : scriptTops) {
            if (value != null) {
                stats.append(value);
                value = null;
            }
            switch (tree.getFXTag()) {
                case IMPORT:
                    topLevelDefs.append(tree);
                    break;
                case CLASS_DEF: {
                    JFXClassDeclaration decl = (JFXClassDeclaration) tree;
                    Name name = decl.getName();
                    checkName(tree.pos, name);
                    if (name == moduleClassName) {
                        moduleClass = decl;
                        // script-class added to topLevelDefs below
                    } else {
                        // classes other than the script-class become nested static classes
                        decl.mods.flags |= STATIC | SCRIPT_LEVEL_SYNTH_STATIC;
                        scriptClassDefs.append(tree);
                    }
                    break;
                }
                case FUNCTION_DEF: {
                    // turn script-level functions into script-class static functions
                    JFXFunctionDefinition decl = (JFXFunctionDefinition) tree;
                    decl.mods.flags |= STATIC | SCRIPT_LEVEL_SYNTH_STATIC;
                    Name name = decl.name;
                    checkName(tree.pos, name);
                    // User run function isn't used directly.
                    // Guts will be added to internal run function.
                    // Other functions added to the script-class
                    if (name != defs.userRunFunctionName) {
                        scriptClassDefs.append(tree);
                    }
                    break;
                }
                case VAR_DEF: {
                    // turn script-level variables into script-class static variables
                    JFXVar decl = (JFXVar) tree;
                    if ( (decl.mods.flags & SCRIPT_LEVEL_SYNTH_STATIC) == 0) {
                        // if this wasn't already created as a synthetic
                        checkName(tree.pos, decl.getName());
                    }
                    decl.mods.flags |= STATIC | SCRIPT_LEVEL_SYNTH_STATIC;
                    scriptClassDefs.append(decl);  // declare variable as a static in the script class
                    if (!isLibrary) {
                        // This is a simple-form script where the main-code is just loose at the script-level.
                        // The main-code will go into the run method.  The variable initializations should
                        // be in-place inline.   Place the variable initialization in 'value' so that
                        // it will wind up in the code of the run method.
                        value = fxmake.VarScriptInit(decl);
                    }
                    break;
                }
                default: {
                    // loose expressions, if allowed, get added to the statements/value
                    if (isLibrary && !looseExpressionErrorShown) {
                        log.error(tree.pos(), MsgSym.MESSAGE_JAVAFX_LOOSE_EXPRESSIONS);
                        looseExpressionErrorShown = true;
                    }
                    value = (JFXExpression) tree;
                    break;
                }
            }
        }
        
        {
            // Create the internal run function, take as much as
            // possible from the user run function (if it exists)
            Name commandLineArgs = defaultRunArgName;
            if (userRunFunction != null) {
                List<JFXVar> params = userRunFunction.operation.getParams();
                if (params.size() == 1) {
                    commandLineArgs = params.head.getName();
                }
                // a run function was specified, start the statement
                JFXBlock body = userRunFunction.getBodyExpression();
                if (body.getStmts().size() > 0 || body.getValue() != null) {
                    if (value != null) {
                        stats.append(value);
                    }
                    stats.appendList(body.getStmts());
                    if (body.getValue() != null) {
                        value = body.getValue();
                    }
                }
            }
            JFXFunctionDefinition internalRunFunction = makeInternalRunFunction(commandLineArgs, stats.toList(), value);
            internalRunFunction.setGenType(SynthType.SYNTHETIC);
            scriptClassDefs.prepend(internalRunFunction);
        }

        if (moduleClass == null) {

            // Synthesize a Main class definition and flag it as
            // such.
            //
            JFXModifiers cMods = fxmake.Modifiers(PUBLIC);
            cMods.setGenType(SynthType.SYNTHETIC);
            moduleClass = fxmake.ClassDeclaration(
                    cMods, //public access needed for applet initialization
                    moduleClassName,
                    List.<JFXExpression>nil(), // no supertypes
                    scriptClassDefs.toList());
            moduleClass.setGenType(SynthType.SYNTHETIC);

        } else {
            moduleClass.setMembers(scriptClassDefs.appendList(moduleClass.getMembers()).toList());
        }
        
        moduleClass.isModuleClass = true;
        moduleClass.runMethod = userRunFunction;

        topLevelDefs.append(moduleClass);
        
        module.defs = topLevelDefs.toList();

        convertAccessFlags(module);

        reservedTopLevelNamesSet = null;
    }
    
    private void debugPositions(final JFXScript module) {
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
    
    private List<JFXTree> pseudoVariables(DiagnosticPosition diagPos, Name moduleClassName, JFXScript module,
            boolean usesFile, boolean usesDir) {
        ListBuffer<JFXTree> pseudoDefs = ListBuffer.<JFXTree>lb();
        if (usesFile || usesDir) {
            // java.net.URL __FILE__ = Util.get__FILE__(moduleClass);
            JFXExpression moduleClassFQN = module.pid != null ?
                fxmake.at(diagPos).Select(module.pid, moduleClassName) : fxmake.at(diagPos).Ident(moduleClassName);
            JFXExpression getFile = fxmake.at(diagPos).Identifier("com.sun.javafx.runtime.PseudoVariables.get__FILE__");
            JFXExpression forName = fxmake.at(diagPos).Identifier("java.lang.Class.forName");
            List<JFXExpression> args = List.<JFXExpression>of(fxmake.at(diagPos).Literal(moduleClassFQN.toString()));
            JFXExpression loaderCall = fxmake.at(diagPos).Apply(List.<JFXExpression>nil(), forName, args);
            args = List.<JFXExpression>of(loaderCall);
            JFXExpression getFileURL = fxmake.at(diagPos).Apply(List.<JFXExpression>nil(), getFile, args);
            JFXExpression fileVar =
                fxmake.at(diagPos).Var(pseudoFile, getURLType(diagPos),
                         fxmake.at(diagPos).Modifiers(FINAL|STATIC|SCRIPT_LEVEL_SYNTH_STATIC),
                         getFileURL, JavafxBindStatus.UNBOUND, null);
            pseudoDefs.append(fileVar);

            // java.net.URL __DIR__;
            if (usesDir) {
                JFXExpression getDir = fxmake.at(diagPos).Identifier("com.sun.javafx.runtime.PseudoVariables.get__DIR__");
                args = List.<JFXExpression>of(fxmake.at(diagPos).Ident(pseudoFile));
                JFXExpression getDirURL = fxmake.at(diagPos).Apply(List.<JFXExpression>nil(), getDir, args);
                pseudoDefs.append(
                    fxmake.at(diagPos).Var(pseudoDir, getURLType(diagPos),
                             fxmake.at(diagPos).Modifiers(FINAL|STATIC|SCRIPT_LEVEL_SYNTH_STATIC),
                             getDirURL, JavafxBindStatus.UNBOUND, null));
            }
        }
        return pseudoDefs.toList();
    }
    
    private JFXType getURLType(DiagnosticPosition diagPos) {
        JFXExpression urlFQN = fxmake.at(diagPos).Identifier("java.net.URL");
        return fxmake.at(diagPos).TypeClass(urlFQN, TypeTree.Cardinality.SINGLETON);
    }

    private List<JFXVar> makeRunFunctionArgs(Name argName) {
        JFXVar mainArgs = fxmake.Param(argName, fxmake.TypeClass(
                fxmake.Ident(syms.stringTypeName),
                TypeTree.Cardinality.ANY));
         return List.<JFXVar>of(mainArgs);
    }

    private JFXType makeRunFunctionType() {
         JFXExpression rettree = fxmake.Type(syms.objectType);
        rettree.type = syms.objectType;
        return fxmake.TypeClass(rettree, JFXType.Cardinality.SINGLETON);
    }

    private JFXFunctionDefinition makeInternalRunFunction(Name argName, List<JFXExpression> stats, JFXExpression value) {
        JFXBlock body = fxmake.Block(0, stats, value);
        return fxmake.FunctionDefinition(
                fxmake.Modifiers(PUBLIC | STATIC | SCRIPT_LEVEL_SYNTH_STATIC | SYNTHETIC),
                defs.internalRunFunctionName,
                makeRunFunctionType(),
                makeRunFunctionArgs(argName),
                body);        
    }
    
    private Name scriptName(JFXScript tree) {
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
        }
        
        if (reservedTopLevelNamesSet.contains(name)) {
            log.error(pos, MsgSym.MESSAGE_JAVAFX_RESERVED_TOP_LEVEL_SCRIPT_MEMBER, name.toString());
        }
    }
    
    private void checkForBadPositions(JFXScript testTree) {
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
