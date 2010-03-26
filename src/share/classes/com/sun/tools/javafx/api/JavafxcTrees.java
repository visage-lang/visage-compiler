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

package com.sun.tools.javafx.api;

import com.sun.javafx.api.*;
import com.sun.javafx.api.tree.*;
import java.io.IOException;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Symbol.TypeSymbol;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.PackageSymbol;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Log;
import com.sun.tools.mjavac.util.Pair;
import com.sun.tools.javafx.comp.JavafxAttr;
import com.sun.tools.javafx.comp.JavafxAttrContext;
import com.sun.tools.javafx.comp.JavafxEnter;
import com.sun.tools.javafx.comp.JavafxEnv;
import com.sun.tools.javafx.comp.JavafxMemberEnter;
import com.sun.tools.javafx.comp.JavafxResolve;
import com.sun.tools.javafx.tree.*;

/**
 * Provides an implementation of Trees for the JavaFX Script compiler, based
 * on JavacTrees.
 *
 * @author Peter von der Ah&eacute;
 * @author Tom Ball
 */
public class JavafxcTrees {

    private final JavafxResolve resolve;
    private final JavafxEnter enter;
    private final Log log;
    private final JavafxMemberEnter memberEnter;
    private final JavafxAttr attr;
    private final JavafxTreeMaker fxmake;
    private final JavafxcTaskImpl javafxcTaskImpl;
    private final Context ctx;

    public static JavafxcTrees instance(JavafxCompiler.CompilationTask task) {
        if (!(task instanceof JavafxcTaskImpl))
            throw new IllegalArgumentException();
        return instance(((JavafxcTaskImpl)task).getContext());
    }

    public static JavafxcTrees instance(Context context) {
        JavafxcTrees instance = context.get(JavafxcTrees.class);
        if (instance == null)
            instance = new JavafxcTrees(context);
        return instance;
    }

    private JavafxcTrees(Context context) {
        context.put(JavafxcTrees.class, this);
        ctx = context;
        attr = JavafxAttr.instance(context);
        enter = JavafxEnter.instance(context);
        log = Log.instance(context);
        resolve = JavafxResolve.instance(context);
        fxmake = JavafxTreeMaker.instance(context);
        memberEnter = JavafxMemberEnter.instance(context);
        javafxcTaskImpl = context.get(JavafxcTaskImpl.class);
    }

    public SourcePositions getSourcePositions() {
        return new SourcePositions() {
                public long getStartPosition(UnitTree file, Tree tree) {
                    return JavafxTreeInfo.getStartPos((JFXTree) tree);
                }

                public long getEndPosition(UnitTree file, Tree tree) {
                    Map<JCTree,Integer> endPositions = ((JFXScript) file).endPositions;
                    return JavafxTreeInfo.getEndPos((JFXTree)tree, endPositions);
                }
            };
    }

    public ClassDeclarationTree getTree(TypeElement element) {
        return (ClassDeclarationTree) getTree((Element) element);
    }

    public FunctionDefinitionTree getTree(ExecutableElement method) {
        return (FunctionDefinitionTree) getTree((Element) method);
    }

    public Tree getTree(Element element) {
        Symbol symbol = (Symbol) element;
        TypeSymbol enclosing = symbol.enclClass();
        JavafxEnv<JavafxAttrContext> env = enter.getEnv(enclosing);
        if (env == null)
            return null;
        JFXClassDeclaration classNode = env.enclClass;
        if (classNode != null) {
            if (JavafxTreeInfo.symbolFor(classNode) == element)
                return classNode;
            for (JFXTree node : classNode.getMembers())
                if (JavafxTreeInfo.symbolFor(node) == element)
                    return node;
        }
        return null;
    }

    public JavaFXTreePath getPath(UnitTree unit, Tree node) {
        return getPath(new JavaFXTreePath(unit), node);
    }

