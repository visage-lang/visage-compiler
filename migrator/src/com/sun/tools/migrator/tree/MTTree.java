/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.migrator.tree;

import java.util.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.tools.JavaFileObject;

import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.source.tree.Tree;
import com.sun.source.tree.*;

import static com.sun.tools.javac.code.BoundKind.*;

/**
 * Root class for abstract syntax tree nodes. It provides definitions
 * for specific tree nodes as subclasses nested inside.
 *
 * <p>Each subclass is highly standardized.  It generally contains
 * only tree fields for the syntactic subcomponents of the node.  Some
 * classes that represent identifier uses or definitions also define a
 * Symbol field that denotes the represented identifier.  Classes for
 * non-local jumps also carry the jump target as a field.  The root
 * class Tree itself defines fields for the tree's type and position.
 * No other fields are kept in a tree node; instead parameters are
 * passed to methods accessing the node.
 *
 * <p>Except for the methods defined by com.sun.source, the only
 * method defined in subclasses is `visit' which applies a given
 * visitor to the tree. The actual tree processing is done by visitor
 * classes in other packages. The abstract class JavafxVisitor, as well as
 * an Factory interface for trees, are defined as inner classes in
 * Tree.
 *
 * <p>To avoid ambiguities with the Tree API in com.sun.source all sub
 * classes should, by convention, start with JC (javac).
 *
 * <p><b>This is NOT part of any API supported by Sun Microsystems.
 * If you write code that depends on this, you do so at your own risk.
 * This code and its internal interfaces are subject to change or
 * deletion without notice.</b>
 *
 * @see TreeMaker
 * @see JavafxTreeInfo
 * @see TreeTranslator
 * @see Pretty
 */
public abstract class MTTree implements Tree, Cloneable {

    /* Tree tag values, identifying kinds of trees */

    /** Toplevel nodes, of type TopLevel, representing entire source files.
     */
    public static final int  TOPLEVEL = 1;

    /** Import clauses, of type Import.
     */
    public static final int IMPORT = TOPLEVEL + 1;

    /** Class definitions, of type ClassDef.
     */
    public static final int CLASSDEF = IMPORT + 1;

    /** Method definitions, of type MethodDef.
     */
    public static final int METHODDEF = CLASSDEF + 1;

    /** Variable definitions, of type VarDef.
     */
    public static final int VARDEF = METHODDEF + 1;

    /** The no-op statement ";", of type Skip
     */
    public static final int SKIP = VARDEF + 1;

    /** Blocks, of type Block.
     */
    public static final int BLOCK = SKIP + 1;

    /** Do-while loops, of type DoLoop.
     */
    public static final int DOLOOP = BLOCK + 1;

    /** While-loops, of type WhileLoop.
     */
    public static final int WHILELOOP = DOLOOP + 1;

    /** For-loops, of type ForLoop.
     */
    public static final int FORLOOP = WHILELOOP + 1;

    /** Foreach-loops, of type ForeachLoop.
     */
    public static final int FOREACHLOOP = FORLOOP + 1;

    /** Labelled statements, of type Labelled.
     */
    public static final int LABELLED = FOREACHLOOP + 1;

    /** Switch statements, of type Switch.
     */
    public static final int SWITCH = LABELLED + 1;

    /** Case parts in switch statements, of type Case.
     */
    public static final int CASE = SWITCH + 1;

    /** Synchronized statements, of type Synchonized.
     */
    public static final int SYNCHRONIZED = CASE + 1;

    /** Try statements, of type Try.
     */
    public static final int TRY = SYNCHRONIZED + 1;

    /** Catch clauses in try statements, of type Catch.
     */
    public static final int CATCH = TRY + 1;

    /** Conditional expressions, of type Conditional.
     */
    public static final int CONDEXPR = CATCH + 1;

    /** Conditional statements, of type If.
     */
    public static final int IF = CONDEXPR + 1;

    /** Expression statements, of type Exec.
     */
    public static final int EXEC = IF + 1;

    /** Break statements, of type Break.
     */
    public static final int BREAK = EXEC + 1;

    /** Continue statements, of type Continue.
     */
    public static final int CONTINUE = BREAK + 1;

    /** Return statements, of type Return.
     */
    public static final int RETURN = CONTINUE + 1;

    /** Throw statements, of type Throw.
     */
    public static final int THROW = RETURN + 1;

    /** Assert statements, of type Assert.
     */
    public static final int ASSERT = THROW + 1;

    /** Method invocation expressions, of type Apply.
     */
    public static final int APPLY = ASSERT + 1;

    /** Class instance creation expressions, of type NewClass.
     */
    public static final int NEWCLASS = APPLY + 1;

    /** Array creation expressions, of type NewArray.
     */
    public static final int NEWARRAY = NEWCLASS + 1;

    /** Parenthesized subexpressions, of type Parens.
     */
    public static final int PARENS = NEWARRAY + 1;

    /** Assignment expressions, of type Assign.
     */
    public static final int ASSIGN = PARENS + 1;

    /** Type cast expressions, of type TypeCast.
     */
    public static final int TYPECAST = ASSIGN + 1;

    /** Type test expressions, of type TypeTest.
     */
    public static final int TYPETEST = TYPECAST + 1;

