/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.api;

import com.sun.javafx.api.*;
import com.sun.javafx.api.tree.ClassDeclarationTree;
import com.sun.javafx.api.tree.JavaFXTreePathScanner;
import com.sun.javafx.api.tree.FunctionDefinitionTree;
import java.io.IOException;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Scope;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeCopier;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Pair;
import com.sun.tools.javafx.tree.JavafxTreeInfo;

/**
 * Provides an implementation of Trees for the JavaFX Script compiler, based
 * on JavacTrees.
 *
 * @author Peter von der Ah&eacute;
 * @author Tom Ball
 */
public class JavafxcTrees {

    private final Resolve resolve;
    private final Enter enter;
    private final Log log;
    private final MemberEnter memberEnter;
    private final Attr attr;
    private final TreeMaker treeMaker;
    private final JavafxcTaskImpl javafxcTaskImpl;

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
        attr = Attr.instance(context);
        enter = Enter.instance(context);
        log = Log.instance(context);
        resolve = Resolve.instance(context);
        treeMaker = TreeMaker.instance(context);
        memberEnter = MemberEnter.instance(context);
        javafxcTaskImpl = context.get(JavafxcTaskImpl.class);
    }

    public SourcePositions getSourcePositions() {
        return new SourcePositions() {
                public long getStartPosition(CompilationUnitTree file, Tree tree) {
                    return JavafxTreeInfo.getStartPos((JCTree) tree);
                }

                public long getEndPosition(CompilationUnitTree file, Tree tree) {
                    Map<JCTree,Integer> endPositions = ((JCCompilationUnit) file).endPositions;
                    return JavafxTreeInfo.getEndPos((JCTree) tree, endPositions);
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
        Env<AttrContext> env = enter.getEnv(enclosing);
        if (env == null)
            return null;
        JCClassDecl classNode = env.enclClass;
        if (classNode != null) {
            if (JavafxTreeInfo.symbolFor(classNode) == element)
                return classNode;
            for (JCTree node : classNode.getMembers())
                if (JavafxTreeInfo.symbolFor(node) == element)
                    return node;
        }
        return null;
    }

    public TreePath getPath(CompilationUnitTree unit, Tree node) {
        return getPath(new TreePath(unit), node);
    }

    public TreePath getPath(Element e) {
        final Pair<JCTree, JCCompilationUnit> treeTopLevel = getTreeAndTopLevel(e);
        if (treeTopLevel == null)
            return null;
        return getPath(treeTopLevel.snd, treeTopLevel.fst);
    }
    
    /**
     * Gets a tree path for a tree node within a subtree identified by a TreePath object.
     * @return null if the node is not found
     */
    public static TreePath getPath(TreePath path, Tree target) {
        path.getClass();
        target.getClass();

        class Result extends Error {
            static final long serialVersionUID = -5942088234594905625L;
            TreePath path;
            Result(TreePath path) {
                this.path = path;
            }
        }
        class PathFinder extends JavaFXTreePathScanner<TreePath,Tree> {
            public TreePath scan(Tree tree, Tree target) {
                if (tree == target)
                    throw new Result(new TreePath(getCurrentPath(), target));
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

    public Element getElement(TreePath path) {
        Tree t = path.getLeaf();
        return JavafxTreeInfo.symbolFor((JCTree) t);
    }

    public TypeMirror getTypeMirror(TreePath path) {
        Tree t = path.getLeaf();
        return ((JCTree)t).type;
    }

    public JavafxcScope getScope(TreePath path) {
        return new JavafxcScope(getAttrContext(path));
    }

    public boolean isAccessible(Scope scope, TypeElement type) {
        if (scope instanceof JavafxcScope && type instanceof ClassSymbol) {
            Env<AttrContext> env = ((JavafxcScope) scope).env;
            return resolve.isAccessible(env, (ClassSymbol)type);
        } else
            return false;
    }

    public boolean isAccessible(Scope scope, Element member, DeclaredType type) {
        if (scope instanceof JavafxcScope
                && member instanceof Symbol
                && type instanceof com.sun.tools.javac.code.Type) {
            Env<AttrContext> env = ((JavafxcScope) scope).env;
            return resolve.isAccessible(env, (com.sun.tools.javac.code.Type)type, (Symbol)member);
        } else
            return false;
    }

    private Env<AttrContext> getAttrContext(TreePath path) {
        if (!(path.getLeaf() instanceof JCTree))  // implicit null-check
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


        JCCompilationUnit unit = (JCCompilationUnit) path.getCompilationUnit();
        Copier copier = new Copier(treeMaker.forToplevel(unit));

        Env<AttrContext> env = null;
        JCMethodDecl method = null;
        JCVariableDecl field = null;

        List<Tree> l = List.nil();
        TreePath p = path;
        while (p != null) {
            l = l.prepend(p.getLeaf());
            p = p.getParentPath();
        }

        for ( ; l.nonEmpty(); l = l.tail) {
            Tree tree = l.head;
            switch (tree.getKind()) {
                case COMPILATION_UNIT:
//                    System.err.println("COMP: " + ((JCCompilationUnit)tree).sourcefile);
                    env = enter.getTopLevelEnv((JCCompilationUnit)tree);
                    break;
                case CLASS:
//                    System.err.println("CLASS: " + ((JCClassDecl)tree).sym.getSimpleName());
                    env = enter.getClassEnv(((JCClassDecl)tree).sym);
                    break;
                case METHOD:
//                    System.err.println("METHOD: " + ((JCMethodDecl)tree).sym.getSimpleName());
                    method = (JCMethodDecl)tree;
                    break;
                case VARIABLE:
//                    System.err.println("FIELD: " + ((JCVariableDecl)tree).sym.getSimpleName());
                    field = (JCVariableDecl)tree;
                    break;
                case BLOCK: {
//                    System.err.println("BLOCK: ");
                    if (method != null)
                        env = memberEnter.getMethodEnv(method, env);
                    JCTree body = copier.copy((JCTree)tree, (JCTree) path.getLeaf());
                    env = attribStatToTree(body, env, copier.leafCopy);
                    return env;
                }
                default:
//                    System.err.println("DEFAULT: " + tree.getKind());
                    if (field != null && field.getInitializer() == tree) {
                        env = memberEnter.getInitEnv(field, env);
                        JCExpression expr = copier.copy((JCExpression)tree, (JCTree) path.getLeaf());
                        env = attribExprToTree(expr, env, copier.leafCopy);
                        return env;
                    }
            }
        }
        return field != null ? memberEnter.getInitEnv(field, env) : env;
    }

    private Env<AttrContext> attribStatToTree(JCTree stat, Env<AttrContext>env, JCTree tree) {
        JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
        try {
            return attr.attribStatToTree(stat, env, tree);
        } finally {
            log.useSource(prev);
        }
    }

    private Env<AttrContext> attribExprToTree(JCExpression expr, Env<AttrContext>env, JCTree tree) {
        JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
        try {
            return attr.attribExprToTree(expr, env, tree);
        } finally {
            log.useSource(prev);
        }
    }
    
    private Pair<JCTree, JCCompilationUnit> getTreeAndTopLevel(Element e) {
        if (e == null)
            return null;

        Symbol sym = (Symbol)e;
        TypeSymbol ts = (sym.kind != Kinds.PCK)
                        ? sym.enclClass()
                        : (PackageSymbol) sym;
        Env<AttrContext> enterEnv = ts != null ? enter.getEnv(ts) : null;        
        if (enterEnv == null)
            return null;
        
        JCTree tree = JavafxTreeInfo.declarationFor(sym, enterEnv.tree);
        if (tree == null || enterEnv.toplevel == null)
            return null;
        return new Pair<JCTree,JCCompilationUnit>(tree, enterEnv.toplevel);
    }

    /**
     * Makes a copy of a tree, noting the value resulting from copying a particular leaf.
     **/
    static class Copier extends TreeCopier<JCTree> {
        JCTree leafCopy = null;

        Copier(TreeMaker M) {
            super(M);
        }

        @Override
        public <T extends JCTree> T copy(T t, JCTree leaf) {
            T t2 = super.copy(t, leaf);
            if (t == leaf)
                leafCopy = t2;
            return t2;
        }
    }
}
