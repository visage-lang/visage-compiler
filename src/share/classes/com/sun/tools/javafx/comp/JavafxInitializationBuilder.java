/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.tree.JFXBlockExpression;
import com.sun.tools.javafx.tree.JavafxAbstractVisitor;
import com.sun.tools.javafx.tree.JavafxJCClassDecl;
import com.sun.tools.javafx.tree.JavafxJCNewClassObjectLiteral;
import com.sun.tools.javafx.tree.JavafxJCVarDecl;
import com.sun.tools.javafx.tree.JavafxTreeMaker;
import java.util.HashMap;
import java.util.Map;

public class JavafxInitializationBuilder extends JavafxAbstractVisitor {
    protected static final Context.Key<JavafxInitializationBuilder> javafxInitializationBuilderKey =
        new Context.Key<JavafxInitializationBuilder>();

    private JCClassDecl currentClassDef;
    private JavafxTreeMaker make;
    private Resolve rs;
    private Env<AttrContext> env;
    private JCStatement currentStatement;
    private JCTree currentBlock;
    private Map<JCNewClass, JavafxNewClassHelper> newsToProcess; 
    
    public static JavafxInitializationBuilder instance(Context context) {
        JavafxInitializationBuilder instance = context.get(javafxInitializationBuilderKey);
        if (instance == null)
            instance = new JavafxInitializationBuilder(context);
        return instance;
    }

    protected JavafxInitializationBuilder(Context context) {
        super(null);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        rs = Resolve.instance(context);
    }
    
    public void visitTopLevel(JCCompilationUnit cu, Env<AttrContext> env) {
        this.env = env;
        visitTopLevel(cu);
        this.env = null;
    }
    
    @Override
    public void visitClassDef(JCClassDecl tree) {
        JCClassDecl prevClassDef = currentClassDef;
        JCStatement prevStatement = currentStatement;
        try {
            currentClassDef = tree;
            currentStatement = tree;
            super.visitClassDef(tree);
            handleInitializerMethod();
            handleNewClasses();
        }
        finally {
            currentClassDef = prevClassDef;
            currentStatement = prevStatement;
        }
    }
    