    /** Indexed array expressions, of type Indexed.
     */
    public static final int INDEXED = TYPETEST + 1;

    /** Selections, of type Select.
     */
    public static final int SELECT = INDEXED + 1;

    /** Simple identifiers, of type Ident.
     */
    public static final int IDENT = SELECT + 1;

    /** Literals, of type Literal.
     */
    public static final int LITERAL = IDENT + 1;

    /** Basic type identifiers, of type TypeIdent.
     */
    public static final int TYPEIDENT = LITERAL + 1;

    /** Array types, of type TypeArray.
     */
    public static final int TYPEARRAY = TYPEIDENT + 1;

    /** Parameterized types, of type TypeApply.
     */
    public static final int TYPEAPPLY = TYPEARRAY + 1;

    /** Formal type parameters, of type TypeParameter.
     */
    public static final int TYPEPARAMETER = TYPEAPPLY + 1;

    /** Type argument.
     */
    public static final int WILDCARD = TYPEPARAMETER + 1;

    /** Bound kind: extends, super, exact, or unbound
     */
    public static final int TYPEBOUNDKIND = WILDCARD + 1;

    /** metadata: Annotation.
     */
    public static final int ANNOTATION = TYPEBOUNDKIND + 1;

    /** metadata: Modifiers
     */
    public static final int MODIFIERS = ANNOTATION + 1;

    /** Error trees, of type Erroneous.
     */
    public static final int ERRONEOUS = MODIFIERS + 1;

    /** Unary operators, of type Unary.
     */
    public static final int POS = ERRONEOUS + 1;             // +
    public static final int NEG = POS + 1;                   // -
    public static final int NOT = NEG + 1;                   // !
    public static final int COMPL = NOT + 1;                 // ~
    public static final int PREINC = COMPL + 1;              // ++ _
    public static final int PREDEC = PREINC + 1;             // -- _
    public static final int POSTINC = PREDEC + 1;            // _ ++
    public static final int POSTDEC = POSTINC + 1;           // _ --

    /** unary operator for null reference checks, only used internally.
     */
    public static final int NULLCHK = POSTDEC + 1;

    /** Binary operators, of type Binary.
     */
    public static final int OR = NULLCHK + 1;                // ||
    public static final int AND = OR + 1;                    // &&
    public static final int BITOR = AND + 1;                 // |
    public static final int BITXOR = BITOR + 1;              // ^
    public static final int BITAND = BITXOR + 1;             // &
    public static final int EQ = BITAND + 1;                 // ==
    public static final int NE = EQ + 1;                     // !=
    public static final int LT = NE + 1;                     // <
    public static final int GT = LT + 1;                     // >
    public static final int LE = GT + 1;                     // <=
    public static final int GE = LE + 1;                     // >=
    public static final int SL = GE + 1;                     // <<
    public static final int SR = SL + 1;                     // >>
    public static final int USR = SR + 1;                    // >>>
    public static final int PLUS = USR + 1;                  // +
    public static final int MINUS = PLUS + 1;                // -
    public static final int MUL = MINUS + 1;                 // *
    public static final int DIV = MUL + 1;                   // /
    public static final int MOD = DIV + 1;                   // %

    /** Assignment operators, of type Assignop.
     */
    public static final int BITOR_ASG = MOD + 1;             // |=
    public static final int BITXOR_ASG = BITOR_ASG + 1;      // ^=
    public static final int BITAND_ASG = BITXOR_ASG + 1;     // &=

    public static final int SL_ASG = SL + BITOR_ASG - BITOR; // <<=
    public static final int SR_ASG = SL_ASG + 1;             // >>=
    public static final int USR_ASG = SR_ASG + 1;            // >>>=
    public static final int PLUS_ASG = USR_ASG + 1;          // +=
    public static final int MINUS_ASG = PLUS_ASG + 1;        // -=
    public static final int MUL_ASG = MINUS_ASG + 1;         // *=
    public static final int DIV_ASG = MUL_ASG + 1;           // /=
    public static final int MOD_ASG = DIV_ASG + 1;           // %=

    /** A synthetic let expression, of type LetExpr.
     */
    public static final int LETEXPR = MOD_ASG + 1;           // ala scheme


    /** The offset between assignment operators and normal operators.
     */
    public static final int ASGOffset = BITOR_ASG - BITOR;

    /* The (encoded) position in the source file. @see util.Position.
     */
    public int pos;

    /* The type of this node.
     */
    public Type type;

    /* The tag of this node -- one of the constants declared above.
     */
    public abstract int getTag();

    /** Convert a tree to a pretty-printed string. */
    public String toString() {
        StringWriter s = new StringWriter();
        try {
            new MTPretty(s, false).printExpr(this);
        }
        catch (IOException e) {
            // should never happen, because StringWriter is defined
            // never to throw any IOExceptions
	    throw new AssertionError(e);
        }
        return s.toString();
    }

    /** Set position field and return this tree.
     */
    public MTTree setPos(int pos) {
        this.pos = pos;
        return this;
    }

    /** Set type field and return this tree.
     */
    public MTTree setType(Type type) {
        this.type = type;
        return this;
    }

    /** Visit this tree with a given visitor.
     */
    public abstract void accept(MTVisitor v);

