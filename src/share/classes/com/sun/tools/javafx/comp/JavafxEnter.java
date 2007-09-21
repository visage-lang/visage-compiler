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
import static com.sun.tools.javac.code.Kinds.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.ErrorType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.code.*;

import java.util.Map;
import java.util.HashMap;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileManager;

public class JavafxEnter extends JCTree.Visitor implements JavafxVisitor {
    protected static final Context.Key<JavafxEnter> javafxEnterKey =
	new Context.Key<JavafxEnter>();

    private final Log log;
    private final JavafxSymtab syms;
    private final JavafxCheck chk;
    private final JavafxTreeMaker make;
    private final ClassReader reader;
    private final JavafxAnnotate annotate;
    private final JavafxMemberEnter memberEnter;
    private final Lint lint;
    private final JavaFileManager fileManager;
    private final JavafxTodo todo;

    public static JavafxEnter instance(Context context) {
	JavafxEnter instance = context.get(javafxEnterKey);
	if (instance == null)
	    instance = new JavafxEnter(context);
	return instance;
    }

    protected JavafxEnter(Context context) {
	context.put(javafxEnterKey, this);

	log = Log.instance(context);
	reader = ClassReader.instance(context);
	make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
	syms = (JavafxSymtab)JavafxSymtab.instance(context);
	chk = (JavafxCheck)JavafxCheck.instance(context);
	memberEnter = JavafxMemberEnter.instance(context);
	annotate = JavafxAnnotate.instance(context);
	lint = Lint.instance(context);

	predefClassDef = make.ClassDef(
	    make.Modifiers(PUBLIC),
	    syms.predefClass.name, null, null, null, null);
	predefClassDef.sym = syms.predefClass;
	todo = JavafxTodo.instance(context);
        fileManager = context.get(JavaFileManager.class);
    }

    /** A hashtable mapping classes and packages to the environments current
     *  at the points of their definitions.
     */
// JavaFX change
    protected
// JavaFX change
    Map<TypeSymbol,JavafxEnv<JavafxAttrContext>> typeEnvs =
	    new HashMap<TypeSymbol,JavafxEnv<JavafxAttrContext>>();

    /** Visitor method: Scan a single node.
     */
    public void scan(JCTree tree) {
	if(tree!=null) tree.accept(this);
    }

    /** Visitor method: scan a list of nodes.
     */
    public void scan(List<? extends JCTree> trees) {
	if (trees != null)
	for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail)
	    scan(l.head);
    }

    /** Accessor for typeEnvs
     */
    public JavafxEnv<JavafxAttrContext> getEnv(TypeSymbol sym) {
	return typeEnvs.get(sym);
    }
    
    /** The queue of all classes that might still need to be completed;
     *	saved and initialized by main().
     */
// JavaFX change
    protected
// JavaFX change
    ListBuffer<ClassSymbol> uncompleted;

    /** A dummy class to serve as enclClass for toplevel environments.
     */
    private JCClassDecl predefClassDef;

/* ************************************************************************
 * environment construction
 *************************************************************************/


    /** Create a fresh environment for class bodies.
     *	This will create a fresh scope for local symbols of a class, referred
     *	to by the environments info.scope field.
     *	This scope will contain
     *	  - symbols for this and super
     *	  - symbols for any type parameters
     *	In addition, it serves as an anchor for scopes of methods and initializers
     *	which are nested in this scope via Scope.dup().
     *	This scope should not be confused with the members scope of a class.
     *
     *	@param tree	The class definition.
     *	@param env	The environment current outside of the class definition.
     */
    public JavafxEnv<JavafxAttrContext> classEnv(JCClassDecl tree, JavafxEnv<JavafxAttrContext> env) {
	JavafxEnv<JavafxAttrContext> localEnv =
	    env.dup(tree, env.info.dup(new Scope(tree.sym)));
	localEnv.enclClass = tree;
	localEnv.outer = env;
	localEnv.info.isSelfCall = false;
	localEnv.info.lint = null; // leave this to be filled in by Attr, 
	                           // when annotations have been processed
	return localEnv;
    }

    /** Create a fresh environment for toplevels.
     *	@param tree	The toplevel tree.
     */
    JavafxEnv<JavafxAttrContext> topLevelEnv(JCCompilationUnit tree) {
	JavafxEnv<JavafxAttrContext> localEnv = new JavafxEnv<JavafxAttrContext>(tree, new JavafxAttrContext());
	localEnv.toplevel = tree;
	localEnv.enclClass = predefClassDef;
	tree.namedImportScope = new Scope.ImportScope(tree.packge);
	tree.starImportScope = new Scope.ImportScope(tree.packge);
	localEnv.info.scope = tree.namedImportScope;
	localEnv.info.lint = lint;
	return localEnv;
    } 

    /** The scope in which a member definition in environment env is to be entered
     *	This is usually the environment's scope, except for class environments,
     *	where the local scope is for type variables, and the this and super symbol
     *	only, and members go into the class member scope.
     */
    public Scope enterScope(JavafxEnv<JavafxAttrContext> env) {
	return (env.tree.getTag() == JCTree.CLASSDEF ||
                env.tree.getTag() == JavafxTag.CLASSDECL)
	    ? ((JCClassDecl) env.tree).sym.members_field
	    : env.info.scope;
    }

