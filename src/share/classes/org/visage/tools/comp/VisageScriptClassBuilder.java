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

package org.visage.tools.comp;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.tools.FileObject;

import org.visage.api.VisageBindStatus;
import org.visage.api.tree.SyntheticTree.SynthType;
import org.visage.api.tree.TypeTree;
import org.visage.api.tree.TypeTree.Cardinality;
import com.sun.tools.mjavac.code.Flags;
import static com.sun.tools.mjavac.code.Flags.*;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.Name.Table;
import org.visage.tools.code.VisageFlags;
import static org.visage.tools.code.VisageFlags.SCRIPT_LEVEL_SYNTH_STATIC;
import org.visage.tools.code.VisageSymtab;
import org.visage.tools.tree.*;
import org.visage.tools.util.MsgSym;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VisageScriptClassBuilder {
    protected static final Context.Key<VisageScriptClassBuilder> visageModuleBuilderKey =
        new Context.Key<VisageScriptClassBuilder>();

    private final VisageDefs defs;
    private Table names;
    private VisageTreeMaker visagemake;
    private JCDiagnostic.Factory diags;
    private Log log;
    private VisageSymtab syms;
    private Set<Name> reservedTopLevelNamesSet;
    private Name pseudoSourceFile;
    private Name pseudoFile;
    private Name pseudoDir;
    private Name pseudoProfile;
    private Name defaultRunArgName;

    public boolean scriptingMode;
    
    private static final boolean debugBadPositions = Boolean.getBoolean("VisageModuleBuilder.debugBadPositions");

    public static VisageScriptClassBuilder instance(Context context) {
        VisageScriptClassBuilder instance = context.get(visageModuleBuilderKey);
        if (instance == null)
            instance = new VisageScriptClassBuilder(context);
        return instance;
    }

    protected VisageScriptClassBuilder(Context context) {
        context.put(visageModuleBuilderKey, this);
        defs = VisageDefs.instance(context);
        names = Table.instance(context);
        visagemake = (VisageTreeMaker)VisageTreeMaker.instance(context);
        diags = JCDiagnostic.Factory.instance(context);
        log = Log.instance(context);
        syms = (VisageSymtab)VisageSymtab.instance(context);
        pseudoSourceFile = names.fromString("__SOURCE_FILE__");
        pseudoFile = names.fromString("__FILE__");
        pseudoDir = names.fromString("__DIR__");
        pseudoProfile = names.fromString("__PROFILE__");
        defaultRunArgName = names.fromString("_$UNUSED$_$ARGS$_");
    }

    public void convertAccessFlags(VisageScript script) {
        new VisageTreeScanner() {
            
            void convertFlags(VisageModifiers mods) {
                long flags = mods.flags;
                long access = flags & (Flags.AccessFlags | VisageFlags.PACKAGE_ACCESS);
                if (access == 0L) {
                    flags |= VisageFlags.SCRIPT_PRIVATE;
                }
                mods.flags = flags;
            }

            @Override
            public void visitClassDeclaration(VisageClassDeclaration tree) {
                super.visitClassDeclaration(tree);
                convertFlags(tree.getModifiers());
            }

            @Override
            public void visitFunctionDefinition(VisageFunctionDefinition tree) {
                super.visitFunctionDefinition(tree);
                convertFlags(tree.getModifiers());
            }

            @Override
            public void visitVar(VisageVar tree) {
                super.visitVar(tree);
                convertFlags(tree.getModifiers());
            }
        }.scan(script);
    }
    
    private void checkAndNormalizeUserRunFunction(VisageFunctionDefinition runFunc) {
        VisageFunctionValue fval = runFunc.operation;
        List<VisageVar> params = fval.funParams;
        switch (params.size()) {
            case 0: {
                // no parameter specified, fill it in
                fval.funParams = makeRunFunctionArgs(defaultRunArgName);
                break;
            }
            case 1: {
                VisageType paramType = params.head.getVisageType();
                if (paramType.getCardinality() == Cardinality.ANY &&
                        paramType instanceof VisageTypeClass) {
                    VisageExpression cnExp = ((VisageTypeClass) paramType).getClassName();
                    if (cnExp instanceof VisageIdent) {
                        Name cName = ((VisageIdent)cnExp).getName();
                        if (cName == syms.stringTypeName) {
                            break;
                        }
                    }
                }
                // not well-formed, fall-through
            }
            default: {
                // bad arguments
                log.error(runFunc.pos(), MsgSym.MESSAGE_VISAGE_RUN_FUNCTION_PARAM);
                fval.funParams = makeRunFunctionArgs(defaultRunArgName);
            }
        }
        
        //TODO: check specified return type
        
        // set return type
        fval.rettype = makeRunFunctionType();
    }


    public VisageClassDeclaration preProcessVisageTopLevel(VisageScript module) {
        Name moduleClassName = scriptName(module);
        
        if (debugBadPositions) {
            checkForBadPositions(module);
        }

        if (scriptingMode && module.pid != null)
            log.error(module.pos(), MsgSym.MESSAGE_VISAGE_PACKAGE_IN_SCRIPT_EVAL_MODE);

        // check for references to pseudo variables and if found, declare them
        class PseudoIdentScanner extends VisageTreeScanner {
            public boolean usesSourceFile;
            public boolean usesFile;
            public boolean usesDir;
            public boolean usesProfile;
            public DiagnosticPosition diagPos;
            @Override
            public void visitIdent(VisageIdent id) {
                super.visitIdent(id);
                if (id.getName().equals(pseudoSourceFile)) {
                    usesSourceFile = true;
                    markPosition(id);
                }
                if (id.getName().equals(pseudoFile)) {
                    usesFile = true;
                    markPosition(id);
                }
                if (id.getName().equals(pseudoDir)) {
                    usesDir = true;
                    markPosition(id);
                }
                if (id.getName().equals(pseudoProfile)) {
                    usesProfile = true;
                    markPosition(id);
		}
            }
            void markPosition(VisageTree tree) {
                if (diagPos == null) { // want the first only
                    diagPos = tree.pos();
                }
            }
        }
        PseudoIdentScanner pseudoScanner = new PseudoIdentScanner();
        pseudoScanner.scan(module.defs);
        //debugPositions(module);

        ListBuffer<VisageTree> scriptTops = ListBuffer.<VisageTree>lb();
        final List<VisageTree> pseudoVars = pseudoVariables(module.pos(), moduleClassName, module,
                pseudoScanner.usesSourceFile, pseudoScanner.usesFile, pseudoScanner.usesDir, pseudoScanner.usesProfile);
        scriptTops.appendList(pseudoVars);
        scriptTops.appendList(module.defs);
        
        // Determine if this is a library script
        boolean externalAccessFound = false;
        VisageFunctionDefinition userRunFunction = null;
        final long EXTERNALIZING_FLAGS = Flags.PUBLIC | Flags.PROTECTED | VisageFlags.PACKAGE_ACCESS | VisageFlags.PUBLIC_READ | VisageFlags.PUBLIC_INIT;
        for (VisageTree tree : scriptTops) {

            // Protect against erroneous scripts being attributed by IDE plugin
            //
            if (tree == null ) continue;

            switch (tree.getVisageTag()) {
                case CLASS_DEF: {
                    VisageClassDeclaration decl = (VisageClassDeclaration) tree;
                    if ((decl.getModifiers().flags & EXTERNALIZING_FLAGS) != 0) {
                        externalAccessFound = true;
                    }
                    break;
                }
                case FUNCTION_DEF: {
                    VisageFunctionDefinition decl =
                            (VisageFunctionDefinition) tree;
                    Name name = decl.name;
                    if (name == defs.userRunFunctionName) {
                        if (userRunFunction == null) {
                            checkAndNormalizeUserRunFunction(decl);
                            userRunFunction = decl;
                        } else {
                            log.error(decl.pos(), MsgSym.MESSAGE_VISAGE_RUN_FUNCTION_SINGLE);
                        }
                    }
                    if ((decl.getModifiers().flags & EXTERNALIZING_FLAGS) != 0) {
                        externalAccessFound = true;
                    }
                    break;
                }
                case VAR_DEF: { 
                    VisageVar decl = (VisageVar) tree;
                    if ((decl.getModifiers().flags & EXTERNALIZING_FLAGS) != 0) {
                        externalAccessFound = true;
                    }
                    break;
                }
            }
        }
        final boolean isLibrary = externalAccessFound || (userRunFunction != null);
        module.isLibrary = isLibrary;
        ListBuffer<VisageTree> scriptClassDefs = new ListBuffer<VisageTree>();
        ListBuffer<VisageExpression> stats = new ListBuffer<VisageExpression>();
        VisageExpression value = null;
       
        // Divide module defs between internsl run function body, Java compilation unit, and module class
        ListBuffer<VisageTree> topLevelDefs = new ListBuffer<VisageTree>();
        VisageClassDeclaration moduleClass = null;
        boolean looseExpressionsSeen = false;
        for (VisageTree tree : scriptTops) {
            
            // Protect against errneous script trees being attributed by
            // IDE plugins.
            //
            if (tree == null) continue;
            if (value != null) {
                stats.append(value);
                value = null;
            }
            switch (tree.getVisageTag()) {
                case IMPORT:
                    topLevelDefs.append(tree);
                    break;
                case CLASS_DEF: {
                    VisageClassDeclaration decl = (VisageClassDeclaration) tree;
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
                    VisageFunctionDefinition decl = (VisageFunctionDefinition) tree;
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
                    VisageVar decl = (VisageVar) tree;
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
                        value = visagemake.VarInit(decl);
                    }
                    break;
                }
                default: {
                    // loose expressions, if allowed, get added to the statements/value
                    if (isLibrary && !looseExpressionsSeen) {
                        JCDiagnostic reason = externalAccessFound ?
                            diags.fragment(MsgSym.MESSAGE_VISAGE_LOOSE_IN_LIB) :
                            diags.fragment(MsgSym.MESSAGE_VISAGE_LOOSE_IN_RUN);
                        log.error(tree.pos(), MsgSym.MESSAGE_VISAGE_LOOSE_EXPRESSIONS, reason);
                    }
                    looseExpressionsSeen = true;
                    value = (VisageExpression) tree;
                    break;
                }
            }
        }
        
        {
            // Create the internal run function, take as much as
            // possible from the user run function (if it exists)
            //
            // If there was no user supplied run function then we mark the
            // funcitonas synthetic and make sense of the start and endpos
            // for the node. If the user supplied a run function, then
            // we use the information it gives us and neither flag it as
            // synthetic nor change the node postions.
            //
            SynthType               sType                   = SynthType.SYNTHETIC;
            VisageFunctionDefinition   internalRunFunction     = null;

            Name commandLineArgs = defaultRunArgName;
            if (userRunFunction != null) {

                List<VisageVar> params = userRunFunction.operation.getParams();

                // Protect IDE plugin against partially typed run function
                // returning null for the parameters, statements or body, by
                // null checking for each of those elements.
                //
                if (params != null && params.size() == 1) {
                    commandLineArgs = params.head.getName();
                }
                // a run function was specified, start the statement, protecting
                // against IDE generated errors.
                //
                VisageBlock body = userRunFunction.getBodyExpression();
                if (body != null) {

                    int sSize = 0;
                    List<VisageExpression> statements = body.getStmts();

                    if (statements != null) {
                        sSize = statements.size();
                    }
                    if (sSize > 0 || body.getValue() != null) {

                        if (value != null) {
                            stats.append(value);
                        }

                        if (sSize > 0) {
                            stats.appendList(body.getStmts());
                        }

                        if (body.getValue() != null) {
                            value = body.getValue();
                        }
                    }
                }
            }
            // If there is a user supplied run function, use content and position from it.
            // Otherwise, unless this is a pure library, create a run function from the loose expressions, 
            // with no position.
            if (userRunFunction != null || !isLibrary || looseExpressionsSeen) {
                internalRunFunction = makeInternalRunFunction(module, commandLineArgs, userRunFunction, stats.toList(), value);
                scriptClassDefs.prepend(internalRunFunction);
                module.isRunnable = true;
            }
        }

        if (moduleClass == null) {

            // Synthesize a Main class definition and flag it as
            // such.
            //
            VisageModifiers cMods = visagemake.Modifiers(PUBLIC);
            cMods.setGenType(SynthType.SYNTHETIC);
            moduleClass = visagemake.ClassDeclaration(
                    cMods, //public access needed for applet initialization
                    moduleClassName,
                    List.<VisageExpression>nil(), // no supertypes
                    scriptClassDefs.toList());
            moduleClass.setGenType(SynthType.SYNTHETIC);
            moduleClass.setPos(module.getStartPosition());
        } else {
            moduleClass.setMembers(scriptClassDefs.appendList(moduleClass.getMembers()).toList());
        }
        
        // Check endpos for IDE
        //
        setEndPos(module, moduleClass, module);
        
        moduleClass.isScriptClass   = true;
        if (scriptingMode)
            moduleClass.setScriptingModeScript();
        moduleClass.runMethod       = userRunFunction;
        topLevelDefs.append(moduleClass);

        module.defs = topLevelDefs.toList();

        // Sort the list into startPosition order for IDEs
        //
        
        ArrayList<VisageTree> sortL = new ArrayList<VisageTree>(moduleClass.getMembers());
        Collections.sort(sortL, new Comparator<VisageTree>() {
            public int compare(VisageTree t1, VisageTree t2) {
                if (pseudoVars.contains(t1)) {
                    return -1;
                } else if (pseudoVars.contains(t2)) {
                    return +1;
                } else {
                    return t1.getStartPosition() - t2.getStartPosition();
                }
            }
        });

/***** This is part of the fix for VSGC-3416.  But, it causes incorrect compile time 
       errors in the ShoppingService sample so we disable this.
       The error msgs in functional/should-fail/AccessModifiersTest.visage.EXPECTED
       are sensitive to this so if you fix the problem, you will have to fix that
       file too.
        // This a hokey way to do this, but we are using mjavac.util.List and it doesn't
        // support much. Fortunately, there won't be thousands of entries in the member lists
        //
        scriptClassDefs.clear();
        for (VisageTree e : sortL) {
            scriptClassDefs.append(e);
        }
        moduleClass.setMembers(scriptClassDefs.toList());
*****/
        convertAccessFlags(module);

        reservedTopLevelNamesSet = null;
        return moduleClass;
    }
    
    /**
     * Helper method that checks to see if we can/need to record the correct end
     * position for any synthesized nodes, based upon the end position of some
     * supplied node that makes sense to use the end position of.
     * 
     * @param module The top level script node
     * @param built  The AST we are synthesizing
     * @param copy   The AST we are copying information from
     */
    protected void setEndPos(final VisageScript module, final VisageTree build, final VisageTree copy)
    {
        // We can only calculate end position spans if we have an
        // end position map, for debugging, or for the IDE.
        //
        if  (module.endPositions != null) {
            module.endPositions.put(build, copy.getEndPosition(module.endPositions));
        }
    }

    private void debugPositions(final VisageScript module) {
        new VisageTreeScanner() {

            @Override
            public void scan(VisageTree tree) {
                super.scan(tree);
                if (tree != null) {
                    System.out.println("[" + tree.getStartPosition() + "," + tree.getEndPosition(module.endPositions) + "]  " + tree.toString());
                }
            }
        }.scan(module);

    }
    
    private List<VisageTree> pseudoVariables(DiagnosticPosition diagPos, Name moduleClassName, VisageScript module,
            boolean usesSourceFile, boolean usesFile, boolean usesDir, boolean usesProfile) {
        ListBuffer<VisageTree> pseudoDefs = ListBuffer.<VisageTree>lb();
        if (usesSourceFile) {
            String sourceName = module.getSourceFile().toUri().toString();
            VisageExpression sourceFileVar =
                visagemake.at(diagPos).Var(pseudoSourceFile, getPseudoVarType(diagPos),
                         visagemake.at(diagPos).Modifiers(FINAL|STATIC|SCRIPT_LEVEL_SYNTH_STATIC|VisageFlags.IS_DEF),
                         visagemake.Literal(sourceName), VisageBindStatus.UNBOUND, null, null);
            pseudoDefs.append(sourceFileVar);
        }
        if (usesFile || usesDir) {
            VisageExpression moduleClassFQN = module.pid != null ?
                visagemake.at(diagPos).Select(module.pid, moduleClassName, false) : visagemake.at(diagPos).Ident(moduleClassName);
            VisageExpression getFile = visagemake.at(diagPos).Identifier("org.visage.runtime.PseudoVariables.get__FILE__");
            VisageExpression forName = visagemake.at(diagPos).Identifier("java.lang.Class.forName");
            List<VisageExpression> args = List.<VisageExpression>of(visagemake.at(diagPos).Literal(moduleClassFQN.toString()));
            VisageExpression loaderCall = visagemake.at(diagPos).Apply(List.<VisageExpression>nil(), forName, args);
            args = List.<VisageExpression>of(loaderCall);
            VisageExpression getFileURL = visagemake.at(diagPos).Apply(List.<VisageExpression>nil(), getFile, args);
            VisageExpression fileVar =
                visagemake.at(diagPos).Var(pseudoFile, getPseudoVarType(diagPos),
                         visagemake.at(diagPos).Modifiers(FINAL|STATIC|SCRIPT_LEVEL_SYNTH_STATIC|VisageFlags.IS_DEF),
                         getFileURL, VisageBindStatus.UNBOUND, null, null);
            pseudoDefs.append(fileVar);

            if (usesDir) {
                VisageExpression getDir = visagemake.at(diagPos).Identifier("org.visage.runtime.PseudoVariables.get__DIR__");
                args = List.<VisageExpression>of(visagemake.at(diagPos).Ident(pseudoFile));
                VisageExpression getDirURL = visagemake.at(diagPos).Apply(List.<VisageExpression>nil(), getDir, args);
                pseudoDefs.append(
                    visagemake.at(diagPos).Var(pseudoDir, getPseudoVarType(diagPos),
                             visagemake.at(diagPos).Modifiers(FINAL|STATIC|SCRIPT_LEVEL_SYNTH_STATIC|VisageFlags.IS_DEF),
                             getDirURL, VisageBindStatus.UNBOUND, null, null));
            }
        }
	if (usesProfile) {
           VisageExpression getProfile = visagemake.at(diagPos).Identifier("org.visage.runtime.PseudoVariables.get__PROFILE__");
           VisageExpression getProfileString = visagemake.at(diagPos).Apply(List.<VisageExpression>nil(), getProfile, List.<VisageExpression>nil());
           VisageExpression profileVar =
                visagemake.at(diagPos).Var(pseudoProfile, getPseudoVarType(diagPos),
                         visagemake.at(diagPos).Modifiers(FINAL|STATIC|SCRIPT_LEVEL_SYNTH_STATIC|VisageFlags.IS_DEF),
                         getProfileString, VisageBindStatus.UNBOUND, null, null);
            pseudoDefs.append(profileVar);
	}
        return pseudoDefs.toList();
    }
    
    private VisageType getPseudoVarType(DiagnosticPosition diagPos) {
        VisageExpression fqn = visagemake.at(diagPos).Identifier("java.lang.String");
        return visagemake.at(diagPos).TypeClass(fqn, TypeTree.Cardinality.SINGLETON);
    }

    private List<VisageVar> makeRunFunctionArgs(Name argName) {
        VisageVar mainArgs = visagemake.Param(argName, visagemake.TypeClass(
                visagemake.Ident(syms.stringTypeName),
                TypeTree.Cardinality.ANY));
         return List.<VisageVar>of(mainArgs);
    }

    private VisageType makeRunFunctionType() {
         VisageExpression rettree = visagemake.Type(syms.objectType);
        rettree.type = syms.objectType;
        return visagemake.TypeClass(rettree, VisageType.Cardinality.SINGLETON);
    }

    /**
     * Constructs the internal static run function when the user has explicitly supplied a
     * declaration and body for that function.
     *
     * TODO: Review whether the caller even needs to copy the statements from the existing
     *       body into stats, or can just use it. This change to code positions was done as
     *       an emergency patch (VSGC-2291) for release 1.0 and I thought
     *       it best to perform minimal surgery on the existing mechanism - Jim Idle.
     *
     * @param module           The Script level node
     * @param argName          The symbol table name of the args array
     * @param userRunFunction  The user written run function (if there is one)
     * @param value            The value of the function
     * @return                 The run function we have constructed
     */
    private VisageFunctionDefinition makeInternalRunFunction(VisageScript module, Name argName, VisageFunctionDefinition userRunFunction, List<VisageExpression> stats, VisageExpression value) {

        VisageBlock existingBody = null;
        VisageBlock body = visagemake.at(null).Block(module.getStartPosition(), stats, value);
        int sPos = module.getStartPosition();

        // First assume that this is synthetic
        //
        setEndPos(module, body, module);
        body.setGenType(SynthType.SYNTHETIC);

        // Now, override if it is not synthetic and there is a function body
        //  - there will only not be a body if this is coming from the IDE and the
        // script is in error at this point.
        //
        if  (userRunFunction != null) {

            existingBody = userRunFunction.getBodyExpression();
            body.setGenType(SynthType.COMPILED);

            if  (existingBody != null) {

                body.setPos(existingBody.getStartPosition());
                setEndPos(module, body, existingBody);
                sPos = userRunFunction.getStartPosition();

            }
        }

        // Make the static run function
        //
        VisageFunctionDefinition func = visagemake.at(sPos).FunctionDefinition(
                visagemake.Modifiers(PUBLIC | STATIC | SCRIPT_LEVEL_SYNTH_STATIC | SYNTHETIC),
                defs.internalRunFunctionName,
                makeRunFunctionType(),
                makeRunFunctionArgs(argName),
                body);

        // Construct the source code end position from either the existing
        // function, or the module level.
        //
        if  (userRunFunction != null) {

            setEndPos(module, func, userRunFunction);
            func.operation.setPos(body.getStartPosition());
            VisageVar param = func.getParams().head;
            VisageVar existingParam = userRunFunction.getParams().head;

            if  (existingParam != null) {

                param.setPos(existingParam.getStartPosition());
                setEndPos(module, param, existingParam);
            }

        } else {

            setEndPos(module, func, module);
            func.setGenType(SynthType.SYNTHETIC);
        }

        setEndPos(module, func.operation, body);
        return func;
    }

    private Name scriptName(VisageScript tree) {
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

        return names.fromString(fileObjName);
    }

    private void checkName(int pos, Name name) {
        if (reservedTopLevelNamesSet == null) {
            reservedTopLevelNamesSet = new HashSet<Name>();
            
            // make sure no one tries to declare these reserved names
            reservedTopLevelNamesSet.add(pseudoFile);
            reservedTopLevelNamesSet.add(pseudoDir);
        }
        
        if (reservedTopLevelNamesSet.contains(name)) {
            log.error(pos, MsgSym.MESSAGE_VISAGE_RESERVED_TOP_LEVEL_SCRIPT_MEMBER, name.toString());
        }
    }
    
    private void checkForBadPositions(VisageScript testTree) {
        final Map<JCTree, Integer> endPositions = testTree.endPositions;  
        new VisageTreeScanner() {

            @Override
            public void scan(VisageTree tree) {
                super.scan(tree);
                
                // A Modifiers instance with no modifier tokens and no annotations
                // is defined as having no position.
                if (tree instanceof VisageModifiers) {
                    VisageModifiers mods = (VisageModifiers)tree;
                    if (mods.getFlags().isEmpty() || 
                        (mods.flags & Flags.SYNTHETIC) != 0)
                        return;
                }
                
                // TypeUnknown trees have no associated tokens.
                if (tree instanceof VisageTypeUnknown)
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