    /**
     * Gets the kind of this tree.
     *
     * @return the kind of this tree.
     */
    public Kind getKind() { return Kind.OTHER; }  // just make the errors go away
    
    /**
     * Accept method used to implement the visitor pattern.  The
     * visitor pattern is used to implement operations on trees.
     *
     * @param <R> result type of this operation.
     * @param <D> type of additonal data.
     */
    public <R,D> R accept(TreeVisitor<R,D> visitor, D data) { return null; } // just make the errors go away

    /** Return a shallow copy of this tree.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /** Get a default position for this tree node.
     */
    public DiagnosticPosition pos() {
        return null;
    }

    // for default DiagnosticPosition
    public MTTree getTree() {
        return this;
    }

    // for default DiagnosticPosition
    public int getStartPosition() {
        return MTTreeInfo.getStartPos(this);
    }

    // for default DiagnosticPosition
    public int getPreferredPosition() {
        return pos;
    }

    // for default DiagnosticPosition
    public int getEndPosition(Map<MTTree, Integer> endPosTable) {
        return MTTreeInfo.getEndPos(this, endPosTable);
    }

    /**
     * Everything in one source file is kept in a TopLevel structure.
     * @param pid              The tree representing the package clause.
     * @param sourcefile       The source file name.
     * @param defs             All definitions in this file (ClassDef, Import, and Skip)
     * @param packge           The package it belongs to.
     * @param namedImportScope A scope for all named imports.
     * @param starImportScope  A scope for all import-on-demands.
     * @param lineMap          Line starting positions, defined only
     *                         if option -g is set.
     * @param docComments      A hashtable that stores all documentation comments
     *                         indexed by the tree nodes they refer to.
     *                         defined only if option -s is set.
     * @param endPositions     A hashtable that stores ending positions of source
     *                         ranges indexed by the tree nodes they belong to.
     *                         Defined only if option -Xjcov is set.
     */
    public static class MTCompilationUnit extends MTTree {
        public MTExpression pid;
        public List<MTTree> defs;
        public PackageSymbol packge;
        public Scope namedImportScope;
        public Scope starImportScope;
        public long flags;
        public Position.LineMap lineMap = null;
        public Map<MTTree, String> docComments = null;
        public Map<MTTree, Integer> endPositions = null;
        protected MTCompilationUnit(
                        MTExpression pid,
                        List<MTTree> defs,
                        PackageSymbol packge,
                        Scope namedImportScope,
                        Scope starImportScope) {
            this.pid = pid;
            this.defs = defs;
            this.packge = packge;
            this.namedImportScope = namedImportScope;
            this.starImportScope = starImportScope;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTopLevel(this); }

        public Kind getKind() { return Kind.COMPILATION_UNIT; }
        public List<MTImport> getImports() {
            ListBuffer<MTImport> imports = new ListBuffer<MTImport>();
            for (MTTree tree : defs) {
                if (tree.getTag() == IMPORT)
                    imports.append((MTImport)tree);
                else
                    break;
            }
            return imports.toList();
        }
        public MTExpression getPackageName() { return pid; }
	public Position.LineMap getLineMap() {
	    return lineMap;
        }  
        public List<MTTree> getTypeDecls() {
            List<MTTree> typeDefs;
            for (typeDefs = defs; !typeDefs.isEmpty(); typeDefs = typeDefs.tail)
                if (typeDefs.head.getTag() != IMPORT)
                    break;
            return typeDefs;
        }

        @Override
        public int getTag() {
            return TOPLEVEL;
        }
    }

    /**
     * An import clause.
     * @param qualid    The imported class(es).
     */
    public static class MTImport extends MTTree implements ImportTree {
        public boolean staticImport;
        public MTTree qualid;
        protected MTImport(MTTree qualid, boolean importStatic) {
            this.qualid = qualid;
            this.staticImport = importStatic;
        }
        @Override
        public void accept(MTVisitor v) { v.visitImport(this); }

        public boolean isStatic() { return staticImport; }
        public MTTree getQualifiedIdentifier() { return qualid; }

        public Kind getKind() { return Kind.IMPORT; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitImport(this, d);
        }

        @Override
        public int getTag() {
            return IMPORT;
        }
    }

    public static abstract class MTStatement extends MTTree implements StatementTree {
        @Override
        public MTStatement setType(Type type) {
            super.setType(type);
            return this;
        }
        @Override
        public MTStatement setPos(int pos) {
            super.setPos(pos);
            return this;
        }
    }

    public static abstract class MTExpression extends MTTree implements ExpressionTree {
        @Override
        public MTExpression setType(Type type) {
            super.setType(type);
            return this;
        }
        @Override
        public MTExpression setPos(int pos) {
            super.setPos(pos);
            return this;
        }
    }

    /**
     * A variable definition.
     * @param modifiers variable modifiers
     * @param name variable name
     * @param vartype type of the variable
     * @param init variables initial value
     * @param sym symbol
     */
    public static class MTVariableDecl extends MTStatement implements VariableTree {
        public MTModifiers mods;
        public Name name;
        public MTExpression vartype;
        public MTExpression init;
        public VarSymbol sym;
        protected MTVariableDecl(MTModifiers mods,
			 Name name,
			 MTExpression vartype,
			 MTExpression init,
			 VarSymbol sym) {
            this.mods = mods;
            this.name = name;
            this.vartype = vartype;
            this.init = init;
            this.sym = sym;
        }
        @Override
        public void accept(MTVisitor v) { v.visitVarDef(this); }

