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

package com.sun.tools.migrator.tree;

import java.io.*;
import java.util.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.migrator.tree.MTTree.*;
import com.sun.javafx.api.JavafxBindStatus;
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
public class MTPretty implements MTVisitor {

    public MTPretty(Writer out, boolean sourceOutput) {
        this.out = out;
        this.sourceOutput = sourceOutput;
    }

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

    /** The enclosing class name.
     */
    protected Name enclClassName;

    /** A hashtable mapping trees to their documentation comments
     *  (can be null)
     */
    Map<MTTree, String> docComments = null;

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
        out.write(Convert.escapeUnicode(s.toString()));
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
    protected static class UncheckedIOException extends Error {
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
    public void printExpr(MTTree tree, int prec) throws IOException {
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

    /** Derived visitor method: print expression tree at minimum precedence level
     *  for expression.
     */
    public void printExpr(MTTree tree) throws IOException {
        printExpr(tree, MTTreeInfo.noPrec);
    }

    /** Derived visitor method: print statement tree.
     */
    public void printStat(MTTree tree) throws IOException {
        printExpr(tree, MTTreeInfo.notExpression);
    }

    /** Derived visitor method: print list of expression trees, separated by given string.
     *  @param sep the separator string
     */
    public <T extends MTTree> void printExprs(List<T> trees, String sep) throws IOException {
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
    public <T extends MTTree> void printExprs(List<T> trees) throws IOException {
        printExprs(trees, ", ");
    }

    /** Derived visitor method: print list of statements, each on a separate line.
     */
    public void printStats(List<? extends MTTree> trees) throws IOException {
        for (List<? extends MTTree> l = trees; l.nonEmpty(); l = l.tail) {
            align();
            printStat(l.head);
            println();
        }
    }

    /** Print a set of modifiers.
     */
    public void printFlags(long flags) throws IOException {
        if ((flags & SYNTHETIC) != 0) print("/*synthetic*/ ");
        print(MTTreeInfo.flagNames(flags));
        if ((flags & StandardFlags) != 0) print(" ");
        if ((flags & ANNOTATION) != 0) print("@");
    }

    public void printAnnotations(List<MTAnnotation> trees) throws IOException {
        for (List<MTAnnotation> l = trees; l.nonEmpty(); l = l.tail) {
            printStat(l.head);
            println();
            align();
        }
    }

    /** Print documentation comment, if it exists
     *  @param tree    The tree for which a documentation comment should be printed.
     */
    public void printDocComment(MTTree tree) throws IOException {
        if (docComments != null) {
            String dc = docComments.get(tree);
            if (dc != null) {
                print("/**"); println();
                int pos = 0;
                int endpos = lineEndPos(dc, pos);
                while (pos < dc.length()) {
                    align();
                    print(" *");
                    if (pos < dc.length() && dc.charAt(pos) > ' ') print(" ");
                    print(dc.substring(pos, endpos)); println();
                    pos = endpos + 1;
                    endpos = lineEndPos(dc, pos);
                }
                align(); print(" */"); println();
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

    /** If type parameter list is non-empty, print it enclosed in "<...>" brackets.
     */
    public void printTypeParameters(List<MTTypeParameter> trees) throws IOException {
        if (trees.nonEmpty()) {
            print("<");
            printExprs(trees);
            print(">");
        }
    }

    /** Print a block.
     */
    public void printBlock(List<? extends MTTree> stats) throws IOException {
        print("{");
        println();
        indent();
        printStats(stats);
        undent();
        align();
        print("}");
    }

    /** Print a block.
     */
    public void printEnumBody(List<MTTree> stats) throws IOException {
        print("{");
        println();
        indent();
        boolean first = true;
        for (List<MTTree> l = stats; l.nonEmpty(); l = l.tail) {
            if (isEnumerator(l.head)) {
                if (!first) {
                    print(",");
                    println();
                }
                align();
                printStat(l.head);
                first = false;
            }
        }
        print(";");
        println();
        for (List<MTTree> l = stats; l.nonEmpty(); l = l.tail) {
            if (!isEnumerator(l.head)) {
                align();
                printStat(l.head);
                println();
            }
        }
        undent();
        align();
        print("}");
    }

    /** Is the given tree an enumerator definition? */
    boolean isEnumerator(MTTree t) {
        return t.getTag() == MTTree.VARDEF && (((MTVariableDecl) t).mods.flags & ENUM) != 0;
    }

    /** Print unit consisting of package clause and import statements in toplevel,
     *  followed by class definition. if class definition == null,
     *  print all definitions in toplevel.
     *  @param tree     The toplevel tree
     *  @param cdef     The class definition, which is assumed to be part of the
     *                  toplevel tree.
     */
    public void printUnit(MTCompilationUnit tree, MTClassDeclaration cdef) throws IOException {
        docComments = tree.docComments;
        printDocComment(tree);
        if (tree.pid != null) {
            print("package ");
            printExpr(tree.pid);
            print(";");
            println();
        }
        boolean firstImport = true;
        for (List<MTTree> l = tree.defs;
        l.nonEmpty() && (cdef == null || l.head.getTag() == MTTree.IMPORT);
        l = l.tail) {
            if (l.head.getTag() == MTTree.IMPORT) {
                MTImport imp = (MTImport)l.head;
                Name name = MTTreeInfo.name(imp.qualid);
                if (name == name.table.asterisk ||
                        cdef == null ||
                        isUsed(MTTreeInfo.symbol(imp.qualid), cdef)) {
                    if (firstImport) {
                        firstImport = false;
                        println();
                    }
                    printStat(imp);
                }
            } else {
                printStat(l.head);
            }
        }
        if (cdef != null) {
            printStat(cdef);
            println();
        }
    }
    // where
    boolean isUsed(final Symbol t, MTTree cdef) {
        class UsedVisitor extends MTTreeScanner {
            public void scan(MTTree tree) {
                if (tree!=null && !result) tree.accept(this);
            }
            boolean result = false;
            public void visitIdent(MTIdent tree) {
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

    public void visitTopLevel(MTCompilationUnit tree) {
        try {
            printUnit(tree, null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitImport(MTImport tree) {
        try {
            print("import ");
            if (tree.staticImport) print("static ");
            printExpr(tree.qualid);
            print(";");
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitVarDef(MTVariableDecl tree) {
        try {
            if (docComments != null && docComments.get(tree) != null) {
                println(); align();
            }
            printDocComment(tree);
            if ((tree.mods.flags & ENUM) != 0) {
                print("/*public static final*/ ");
                print(tree.name);
                if (tree.init != null) {
                    print(" /* = ");
                    printExpr(tree.init);
                    print(" */");
                }
            } else {
                printExpr(tree.mods);
                if ((tree.mods.flags & VARARGS) != 0) {
                    printExpr(((MTArrayTypeTree) tree.vartype).elemtype);
                    print("... " + tree.name);
                } else {
                    printExpr(tree.vartype);
                    print(" " + tree.name);
                }
                if (tree.init != null) {
                    print(" = ");
                    printExpr(tree.init);
                }
                if (prec == MTTreeInfo.notExpression) print(";");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitSkip(MTSkip tree) {
        try {
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitBlock(MTBlock tree) {
        try {
            printFlags(tree.flags);
            printBlock(tree.stats);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitWhileLoop(MTWhileLoop tree) {
        try {
            print("while ");
            if (tree.cond.getTag() == MTTree.PARENS) {
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

    public void visitTry(MTTry tree) {
        try {
            print("try ");
            printStat(tree.body);
            for (List<MTCatch> l = tree.catchers; l.nonEmpty(); l = l.tail) {
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

    public void visitCatch(MTCatch tree) {
        try {
            print(" catch (");
            printExpr(tree.param);
            print(") ");
            printStat(tree.body);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitConditional(MTConditional tree) {
        try {
            open(prec, MTTreeInfo.condPrec);
            printExpr(tree.cond, MTTreeInfo.condPrec);
            print(" ? ");
            printExpr(tree.truepart, MTTreeInfo.condPrec);
            print(" : ");
            printExpr(tree.falsepart, MTTreeInfo.condPrec);
            close(prec, MTTreeInfo.condPrec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitIf(MTIf tree) {
        try {
            print("if ");
            if (tree.cond.getTag() == MTTree.PARENS) {
                printExpr(tree.cond);
            } else {
                print("(");
                printExpr(tree.cond);
                print(")");
            }
            print(" ");
            printStat(tree.thenpart);
            if (tree.elsepart != null) {
                print(" else ");
                printStat(tree.elsepart);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitExec(MTExpressionStatement tree) {
        try {
            printExpr(tree.expr);
            if (prec == MTTreeInfo.notExpression) print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitBreak(MTBreak tree) {
        try {
            print("break");
            if (tree.label != null) print(" " + tree.label);
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitContinue(MTContinue tree) {
        try {
            print("continue");
            if (tree.label != null) print(" " + tree.label);
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitReturn(MTReturn tree) {
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

    public void visitThrow(MTThrow tree) {
        try {
            print("throw ");
            printExpr(tree.expr);
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitAssert(MTAssert tree) {
        try {
            print("assert ");
            printExpr(tree.cond);
            if (tree.detail != null) {
                print(" : ");
                printExpr(tree.detail);
            }
            print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitApply(MTMethodInvocation tree) {
        try {
            if (!tree.typeargs.isEmpty()) {
                if (tree.meth.getTag() == MTTree.SELECT) {
                    MTFieldAccess left = (MTFieldAccess)tree.meth;
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

    public void visitParens(MTParens tree) {
        try {
            print("(");
            printExpr(tree.expr);
            print(")");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitAssign(MTAssign tree) {
        try {
            open(prec, MTTreeInfo.assignPrec);
            printExpr(tree.lhs, MTTreeInfo.assignPrec + 1);
            print(" = ");
            printExpr(tree.rhs, MTTreeInfo.assignPrec);
            close(prec, MTTreeInfo.assignPrec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String operatorName(int tag) {
        switch(tag) {
            case MTTree.POS:     return "+";
            case MTTree.NEG:     return "-";
            case MTTree.NOT:     return "!";
            case MTTree.COMPL:   return "~";
            case MTTree.PREINC:  return "++";
            case MTTree.PREDEC:  return "--";
            case MTTree.POSTINC: return "++";
            case MTTree.POSTDEC: return "--";
            case MTTree.NULLCHK: return "<*nullchk*>";
            case MTTree.OR:      return "||";
            case MTTree.AND:     return "&&";
            case MTTree.EQ:      return "==";
            case MTTree.NE:      return "!=";
            case MTTree.LT:      return "<";
            case MTTree.GT:      return ">";
            case MTTree.LE:      return "<=";
            case MTTree.GE:      return ">=";
            case MTTree.BITOR:   return "|";
            case MTTree.BITXOR:  return "^";
            case MTTree.BITAND:  return "&";
            case MTTree.SL:      return "<<";
            case MTTree.SR:      return ">>";
            case MTTree.USR:     return ">>>";
            case MTTree.PLUS:    return "+";
            case MTTree.MINUS:   return "-";
            case MTTree.MUL:     return "*";
            case MTTree.DIV:     return "/";
            case MTTree.MOD:     return "%";
            default: throw new Error();
        }
    }

    public void visitAssignop(MTAssignOp tree) {
        try {
            open(prec, MTTreeInfo.assignopPrec);
            printExpr(tree.lhs, MTTreeInfo.assignopPrec + 1);
            print(" " + operatorName(tree.getTag() - MTTree.ASGOffset) + "= ");
            printExpr(tree.rhs, MTTreeInfo.assignopPrec);
            close(prec, MTTreeInfo.assignopPrec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitUnary(MTUnary tree) {
        try {
            int ownprec = MTTreeInfo.opPrec(tree.getTag());
            String opname = operatorName(tree.getTag());
            open(prec, ownprec);
            if (tree.getTag() <= MTTree.PREDEC) {
                print(opname);
                printExpr(tree.arg, ownprec);
            } else {
                printExpr(tree.arg, ownprec);
                print(opname);
            }
            close(prec, ownprec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitBinary(MTBinary tree) {
        try {
            int ownprec = MTTreeInfo.opPrec(tree.getTag());
            String opname = operatorName(tree.getTag());
            open(prec, ownprec);
            printExpr(tree.lhs, ownprec);
            print(" " + opname + " ");
            printExpr(tree.rhs, ownprec + 1);
            close(prec, ownprec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeCast(MTTypeCast tree) {
        try {
            open(prec, MTTreeInfo.prefixPrec);
            print("(");
            printExpr(tree.clazz);
            print(")");
            printExpr(tree.expr, MTTreeInfo.prefixPrec);
            close(prec, MTTreeInfo.prefixPrec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeTest(MTInstanceOf tree) {
        try {
            open(prec, MTTreeInfo.ordPrec);
            printExpr(tree.expr, MTTreeInfo.ordPrec);
            print(" instanceof ");
            printExpr(tree.clazz, MTTreeInfo.ordPrec + 1);
            close(prec, MTTreeInfo.ordPrec);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitIndexed(MTArrayAccess tree) {
        try {
            printExpr(tree.indexed, MTTreeInfo.postfixPrec);
            print("[");
            printExpr(tree.index);
            print("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitSelect(MTFieldAccess tree) {
        try {
            printExpr(tree.selected, MTTreeInfo.postfixPrec);
            print("." + tree.name);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitIdent(MTIdent tree) {
        try {
            print(tree.name);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitLiteral(MTLiteral tree) {
        try {
            switch (tree.typetag) {
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

    public void visitTypeIdent(MTPrimitiveTypeTree tree) {
        try {
            switch(tree.typetag) {
                case TypeTags.BYTE:
                    print("byte");
                    break;
                case TypeTags.CHAR:
                    print("char");
                    break;
                case TypeTags.SHORT:
                    print("short");
                    break;
                case TypeTags.INT:
                    print("int");
                    break;
                case TypeTags.LONG:
                    print("long");
                    break;
                case TypeTags.FLOAT:
                    print("float");
                    break;
                case TypeTags.DOUBLE:
                    print("double");
                    break;
                case TypeTags.BOOLEAN:
                    print("boolean");
                    break;
                case TypeTags.VOID:
                    print("void");
                    break;
                default:
                    print("error(type for "+tree.getClass()+")");
                    break;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeArray(MTArrayTypeTree tree) {
        try {
            printBaseElementType(tree);
            printBrackets(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // Prints the inner element type of a nested array
    private void printBaseElementType(MTArrayTypeTree tree) throws IOException {
        MTTree elem = tree.elemtype;
        while (elem instanceof MTWildcard)
            elem = ((MTWildcard) elem).inner;
        if (elem instanceof MTArrayTypeTree)
            printBaseElementType((MTArrayTypeTree) elem);
        else
            printExpr(elem);
    }

    // prints the brackets of a nested array in reverse order
    private void printBrackets(MTArrayTypeTree tree) throws IOException {
        MTTree elem;
        while (true) {
            elem = tree.elemtype;
            print("[]");
            if (!(elem instanceof MTArrayTypeTree)) break;
            tree = (MTArrayTypeTree) elem;
        }
    }

    public void visitTypeApply(MTTypeApply tree) {
        try {
            printExpr(tree.clazz);
            print("<");
            printExprs(tree.arguments);
            print(">");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeParameter(MTTypeParameter tree) {
        try {
            print(tree.name);
            if (tree.bounds.nonEmpty()) {
                print(" extends ");
                printExprs(tree.bounds, " & ");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitWildcard(MTWildcard tree) {
        try {
            print(tree.kind);
            if (tree.kind.kind != BoundKind.UNBOUND)
                printExpr(tree.inner);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitTypeBoundKind(MTTypeBoundKind tree) {
        try {
            print(String.valueOf(tree.kind));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitErroneous(MTErroneous tree) {
        try {
            print("(ERROR)");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitLetExpr(MTLetExpr tree) {
        try {
            print("(let " + tree.defs + " in " + tree.expr + ")");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitModifiers(MTModifiers mods) {
        try {
            printAnnotations(mods.annotations);
            printFlags(mods.flags);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitAnnotation(MTAnnotation tree) {
        try {
            print("@");
            printExpr(tree.annotationType);
            print("(");
            printExprs(tree.args);
            print(")");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTree(MTTree tree) {
        try {
            print("(UNKNOWN: " + tree + ")");
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitClassDeclaration(MTClassDeclaration tree) {
        try {
            println();
            align();
            printDocComment(tree);
            print("class ");
            print(" ");
            print(tree.name);
            print(" {");
            println();
            indent();
            for (MTTree mem : tree.defs) {
                align();
                printStat(mem);
            }
            undent();
            align();
            print("}");
            println();
            align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

   public void visitOperationValue(MTOperationValue tree) {
        try {
            println();
            align();
            printDocComment(tree);
            print(" function ");
            print("(");
            printExprs(tree.getParameters());
            print(")");
            if (tree.getType() != null) {
                printExpr(tree.getType());
            }
            if (tree.getBodyExpression() != null) {
                printExpr(tree.getBodyExpression());
            }
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitOperationDefinition(MTOperationDefinition tree) {
        try {
            println();
            align();
            printDocComment(tree);
            printExpr(tree.mods);
            print(" operation ");
            print(tree.name);
            print("(");
            printExprs(tree.getParameters());
            print(")");
            printExpr(tree.operation.rettype);
            if (tree.getBodyExpression() != null) {
                printExpr(tree.getBodyExpression());
            }
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitInitDefinition(MTInitDefinition tree) {
        try {
            println();
            align();
            printDocComment(tree);
            print(" init ");
            print(tree.getBody());
            println();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitBlockExpression(MTBlockExpression tree) {
        visitBlockExpression(this, tree);
    }

    public static void visitBlockExpression(MTPretty pretty, MTBlockExpression tree) {
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

    public void visitMemberSelector(MTMemberSelector tree) {
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
                print(" stays ");
            }
            if (bindStatus.isBidiBind()) {
                print(" tie ");
            }
            if (bindStatus.isLazy()) {
                print(" lazy ");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitDoLater(MTDoLater tree) {
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

    public void visitSequenceEmpty(MTSequenceEmpty that) {
        try {
            print("[]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitSequenceRange(MTSequenceRange that) {
         try {
            print("[");
            printExpr(that.getLower());
            print("..");
            printExpr(that.getUpper());
            print("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void visitSequenceExplicit(MTSequenceExplicit that) {
        try {
            boolean first = true;
            print("[");
            for (MTExpression expr : that.getItems()) {
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

    public void visitSequenceIndexed(MTSequenceIndexed that) {
        try {
            printExpr(that.getSequence());
            print("[ ");
            printExpr(that.getIndex());
            print("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Override
    public void visitSequenceInsert(MTSequenceInsert that) {
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
    public void visitSequenceDelete(MTSequenceDelete that) {
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

    public void visitStringExpression(MTStringExpression tree) {
        try {
            int i;
            List<MTExpression> parts = tree.getParts();
            for (i = 0; i < parts.length() - 1; i += 3) {
                printExpr(parts.get(i));
                print("{");
                MTExpression format = parts.get(i + 1);
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

    public void visitPureObjectLiteral(MTPureObjectLiteral tree) {
        try {
            MTExpression id = tree.getIdentifier();
            if (id != null) {
                printExpr(id);
            }
            print(" {");
            indent();
            List<MTStatement> mems = tree.getParts();
            for (MTStatement mem : mems) {
                println();
                align();
                printExpr(mem);
            }
            undent();
            println();
            align();
            print("}");
            println();
            align();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitVarIsObjectBeingInitialized(MTVarIsObjectBeingInitialized tree) {
        assert false : "not yet implemented";
    }
    
    public void visitSetAttributeToObjectBeingInitialized(MTSetAttributeToObjectBeingInitialized tree) {
        try {
            print("attribute : ");
            print(tree.getAttributeName());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitObjectLiteralPart(MTObjectLiteralPart tree) {
        try {
            print(tree.getName());
            print(" : ");
            printBind(tree.getBindStatus());
            printExpr(tree.getExpression());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeAny(MTTypeAny tree) {
        try {
            print(" : * ");
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeClass(MTTypeClass tree) {
        try {
            print(" : ");
            print(tree.getClassName());
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitTypeFunctional(MTTypeFunctional tree) {
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

    public void visitTypeUnknown(MTTypeUnknown tree) {
        try {
            print(ary(tree));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitInstanciate(MTInstanciate tree) {
        try {
            if (tree.encl != null) {
                printExpr(tree.encl);
                print(".");
            }
            print("new ");
            printExpr(tree.clazz);
            print("(");
            printExprs(tree.args);
            print(")");
            if (tree.def != null) {
                Name enclClassNamePrev = enclClassName;
                enclClassName =
                        tree.def.name != null ? tree.def.name :
                            tree.type != null && tree.type.tsym.name != tree.type.tsym.name.table.empty ? tree.type.tsym.name :
                                null;
                if ((tree.def.mods.flags & Flags.ENUM) != 0) print("/*enum*/");
                printBlock(tree.def.defs);
                enclClassName = enclClassNamePrev;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    String ary(MTType tree) {
        String show;
        switch (tree.getCardinality()) {
            case MTType.CARDINALITY_ANY:
                return "[]";
            case MTType.CARDINALITY_SINGLETON:
                return " ";
        }
        return "";
    }

    public void visitVar(MTVar tree) {
        try {
            print(tree.getName());
            printExpr(tree.getJFXType());
            if (tree.getInitializer() != null) {
                print(" = ");
                printExpr(tree.getInitializer());
            }
            if (prec == MTTreeInfo.notExpression) print(";");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void visitAbstractOnChange(MTAbstractOnChange tree) {
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
    public void visitOnReplace(MTOnReplace tree) {
        try {
            print(" on replace ");
            visitAbstractOnChange(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitOnReplaceElement(MTOnReplaceElement tree) {
        try {
            print(" on replace ");
            visitAbstractOnChange(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitOnInsertElement(MTOnInsertElement tree) {
        try {
            print(" on insert ");
            visitAbstractOnChange(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitOnDeleteElement(MTOnDeleteElement tree) {
        try {
            print(" on delete ");
            visitAbstractOnChange(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }      
    }
    
    @Override
    public void visitForExpression(MTForExpression tree) {
        try {
            boolean first = true;
            print("for (");
            for (MTForExpressionInClause clause : tree.getInClauses()) {
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
    public void visitForExpressionInClause(MTForExpressionInClause that) {
        assert false : "should not reach here";
    }
}