    @Override
    public void visitVarDef(JCVariableDecl tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitVarDef(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitSkip(JCSkip tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitSkip(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitBlock(JCBlock tree) {
        JCStatement prevStatement = currentStatement;
        JCTree prevBlock = currentBlock;
        try {
            currentStatement = tree;
            currentBlock = tree;
            super.visitBlock(tree);
        }
        finally {
            currentStatement = prevStatement;
            currentBlock = prevBlock;
        }
    }
    
    @Override
    public void visitBlockExpression(JFXBlockExpression tree) {
        JCTree prevBlock = currentBlock;
        try {
            currentBlock = tree;
            super.visitBlockExpression(tree);
        }
        finally {
            currentBlock = prevBlock;
        }        
    }

    @Override
    public void visitDoLoop(JCDoWhileLoop tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitDoLoop(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitTry(JCTry tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitTry(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitIf(JCIf tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitIf(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitExec(JCExpressionStatement tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitExec(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitBreak(JCBreak tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitBreak(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitContinue(JCContinue tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitContinue(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitReturn(JCReturn tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitReturn(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitThrow(JCThrow tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitThrow(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitAssert(JCAssert tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitAssert(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitSynchronized(JCSynchronized tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitSynchronized(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitCase(JCCase tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitCase(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitSwitch(JCSwitch tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitSwitch(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitLabelled(JCLabeledStatement tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitLabelled(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitForeachLoop(JCEnhancedForLoop tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitForeachLoop(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitForLoop(JCForLoop tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitForLoop(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    @Override
    public void visitWhileLoop(JCWhileLoop tree) {
        JCStatement prevStatement = currentStatement;
        try {
            currentStatement = tree;
            super.visitWhileLoop(tree);
        }
        finally {
            currentStatement = prevStatement;
        }
    }

    public void visitNewClass(JCNewClass tree) {
        if (!(tree instanceof JavafxJCNewClassObjectLiteral)) {
            storeNewClassForProcessing(tree);
        }
        
        super.visitNewClass(tree);
    }
    
    private void storeNewClassForProcessing(JCNewClass newClass) {
        if (newsToProcess == null) {
            newsToProcess = new HashMap<JCNewClass, JavafxNewClassHelper>();
        }
        
        newsToProcess.put(newClass, new JavafxNewClassHelper(newClass, currentStatement, currentBlock));
    }
    
    private void handleInitializerMethod() {
        JCBlock initializerBlock = null;
        if (currentClassDef != null && 
                currentClassDef instanceof JavafxJCClassDecl &&
                currentClassDef.defs != null &&
                !currentClassDef.defs.isEmpty()) {
            // Initialize the default values for the atttributes.
            for (JCTree tree : currentClassDef.defs) {
                if (tree.getTag() == JCTree.VARDEF &&
                        tree instanceof JavafxJCVarDecl &&
                        (((JavafxJCVarDecl)tree).getJavafxVarType() == JavafxFlags.ATTRIBUTE || 
                            (((JavafxJCVarDecl)tree).getJavafxVarType() == JavafxFlags.LOCAL_ATTRIBUTE))) {
                    JavafxJCVarDecl jfxVarDecl = (JavafxJCVarDecl)tree;
                    JCLiteral jcLiteral = getEmptyLiteral(jfxVarDecl.type);
                    if (jcLiteral == null) {
                        continue;
                    }
                    
                    // If not initialized, skip it.
                    if (jfxVarDecl.init == null) {
                        continue;
                    }
                    
                    if (initializerBlock == null) {
                        initializerBlock = ((JavafxJCClassDecl)currentClassDef).initializer.body;
                    }
                    
                    // Assert that there is an initializerBlock
                    assert initializerBlock != null : "initializerBlock must not be null!";
                    
                    JCIdent lhsIdent = make.Ident(jfxVarDecl.name);
                    lhsIdent.sym = jfxVarDecl.sym;
                    lhsIdent.type = jfxVarDecl.type;
                    JCBinary cond = make.Binary(JCTree.EQ, lhsIdent, jcLiteral);
                    cond.type = Symtab.booleanType;
                    cond.operator = rs.resolveBinaryOperator(cond.pos(), cond.getTag(), env, lhsIdent.type, jcLiteral.type);

                    JCIdent lhsAssignIdent = make.Ident(jfxVarDecl.name);
                    lhsAssignIdent.sym = jfxVarDecl.sym;
                    lhsAssignIdent.type = jfxVarDecl.type;
                    JCAssign defValAssign = make.Assign(lhsAssignIdent, jfxVarDecl.init);
                    defValAssign.type = lhsAssignIdent.type;
                    
                    JCExpressionStatement defAttrValue = make.Exec(defValAssign);
                    defAttrValue.type = null;
                    
                    JCIf jcIf = make.If(cond, defAttrValue, null);
                    jcIf.type = null;
                    
                    initializerBlock.stats = initializerBlock.stats.append(jcIf);
// TODO: Enable when the constructors are handled....                    jfxVarDecl.init = null;
                    // TODO: Do super.initialize(), init block, new tyriggers, change attr triggers.
                }
            }
        }
    }
    
    // TODO: FixMe... Figure out what literal to create for the non-set primitives.
    private JCLiteral getEmptyLiteral(Type type) {
        JCLiteral ret = null;
        if (type == null) {
            return ret;
        }
        
        if (type.isPrimitive()) {
            if (type.tag == TypeTags.BOOLEAN) {
                ret = make.Literal(TypeTags.BOOLEAN, Boolean.FALSE);
                ret.type = Symtab.booleanType;
                return ret;
            }
            else if (type.tag == TypeTags.BYTE) {
                ret = make.Literal(TypeTags.BYTE, new Byte((byte)0));
                ret.type = Symtab.byteType;
                return ret;
            }
            else if (type.tag == TypeTags.CHAR) {
                ret = make.Literal(TypeTags.CHAR, new Character((char)0));
                ret.type = Symtab.charType;
                return ret;
            }
            else if (type.tag == TypeTags.DOUBLE) {
                ret = make.Literal(TypeTags.DOUBLE, new Double(0.0));
                ret.type = Symtab.doubleType;
                return ret;
            }
            else if (type.tag == TypeTags.FLOAT) {
                ret = make.Literal(TypeTags.FLOAT, new Float(0.0));
                ret.type = Symtab.floatType;
                return ret;
            }
            else if (type.tag == TypeTags.INT) {
                ret = make.Literal(TypeTags.INT, new Integer(0));
                ret.type = Symtab.intType;
                return ret;
            }
            else if (type.tag == TypeTags.LONG) {
                ret = make.Literal(TypeTags.LONG, new Long(0L));
                ret.type = Symtab.longType;
                return ret;
            }
            else if (type.tag == TypeTags.SHORT) {
                ret = make.Literal(TypeTags.SHORT, new Short((short)0));
                ret.type = Symtab.shortType;
                return ret;
            }
            else 
                return ret;
        }
        
        ret =  make.Literal(TypeTags.BOT, null);
        ret.type = type;
        return ret;
    }
 
    private void handleNewClasses() {
        // TODO: 
    }
    
    class JavafxNewClassHelper {
        public JCNewClass newClassTree;
        public JCStatement ownerStatement;
        public JCTree ownerBlock;
        
        JavafxNewClassHelper(JCNewClass newClassTree, JCStatement ownerStatement, JCTree ownerBlock) {
            this.newClassTree = newClassTree;
            this.ownerStatement = ownerStatement;
            this.ownerBlock = ownerBlock;
        }
    }
}