        public Kind getKind() { return Kind.VARIABLE; }
        public MTModifiers getModifiers() { return mods; }
        public Name getName() { return name; }
        public MTTree getType() { return vartype; }
        public MTExpression getInitializer() {
            return init;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitVariable(this, d);
        }

        @Override
        public int getTag() {
            return VARDEF;
        }
    }

      /**
     * A no-op statement ";".
     */
    public static class MTSkip extends MTStatement implements EmptyStatementTree {
        protected MTSkip() {
        }
        @Override
        public void accept(MTVisitor v) { v.visitSkip(this); }

        public Kind getKind() { return Kind.EMPTY_STATEMENT; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitEmptyStatement(this, d);
        }

        @Override
        public int getTag() {
            return SKIP;
        }
    }

    /**
     * A statement block.
     * @param stats statements
     * @param flags flags
     */
    public static class MTBlock extends MTStatement implements BlockTree {
        public long flags;
        public List<MTStatement> stats;
        /** Position of closing brace, optional. */
        public int endpos = Position.NOPOS;
        protected MTBlock(long flags, List<MTStatement> stats) {
            this.stats = stats;
            this.flags = flags;
        }
        @Override
        public void accept(MTVisitor v) { v.visitBlock(this); }

        public Kind getKind() { return Kind.BLOCK; }
        public List<MTStatement> getStatements() {
            return stats;
        }
        public boolean isStatic() { return (flags & Flags.STATIC) != 0; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitBlock(this, d);
        }

        @Override
        public int getTag() {
            return BLOCK;
        }
    }

    /**
     * A while loop
     */
    public static class MTWhileLoop extends MTStatement implements WhileLoopTree {
        public MTExpression cond;
        public MTStatement body;
        protected MTWhileLoop(MTExpression cond, MTStatement body) {
            this.cond = cond;
            this.body = body;
        }
        @Override
        public void accept(MTVisitor v) { v.visitWhileLoop(this); }

        public Kind getKind() { return Kind.WHILE_LOOP; }
        public MTExpression getCondition() { return cond; }
        public MTStatement getStatement() { return body; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitWhileLoop(this, d);
        }

        @Override
        public int getTag() {
            return WHILELOOP;
        }
    }

    /**
     * A "try { } catch ( ) { } finally { }" block.
     */
    public static class MTTry extends MTStatement implements TryTree {
        public MTBlock body;
        public List<MTCatch> catchers;
        public MTBlock finalizer;
        protected MTTry(MTBlock body, List<MTCatch> catchers, MTBlock finalizer) {
            this.body = body;
            this.catchers = catchers;
            this.finalizer = finalizer;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTry(this); }

        public Kind getKind() { return Kind.TRY; }
        public MTBlock getBlock() { return body; }
        public List<MTCatch> getCatches() {
            return catchers;
        }
        public MTBlock getFinallyBlock() { return finalizer; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitTry(this, d);
        }
        @Override
        public int getTag() {
            return TRY;
        }
    }

    /**
     * A catch block.
     */
    public static class MTCatch extends MTTree implements CatchTree {
        public MTVariableDecl param;
        public MTBlock body;
        protected MTCatch(MTVariableDecl param, MTBlock body) {
            this.param = param;
            this.body = body;
        }
        @Override
        public void accept(MTVisitor v) { v.visitCatch(this); }

        public Kind getKind() { return Kind.CATCH; }
        public MTVariableDecl getParameter() { return param; }
        public MTBlock getBlock() { return body; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitCatch(this, d);
        }
        @Override
        public int getTag() {
            return CATCH;
        }
    }

    /**
     * A ( ) ? ( ) : ( ) conditional expression
     */
    public static class MTConditional extends MTExpression implements ConditionalExpressionTree {
        public MTExpression cond;
        public MTExpression truepart;
        public MTExpression falsepart;
        protected MTConditional(MTExpression cond,
			      MTExpression truepart,
			      MTExpression falsepart)
	{
            this.cond = cond;
            this.truepart = truepart;
            this.falsepart = falsepart;
        }
        @Override
        public void accept(MTVisitor v) { v.visitConditional(this); }

        public Kind getKind() { return Kind.CONDITIONAL_EXPRESSION; }
        public MTExpression getCondition() { return cond; }
        public MTExpression getTrueExpression() { return truepart; }
        public MTExpression getFalseExpression() { return falsepart; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitConditionalExpression(this, d);
        }
        @Override
        public int getTag() {
            return CONDEXPR;
        }
    }

    /**
     * An "if ( ) { } else { }" block
     */
    public static class MTIf extends MTStatement implements IfTree {
        public MTExpression cond;
        public MTStatement thenpart;
        public MTStatement elsepart;
        protected MTIf(MTExpression cond,
		     MTStatement thenpart,
		     MTStatement elsepart)
	{
            this.cond = cond;
            this.thenpart = thenpart;
            this.elsepart = elsepart;
        }
        @Override
        public void accept(MTVisitor v) { v.visitIf(this); }

