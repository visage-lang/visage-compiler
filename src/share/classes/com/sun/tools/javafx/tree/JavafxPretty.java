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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.ForExpressionInClauseTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Position;

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
    public static final int SCOPE_OUTER = 0;
    public static final int SCOPE_CLASS = 1;
    public static final int SCOPE_METHOD = 2;
    public static final int SCOPE_PARAMS = 3;
    protected int variableScope = SCOPE_OUTER;

    /** A hashtable mapping trees to their documentation comments
     *  (can be null)
     */
    Map<JCTree, String> docComments = null;
    
    CharSequence sourceContent;

    public JavafxPretty(Writer out, boolean sourceOutput, CharSequence content) {
        super(out, sourceOutput);
        sourceContent = content;
    }

    public JavafxPretty(Writer out, boolean sourceOutput) {
        super(out, sourceOutput);
        sourceContent = null;
    }

    @Override
    public void printUnit(JCCompilationUnit tree, JCClassDecl cdef) throws IOException {
        docComments = tree.docComments;
        super.printUnit(tree, cdef);
    }
    
    private String posAsString(int pos) {
        if (pos == Position.NOPOS || sourceContent == null) {
            return "%?%";
        }
        int line = 1;
        int bp = 0;
        while (bp < sourceContent.length() && bp < pos) {
            switch (sourceContent.charAt(bp++)) {
                case 0xD: //CR
                    if (bp < sourceContent.length() && sourceContent.charAt(bp) == 0xA) {
                        bp++;
                    }
                    line++;
                    break;
                case 0xA: //LF
                    line++;
                    break;
                }
        }
        return " %(" + pos + ")" + line + "% ";
    }

    /** Visitor method: print expression tree.
     *  @param prec  The current precedence level.
     */
    @Override
    public void printExpr(JCTree tree, int prec) throws IOException {
        int prevPrec = this.prec;
        try {
//          uncomment to debug position information
//            println();
//            print(posAsString(tree.getStartPosition()));
            this.prec = prec;
            if (tree == null) {
                print("/*missing*/");
            } else {
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
            int oldScope = variableScope;
            variableScope = SCOPE_CLASS;
            println();
            align();
            printDocComment(tree);
            print("class ");
            print(tree.getName());
            print(" {");
            println();
            indent();
            for (JCTree mem : tree.getMembers()) {
                align();
                printExpr(mem);
            }
            undent();
            println();
            print("}");
            println();
            align();
            variableScope = oldScope;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

     public static void visitOperationValue(Pretty pretty, JFXOperationValue tree) {
        try {
            pretty.println();
            pretty.align();
            pretty.printDocComment(tree);
            pretty.print("function ");
            pretty.print("(");
            pretty.printExprs(tree.getParams());
            pretty.print(")");
            if (tree.getType() != null) {
                pretty.printExpr(tree.getType());
            }
            JFXBlockExpression body = tree.getBodyExpression();
            if (body != null) {
                pretty.printExpr(body);
            }
            pretty.println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

  public void visitOperationValue(JFXOperationValue tree) {
          visitOperationValue(this, tree);
    }

    public static void visitOperationDefinition(Pretty pretty, JFXOperationDefinition tree) {
        try {
            JavafxPretty fxpretty = (JavafxPretty)pretty;
            int oldScope = fxpretty.variableScope;
            pretty.println();
            pretty.align();
            pretty.printDocComment(tree);
            pretty.printExpr(tree.mods);
            pretty.print("function ");
            pretty.print(tree.name);
            pretty.print("(");
            fxpretty.variableScope = SCOPE_PARAMS;
            pretty.printExprs(tree.getParameters());
            fxpretty.variableScope = SCOPE_METHOD;
            pretty.print(")");
            pretty.printExpr(tree.operation.rettype);
            JFXBlockExpression body = tree.getBodyExpression();
            if (body != null) {
                pretty.print(" ");
                pretty.printExpr(body);
            }
            pretty.println();
            fxpretty.variableScope = oldScope;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitOperationDefinition(JFXOperationDefinition tree) {
        visitOperationDefinition(this, tree);
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        try {
            println();
            align();
            printDocComment(tree);
            print("init ");
            print(tree.getBody());
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        try {
            println();
            align();
            printDocComment(tree);
            print("postinit ");
            print(tree.getBody());
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        visitBlockExpression(this, tree);
    }

    public static void visitBlockExpression(Pretty pretty, JFXBlockExpression tree) {
        try {
            pretty.printFlags(tree.flags);
            pretty.print("{");
            pretty.println();
            pretty.indent();
            pretty.printStats(tree.stats);
            if (tree.value != null) {
                pretty.align();
                pretty.printExpr(tree.value);
            }
            pretty.undent();
            pretty.println();
            pretty.align();
            pretty.print("}");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

    void printBind(JavafxBindStatus bindStatus) {
        try {
            if (bindStatus.isUnidiBind()) {
                print(" bind /*stays*/ ");
            }
            if (bindStatus.isBidiBind()) {
                print(" bind /*tie*/ ");
            }
            if (bindStatus.isLazy()) {
                print(" bind /*lazy*/ ");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitConditional(JCConditional tree) {
        try {
            print("if (");
            printExpr(tree.cond, TreeInfo.condPrec);
            print(") ");
            printExpr(tree.truepart, TreeInfo.condPrec);
            if (tree.falsepart != null) {
                print(" else ");
                printExpr(tree.falsepart, TreeInfo.condPrec);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitDoLater(JFXDoLater tree) {
        try {
            println();
            align();
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

    public void visitSequenceEmpty(JFXSequenceEmpty that) {
        try {
            print("[]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitSequenceRange(JFXSequenceRange that) {
         try {
            print("[");
            printExpr(that.getLower());
            print("..");
            printExpr(that.getUpper());
            if (that.getStepOrNull() != null) {
                print("step ");
                printExpr(that.getStepOrNull());
            }
            if (that.isExclusive()) {
                print(" exclusive");
            }
            print("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        try {
            boolean first = true;
            print("[");
            for (JCExpression expr : that.getItems()) {
                if (!first) {
                    print(", ");
                }
                first = false;
                printExpr(expr);
            }
            print("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        try {
            printExpr(that.getSequence());
            print("[ ");
            printExpr(that.getIndex());
            print("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

     public void visitSequenceSlice(JFXSequenceSlice that) {
        try {
            printExpr(that.getSequence());
            print("[ ");
            printExpr(that.getFirstIndex());
            print(" .. ");
            printExpr(that.getLastIndex());
            print("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Override
    public void visitSequenceInsert(JFXSequenceInsert that) {
        try {
            print("insert ");
            printExpr(that.getElement());
            print(" into ");
            printExpr(that.getSequence());
            print("; ");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Override
    public void visitSequenceDelete(JFXSequenceDelete that) {
        try {
            print("delete ");
            printExpr(that.getSequence());
            if (that.getElement() != null) {
                print(" (");
                printExpr(that.getElement());
                print(")");
            }
            print("; ");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        try {
            int i;
            List<JCExpression> parts = tree.getParts();
            for (i = 0; i < parts.length() - 1; i += 3) {
                printExpr(parts.get(i));
                print("{");
                JCExpression format = parts.get(i + 1);
                if (format != null) {
                    printExpr(format);
                }
                printExpr(parts.get(i + 2));
                print("}");
            }
            printExpr(parts.get(i));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitInstanciate(JFXInstanciate tree) {
        try {
            JCExpression id = tree.getIdentifier();
            if (tree.getArgs().nonEmpty())
                print("new ");
            if (id != null) {
                printExpr(id);
            }
            if (tree.getArgs().nonEmpty()) {
                // Java constructor call
                print("(");
                printExprs(tree.getArgs());
                print(")");
            } else {
                // JFX instantiation
                print(" {");
                if (tree.getParts().nonEmpty()) {
                    indent();
                    for (JFXObjectLiteralPart mem : tree.getParts()) {
                        println();
                        align();
                        printExpr(mem);
                    }
                    //TODO: add defs
                    undent();
                    println();
                    align();
                }
                print("}");
            }
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
            print(": ");
            printBind(tree.getBindStatus());
            printExpr(tree.getExpression());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeAny(JFXTypeAny tree) {
        try {
            print(": * ");
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitTypeCast(JCTypeCast tree) {
        try {
            printExpr(tree.expr, TreeInfo.prefixPrec);
            print(" as ");
            printExpr(tree.clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeClass(JFXTypeClass tree) {
        try {
            print(": ");
            print(tree.getClassName());
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeFunctional(JFXTypeFunctional tree) {
        try {
            print(": (");
            printExprs(tree.getParams());
            print(")");
            printExpr((JFXType)tree.getReturnType());
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
            case ANY:
                return "[]";
            case SINGLETON:
                return "";
        }
        return "";
    }

    public void visitVar(JFXVar tree) {
        try {
            if (docComments != null && docComments.get(tree) != null) {
                println(); align();
            }
            printDocComment(tree);
            printExpr(tree.mods);
            if (variableScope == SCOPE_CLASS)
                print("attribute ");
            else if (variableScope != SCOPE_PARAMS)
                print("var ");
            print(tree.getName());
            printExpr(tree.getJFXType());
            if (variableScope != SCOPE_PARAMS) {
                if (tree.getInitializer() != null) {
                    print(" = ");
                    printExpr(tree.getInitializer());
                }
                print(";");
                if (variableScope == SCOPE_OUTER || variableScope == SCOPE_CLASS)
                    println();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitAbstractOnChange(JFXAbstractOnChange tree) {
        try {
            if (tree.getIndex() != null) {
                print(" [ ");
                printExpr(tree.getIndex());
                print(" ] ");
            }
            if (tree.getOldValue() != null) {
                print(" ( ");
                printExpr(tree.getOldValue());
                print(" ) ");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }     
    }
    
    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        try {
            print(" on replace");
            if (tree.getOldValue() != null) {
                print(" ");
                printExpr(tree.getOldValue());
            }
            if (tree.getIndex() != null) {
                print("[");
                printExpr(tree.getIndex());
                if (tree.getLastIndex() != null) {
                    print(" .. ");
                    printExpr(tree.getLastIndex());
                }
                print(" ]");
            }
            if (tree.getNewElements() != null) {
                print("= ");
                printExpr(tree.getNewElements());
            }
            print(" ");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitOnReplaceElement(JFXOnReplaceElement tree) {
        try {
            print(" on replace ");
            visitAbstractOnChange(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitOnInsertElement(JFXOnInsertElement tree) {
        try {
            print(" on insert ");
            visitAbstractOnChange(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitOnDeleteElement(JFXOnDeleteElement tree) {
        try {
            print(" on delete ");
            visitAbstractOnChange(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitOnDeleteAll(JFXOnDeleteAll tree) {
        try {
            print(" on delete ");
            visitAbstractOnChange(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitForExpression(JFXForExpression tree) {
        try {
            boolean first = true;
            print("foreach (");
            for (ForExpressionInClauseTree cl : tree.getInClauses()) {
                JFXForExpressionInClause clause = (JFXForExpressionInClause)cl;
                if (first) {
                    first = false;
                } else {
                    print(", ");
                }
                print(clause.getVar().getName());
                print(" in ");
                printExpr(clause.getSequenceExpression());
                if (clause.getWhereExpression() != null) {
                    print(" where ");
                    printExpr(clause.getWhereExpression());
                }
            }
            print(") ");
            printExpr(tree.getBodyExpression());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should not reach here";
    }
    
    @Override
    public void visitUnary(JCUnary tree) {
        try {
           if (tree.getTag() == JavafxTag.SIZEOF) {
               print("(sizeof ");
               printExpr(tree.arg);
               print(")");
           } else {
               super.visitUnary(tree);
           }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitTree(JCTree tree) {
        assert false : "Should not be here!!!";
    }

    @Override
    public String operatorName(int tag) {
        switch(tag) {
            case JCTree.OR:      return "or";
            case JCTree.AND:     return "and";
            case JCTree.EQ:      return "==";
            case JCTree.NE:      return "<>";
            default: return super.operatorName(tag);
        }
    }
    
    /** Convert a tree to a pretty-printed string. */
    public static String toString(JCTree tree) {
        StringWriter s = new StringWriter();
        try {
            new JavafxPretty(s, false).printExpr(tree);
        }
        catch (IOException e) {
            // should never happen, because StringWriter is defined             
            // never to throw any IOExceptions                                  
            throw new AssertionError(e);
        }
        return s.toString();
    }
}