    public JavaFXTreePath getPath(Element e) {
        final Pair<JFXTree, JFXScript> treeTopLevel = getTreeAndTopLevel(e);
        if (treeTopLevel == null)
            return null;
        return getPath(treeTopLevel.snd, treeTopLevel.fst);
    }
    
    /**
     * Gets a tree path for a tree node within a subtree identified by a JavaFXTreePath object.
     * @return null if the node is not found
     */
    public static JavaFXTreePath getPath(JavaFXTreePath path, Tree target) {
        path.getClass();
        target.getClass();

        class Result extends Error {
            static final long serialVersionUID = -5942088234594905625L;
            JavaFXTreePath path;
            Result(JavaFXTreePath path) {
                this.path = path;
            }
        }
        class PathFinder extends JavaFXTreePathScanner<JavaFXTreePath,Tree> {
            @Override
            public JavaFXTreePath scan(Tree tree, Tree target) {
                if (tree == target)
                    throw new Result(new JavaFXTreePath(getCurrentPath(), target));
                return super.scan(tree, target);
            }
        }

        try {
            new PathFinder().scan(path, target);
        } catch (Result result) {
            return result.path;
        }
        return null;
    }

    public Element getElement(JavaFXTreePath path) {
        Tree t = path.getLeaf();
        return JavafxTreeInfo.symbolFor((JFXTree) t);
    }

    public TypeMirror getTypeMirror(JavaFXTreePath path) {
        Tree t = path.getLeaf();
        return ((JFXTree)t).type;
    }

    public JavafxcScope getScope(JavaFXTreePath path) {
        return new JavafxcScope(ctx, getAttrContext(path));
    }

    public boolean isAccessible(Scope scope, TypeElement type) {
        if (scope instanceof JavafxcScope && type instanceof ClassSymbol) {
            JavafxEnv<JavafxAttrContext> env = ((JavafxcScope) scope).env;
            return resolve.isAccessible(env, (ClassSymbol)type);
        } else
            return false;
    }

    public boolean isAccessible(Scope scope, Element member, DeclaredType type) {
        if (scope instanceof JavafxcScope
                && member instanceof Symbol
                && type instanceof com.sun.tools.mjavac.code.Type) {
            JavafxEnv<JavafxAttrContext> env = ((JavafxcScope) scope).env;
            return resolve.isAccessible(env, (com.sun.tools.mjavac.code.Type)type, (Symbol)member);
        } else
            return false;
    }

    private JavafxEnv<JavafxAttrContext> getAttrContext(JavaFXTreePath path) {
        if (!(path.getLeaf() instanceof JFXTree))  // implicit null-check
            throw new IllegalArgumentException();

        // if we're being invoked via from a JSR199 client, we need to make sure
        // all the classes have been entered; if we're being invoked from JSR269,
        // then the classes will already have been entered.
        if (javafxcTaskImpl != null) {
            try {
                javafxcTaskImpl.enter();
            } catch (IOException e) {
                throw new Error("unexpected error while entering symbols: " + e);
            }
        }

        JFXScript unit = (JFXScript) path.getCompilationUnit();
        Copier copier = new Copier(fxmake.forToplevel(unit));

        copier.endPositions = unit.endPositions;

        JavafxEnv<JavafxAttrContext> env = null;
        JFXFunctionDefinition function = null;
        JFXVar field = null;

        List<Tree> l = List.nil();
        JavaFXTreePath p = path;
        while (p != null) {
            l = l.prepend(p.getLeaf());
            p = p.getParentPath();
        }

        for ( ; l.nonEmpty(); l = l.tail) {
            Tree tree = l.head;
            if (tree instanceof JFXScript) {
                env = enter.getTopLevelEnv((JFXScript)tree);
            }
            else if (tree instanceof JFXClassDeclaration) {
                env = enter.getClassEnv(((JFXClassDeclaration)tree).sym);
            }
            else if (tree instanceof JFXFunctionDefinition) {
                function = (JFXFunctionDefinition)tree;
            }
            else if (tree instanceof JFXVar) {
                field = (JFXVar)tree;
            }
            else if (tree instanceof JFXBlock) {
                if (function != null)
                    env = memberEnter.getMethodEnv(function, env);
                JFXTree body = copier.copy((JFXTree)tree, (JFXTree) path.getLeaf());
                env = attribStatToTree(body, env, copier.leafCopy);
                return env;
            } else if (field != null && field.getInitializer() == tree) {
                env = memberEnter.getInitEnv(field, env);
                JFXExpression expr = copier.copy((JFXExpression)tree, (JFXTree) path.getLeaf());
                env = attribExprToTree(expr, env, copier.leafCopy);
                return env;
            }
        }
        return field != null ? memberEnter.getInitEnv(field, env) : env;
    }