        public Kind getKind() { return Kind.IF; }
        public MTExpression getCondition() { return cond; }
        public MTStatement getThenStatement() { return thenpart; }
        public MTStatement getElseStatement() { return elsepart; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitIf(this, d);
        }
        @Override
        public int getTag() {
            return IF;
        }
    }

    /**
     * an expression statement
     * @param expr expression structure
     */
    public static class MTExpressionStatement extends MTStatement implements ExpressionStatementTree {
        public MTExpression expr;
        protected MTExpressionStatement(MTExpression expr)
	{
            this.expr = expr;
        }
        @Override
        public void accept(MTVisitor v) { v.visitExec(this); }

        public Kind getKind() { return Kind.EXPRESSION_STATEMENT; }
        public MTExpression getExpression() { return expr; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitExpressionStatement(this, d);
        }
        @Override
        public int getTag() {
            return EXEC;
        }
    }

    /**
     * A break from a loop or switch.
     */
    public static class MTBreak extends MTStatement implements BreakTree {
        public Name label;
        public MTTree target;
        protected MTBreak(Name label, MTTree target) {
            this.label = label;
            this.target = target;
        }
        @Override
        public void accept(MTVisitor v) { v.visitBreak(this); }

        public Kind getKind() { return Kind.BREAK; }
        public Name getLabel() { return label; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitBreak(this, d);
        }
        @Override
        public int getTag() {
            return BREAK;
        }
    }

    /**
     * A continue of a loop.
     */
    public static class MTContinue extends MTStatement implements ContinueTree {
        public Name label;
        public MTTree target;
        protected MTContinue(Name label, MTTree target) {
            this.label = label;
            this.target = target;
        }
        @Override
        public void accept(MTVisitor v) { v.visitContinue(this); }

        public Kind getKind() { return Kind.CONTINUE; }
        public Name getLabel() { return label; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitContinue(this, d);
        }
        @Override
        public int getTag() {
            return CONTINUE;
        }
    }

    /**
     * A return statement.
     */
    public static class MTReturn extends MTStatement implements ReturnTree {
        public MTExpression expr;
        protected MTReturn(MTExpression expr) {
            this.expr = expr;
        }
        @Override
        public void accept(MTVisitor v) { v.visitReturn(this); }

        public Kind getKind() { return Kind.RETURN; }
        public MTExpression getExpression() { return expr; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitReturn(this, d);
        }
        @Override
        public int getTag() {
            return RETURN;
        }
    }

    /**
     * A throw statement.
     */
    public static class MTThrow extends MTStatement implements ThrowTree {
        public MTExpression expr;
        protected MTThrow(MTTree expr) {
            this.expr = (MTExpression)expr;
        }
        @Override
        public void accept(MTVisitor v) { v.visitThrow(this); }

        public Kind getKind() { return Kind.THROW; }
        public MTExpression getExpression() { return expr; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitThrow(this, d);
        }
        @Override
        public int getTag() {
            return THROW;
        }
    }

    /**
     * An assert statement.
     */
    public static class MTAssert extends MTStatement implements AssertTree {
        public MTExpression cond;
        public MTExpression detail;
        protected MTAssert(MTExpression cond, MTExpression detail) {
            this.cond = cond;
            this.detail = detail;
        }
        @Override
        public void accept(MTVisitor v) { v.visitAssert(this); }

        public Kind getKind() { return Kind.ASSERT; }
        public MTExpression getCondition() { return cond; }
        public MTExpression getDetail() { return detail; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitAssert(this, d);
        }
        @Override
        public int getTag() {
            return ASSERT;
        }
    }

    /**
     * A method invocation
     */
    public static class MTMethodInvocation extends MTExpression implements MethodInvocationTree {
        public List<MTExpression> typeargs;
        public MTExpression meth;
        public List<MTExpression> args;
        public Type varargsElement;
        protected MTMethodInvocation(List<MTExpression> typeargs,
			MTExpression meth,
			List<MTExpression> args)
	{
	    this.typeargs = (typeargs == null) ? List.<MTExpression>nil()
		                               : typeargs;
            this.meth = meth;
            this.args = args;
        }
        @Override
        public void accept(MTVisitor v) { v.visitApply(this); }

        public Kind getKind() { return Kind.METHOD_INVOCATION; }
        public List<MTExpression> getTypeArguments() {
            return typeargs;
        }
        public MTExpression getMethodSelect() { return meth; }
        public List<MTExpression> getArguments() {
            return args;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitMethodInvocation(this, d);
        }
        @Override
        public MTMethodInvocation setType(Type type) {
            super.setType(type);
            return this;
        }
        @Override
        public int getTag() {
            return(APPLY);
        }
    }

    /**
     * A parenthesized subexpression ( ... )
     */
    public static class MTParens extends MTExpression implements ParenthesizedTree {
        public MTExpression expr;
        protected MTParens(MTExpression expr) {
            this.expr = expr;
        }
        @Override
        public void accept(MTVisitor v) { v.visitParens(this); }