/* ************************************************************************
 * Visitor methods for phase 1: class enter
 *************************************************************************/

    /** Visitor argument: the current environment.
     */
    protected JavafxEnv<JavafxAttrContext> env;

    /** Visitor result: the computed type.
     */
// JavaFX change
    protected
// JavaFX change
    Type result;

    /** Visitor method: enter all classes in given tree, catching any
     *	completion failure exceptions. Return the tree's type.
     *
     *	@param tree    The tree to be visited.
     *	@param env     The environment visitor argument.
     */
    Type classEnter(JCTree tree, JavafxEnv<JavafxAttrContext> env) {
	JavafxEnv<JavafxAttrContext> prevEnv = this.env;
	try {
	    this.env = env;
	    tree.accept(this);
	    return result;
	}  catch (CompletionFailure ex) {
	    return chk.completionError(tree.pos(), ex);
	} finally {
	    this.env = prevEnv;
	}
    }

    /** Visitor method: enter classes of a list of trees, returning a list of types.
     */
// JavaFX change
    protected
// JavaFX change
    <T extends JCTree> List<Type> classEnter(List<T> trees, JavafxEnv<JavafxAttrContext> env) {
	ListBuffer<Type> ts = new ListBuffer<Type>();
	for (List<T> l = trees; l.nonEmpty(); l = l.tail)
	    ts.append(classEnter(l.head, env));
	return ts.toList();
    }

    public void visitTopLevel(JCCompilationUnit tree) {
	JavaFileObject prev = log.useSource(tree.sourcefile);
        boolean addEnv = false;
	boolean isPkgInfo = tree.sourcefile.isNameCompatible("package-info",
							     JavaFileObject.Kind.SOURCE);
	if (tree.pid != null) {
	    tree.packge = reader.enterPackage(JavafxTreeInfo.fullName(tree.pid));
	    if (tree.packageAnnotations.nonEmpty()) {
                if (isPkgInfo) {
                    addEnv = true;
                } else {
                    log.error(tree.packageAnnotations.head.pos(),
                              "pkg.annotations.sb.in.package-info.java");
                }
	    }
	} else {
	    tree.packge = syms.unnamedPackage;
	}
	tree.packge.complete(); // Find all classes in package.
        JavafxEnv<JavafxAttrContext> env = topLevelEnv(tree);

	// Save environment of package-info.java file.
	if (isPkgInfo) {
	    JavafxEnv<JavafxAttrContext> env0 = typeEnvs.get(tree.packge);
	    if (env0 == null) {
		typeEnvs.put(tree.packge, env);
	    } else {
		JCCompilationUnit tree0 = env0.toplevel;
                if (!fileManager.isSameFile(tree.sourcefile, tree0.sourcefile)) {
		    log.warning(tree.pid != null ? tree.pid.pos()
						 : null,
				"pkg-info.already.seen",
				tree.packge);
		    if (addEnv || (tree0.packageAnnotations.isEmpty() &&
				   tree.docComments != null &&
				   tree.docComments.get(tree) != null)) {
			typeEnvs.put(tree.packge, env);
		    }
		}
	    }
	}
	classEnter(tree.defs, env);
        if (addEnv) {
            todo.append(env);
        }
	log.useSource(prev);
	result = null;
    }

    @Override
    public void visitClassDef(JCClassDecl tree) {
        Symbol owner = env.info.scope.owner;
        Scope enclScope = enterScope(env);
        ClassSymbol c;
        if (owner.kind == PCK) {
            // We are seeing a toplevel class.
            PackageSymbol packge = (PackageSymbol) owner;
            for (Symbol q = packge; q != null && q.kind == PCK; q = q.owner)
                q.flags_field |= EXISTS;
            c = reader.enterClass(tree.name, packge);
            packge.members().enterIfAbsent(c);
        } else {
            if (tree.name.len != 0 &&
                    !chk.checkUniqueClassName(tree.pos(), tree.name, enclScope)) {
                result = null;
                return;
            }
            if (owner.kind == TYP) {
                // We are seeing a member class.
                c = reader.enterClass(tree.name, (TypeSymbol) owner);
                if ((owner.flags_field & INTERFACE) != 0) {
                    tree.mods.flags |= PUBLIC | STATIC;
                }
            } else {
                // We are seeing a local class.
                c = reader.defineClass(tree.name, owner);
                c.flatname = chk.localClassName(c);
                if (c.name.len != 0)
                    chk.checkTransparentClass(tree.pos(), c, env.info.scope);
            }
        }
        tree.sym = c;

        // Enter class into `compiled' table and enclosing scope.
        if (chk.compiled.get(c.flatname) != null) {
            duplicateClass(tree.pos(), c);
            result = new ErrorType(tree.name, (TypeSymbol) owner);
            tree.sym = (ClassSymbol) result.tsym;
            return;
        }
        chk.compiled.put(c.flatname, c);
        enclScope.enter(c);

        // Set up an environment for class block and store in `typeEnvs'
        // table, to be retrieved later in memberEnter and attribution.
        JavafxEnv<JavafxAttrContext> localEnv = classEnv(tree, env);
        typeEnvs.put(c, localEnv);

        // Fill out class fields.
        c.completer = memberEnter;
        c.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, c, tree);
        c.sourcefile = env.toplevel.sourcefile;
        c.members_field = new Scope(c);

        ClassType ct = (ClassType) c.type;
        if (owner.kind != PCK && (c.flags_field & STATIC) == 0) {
            // We are seeing a local or inner class.
            // Set outer_field of this class to closest enclosing class
            // which contains this class in a non-static context
            // (its "enclosing instance class"), provided such a class exists.
            Symbol owner1 = owner;
            while ((owner1.kind & (VAR | MTH)) != 0 &&
                    (owner1.flags_field & STATIC) == 0) {
                owner1 = owner1.owner;
            }
            if (owner1.kind == TYP) {
                ct.setEnclosingType(owner1.type);
            }
        }

        // Enter type parameters.
        ct.typarams_field = classEnter(tree.typarams, localEnv);

        // Add non-local class to uncompleted, to make sure it will be
        // completed later.
        if (!c.isLocal() && uncompleted != null) uncompleted.append(c);
