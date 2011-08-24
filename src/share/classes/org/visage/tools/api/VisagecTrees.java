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

package org.visage.tools.api;

import org.visage.api.*;
import org.visage.api.tree.*;
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
import org.visage.tools.comp.VisageAttr;
import org.visage.tools.comp.VisageAttrContext;
import org.visage.tools.comp.VisageEnter;
import org.visage.tools.comp.VisageEnv;
import org.visage.tools.comp.VisageMemberEnter;
import org.visage.tools.comp.VisageResolve;
import org.visage.tools.tree.*;

/**
 * Provides an implementation of Trees for the Visage compiler, based
 * on JavacTrees.
 *
 * @author Peter von der Ah&eacute;
 * @author Tom Ball
 */
public class VisagecTrees {

    private final VisageResolve resolve;
    private final VisageEnter enter;
    private final Log log;
    private final VisageMemberEnter memberEnter;
    private final VisageAttr attr;
    private final VisageTreeMaker fxmake;
    private final VisagecTaskImpl visagecTaskImpl;
    private final Context ctx;

    public static VisagecTrees instance(VisageCompiler.CompilationTask task) {
        if (!(task instanceof VisagecTaskImpl))
            throw new IllegalArgumentException();
        return instance(((VisagecTaskImpl)task).getContext());
    }

    public static VisagecTrees instance(Context context) {
        VisagecTrees instance = context.get(VisagecTrees.class);
        if (instance == null)
            instance = new VisagecTrees(context);
        return instance;
    }

    private VisagecTrees(Context context) {
        context.put(VisagecTrees.class, this);
        ctx = context;
        attr = VisageAttr.instance(context);
        enter = VisageEnter.instance(context);
        log = Log.instance(context);
        resolve = VisageResolve.instance(context);
        fxmake = VisageTreeMaker.instance(context);
        memberEnter = VisageMemberEnter.instance(context);
        visagecTaskImpl = context.get(VisagecTaskImpl.class);
    }

