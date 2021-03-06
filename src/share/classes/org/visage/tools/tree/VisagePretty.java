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

package org.visage.tools.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.visage.api.VisageBindStatus;
import org.visage.api.tree.ForExpressionInClauseTree;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.TypeTags;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.util.Convert;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.Position;
import org.visage.tools.code.VisageFlags;

import static com.sun.tools.mjavac.code.Flags.*;

/** Prints out a tree as an indented Java source program.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code tree depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Robert Field
 */
public class VisagePretty implements VisageVisitor {
    public static final int SCOPE_OUTER = 0;
    public static final int SCOPE_CLASS = 1;
    public static final int SCOPE_METHOD = 2;
    public static final int SCOPE_PARAMS = 3;
    protected int variableScope = SCOPE_OUTER;

   /** Set when we are producing source output.  If we're not
     *  producing source output, we can sometimes give more detail in
     *  the output even though that detail would not be valid java
     *  soruce.
     */
    protected final boolean sourceOutput;

    /** The output stream on which trees are printed.
     */
    Writer out;

    /** Indentation width (can be reassigned from outside).
     */
    public int width = 4;

    /** The current left margin.
     */
    int lmargin = 0;

    /** A hashtable mapping trees to their documentation comments
     *  (can be null)
     */
    Map<JCTree, String> docComments = null;
    
    CharSequence sourceContent;

    public VisagePretty(Writer out, boolean sourceOutput, CharSequence content) {
        this.out = out;
        this.sourceOutput = sourceOutput;
        sourceContent = content;
    }

    public VisagePretty(Writer out, boolean sourceOutput) {
        this.out = out;
        this.sourceOutput = sourceOutput;
        sourceContent = null;
    }

    /** Align code to be indented to left margin.
     */
    public void align() throws IOException {
        for (int i = 0; i < lmargin; i++) out.write(" ");
    }

    /** Increase left margin by indentation width.
     */
    public void indent() {
        lmargin = lmargin + width;
    }

    /** Decrease left margin by indentation width.
     */
    public void undent() {
        lmargin = lmargin - width;
    }

    /** Enter a new precedence level. Emit a `(' if new precedence level
     *  is less than precedence level so far.
     *  @param contextPrec    The precedence level in force so far.
     *  @param ownPrec        The new precedence level.
     */
    void open(int contextPrec, int ownPrec) throws IOException {
        if (ownPrec < contextPrec) out.write("(");
    }

    /** Leave precedence level. Emit a `(' if inner precedence level
     *  is less than precedence level we revert to.
     *  @param contextPrec    The precedence level we revert to.
     *  @param ownPrec        The inner precedence level.
     */
    void close(int contextPrec, int ownPrec) throws IOException {
        if (ownPrec < contextPrec) out.write(")");
    }

    /** Print string, replacing all non-ascii character with unicode escapes.
     */
    public void print(Object s) throws IOException {
        // s may be null for CATCH in "try {} catch() {}"
        out.write(Convert.escapeUnicode((s != null) ? s.toString() : ""));
    }

    /** Print new line.
     */
    public void println() throws IOException {
        out.write(lineSep);
    }

    String lineSep = System.getProperty("line.separator");

    /**************************************************************************
     * Traversal methods
     *************************************************************************/

    /** Exception to propogate IOException through visitXXX methods */
    protected static class UncheckedIOException extends RuntimeException {
        static final long serialVersionUID = -4032692679158424751L;
        public UncheckedIOException(IOException e) {
            super(e.getMessage(), e);
        }
    }

    /** Visitor argument: the current precedence level.
     */
    protected int prec;