        public Kind getKind() { return Kind.PARENTHESIZED; }
        public MTExpression getExpression() { return expr; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitParenthesized(this, d);
        }
        @Override
        public int getTag() {
            return PARENS;
        }
    }

    /**
     * A assignment with "=".
     */
    public static class MTAssign extends MTExpression implements AssignmentTree {
        public MTExpression lhs;
        public MTExpression rhs;
        protected MTAssign(MTExpression lhs, MTExpression rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }
        @Override
        public void accept(MTVisitor v) { v.visitAssign(this); }

        public Kind getKind() { return Kind.ASSIGNMENT; }
        public MTExpression getVariable() { return lhs; }
        public MTExpression getExpression() { return rhs; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitAssignment(this, d);
        }
        @Override
        public int getTag() {
            return ASSIGN;
        }
    }

    /**
     * An assignment with "+=", "|=" ...
     */
    public static class MTAssignOp extends MTExpression implements CompoundAssignmentTree {
	private int opcode;
        public MTExpression lhs;
        public MTExpression rhs;
        public Symbol operator;
        protected MTAssignOp(int opcode, MTTree lhs, MTTree rhs, Symbol operator) {
	    this.opcode = opcode;
            this.lhs = (MTExpression)lhs;
            this.rhs = (MTExpression)rhs;
            this.operator = operator;
        }
        @Override
        public void accept(MTVisitor v) { v.visitAssignop(this); }

        public Kind getKind() { return MTTreeInfo.tagToKind(getTag()); }
        public MTExpression getVariable() { return lhs; }
        public MTExpression getExpression() { return rhs; }
        public Symbol getOperator() {
	    return operator;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitCompoundAssignment(this, d);
        }
	@Override
        public int getTag() {
            return opcode;
        }
    }

    /**
     * A unary operation.
     */
    public static class MTUnary extends MTExpression implements UnaryTree {
	private int opcode;
        public MTExpression arg;
        public Symbol operator;
        protected MTUnary(int opcode, MTExpression arg) {
            this.opcode = opcode;
            this.arg = arg;
        }
        @Override
        public void accept(MTVisitor v) { v.visitUnary(this); }

        public Kind getKind() { return MTTreeInfo.tagToKind(getTag()); }
        public MTExpression getExpression() { return arg; }
        public Symbol getOperator() {
	    return operator;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitUnary(this, d);
        }
	@Override
        public int getTag() {
            return opcode;
        }
        
        public void setTag(int tag) {
            opcode = tag;
        }
    }

    /**
     * A binary operation.
     */
    public static class MTBinary extends MTExpression implements BinaryTree {
	private int opcode;
        public MTExpression lhs;
        public MTExpression rhs;
        public Symbol operator;
        protected MTBinary(int opcode,
			 MTExpression lhs,
			 MTExpression rhs,
			 Symbol operator) {
	    this.opcode = opcode;
            this.lhs = lhs;
            this.rhs = rhs;
            this.operator = operator;
        }
        @Override
        public void accept(MTVisitor v) { v.visitBinary(this); }

        public Kind getKind() { return MTTreeInfo.tagToKind(getTag()); }
        public MTExpression getLeftOperand() { return lhs; }
        public MTExpression getRightOperand() { return rhs; }
        public Symbol getOperator() {
	    return operator;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitBinary(this, d);
        }
	@Override
        public int getTag() {
            return opcode;
        }
    }

    /**
     * A type cast.
     */
    public static class MTTypeCast extends MTExpression implements TypeCastTree {
        public MTTree clazz;
        public MTExpression expr;
        protected MTTypeCast(MTTree clazz, MTExpression expr) {
            this.clazz = clazz;
            this.expr = expr;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTypeCast(this); }

        public Kind getKind() { return Kind.TYPE_CAST; }
        public MTTree getType() { return clazz; }
        public MTExpression getExpression() { return expr; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitTypeCast(this, d);
        }
	@Override
        public int getTag() {
            return TYPECAST;
        }
    }

    /**
     * A type test.
     */
    public static class MTInstanceOf extends MTExpression implements InstanceOfTree {
        public MTExpression expr;
        public MTTree clazz;
        protected MTInstanceOf(MTExpression expr, MTTree clazz) {
            this.expr = expr;
            this.clazz = clazz;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTypeTest(this); }

        public Kind getKind() { return Kind.INSTANCE_OF; }
        public MTTree getType() { return clazz; }
        public MTExpression getExpression() { return expr; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitInstanceOf(this, d);
        }
        @Override
        public int getTag() {
            return TYPETEST;
        }
    }

    /**
     * An array selection
     */
    public static class MTArrayAccess extends MTExpression implements ArrayAccessTree {
        public MTExpression indexed;
        public MTExpression index;
        protected MTArrayAccess(MTExpression indexed, MTExpression index) {
            this.indexed = indexed;
            this.index = index;
        }
        @Override
        public void accept(MTVisitor v) { v.visitIndexed(this); }

        public Kind getKind() { return Kind.ARRAY_ACCESS; }
        public MTExpression getExpression() { return indexed; }
        public MTExpression getIndex() { return index; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitArrayAccess(this, d);
        }
        @Override
        public int getTag() {
            return INDEXED;
        }
    }