//	System.err.println("entering " + c.fullname + " in " + c.owner);//DEBUG

        // Recursively enter all member classes.
        classEnter(tree.defs, localEnv);

        result = c.type;
    }

    // Begin JavaFX trees
    @Override
    public void visitClassDeclaration(JFXClassDeclaration that) {
        visitClassDef(that);
    }
    
    @Override
    public void visitAbstractMember(JFXAbstractMember that) {
        that.modifiers.accept(this);
        if (that.getType() != null) {
            that.getType().accept((JavafxVisitor)this);
        }
    }
    
    @Override
    public void visitAbstractFunction(JFXAbstractFunction that) {
        assert false;
    }
    
    @Override
    public void visitAttributeDefinition(JFXAttributeDefinition that) {
        visitVar(that);
        if (that.getInitializer() != null) {
            that.getInitializer().accept(this);
        }
    }
    
    @Override
    public void visitOperationDefinition(JFXOperationDefinition that) {
        visitType(that.rettype);
        visitMethodDef(that);
        that.getBodyExpression().accept((JavafxVisitor)this);
    }
    
    @Override
    public void visitFunctionDefinitionStatement(JFXFunctionDefinitionStatement that) {
        visitOperationDefinition(that.funcDef);
    }

    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        that.getBody().accept(this);
    }

    @Override
    public void visitDoLater(JFXDoLater that) {
        that.getBody().accept(this);
    }

    @Override
    public void visitMemberSelector(JFXMemberSelector that) {
    }
    
    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty that) {
    }
    
    @Override
    public void visitSequenceRange(JFXSequenceRange that) {
        that.getLower().accept(this);
        that.getUpper().accept(this);
    }
    
    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        for (JCExpression expr : that.getItems()) {
            expr.accept(this);
        }
    }

    @Override
    public void visitStringExpression(JFXStringExpression that) {
        List<JCExpression> parts = that.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            parts.head.accept(this);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    @Override
    public void visitPureObjectLiteral(JFXPureObjectLiteral that) {
        that.getIdentifier().accept(this);
        for (JCStatement part : that.getParts()) {
            part.accept(this);
        }
    }
    
    @Override
    public void visitVarIsObjectBeingInitialized(JFXVarIsObjectBeingInitialized that) {
        visitVar(that);
    }
    
    @Override
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
    }
    
    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        that.getExpression().accept(this);
    }  
    
    @Override
    public void visitTypeAny(JFXTypeAny that) {
        visitType(that);
    }
    
    @Override
    public void visitTypeClass(JFXTypeClass that) {
        visitType(that);
    }
    
    @Override
    public void visitTypeFunctional(JFXTypeFunctional that) {
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
        that.getReturnType().accept((JavafxVisitor)this);
        visitType(that);
    }
    
    @Override
    public void visitTypeUnknown(JFXTypeUnknown that) {
        visitType(that);
    }
    
    @Override
    public void visitType(JFXType that) {
    }
    
    @Override
    public void visitVar(JFXVar that) {
        visitType(that.getJFXType());
        visitVarDef(that);
    }
    
    @Override
    public void visitForExpression(JFXForExpression that) {
        for (JFXForExpressionInClause clause : that.getInClauses()) {
            clause.accept((JavafxVisitor)this);
        }
        that.getBodyExpression().accept(this);
    }
    
    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        that.getVar().accept((JavafxVisitor)this);
        that.getSequenceExpression().accept(this);
        if (that.getWhereExpression() != null) {
            that.getWhereExpression().accept(this);
        }
    }
    
    @Override
    public void visitInstanciate(JFXInstanciate that) {
        visitNewClass(that);
    }
        
    @Override
    public boolean shouldVisitRemoved() {
        return false;
    }
    
    @Override
    public boolean shouldVisitSynthetic() {
        return true;
    }
    
    @Override
    public void visitBlockExpression(JFXBlockExpression that) {
        scan(that.stats);
        scan(that.value);
    }

    /** Complain about a duplicate class. */
    protected void duplicateClass(DiagnosticPosition pos, ClassSymbol c) {
	log.error(pos, "duplicate.class", c.fullname);
    }