    private JavafxEnv<JavafxAttrContext> attribStatToTree(JFXTree stat, JavafxEnv<JavafxAttrContext>env, JFXTree tree) {
        JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
        try {
            return attr.attribStatToTree(stat, env, tree);
        } finally {
            log.useSource(prev);
        }
    }

    private JavafxEnv<JavafxAttrContext> attribExprToTree(JFXExpression expr, JavafxEnv<JavafxAttrContext>env, JFXTree tree) {
        JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
        try {
            return attr.attribExprToTree(expr, env, tree);
        } finally {
            log.useSource(prev);
        }
    }
    
    private Pair<JFXTree, JFXScript> getTreeAndTopLevel(Element e) {
        if (e == null)
            return null;

        Symbol sym = (Symbol)e;
        TypeSymbol ts = (sym.kind != Kinds.PCK)
                        ? sym.enclClass()
                        : (PackageSymbol) sym;
        JavafxEnv<JavafxAttrContext> enterEnv = ts != null ? enter.getEnv(ts) : null;        
        if (enterEnv == null)
            return null;
        
        JFXTree tree = JavafxTreeInfo.declarationFor(sym, enterEnv.tree);
        if (tree == null || enterEnv.toplevel == null)
            return null;
        return new Pair<JFXTree,JFXScript>(tree, enterEnv.toplevel);
    }

    public JavafxEnv<JavafxAttrContext> getFunctionEnv(JFXFunctionDefinition tree, JavafxEnv<JavafxAttrContext> env) {
        JavafxEnv<JavafxAttrContext> mEnv = memberEnter.methodEnv(tree, env);
        mEnv.info.lint = mEnv.info.lint.augment(tree.sym.attributes_field, tree.sym.flags());
        for (List<JFXVar> l = tree.getParams(); l.nonEmpty(); l = l.tail)
            mEnv.info.scope.enterIfAbsent(l.head.sym);
        return mEnv;
    }

    public JavafxEnv<JavafxAttrContext> getInitEnv(JFXVar tree, JavafxEnv<JavafxAttrContext> env) {
        JavafxEnv<JavafxAttrContext> iEnv = memberEnter.initEnv(tree, env);
        return iEnv;
    }

    /**
     * Makes a copy of a tree, noting the value resulting from copying a particular leaf.
     **/
    static class Copier extends JavafxTreeCopier {
        JFXTree leaf;
        JFXTree leafCopy = null;

        Copier(JavafxTreeMaker M) {
            super(M);
        }

        public <T extends JFXTree> T copy(T t, JFXTree leaf) {
            this.leaf = leaf;
            return copy(t);
        }
        
        @Override
        public <T extends JFXTree> T copy(T t) {
            T t2 = super.copy(t);
            if (t == leaf)
                leafCopy = t2;
            return t2;
        }

        @Override
        public void visitForExpressionInClause(JFXForExpressionInClause tree) {
            result = maker.InClause(copy(tree.var), copy(tree.getSequenceExpression()), copy(tree.getWhereExpression()));
        }
    }
}