    /**
     * Selects through packages and classes
     * @param selected selected Tree hierarchie
     * @param selector name of field to select thru
     * @param sym symbol of the selected class
     */
    public static class MTFieldAccess extends MTExpression implements MemberSelectTree {
        public MTExpression selected;
        public Name name;
        public Symbol sym;
        protected MTFieldAccess(MTExpression selected, Name name, Symbol sym) {
            this.selected = selected;
            this.name = name;
            this.sym = sym;
        }
        @Override
        public void accept(MTVisitor v) { v.visitSelect(this); }

        public Kind getKind() { return Kind.MEMBER_SELECT; }
        public MTExpression getExpression() { return selected; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitMemberSelect(this, d);
        }
        public Name getIdentifier() { return name; }
        @Override
        public int getTag() {
            return SELECT;
        }
    }

    /**
     * An identifier
     * @param idname the name
     * @param sym the symbol
     */
    public static class MTIdent extends MTExpression implements IdentifierTree {
        public Name name;
        public Symbol sym;
        protected MTIdent(Name name, Symbol sym) {
            this.name = name;
            this.sym = sym;
        }
        @Override
        public void accept(MTVisitor v) { v.visitIdent(this); }

        public Kind getKind() { return Kind.IDENTIFIER; }
        public Name getName() { return name; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitIdentifier(this, d);
        }
        public int getTag() {
            return IDENT;
        }
    }

    /**
     * A constant value given literally.
     * @param value value representation
     */
    public static class MTLiteral extends MTExpression implements LiteralTree {
        public int typetag;
        public Object value;
        protected MTLiteral(int typetag, Object value) {
            this.typetag = typetag;
            this.value = value;
        }
        @Override
        public void accept(MTVisitor v) { v.visitLiteral(this); }

        public Kind getKind() {
            switch (typetag) {
            case TypeTags.INT:
                return Kind.INT_LITERAL;
            case TypeTags.LONG:
                return Kind.LONG_LITERAL;
            case TypeTags.FLOAT:
                return Kind.FLOAT_LITERAL;
            case TypeTags.DOUBLE:
                return Kind.DOUBLE_LITERAL;
            case TypeTags.BOOLEAN:
                return Kind.BOOLEAN_LITERAL;
            case TypeTags.CHAR:
                return Kind.CHAR_LITERAL;
            case TypeTags.CLASS:
                return Kind.STRING_LITERAL;
   	    case TypeTags.BOT: 
		return Kind.NULL_LITERAL;
            default:
                throw new AssertionError("unknown literal kind " + this);
            }
        }
        public Object getValue() {
            switch (typetag) {
                case TypeTags.BOOLEAN:
                    int bi = (Integer) value;
                    return (bi != 0);
                case TypeTags.CHAR:
                    int ci = (Integer) value;
                    char c = (char) ci;
                    if (c != ci)
                        throw new AssertionError("bad value for char literal");
                    return c;
                default:
                    return value;
            }
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitLiteral(this, d);
        }
        @Override
        public MTLiteral setType(Type type) {
            super.setType(type);
            return this;
        }
        @Override
        public int getTag() {
            return LITERAL;
        }
    }

    /**
     * Identifies a basic type.
     * @param tag the basic type id
     * @see TypeTags
     */
    public static class MTPrimitiveTypeTree extends MTExpression implements PrimitiveTypeTree {
        public int typetag;
        protected MTPrimitiveTypeTree(int typetag) {
            this.typetag = typetag;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTypeIdent(this); }

        public Kind getKind() { return Kind.PRIMITIVE_TYPE; }
        public TypeKind getPrimitiveTypeKind() {
            switch (typetag) {
            case TypeTags.BOOLEAN:
                return TypeKind.BOOLEAN;
            case TypeTags.BYTE:
                return TypeKind.BYTE;
            case TypeTags.SHORT:
                return TypeKind.SHORT;
            case TypeTags.INT:
                return TypeKind.INT;
            case TypeTags.LONG:
                return TypeKind.LONG;
            case TypeTags.CHAR:
                return TypeKind.CHAR;
            case TypeTags.FLOAT:
                return TypeKind.FLOAT;
            case TypeTags.DOUBLE:
                return TypeKind.DOUBLE;
            case TypeTags.VOID:
                return TypeKind.VOID;
            default:
                throw new AssertionError("unknown primitive type " + this);
            }
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitPrimitiveType(this, d);
        }
        @Override
        public int getTag() {
            return TYPEIDENT;
        }
    }

    /**
     * An array type, A[]
     */
    public static class MTArrayTypeTree extends MTExpression implements ArrayTypeTree {
        public MTExpression elemtype;
        protected MTArrayTypeTree(MTExpression elemtype) {
            this.elemtype = elemtype;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTypeArray(this); }

        public Kind getKind() { return Kind.ARRAY_TYPE; }
        public MTTree getType() { return elemtype; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitArrayType(this, d);
        }
        @Override
        public int getTag() {
            return TYPEARRAY;
        }
    }

    /**
     * A parameterized type, T<...>
     */
    public static class MTTypeApply extends MTExpression implements ParameterizedTypeTree {
        public MTExpression clazz;
        public List<MTExpression> arguments;
        protected MTTypeApply(MTExpression clazz, List<MTExpression> arguments) {
            this.clazz = clazz;
            this.arguments = arguments;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTypeApply(this); }