/** Class enter visitor method for type parameters.
     *	Enter a symbol for type parameter in local scope, after checking that it
     *	is unique.
     */
    public void visitTypeParameter(JCTypeParameter tree) {
	TypeVar a = (tree.type != null)
	    ? (TypeVar)tree.type
	    : new TypeVar(tree.name, env.info.scope.owner, syms.botType);
	tree.type = a;
	if (chk.checkUnique(tree.pos(), a.tsym, env.info.scope)) {
	    env.info.scope.enter(a.tsym);
	}
	result = a;
    }

    /** Default class enter visitor method: do nothing.
     */
    public void visitTree(JCTree tree) {
	result = null;
    }
    
    /** Main method: enter all classes in a list of toplevel trees.
     *	@param trees	  The list of trees to be processed.
     */
    public void main(List<JCCompilationUnit> trees) {
	complete(trees, null);
    }

    /** Main method: enter one class from a list of toplevel trees and
     *  place the rest on uncompleted for later processing.
     *  @param trees      The list of trees to be processed.
     *  @param c          The class symbol to be processed.
     */
    public void complete(List<JCCompilationUnit> trees, ClassSymbol c) {
        annotate.enterStart();
	ListBuffer<ClassSymbol> prevUncompleted = uncompleted;
	if (memberEnter.completionEnabled) uncompleted = new ListBuffer<ClassSymbol>();

	try {
	    // enter all classes, and construct uncompleted list
	    classEnter(trees, null);

	    // complete all uncompleted classes in memberEnter
	    if  (memberEnter.completionEnabled) {
		while (uncompleted.nonEmpty()) {
		    ClassSymbol clazz = uncompleted.next();
		    if (c == null || c == clazz || prevUncompleted == null)
			clazz.complete();
		    else
			// defer
			prevUncompleted.append(clazz);
		}

		// if there remain any unimported toplevels (these must have
		// no classes at all), process their import statements as well.
		for (JCCompilationUnit tree : trees) {
		    if (tree.starImportScope.elems == null) {
			JavaFileObject prev = log.useSource(tree.sourcefile);
			JavafxEnv<JavafxAttrContext> env = typeEnvs.get(tree);
			if (env == null)
			    env = topLevelEnv(tree);
			memberEnter.memberEnter(tree, env);
			log.useSource(prev);
		    }
		}
	    }
	} finally {
	    uncompleted = prevUncompleted;
            annotate.enterDone();
	}
    }

}