    /** Visitor method: print expression tree.
     *  @param prec  The current precedence level.
     */
    public void printExpr(VisageTree tree, int prec) throws IOException {
        int prevPrec = this.prec;
        try {
//          uncomment to debug position information
//            println();
//            print(posAsString(tree.getStartPosition()));
            this.prec = prec;
            if (tree == null || tree instanceof VisageErroneous) {
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

    /** Derived visitor method: print expression tree at minimum precedence level
     *  for expression.
     */
    public void printExpr(VisageTree tree) throws IOException {
        printExpr(tree, VisageTreeInfo.noPrec);
    }

    /** Derived visitor method: print statement tree.
     */
    public void printStat(VisageTree tree) throws IOException {
        printExpr(tree, VisageTreeInfo.notExpression);
    }

    /** Derived visitor method: print list of expression trees, separated by given string.
     *  @param sep the separator string
     */
    public <T extends VisageTree> void printExprs(List<T> trees, String sep) throws IOException {
        if (trees.nonEmpty()) {
            printExpr(trees.head);
            for (List<T> l = trees.tail; l.nonEmpty(); l = l.tail) {
                print(sep);
                printExpr(l.head);
            }
        }
    }

    /** Derived visitor method: print list of expression trees, separated by commas.
     */
    public <T extends VisageTree> void printExprs(List<T> trees) throws IOException {
        printExprs(trees, ", ");
    }

    /** Derived visitor method: print list of statements, each on a separate line.
     */
    public void printStats(List<? extends VisageTree> trees) throws IOException {
        for (List<? extends VisageTree> l = trees; l.nonEmpty(); l = l.tail) {
            align();
            printStat(l.head);
            println();
        }
    }

    /** Print a set of modifiers.
     */
    public void printFlags(long flags) throws IOException {
        String sf = VisageTreeInfo.flagNames(flags, true);
        print(sf);
        if (sf.length() > 0) print(" ");
        if ((flags & ANNOTATION) != 0) print("@");
    }

    /** Print documentation comment, if it exists
     *  @param tree    The tree for which a documentation comment should be printed.
     */
    public void printDocComment(VisageTree tree) throws IOException {
        if (docComments != null) {
            String dc = docComments.get(tree);
            if (dc != null) {
                int pos = 0;
                int endpos = lineEndPos(dc, pos);
                while (pos < dc.length()) {
                    align();
                    print(dc.substring(pos, endpos)); 
                    pos = endpos + 1;
                    if (pos < dc.length()) println();
                    endpos = lineEndPos(dc, pos);
                }
                align();
            }
        }
    }
//where
    static int lineEndPos(String s, int start) {
        int pos = s.indexOf('\n', start);
        if (pos < 0) pos = s.length();
        return pos;
    }

    /** Print a block.
     */
    public void printBlock(List<? extends VisageTree> stats) throws IOException {
        print("{");
        println();
        indent();
        printStats(stats);
        undent();
        align();
        print("}");
    }

    /** Print unit consisting of package clause and import statements in toplevel,
     *  followed by class definition. if class definition == null,
     *  print all definitions in toplevel.
     *  @param tree     The toplevel tree
     *  @param cdef     The class definition, which is assumed to be part of the
     *                  toplevel tree.
     */
    public void printUnit(VisageScript tree) throws IOException {
        docComments = tree.docComments;
        printDocComment(tree);
        if (tree.pid != null) {
            print("package ");
            printExpr(tree.pid);
            print(";");
            println();
        }
        boolean firstImport = true;
        for (List<VisageTree> l = tree.defs; l.nonEmpty(); l = l.tail) {
            if (l.head.getVisageTag() == VisageTag.IMPORT) {
                VisageImport imp = (VisageImport)l.head;
                    if (firstImport) {
                        firstImport = false;
                        println();
                    }
                    printStat(imp);
            } else {
                printStat(l.head);
            }
        }
    }
    // where
    boolean isUsed(final Symbol t, VisageTree cdef) {
        class UsedVisitor extends VisageTreeScanner {
            @Override
            public void scan(VisageTree tree) {
                if (tree!=null && !result) tree.accept(this);
            }
            boolean result = false;
            @Override
            public void visitIdent(VisageIdent tree) {
                if (tree.sym == t) result = true;
            }
        }
        UsedVisitor v = new UsedVisitor();
        v.scan(cdef);
        return v.result;
    }

    /**************************************************************************
     * Visitor methods
     *************************************************************************/

    public void visitScript(VisageScript tree) {
        try {
            printUnit(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitImport(VisageImport tree) {
        try {
            print("import ");
            printExpr(tree.qualid);
            print(";");
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitSkip(VisageSkip tree) {
        try {
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitWhileLoop(VisageWhileLoop tree) {
        try {
            print("while ");
            if (tree.cond.getVisageTag() == VisageTag.PARENS) {
                printExpr(tree.cond);
            } else {
                print("(");
                printExpr(tree.cond);
                print(")");
            }
            print(" ");
            printStat(tree.body);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTry(VisageTry tree) {
        try {
            print("try ");
            printStat(tree.body);
            for (List<VisageCatch> l = tree.catchers; l.nonEmpty(); l = l.tail) {
                printStat(l.head);
            }
            if (tree.finalizer != null) {
                print(" finally ");
                printStat(tree.finalizer);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitCatch(VisageCatch tree) {
        try {
            print(" catch (");
            printExpr(tree.param);
            print(") ");
            printStat(tree.body);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitIfExpression(VisageIfExpression tree) {
        try {
            print(" if (");
            printExpr(tree.cond);
            print(") ");
            printExpr(tree.truepart);
            if (tree.falsepart != null) {
                print(" else ");
                printExpr(tree.falsepart);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitBreak(VisageBreak tree) {
        try {
            print("break");
            if (tree.label != null) print(" " + tree.label);
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitContinue(VisageContinue tree) {
        try {
            print("continue");
            if (tree.label != null) print(" " + tree.label);
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitReturn(VisageReturn tree) {
        try {
            print("return");
            if (tree.expr != null) {
                print(" ");
                printExpr(tree.expr);
            }
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitThrow(VisageThrow tree) {
        try {
            print("throw ");
            printExpr(tree.expr);
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitFunctionInvocation(VisageFunctionInvocation tree) {
        try {
            if (!tree.typeargs.isEmpty()) {
                if (tree.meth.getVisageTag() == VisageTag.SELECT) {
                    VisageSelect left = (VisageSelect)tree.meth;
                    printExpr(left.selected);
                    print(".<");
                    printExprs(tree.typeargs);
                    print(">" + left.name);
                } else {
                    print("<");
                    printExprs(tree.typeargs);
                    print(">");
                    printExpr(tree.meth);
                }
            } else {
                printExpr(tree.meth);
            }
            print("(");
            printExprs(tree.args);
            print(")");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitParens(VisageParens tree) {
        try {
            print("(");
            printExpr(tree.expr);
            print(")");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitAssign(VisageAssign tree) {
        try {
            open(prec, VisageTreeInfo.assignPrec);
            printExpr(tree.lhs, VisageTreeInfo.assignPrec + 1);
            print(" = ");
            printExpr(tree.rhs, VisageTreeInfo.assignPrec);
            close(prec, VisageTreeInfo.assignPrec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String operatorName(VisageTag tag) {
        switch(tag) {
            case NEG:     return "-";
            case NOT:     return "not";
            case PREINC:  return "++";
            case PREDEC:  return "--";
            case POSTINC: return "++";
            case POSTDEC: return "--";
            case NULLCHK: return "<*nullchk*>";
            case OR:      return "or";
            case AND:     return "and";
            case EQ:      return "==";
            case NE:      return "!=";
            case LT:      return "<";
            case GT:      return ">";
            case LE:      return "<=";
            case GE:      return ">=";
            case PLUS:    return "+";
            case MINUS:   return "-";
            case MUL:     return "*";
            case DIV:     return "/";
            case MOD:     return "%";
            case PLUS_ASG:   return "+=";
            case MINUS_ASG:  return "-=";
            case MUL_ASG:    return "*=";
            case DIV_ASG:    return "/=";
            case REVERSE:    return "reverse";
            case INDEXOF:    return "indexof";
            case SIZEOF:     return "sizeof";
            default: return "[unexpected operator tag "+tag+"]";
        }
    }

    public void visitAssignop(VisageAssignOp tree) {
        try {
            open(prec, VisageTreeInfo.assignopPrec);
            printExpr(tree.lhs, VisageTreeInfo.assignopPrec + 1);
            print(" " + operatorName(tree.getVisageTag()));
            printExpr(tree.rhs, VisageTreeInfo.assignopPrec);
            close(prec, VisageTreeInfo.assignopPrec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //@Override
    public void visitUnary(VisageUnary tree) {
        try {
           if (tree.getVisageTag() == VisageTag.SIZEOF) {
               print("(sizeof ");
               printExpr(tree.arg);
               print(")");
            } else {
                int ownprec = VisageTreeInfo.opPrec(tree.getVisageTag());
                String opname = operatorName(tree.getVisageTag());
                open(prec, ownprec);
                if (tree.getVisageTag().ordinal() <= VisageTag.PREDEC.ordinal()) {
                    print(opname);
                    printExpr(tree.arg, ownprec);
                } else {
                    printExpr(tree.arg, ownprec);
                    print(opname);
                }
                close(prec, ownprec);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitBinary(VisageBinary tree) {
        try {
            int ownprec = VisageTreeInfo.opPrec(tree.getVisageTag());
            String opname = operatorName(tree.getVisageTag());
            open(prec, ownprec);
            printExpr(tree.lhs, ownprec);
            print(" " + opname + " ");
            printExpr(tree.rhs, ownprec + 1);
            close(prec, ownprec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeCast(VisageTypeCast tree) {
        try {
            printExpr(tree.expr, VisageTreeInfo.prefixPrec);
            print(" as ");
            printExpr(tree.clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitInstanceOf(VisageInstanceOf tree) {
        try {
            open(prec, VisageTreeInfo.ordPrec);
            printExpr(tree.expr, VisageTreeInfo.ordPrec);
            print(" instanceof ");
            printExpr(tree.clazz, VisageTreeInfo.ordPrec + 1);
            close(prec, VisageTreeInfo.ordPrec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitSelect(VisageSelect tree) {
        try {
            printExpr(tree.selected, VisageTreeInfo.postfixPrec);
            print("." + tree.name);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitIdent(VisageIdent tree) {
        try {
            print(tree.getName());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitLiteral(VisageLiteral tree) {
        try {
            switch (tree.typetag) {
                case TypeTags.BYTE:
                case TypeTags.SHORT:
                case TypeTags.INT:
                    print(tree.value.toString());
                    break;
                case TypeTags.LONG:
                    print(tree.value + "L");
                    break;
                case TypeTags.FLOAT:
                    print(tree.value + "F");
                    break;
                case TypeTags.DOUBLE:
                    print(tree.value.toString());
                    break;
                case TypeTags.CHAR:
                    print("\'" +
                            Convert.quote(
                            String.valueOf((char)((Number)tree.value).intValue())) +
                            "\'");
                    break;
                case TypeTags.BOOLEAN:
                    print(((Number)tree.value).intValue() == 1 ? "true" : "false");
                    break;
                case TypeTags.BOT:
                    print("null");
                    break;
                default:
                    print("\"" + Convert.quote(tree.value.toString()) + "\"");
                    break;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public void visitErroneous(VisageErroneous tree) {
        try {
            print("(ERROR)");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitModifiers(VisageModifiers mods) {
        try {
            printFlags(mods.flags);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTree(VisageTree tree) {
        try {
            print("(UNKNOWN: " + tree + ")");
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

    private void printInterpolateValue(VisageInterpolateValue tree) {
        try {
            if (tree.getAttribute() != null) {
                print(tree.getAttribute());
                print("=>");
            }
            print(tree.getValue());
            printTween(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitClassDeclaration(VisageClassDeclaration tree) {
        try {
            int oldScope = variableScope;
            variableScope = SCOPE_CLASS;
            println();
            align();
            printDocComment(tree);
            printFlags(tree.getModifiers().flags);
            print("class ");
            Name n = tree.getName();
            print(n == null ? "<anonymous>" : n);
            if (tree.getSupertypes().nonEmpty()) {
                print(" extends");
                for (VisageExpression sup : tree.getSupertypes()) {
                    print(" ");
                    printExpr(sup);
                }
            }
            print(" {");
            println();
            indent();
            for (VisageTree mem : tree.getMembers()) {
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

     public static void visitFunctionValue(VisagePretty pretty, VisageFunctionValue tree) {
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
            VisageBlock body = tree.getBodyExpression();
            if (body != null) {

                if  (body instanceof VisageErroneousBlock) {
                    pretty.print("<erroroneous>");
                } else {
                    pretty.printExpr(body);
                }
            }
            pretty.println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

  public void visitFunctionValue(VisageFunctionValue tree) {
          visitFunctionValue(this, tree);
    }

    public static void visitFunctionDefinition(VisagePretty pretty, VisageFunctionDefinition tree) {
        try {
            VisagePretty visagepretty = (VisagePretty)pretty;
            int oldScope = visagepretty.variableScope;
            pretty.println();
            pretty.align();
            pretty.printDocComment(tree);
            pretty.printExpr(tree.mods);
            pretty.print("function ");
            pretty.print(tree.name);
            pretty.print("(");
            visagepretty.variableScope = SCOPE_PARAMS;
            pretty.printExprs(tree.getParams());
            visagepretty.variableScope = SCOPE_METHOD;
            pretty.print(")");
            if (tree.operation.rettype != null && tree.operation.rettype.getVisageTag() != VisageTag.TYPEUNKNOWN) {
                pretty.print(" : ");
                pretty.printExpr(tree.operation.rettype);
            }
            VisageBlock body = tree.getBodyExpression();
            if (body != null) {
                pretty.print(" ");
                pretty.printExpr(body);
            }
            pretty.println();
            visagepretty.variableScope = oldScope;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
        visitFunctionDefinition(this, tree);
    }

    public void visitInitDefinition(VisageInitDefinition tree) {
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

    public void visitPostInitDefinition(VisagePostInitDefinition tree) {
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

    public void visitBlockExpression(VisageBlock tree) {
        visitBlockExpression(this, tree);
    }

    public static void visitBlockExpression(VisagePretty pretty, VisageBlock tree) {
        try {
            pretty.printFlags(tree.flags);
            pretty.print("{");
            pretty.println();
            pretty.indent();
            pretty.printStats(tree.stats);
            if (tree.value != null) {
                pretty.align();
                pretty.printExpr(tree.value);
                pretty.println();
            }
            pretty.undent();
            pretty.align();
            pretty.print("}");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    void printBind(VisageBindStatus bindStatus) {
        try {
            if (bindStatus.isUnidiBind()) {
                print(" bind ");
            }
            if (bindStatus.isBidiBind()) {
                print(" bind /*bi-directional*/ ");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitSequenceEmpty(VisageSequenceEmpty that) {
        try {
            print("[]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitSequenceRange(VisageSequenceRange that) {
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
    
    public void visitSequenceExplicit(VisageSequenceExplicit that) {
        try {
            boolean first = true;
            print("[");
            for (VisageExpression expr : that.getItems()) {
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

    public void visitSequenceIndexed(VisageSequenceIndexed that) {
        try {
            printExpr(that.getSequence());
            print("[ ");
            printExpr(that.getIndex());
            print("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

     public void visitSequenceSlice(VisageSequenceSlice that) {
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
    
    //@Override
    public void visitSequenceInsert(VisageSequenceInsert that) {
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
    
    //@Override
    public void visitSequenceDelete(VisageSequenceDelete that) {
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

    //@Override
    public void visitInvalidate(VisageInvalidate that) {
        try {
            print("invalidate ");
            printExpr(that.getVariable());
            print("; ");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //@Override
    public void visitStringExpression(VisageStringExpression tree) {
        try {
            int i;
            List<VisageExpression> parts = tree.getParts();
            for (i = 0; i < parts.length() - 1; i += 3) {
                printExpr(parts.get(i));
                print("{");
                VisageExpression format = parts.get(i + 1);
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

    public void visitInstanciate(VisageInstanciate tree) {
        try {
            VisageExpression id = tree.getIdentifier();
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
            }
            {
                // Visage instantiation
                print(" {");
                if (tree.getParts().nonEmpty()) {
                    indent();
                    for (VisageObjectLiteralPart mem : tree.getParts()) {
                        println();
                        align();
                        printExpr(mem);
                    }
                    //TODO: add defs
                    undent();
                    println();
                    align();
                }
                if (tree.getClassBody() != null) {
                    printExpr(tree.getClassBody());
                }
                print("}");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitObjectLiteralPart(VisageObjectLiteralPart tree) {
        try {
            print(tree.getName());
            print(": ");
            printBind(tree.getExplicitBindStatus());
            printExpr(tree.getExpression());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeAny(VisageTypeAny tree) {
        try {
            print("* ");
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void printTypeSpecifier(VisageType type) {
        try {
            if (type instanceof VisageTypeUnknown)
                return;
            print(": ");
            printExpr(type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    public void visitTypeClass(VisageTypeClass tree) {
        try {
            print(tree.getClassName());
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeFunctional(VisageTypeFunctional tree) {
        try {
            print("(");
            List<VisageType> params = tree.getParams();
            if (params.nonEmpty()) {
                printTypeSpecifier(params.head);
                for (List<VisageType> l = params.tail; l.nonEmpty(); l = l.tail) {
                    print(", ");
                    printTypeSpecifier(l.head);
                }
            }
            print(")");
            printTypeSpecifier((VisageType)tree.getReturnType());
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //@Override
    public void visitTypeArray(VisageTypeArray tree) {
        try {
            print("nativearray of ");
            printTypeSpecifier(tree.getElementType());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeUnknown(VisageTypeUnknown tree) {
        try {
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    String ary(VisageType tree) {
        switch (tree.getCardinality()) {
            case ANY:
                return "[]";
            case SINGLETON:
                return "";
        }
        return "";
    }

    public void visitVarInit(VisageVarInit tree) {
        try {
            print("var-init: ");
            print(tree.getVar());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //@Override
    public void visitVarRef(VisageVarRef tree) {
        try {
            print(tree.getVarRefKind() + "(" + tree.getExpression() + ")");
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitVar(VisageVar tree) {
        try {
            if (docComments != null && docComments.get(tree) != null) {
                println(); align();
            }
            printDocComment(tree);
            printExpr(tree.mods);
            if (variableScope != SCOPE_PARAMS) {
                if ((tree.getModifiers().flags & VisageFlags.IS_DEF) != 0) {
                    print("def ");
                } else {
                    print("var ");
                }
            }
            print(tree.getName());
            if (tree.getVisageType() != null && tree.getVisageType().getVisageTag() != VisageTag.TYPEANY) {
                printTypeSpecifier(tree.getVisageType());
            }
            if (variableScope != SCOPE_PARAMS) {
                if (tree.getInitializer() != null) {
                    print(" = ");
                    if (tree.isBound())
                        print("bind ");
                    printExpr(tree.getInitializer());
                    if (tree.isBidiBind())
                        print(" with inverse");
                }
            }
            if (tree.getOnReplace() != null) {
                printExpr(tree.getOnReplace());
            }
            if (tree.getOnInvalidate() != null) {
                printExpr(tree.getOnInvalidate());
            }
            print(";");
            if (variableScope == SCOPE_OUTER || variableScope == SCOPE_CLASS) {
                println();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitOverrideClassVar(VisageOverrideClassVar tree) {
        try {
            print("override var ");
            printExpr(tree.getId());
            if (tree.getInitializer() != null) {
                print(" = ");
                if (tree.isBound()) {
                    print("bind ");
                }
                printExpr(tree.getInitializer());
                if (tree.isBidiBind()) {
                    print(" with inverse");
                }
            }
            print(" ");
            align();
            if (tree.getOnReplace() != null) {
                printExpr(tree.getOnReplace());
            }
            if (tree.getOnInvalidate() != null) {
                printExpr(tree.getOnInvalidate());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
  
    
    //@Override
    public void visitOnReplace(VisageOnReplace tree) {
        try {
            String triggerKind = tree.getTriggerKind() == VisageOnReplace.Kind.ONREPLACE ?
                "replace" : "invalidate";
            print(" on " + triggerKind);
            if (tree.getOldValue() != null) {
                print(" ");
                printExpr(tree.getOldValue());
            }
            if (tree.getFirstIndex() != null) {
                print("[");
                printExpr(tree.getFirstIndex());
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
            if (tree.getBody() != null) {
                printExpr(tree.getBody());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    
    //@Override
    public void visitForExpression(VisageForExpression tree) {
        try {
            boolean first = true;
            print("for (");
            for (ForExpressionInClauseTree cl : tree.getInClauses()) {

                // Don't try to examine erroneous in clauses. We don't wish to
                // place the entire for expression into error nodes, just because
                // one or more in clauses was in error, so we jsut skip any
                // erroneous ones.
                //
                if  (cl == null || cl instanceof VisageErroneousForExpressionInClause) {
                    continue;
                }

                VisageForExpressionInClause clause = (VisageForExpressionInClause)cl;
                if (first) {
                    first = false;
                } else {
                    print(", ");
                }
                
                VisageVar var = clause.getVar();

                // Don't try to examine erroneous loop controls, such as
                // when a variable was missing. Again, this is because the IDE may
                // try to attribute a node that is mostly correct, but contains
                // one or more components that are in error.
                //
                if  (var == null || var instanceof VisageErroneousVar) 
                {
                    print("<missing>)");
                } else {
                    print(var.getName());
                }
                print(" in ");

                VisageExpression e1 = clause.getSequenceExpression();

                if  (e1 == null || e1 instanceof VisageErroneous) {
                   print("<error>");
                } else {
                    printExpr(e1);
                }
                if (clause.getWhereExpression() != null) {
                    print(" where ");
                    printExpr(clause.getWhereExpression());
                }
            }
            print(") ");

            VisageExpression body = tree.getBodyExpression();

            if  (body == null || body instanceof VisageErroneous) {
                print(" {}\n");
            } else {
                printExpr(body);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitIndexof(VisageIndexof that) {
        try {
            print("indexof ");
            print(that.fname);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }

    //@Override
    public void visitForExpressionInClause(VisageForExpressionInClause that) {
        try {

            if (that.var == null || that.var instanceof VisageErroneousVar) {
                print("<missing var>");
            } else {
                print(that.var);
            }
            print(" in ");

            if (that.seqExpr == null || that.seqExpr instanceof VisageErroneous) {
                print("<missing expr>");
            } else {
                print(that.seqExpr);
            }
            if (that.getWhereExpression() != null) {
                print(" where ");
                if (that.getWhereExpression() instanceof VisageErroneous) {
                    print("<erroreous where>");
                } else {
                    print(that.getWhereExpression());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    /** Convert a tree to a pretty-printed string. */
    public static String toString(VisageTree tree) {
        StringWriter s = new StringWriter();
        try {
            new VisagePretty(s, false).printExpr(tree);
        }
        catch (IOException e) {
            // should never happen, because StringWriter is defined             
            // never to throw any IOExceptions                                  
            throw new AssertionError(e);
        }
        return s.toString();
    }

    public void visitTimeLiteral(VisageTimeLiteral tree) {
        try {
            Double d = ((Number)tree.value.value).doubleValue();
            d /= tree.duration.getMultiplier();
            print(d + tree.duration.getSuffix());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitLengthLiteral(VisageLengthLiteral tree) {
        try {
            Double d = ((Number)tree.value.value).doubleValue();
            print(d + tree.units.getSuffix());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitAngleLiteral(VisageAngleLiteral tree) {
        try {
            Double d = ((Number)tree.value.value).doubleValue();
            print(d + tree.units.getSuffix());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitColorLiteral(VisageColorLiteral tree) {
        try {
            Integer i = ((Number)tree.value.value).intValue();
            print("#" + Integer.toHexString(i));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitInterpolateValue(VisageInterpolateValue tree) {
        printInterpolateValue(tree);
    }
    
    private void printTween(VisageInterpolateValue tree) throws IOException {
        VisageExpression tween = tree.getInterpolation();
        if (tween != null) {
            print(" tween ");
            print(tween);
        }
    }

    public void visitKeyFrameLiteral(VisageKeyFrameLiteral tree) {
        try {
            print("at (");
            print(tree.getStartDuration());
            print(") {");
            println();
            
            indent();
            printStats(List.convert(VisageTree.class, tree.getInterpolationValues()));
            
            if (tree.getTrigger() != null) {
                align();
                print("trigger ");
                visitBlockExpression(this, (VisageBlock)tree.getTrigger());
            }
            
            undent();
            
            println();
            align();
            print("}");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
