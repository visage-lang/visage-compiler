/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file tree accompanied this code.
 *
 * This code is distributed in the hope tree it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file tree
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

package com.sun.tools.javafx.tree;

import java.io.*;
import java.util.*;

import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.code.*;

import com.sun.tools.javac.code.Symbol.*;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.Pretty;

import static com.sun.tools.javac.code.Flags.*;

/** Prints out a tree as an indented Java source program.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code tree depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Robert Field
 */
public class JavafxPretty extends Pretty implements JavafxVisitor {
    
    public boolean retro = false;
    
    public JavafxPretty(Writer out, boolean sourceOutput) {
        super(out, sourceOutput);
    }
    
    public boolean shouldVisitRemoved() {
        return false;
    }
    
    public boolean shouldVisitSynthetic() {
        return true;
    }
    
    /** Visitor method: print expression tree.
     *  @param prec  The current precedence level.
     */
    public void printExpr(JCTree tree, int prec) throws IOException {
        int prevPrec = this.prec;
        try {
            this.prec = prec;
            if (tree == null) print("/*missing*/");
            else {
                tree.accept(this);
            }
        } catch (UncheckedIOException ex) {
            IOException e = new IOException(ex.getMessage());
            e.initCause(ex);
            throw e;
        } finally {
            this.prec = prevPrec;
        }
    }
    
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        try {
            println(); align();
            printDocComment(tree);
            print("class ");
            print(" ");
            print(tree.name);
            print(" {");
            println();
            indent();
            List<JFXMemberDeclaration> mems = tree.getDeclaredMembers();
            for (JFXMemberDeclaration mem : mems) {
                align();
                printExpr(mem);
            }
            undent();
            println();
            print("}");
            println(); align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitFunctionDeclaration(JFXFunctionMemberDeclaration tree) {
        printFuncOpDecl(tree, "function");
    }
    
    public void visitOperationDeclaration(JFXOperationMemberDeclaration tree) {
        printFuncOpDecl(tree, "operation");
    }
    
    public void visitOperationDefinition(JFXOperationMemberDefinition tree) {
        printFuncOpDef(tree, "operation");
    }
    
    public void visitFunctionDefinition(JFXFunctionMemberDefinition tree) {
        printFuncOpDef(tree, "function");
    }
    
    public void visitAttributeDeclaration(JFXAttributeDeclaration tree)  {
        try {
            printDocComment(tree);
            printExpr(tree.getModifiers());
            print(" attribute ");
            print(tree.getName());
            print(" ");
            if (tree.getType() != null) {
                printExpr(tree.getType());
            }
            if (tree.getInverse() != null) {
                print(" inverse ");
                printExpr(tree.getInverse());
            }
            print(";");
            println(); align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitAttributeDefinition(JFXAttributeDefinition tree) {
        try {
            printDocComment(tree);
            print("attribute ");
            printExpr(tree.getSelector());
            print(" = ");
            if (tree.getType() != null) {
                printExpr(tree.getType());
            }
            if (tree.isBound()) {
                print(" bind ");
            }
            if (tree.isLazy()) {
                print(" lazy ");
            }
            printExpr(tree.getInitializer());
            print(";");
            println(); align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitOperationLocalDefinition(JFXOperationLocalDefinition tree)  {
        printFuncOpLocalDef(tree, "operation");
    }
    
    public void visitFunctionLocalDefinition(JFXFunctionLocalDefinition tree) {
        printFuncOpLocalDef(tree, "function");
    }
    
    public void visitMemberSelector(JFXMemberSelector tree) {
        try {
            print(tree.getClassName());
            print(".");
            print(tree.getName());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void printFuncOpDef(JFXFuncOpMemberDefinition tree, String which) {
        try {
            println(); align();
            printDocComment(tree);
            print(which);
            print(" ");
            printExpr(tree.getSelector());
            print("(");
            printExprs(tree.getParameters());
            print(")");
            if (tree.getType() != null) {
                printExpr(tree.getType());
            }
            if (tree.getBody() != null) {
                print(" ");
                printStat(tree.getBody());
            } else {
                print(";");
            }
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void printFuncOpLocalDef(JFXFuncOpLocalDefinition tree, String which) {
        try {
            println(); align();
            printDocComment(tree);
            print(which);
            print(" ");
            print(tree.getName());
            print("(");
            printExprs(tree.getParameters());
            print(")");
            if (tree.getType() != null) {
                printExpr(tree.getType());
            }
            if (tree.getBody() != null) {
                print(" ");
                printStat(tree.getBody());
            } else {
                print(";");
            }
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void printFuncOpDecl(JFXFuncOpMemberDeclaration tree, String which) {
        try {
            println(); align();
            printDocComment(tree);
            printExpr(tree.mods);
            print(" ");
            print(which);
            print(" ");
            print(tree.getName());
            print("(");
            printExprs(tree.getParameters());
            print(")");
            if (tree.getType() != null) {
                printExpr(tree.getType());
            }
            print(";");
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitDoLater(JFXDoLater tree) {
        try {
            println(); align();
            print("do later ");
            if (tree.body != null) {
                print(" ");
                printStat(tree.body);
            } else {
                print(";");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitTriggerOnInsert(JFXTriggerOnInsert tree) {
        visitTree(tree);
    }
    
    public void visitTriggerOnDelete(JFXTriggerOnDelete tree) {
        visitTree(tree);
    }
    
    public void visitTriggerOnDeleteElement(JFXTriggerOnDeleteElement tree) {
        visitTree(tree);
    }
    
    /**
     * changeRule		:=	'trigger' 'on' changeCondition block
     * changeCondition		:=	...
     * |	[ Identifier '=' ] 'new' Identifier
     *
     */
    public void visitTriggerOnNew(JFXTriggerOnNew tree) {
        try {
            println(); align();
            print("trigger on ");
            JCExpression id = tree.getNewValueIdentifier();
            if (id != null ) {
                printExpr(tree.getNewValueIdentifier());
                print(" = ");
            }
            print(" new ");
            printExpr(tree.getClassIdentifier());
            print(" ");
            printExpr(tree.getBlock());
            println(); align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    /**
     * changeCondition	:=	...
     *                  |	memberSelector '=' Identifier
     */
    public void visitTriggerOnReplace(JFXTriggerOnReplace tree) {
        try {
            println(); align();
            print("trigger on ");
            printExpr(tree.getSelector());
            print(" = ");
            printExpr(tree.getNewValueIdentifier());
            print(" ");
            printExpr(tree.getBlock());
            println(); align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    /**
     * changeCondition	:=	...
     *                  |	memberSelector '[' Identifier ']' '=' Identifier
     */
    public void visitTriggerOnReplaceElement(JFXTriggerOnReplaceElement tree) {
        try {
            println(); align();
            print("trigger on ");
            printExpr(tree.getSelector());
            print(" [ ");
            printExpr(tree.getElementIdentifier());
            print(" ] =  ");
            printExpr(tree.getNewValueIdentifier());
            print(" ");
            printExpr(tree.getBlock());
            println(); align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitStringExpression(JFXStringExpression tree) {
        try {
            int i;
            List<JCExpression> parts = tree.getParts();
            for (i = 0; i < parts.length() - 1; i += 3) {
                printExpr(parts.get(i));
                print("{");
                JCExpression format = parts.get(i+1);
                if (format != null) {
                    printExpr(format);
                }
                printExpr(parts.get(i+2));
                print("}");
            }
            printExpr(parts.get(i));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitPureObjectLiteral(JFXPureObjectLiteral tree) {
        try {
            JCExpression id = tree.getIdentifier();
            if (id != null) {
                printExpr(id);
            }
            print(" {");
            indent();
            List<JFXStatement> mems = tree.getParts();
            for (JFXStatement mem : mems) {
                println(); align();
                printExpr(mem);
            }
            undent();
            println(); align();
            print("}");
            println(); align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitVarIsObjectBeingInitialized(JFXVarIsObjectBeingInitialized tree) {
        try {
            print("var : ");
            print(tree.getName());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized tree) {
        try {
            print("attribute : ");
            print(tree.getAttributeName());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        try {
            print(tree.getName());
            print(" : ");
            if (tree.isUnidiBind()) {
                print(" stays ");
            }
            if (tree.isBidiBind()) {
                print(" tie ");
            }
            if (tree.isLazy()) {
                print(" lazy ");
            }
            printExpr(tree.getExpression());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitTypeAny(JFXTypeAny tree) {
        try {
            print(" : * ");
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitTypeClass(JFXTypeClass tree) {
        try {
            print(" : ");
            print(tree.getClassName());
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitTypeFunctional(JFXTypeFunctional tree) {
        try {
            print(" : (");
            printExprs(tree.getParameters());
            print(")");
            printExpr(tree.getReturnType());
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitTypeUnknown(JFXTypeUnknown tree) {
        try {
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    String ary(JFXType tree) {
        String show;
        switch (tree.getCardinality()) {
        case JFXType.CARDINALITY_ANY:
            return "[]";
        case JFXType.CARDINALITY_OPTIONAL:
            return " ";
        }
        return "";
    }
    
    public void visitVar(JFXVar tree) {
        try {
            print(tree.getName());
            if (tree.getType() != null) {
                printExpr(tree.getType());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitVarStatement(JFXVarStatement tree) {
        try {
            print("var ");
            visitVar(tree);
            if (tree instanceof JFXVarInit) {
                print(" = ");
                printExpr(((JFXVarInit)tree).getInitializer());
            }
            print(";");
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitVarInit(JFXVarInit tree) {
        visitVarStatement(tree);
    }
    
    public void visitTree(JCTree tree) {
        assert false : "Should not be here!!!";
    }
}