        public Kind getKind() { return Kind.PARAMETERIZED_TYPE; }
        public MTTree getType() { return clazz; }
        public List<MTExpression> getTypeArguments() {
            return arguments;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitParameterizedType(this, d);
        }
        @Override
        public int getTag() {
            return TYPEAPPLY;
        }
    }

    /**
     * A formal class parameter.
     * @param name name
     * @param bounds bounds
     */
    public static class MTTypeParameter extends MTTree implements TypeParameterTree {
        public Name name;
        public List<MTExpression> bounds;
        protected MTTypeParameter(Name name, List<MTExpression> bounds) {
            this.name = name;
            this.bounds = bounds;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTypeParameter(this); }

        public Kind getKind() { return Kind.TYPE_PARAMETER; }
        public Name getName() { return name; }
        public List<MTExpression> getBounds() {
            return bounds;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitTypeParameter(this, d);
        }
        @Override
        public int getTag() {
            return TYPEPARAMETER;
        }
    }

    public static class MTWildcard extends MTExpression implements WildcardTree {
        public MTTypeBoundKind kind;
        public MTTree inner;
        protected MTWildcard(MTTypeBoundKind kind, MTTree inner) {
            kind.getClass(); // null-check
            this.kind = kind;
            this.inner = inner;
        }
        @Override
        public void accept(MTVisitor v) { v.visitWildcard(this); }

        public Kind getKind() {
            switch (kind.kind) {
            case UNBOUND:
                return Kind.UNBOUNDED_WILDCARD;
            case EXTENDS:
                return Kind.EXTENDS_WILDCARD;
            case SUPER:
                return Kind.SUPER_WILDCARD;
            default:
                throw new AssertionError("Unknown wildcard bound " + kind);
            }
        }
        public MTTree getBound() { return inner; }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitWildcard(this, d);
        }
        @Override
        public int getTag() {
            return WILDCARD;
        }
    }

    public static class MTTypeBoundKind extends MTTree {
        public BoundKind kind;
        protected MTTypeBoundKind(BoundKind kind) {
            this.kind = kind;
        }
        @Override
        public void accept(MTVisitor v) { v.visitTypeBoundKind(this); }

        public Kind getKind() {
            throw new AssertionError("TypeBoundKind is not part of a public API");
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            throw new AssertionError("TypeBoundKind is not part of a public API");
        }
        @Override
        public int getTag() {
            return TYPEBOUNDKIND;
        }
    }

    public static class MTAnnotation extends MTExpression implements AnnotationTree {
        public MTTree annotationType;
        public List<MTExpression> args;
        protected MTAnnotation(MTTree annotationType, List<MTExpression> args) {
            this.annotationType = annotationType;
            this.args = args;
        }
        @Override
        public void accept(MTVisitor v) { v.visitAnnotation(this); }

        public Kind getKind() { return Kind.ANNOTATION; }
        public MTTree getAnnotationType() { return annotationType; }
        public List<MTExpression> getArguments() {
            return args;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitAnnotation(this, d);
        }
        @Override
        public int getTag() {
            return ANNOTATION;
        }
    }

    public static class MTModifiers extends MTTree implements com.sun.source.tree.ModifiersTree {
        public long flags;
        public List<MTAnnotation> annotations;
        protected MTModifiers(long flags, List<MTAnnotation> annotations) {
            this.flags = flags;
            this.annotations = annotations;
        }
        @Override
        public void accept(MTVisitor v) { v.visitModifiers(this); }

        public Kind getKind() { return Kind.MODIFIERS; }
        public Set<Modifier> getFlags() {
            return Flags.asModifierSet(flags);
        }
        public List<MTAnnotation> getAnnotations() {
            return annotations;
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitModifiers(this, d);
        }
        @Override
        public int getTag() {
            return MODIFIERS;
        }
    }

    public static class MTErroneous extends MTExpression
            implements com.sun.source.tree.ErroneousTree {
        public List<? extends MTTree> errs;
        protected MTErroneous(List<? extends MTTree> errs) {
            this.errs = errs;
        }
        @Override
        public void accept(MTVisitor v) { v.visitErroneous(this); }

        public Kind getKind() { return Kind.ERRONEOUS; }

        public List<? extends MTTree> getErrorTrees() {
            return errs;
        }

        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            return v.visitErroneous(this, d);
        }
        @Override
        public int getTag() {
            return ERRONEOUS;
        }
    }

    /** (let int x = 3; in x+2) */
    public static class MTLetExpr extends MTExpression {
        public List<MTVariableDecl> defs;
        public MTTree expr;
        protected MTLetExpr(List<MTVariableDecl> defs, MTTree expr) {
            this.defs = defs;
            this.expr = expr;
        }
        @Override
        public void accept(MTVisitor v) { v.visitLetExpr(this); }

        public Kind getKind() {
            throw new AssertionError("LetExpr is not part of a public API");
        }
        @Override
        public <R,D> R accept(TreeVisitor<R,D> v, D d) {
            throw new AssertionError("LetExpr is not part of a public API");
        }
        @Override
        public int getTag() {
            return LETEXPR;
        }
    }

}