    public SourcePositions getSourcePositions() {
        return new SourcePositions() {
                public long getStartPosition(UnitTree file, Tree tree) {
                    return VisageTreeInfo.getStartPos((VisageTree) tree);
                }

                public long getEndPosition(UnitTree file, Tree tree) {
                    Map<JCTree,Integer> endPositions = ((VisageScript) file).endPositions;
                    return VisageTreeInfo.getEndPos((VisageTree)tree, endPositions);
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
        VisageEnv<VisageAttrContext> env = enter.getEnv(enclosing);
        if (env == null)
            return null;
        VisageClassDeclaration classNode = env.enclClass;
        if (classNode != null) {
            if (VisageTreeInfo.symbolFor(classNode) == element)
                return classNode;
            for (VisageTree node : classNode.getMembers())
                if (VisageTreeInfo.symbolFor(node) == element)
                    return node;
        }
        return null;
    }

    public VisageTreePath getPath(UnitTree unit, Tree node) {
        return getPath(new VisageTreePath(unit), node);
    }

    public VisageTreePath getPath(Element e) {
        final Pair<VisageTree, VisageScript> treeTopLevel = getTreeAndTopLevel(e);
        if (treeTopLevel == null)
            return null;
        return getPath(treeTopLevel.snd, treeTopLevel.fst);
    }
    
    /**
     * Gets a tree path for a tree node within a subtree identified by a VisageTreePath object.
     * @return null if the node is not found
     */
    public static VisageTreePath getPath(VisageTreePath path, Tree target) {
        path.getClass();
        target.getClass();

        class Result extends Error {
            static final long serialVersionUID = -5942088234594905625L;
            VisageTreePath path;
            Result(VisageTreePath path) {
                this.path = path;
            }
        }
        class PathFinder extends VisageTreePathScanner<VisageTreePath,Tree> {
            @Override
            public VisageTreePath scan(Tree tree, Tree target) {
                if (tree == target)
                    throw new Result(new VisageTreePath(getCurrentPath(), target));
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

    public Element getElement(VisageTreePath path) {
        Tree t = path.getLeaf();
        return VisageTreeInfo.symbolFor((VisageTree) t);
    }

    public TypeMirror getTypeMirror(VisageTreePath path) {
        Tree t = path.getLeaf();
        return ((VisageTree)t).type;
    }

    public VisagecScope getScope(VisageTreePath path) {
        return new VisagecScope(ctx, getAttrContext(path));
    }

    public boolean isAccessible(Scope scope, TypeElement type) {
        if (scope instanceof VisagecScope && type instanceof ClassSymbol) {
            VisageEnv<VisageAttrContext> env = ((VisagecScope) scope).env;
            return resolve.isAccessible(env, (ClassSymbol)type);
        } else
            return false;
    }

    public boolean isAccessible(Scope scope, Element member, DeclaredType type) {
        if (scope instanceof VisagecScope
                && member instanceof Symbol
                && type instanceof com.sun.tools.mjavac.code.Type) {
            VisageEnv<VisageAttrContext> env = ((VisagecScope) scope).env;
            return resolve.isAccessible(env, (com.sun.tools.mjavac.code.Type)type, (Symbol)member);
        } else
            return false;
    }

    private VisageEnv<VisageAttrContext> getAttrContext(VisageTreePath path) {
        if (!(path.getLeaf() instanceof VisageTree))  // implicit null-check
            throw new IllegalArgumentException();

        // if we're being invoked via from a JSR199 client, we need to make sure
        // all the classes have been entered; if we're being invoked from JSR269,
        // then the classes will already have been entered.
        if (visagecTaskImpl != null) {
            try {
                visagecTaskImpl.enter();
            } catch (IOException e) {
                throw new Error("unexpected error while entering symbols: " + e);
            }
        }

        VisageScript unit = (VisageScript) path.getCompilationUnit();
        Copier copier = new Copier(fxmake.forToplevel(unit));

        copier.endPositions = unit.endPositions;

        VisageEnv<VisageAttrContext> env = null;
        VisageFunctionDefinition function = null;
        VisageVar field = null;

        List<Tree> l = List.nil();
        VisageTreePath p = path;
        while (p != null) {
            l = l.prepend(p.getLeaf());
            p = p.getParentPath();
        }

        for ( ; l.nonEmpty(); l = l.tail) {
            Tree tree = l.head;
            if (tree instanceof VisageScript) {
                env = enter.getTopLevelEnv((VisageScript)tree);
            }
            else if (tree instanceof VisageClassDeclaration) {
                env = enter.getClassEnv(((VisageClassDeclaration)tree).sym);
            }
            else if (tree instanceof VisageFunctionDefinition) {
                function = (VisageFunctionDefinition)tree;
            }
            else if (tree instanceof VisageVar) {
                field = (VisageVar)tree;
            }
            else if (tree instanceof VisageBlock) {
                if (function != null)
                    env = memberEnter.getMethodEnv(function, env);
                VisageTree body = copier.copy((VisageTree)tree, (VisageTree) path.getLeaf());
                env = attribStatToTree(body, env, copier.leafCopy);
                return env;
            } else if (field != null && field.getInitializer() == tree) {
                env = memberEnter.getInitEnv(field, env);
                VisageExpression expr = copier.copy((VisageExpression)tree, (VisageTree) path.getLeaf());
                env = attribExprToTree(expr, env, copier.leafCopy);
                return env;
            }
        }
        return field != null ? memberEnter.getInitEnv(field, env) : env;
    }

    private VisageEnv<VisageAttrContext> attribStatToTree(VisageTree stat, VisageEnv<VisageAttrContext>env, VisageTree tree) {
        JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
        try {
            return attr.attribStatToTree(stat, env, tree);
        } finally {
            log.useSource(prev);
        }
    }

    private VisageEnv<VisageAttrContext> attribExprToTree(VisageExpression expr, VisageEnv<VisageAttrContext>env, VisageTree tree) {
        JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
        try {
            return attr.attribExprToTree(expr, env, tree);
        } finally {
            log.useSource(prev);
        }
    }
    
    private Pair<VisageTree, VisageScript> getTreeAndTopLevel(Element e) {
        if (e == null)
            return null;

        Symbol sym = (Symbol)e;
        TypeSymbol ts = (sym.kind != Kinds.PCK)
                        ? sym.enclClass()
                        : (PackageSymbol) sym;
        VisageEnv<VisageAttrContext> enterEnv = ts != null ? enter.getEnv(ts) : null;        
        if (enterEnv == null)
            return null;
        
        VisageTree tree = VisageTreeInfo.declarationFor(sym, enterEnv.tree);
        if (tree == null || enterEnv.toplevel == null)
            return null;
        return new Pair<VisageTree,VisageScript>(tree, enterEnv.toplevel);
    }

    public VisageEnv<VisageAttrContext> getFunctionEnv(VisageFunctionDefinition tree, VisageEnv<VisageAttrContext> env) {
        VisageEnv<VisageAttrContext> mEnv = memberEnter.methodEnv(tree, env);
        mEnv.info.lint = mEnv.info.lint.augment(tree.sym.attributes_field, tree.sym.flags());
        for (List<VisageVar> l = tree.getParams(); l.nonEmpty(); l = l.tail)
            mEnv.info.scope.enterIfAbsent(l.head.sym);
        return mEnv;
    }

    public VisageEnv<VisageAttrContext> getInitEnv(VisageVar tree, VisageEnv<VisageAttrContext> env) {
        VisageEnv<VisageAttrContext> iEnv = memberEnter.initEnv(tree, env);
        return iEnv;
    }

    /**
     * Makes a copy of a tree, noting the value resulting from copying a particular leaf.
     **/
    static class Copier extends VisageTreeCopier {
        VisageTree leaf;
        VisageTree leafCopy = null;

        Copier(VisageTreeMaker M) {
            super(M);
        }

        public <T extends VisageTree> T copy(T t, VisageTree leaf) {
            this.leaf = leaf;
            return copy(t);
        }
        
        @Override
        public <T extends VisageTree> T copy(T t) {
            T t2 = super.copy(t);
            if (t == leaf)
                leafCopy = t2;
            return t2;
        }

        @Override
        public void visitForExpressionInClause(VisageForExpressionInClause tree) {
            result = maker.InClause(copy(tree.var), copy(tree.getSequenceExpression()), copy(tree.getWhereExpression()));
        }
    }
}
