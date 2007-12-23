/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.migrator.tree;

import com.sun.tools.migrator.tree.MTTree.*;
//import com.sun.tools.migrator.tree.MigrateDiagnostic.DiagnosticPosition;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import java.util.Map;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.*;
import static com.sun.tools.javac.code.Flags.*;

/** Utility class containing inspector methods for trees.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class MTTreeInfo {

    protected MTTreeInfo(Context context) {
	context.put(treeInfoKey, this);

	Name.Table names = Name.Table.instance(context);
        opname = new Name[MTTag.JFX_OP_LAST - MTTree.POS + 1];
        opname[MTTree.POS     - MTTree.POS] = names.fromString("+");
	opname[MTTree.NEG     - MTTree.POS] = names.hyphen;
	opname[MTTree.NOT     - MTTree.POS] = names.fromString("!");
	opname[MTTree.COMPL   - MTTree.POS] = names.fromString("~");
	opname[MTTree.PREINC  - MTTree.POS] = names.fromString("++");
	opname[MTTree.PREDEC  - MTTree.POS] = names.fromString("--");
	opname[MTTree.POSTINC - MTTree.POS] = names.fromString("++");
	opname[MTTree.POSTDEC - MTTree.POS] = names.fromString("--");
	opname[MTTree.NULLCHK - MTTree.POS] = names.fromString("<*nullchk*>");
	opname[MTTree.OR      - MTTree.POS] = names.fromString("or");
	opname[MTTree.AND     - MTTree.POS] = names.fromString("and");
	opname[MTTree.EQ      - MTTree.POS] = names.fromString("==");
	opname[MTTree.NE      - MTTree.POS] = names.fromString("<>");
	opname[MTTree.LT      - MTTree.POS] = names.fromString("<");
	opname[MTTree.GT      - MTTree.POS] = names.fromString(">");
	opname[MTTree.LE      - MTTree.POS] = names.fromString("<=");
	opname[MTTree.GE      - MTTree.POS] = names.fromString(">=");
	opname[MTTree.SL      - MTTree.POS] = names.fromString("<<");
	opname[MTTree.SR      - MTTree.POS] = names.fromString(">>");
	opname[MTTree.USR     - MTTree.POS] = names.fromString(">>>");
	opname[MTTree.PLUS    - MTTree.POS] = names.fromString("+");
	opname[MTTree.MINUS   - MTTree.POS] = names.hyphen;
	opname[MTTree.MUL     - MTTree.POS] = names.asterisk;
	opname[MTTree.DIV     - MTTree.POS] = names.slash;
	opname[MTTree.MOD     - MTTree.POS] = names.fromString("%");
	opname[MTTag.XOR     - MTTree.POS] = names.fromString("xor");
	opname[MTTag.BINDOP  - MTTree.POS] = names.fromString("bind");
	opname[MTTag.LAZYOP  - MTTree.POS] = names.fromString("lazy");
    }    

    protected static final Context.Key<MTTreeInfo> treeInfoKey =
	new Context.Key<MTTreeInfo>();

    public static void preRegister(final Context context) {
        context.put(treeInfoKey, new Context.Factory<MTTreeInfo>() {
	       public MTTreeInfo make() {
		   return new MTTreeInfo(context);
	       }
        });
    }

    public static MTTreeInfo instance(Context context) {
	MTTreeInfo instance = context.get(treeInfoKey);
	if (instance == null)
	    instance = new MTTreeInfo(context);
	return instance;
    }

    /** The names of all operators.
     */
// JavaFX change
    protected
    /*private*/ Name[] opname = new Name[MTTree.MOD - MTTree.POS + 1];


    /** Return name of operator with given tree tag.
     */
    public Name operatorName(int tag) {
	return opname[tag - MTTree.POS];
    }

    /** Is statement an initializer for a synthetic field?
     */
    public static boolean isSyntheticInit(MTTree stat) {
	if (stat.getTag() == MTTree.EXEC) {
	    MTExpressionStatement exec = (MTExpressionStatement)stat;
	    if (exec.expr.getTag() == MTTree.ASSIGN) {
		MTAssign assign = (MTAssign)exec.expr;
		if (assign.lhs.getTag() == MTTree.SELECT) {
		    MTFieldAccess select = (MTFieldAccess)assign.lhs;
		    if (select.sym != null &&
			(select.sym.flags() & SYNTHETIC) != 0) {
			Name selected = name(select.selected);
			if (selected != null && selected == selected.table._this)
			    return true;
		    }
		}
	    }
	}
	return false;
    }

    /** If the expression is a method call, return the method name, null
     *  otherwise. */
    public static Name calledMethodName(MTTree tree) {
	if (tree.getTag() == MTTree.EXEC) {
	    MTExpressionStatement exec = (MTExpressionStatement)tree;
	    if (exec.expr.getTag() == MTTree.APPLY) {
		Name mname = MTTreeInfo.name(((MTMethodInvocation) exec.expr).meth);
		return mname;
	    }
	}
	return null;
    }

    /** Is this a call to this or super?
     */
    public static boolean isSelfCall(MTTree tree) {
	Name name = calledMethodName(tree);
	if (name != null) {
	    Name.Table names = name.table;
	    return name==names._this || name==names._super;
	} else {
	    return false;
	}
    }

    /** Is this a call to super?
     */
    public static boolean isSuperCall(MTTree tree) {
	Name name = calledMethodName(tree);
	if (name != null) {
	    Name.Table names = name.table;
	    return name==names._super;
	} else {
	    return false;
	}
    }

    /** Return true if a tree represents the null literal. */
    public static boolean isNull(MTTree tree) {
        if (tree.getTag() != MTTree.LITERAL) 
            return false;
        MTLiteral lit = (MTLiteral) tree;
        return (lit.typetag == TypeTags.BOT);
    }

    /** The position of the first statement in a block, or the position of
     *  the block itself if it is empty.
     */
    public static int firstStatPos(MTTree tree) {
	if (tree.getTag() == MTTree.BLOCK && ((MTBlock) tree).stats.nonEmpty())
	    return ((MTBlock) tree).stats.head.pos;
	else
	    return tree.pos;
    }

    /** The end position of given tree, if it is a block with
     *  defined endpos.
     */
    public static int endPos(MTTree tree) {
	if (tree.getTag() == MTTree.BLOCK && ((MTBlock) tree).endpos != Position.NOPOS)
	    return ((MTBlock) tree).endpos;
	else if (tree.getTag() == MTTree.TRY) {
	    MTTry t = (MTTry) tree;
	    return endPos((t.finalizer != null)
			  ? t.finalizer
			  : t.catchers.last().body);
	} else
	    return tree.pos;
    }


    /** Get the start position for a tree node.  The start position is
     * defined to be the position of the first character of the first
     * token of the node's source text.
     * @param tree  The tree node
     */
    public static int getStartPos(MTTree tree) {
	if (tree == null)
	    return Position.NOPOS;
        
	switch(tree.getTag()) {
	case(MTTree.APPLY):
	    return getStartPos(((MTMethodInvocation) tree).meth);
	case(MTTree.ASSIGN):
	    return getStartPos(((MTAssign) tree).lhs);
	case(MTTree.BITOR_ASG): case(MTTree.BITXOR_ASG): case(MTTree.BITAND_ASG):
	case(MTTree.SL_ASG): case(MTTree.SR_ASG): case(MTTree.USR_ASG):
	case(MTTree.PLUS_ASG): case(MTTree.MINUS_ASG): case(MTTree.MUL_ASG):
	case(MTTree.DIV_ASG): case(MTTree.MOD_ASG):
	    return getStartPos(((MTAssignOp) tree).lhs);
	case(MTTree.OR): case(MTTree.AND): case(MTTree.BITOR):
	case(MTTree.BITXOR): case(MTTree.BITAND): case(MTTree.EQ):
	case(MTTree.NE): case(MTTree.LT): case(MTTree.GT):
	case(MTTree.LE): case(MTTree.GE): case(MTTree.SL):
	case(MTTree.SR): case(MTTree.USR): case(MTTree.PLUS):
	case(MTTree.MINUS): case(MTTree.MUL): case(MTTree.DIV):
	case(MTTree.MOD):
	    return getStartPos(((MTBinary) tree).lhs);
            /**
	case(MTTree.CLASSDEF): {
	    JCClassDecl node = (JCClassDecl)tree;
	    if (node.mods.pos != Position.NOPOS)
		return node.mods.pos;
	    break;
	}
             * ***/
	case(MTTree.CONDEXPR):
	    return getStartPos(((MTConditional) tree).cond);
	case(MTTree.EXEC):
	    return getStartPos(((MTExpressionStatement) tree).expr);
	case(MTTree.INDEXED):
	    return getStartPos(((MTArrayAccess) tree).indexed);
            /***
	case(MTTree.METHODDEF): { 
	    JCMethodDecl node = (JCMethodDecl)tree;
	    if (node.mods.pos != Position.NOPOS)
		return node.mods.pos;
	    if (node.typarams.nonEmpty()) // List.nil() used for no typarams
		return getStartPos(node.typarams.head);
            return node.restype == null ? node.pos : getStartPos(node.restype);
	}
             * ****/
	case(MTTree.SELECT):
	    return getStartPos(((MTFieldAccess) tree).selected);
	case(MTTree.TYPEAPPLY):
	    return getStartPos(((MTTypeApply) tree).clazz);
	case(MTTree.TYPEARRAY):
	    return getStartPos(((MTArrayTypeTree) tree).elemtype);
	case(MTTree.TYPETEST):
	    return getStartPos(((MTInstanceOf) tree).expr);
	case(MTTree.POSTINC):
	case(MTTree.POSTDEC):
	    return getStartPos(((MTUnary) tree).arg);
	case(MTTree.VARDEF): {
	    MTVariableDecl node = (MTVariableDecl)tree;
	    if (node.mods.pos != Position.NOPOS) {
		return node.mods.pos;
	    } else {
		return getStartPos(node.vartype);
	    }
	}
        case(MTTree.ERRONEOUS): {
            MTErroneous node = (MTErroneous)tree;
            if (node.errs != null && node.errs.nonEmpty())
                return getStartPos(node.errs.head);
        }
	}
	return tree.pos;
    }

    /** The end position of given tree, given  a table of end positions generated by the parser
     */
    public static int getEndPos(MTTree tree, Map<MTTree, Integer> endPositions) {
	if (tree == null)
	    return Position.NOPOS;
        
        if (endPositions == null) {
            // fall back on limited info in the tree
            return endPos(tree);
        }

	Integer mapPos = endPositions.get(tree);
	if (mapPos != null)
	    return mapPos;

	switch(tree.getTag()) {
	case(MTTree.BITOR_ASG): case(MTTree.BITXOR_ASG): case(MTTree.BITAND_ASG):
	case(MTTree.SL_ASG): case(MTTree.SR_ASG): case(MTTree.USR_ASG):
	case(MTTree.PLUS_ASG): case(MTTree.MINUS_ASG): case(MTTree.MUL_ASG):
	case(MTTree.DIV_ASG): case(MTTree.MOD_ASG):
	    return getEndPos(((MTAssignOp) tree).rhs, endPositions);
	case(MTTree.OR): case(MTTree.AND): case(MTTree.BITOR):
	case(MTTree.BITXOR): case(MTTree.BITAND): case(MTTree.EQ):
	case(MTTree.NE): case(MTTree.LT): case(MTTree.GT):
	case(MTTree.LE): case(MTTree.GE): case(MTTree.SL):
	case(MTTree.SR): case(MTTree.USR): case(MTTree.PLUS):
	case(MTTree.MINUS): case(MTTree.MUL): case(MTTree.DIV):
	case(MTTree.MOD):
	    return getEndPos(((MTBinary) tree).rhs, endPositions);
	case(MTTree.CATCH):
	    return getEndPos(((MTCatch) tree).body, endPositions);
	case(MTTree.CONDEXPR):
	    return getEndPos(((MTConditional) tree).falsepart, endPositions);
	case(MTTree.IF): {
	    MTIf node = (MTIf)tree;
	    if (node.elsepart == null) {
		return getEndPos(node.thenpart, endPositions);
	    } else {
		return getEndPos(node.elsepart, endPositions);
	    }
	}
	case(MTTree.MODIFIERS):
	    return getEndPos(((MTModifiers) tree).annotations.last(), endPositions);
	case(MTTree.TOPLEVEL):
	    return getEndPos(((MTCompilationUnit) tree).defs.last(), endPositions);
	case(MTTree.TRY): {
	    MTTry node = (MTTry)tree;
            if (node.finalizer != null) {
 		return getEndPos(node.finalizer, endPositions);
	    } else if (!node.catchers.isEmpty()) {
		return getEndPos(node.catchers.last(), endPositions);
            } else {
		return getEndPos(node.body, endPositions);                
 	    }
	}
	case(MTTree.WILDCARD):
	    return getEndPos(((MTWildcard) tree).inner, endPositions);
	case(MTTree.TYPECAST):
	    return getEndPos(((MTTypeCast) tree).expr, endPositions);
	case(MTTree.TYPETEST):
	    return getEndPos(((MTInstanceOf) tree).clazz, endPositions);
	case(MTTree.POS):
	case(MTTree.NEG):
	case(MTTree.NOT):
	case(MTTree.COMPL):
	case(MTTree.PREINC):
	case(MTTree.PREDEC):
	    return getEndPos(((MTUnary) tree).arg, endPositions);
	case(MTTree.WHILELOOP):
	    return getEndPos(((MTWhileLoop) tree).body, endPositions);
        case(MTTree.ERRONEOUS): {
            MTErroneous node = (MTErroneous)tree;
            if (node.errs != null && node.errs.nonEmpty())
                return getEndPos(node.errs.last(), endPositions);
        }
        }
	return Position.NOPOS;
    }
    

    /** A DiagnosticPosition with the preferred position set to the 
     *  end position of given tree, if it is a block with
     *  defined endpos.
     */
/****
 * public static DiagnosticPosition diagEndPos(final MTTree tree) {
        final int endPos = JavafxTreeInfo.endPos(tree);
        return new DiagnosticPosition() {
            public MTTree getTree() { return tree; }
            public int getStartPosition() { return JavafxTreeInfo.getStartPos(tree); }
            public int getPreferredPosition() { return endPos; }
            public int getEndPosition(Map<MTTree, Integer> endPosTable) { 
                return JavafxTreeInfo.getEndPos(tree, endPosTable);
            }
        };
    }
 * ****/

    /** The position of the finalizer of given try/synchronized statement.
     */
    public static int finalizerPos(MTTree tree) {
	if (tree.getTag() == MTTree.TRY) {
	    MTTry t = (MTTry) tree;
	    assert t.finalizer != null;
	    return firstStatPos(t.finalizer);
	} else {
	    throw new AssertionError();
	}
    }

    /** Find the position for reporting an error about a symbol, where
     *  that symbol is defined somewhere in the given tree. */
    public static int positionFor(final Symbol sym, final MTTree tree) {
        MTTree decl = declarationFor(sym, tree);
        return ((decl != null) ? decl : tree).pos;
    }

    /** Find the position for reporting an error about a symbol, where
     *  that symbol is defined somewhere in the given tree. */
/***
 * public static DiagnosticPosition diagnosticPositionFor(final Symbol sym, final MTTree tree) {
        MTTree decl = declarationFor(sym, tree);
        return ((decl != null) ? decl : tree).pos();
    }
 * ***/

    public static Env<AttrContext> scopeFor(MTTree node, MTCompilationUnit unit) {
	return scopeFor(pathFor(node, unit));
    }

    public static Env<AttrContext> scopeFor(List<MTTree> path) {
	// TODO: not implemented yet
	throw new UnsupportedOperationException("not implemented yet");
    }

    /** Skip parens and return the enclosed expression
     */
    public static MTExpression skipParens(MTExpression tree) {
	while (tree.getTag() == MTTree.PARENS) {
	    tree = ((MTParens) tree).expr;
	}
	return tree;
    }

    /** Skip parens and return the enclosed expression
     */
    public static MTTree skipParens(MTTree tree) {
	if (tree.getTag() == MTTree.PARENS)
	    return skipParens((MTParens)tree);
	else
	    return tree;
    }

    /** Return the types of a list of trees.
     */
    public static List<Type> types(List<? extends MTTree> trees) {
	ListBuffer<Type> ts = new ListBuffer<Type>();
	for (List<? extends MTTree> l = trees; l.nonEmpty(); l = l.tail)
	    ts.append(l.head.type);
	return ts.toList();
    }

    /** If this tree is an identifier or a field or a parameterized type,
     *  return its name, otherwise return null.
     */
    public static Name name(MTTree tree) {
	switch (tree.getTag()) {
	case MTTree.IDENT:
	    return ((MTIdent) tree).name;
	case MTTree.SELECT:
	    return ((MTFieldAccess) tree).name;
	case MTTree.TYPEAPPLY:
	    return name(((MTTypeApply) tree).clazz);
	default:
	    return null;
	}
    }

    /** If this tree is a qualified identifier, its return fully qualified name,
     *  otherwise return null.
     */
    public static Name fullName(MTTree tree) {
	tree = skipParens(tree);
	switch (tree.getTag()) {
	case MTTree.IDENT:
	    return ((MTIdent) tree).name;
	case MTTree.SELECT:
	    Name sname = fullName(((MTFieldAccess) tree).selected);
	    return sname == null ? null : sname.append('.', name(tree));
	default:
	    return null;
	}
    }

    public static Symbol symbolFor(MTTree node) {
	node = skipParens(node);
	switch (node.getTag()) {
            /***
	case MTTree.CLASSDEF:
	    return ((JCClassDecl) node).sym;
	case MTTree.METHODDEF:
	    return ((JCMethodDecl) node).sym;
             * ****/
	case MTTree.VARDEF:
	    return ((MTVariableDecl) node).sym;
	default:
	    return null;
	}
    }

    /** If this tree is an identifier or a field, return its symbol,
     *  otherwise return null.
     */
    public static Symbol symbol(MTTree tree) {
	tree = skipParens(tree);
	switch (tree.getTag()) {
	case MTTree.IDENT:
	    return ((MTIdent) tree).sym;
	case MTTree.SELECT:
	    return ((MTFieldAccess) tree).sym;
	case MTTree.TYPEAPPLY:
	    return symbol(((MTTypeApply) tree).clazz);
	default:
	    return null;
	}
    }

    /** Return true if this is a nonstatic selection. */
    public static boolean nonstaticSelect(MTTree tree) {
	tree = skipParens(tree);
	if (tree.getTag() != MTTree.SELECT) return false;
	MTFieldAccess s = (MTFieldAccess) tree;
	Symbol e = symbol(s.selected);
	return e == null || (e.kind != Kinds.PCK && e.kind != Kinds.TYP);
    }

    /** If this tree is an identifier or a field, set its symbol, otherwise skip.
     */
    public static void setSymbol(MTTree tree, Symbol sym) {
	tree = skipParens(tree);
	switch (tree.getTag()) {
	case MTTree.IDENT:
	    ((MTIdent) tree).sym = sym; break;
	case MTTree.SELECT:
	    ((MTFieldAccess) tree).sym = sym; break;
	default:
	}
    }

    /** If this tree is a declaration or a block, return its flags field,
     *  otherwise return 0.
     */
    public static long flags(MTTree tree) {
	switch (tree.getTag()) {
	case MTTree.VARDEF:
	    return ((MTVariableDecl) tree).mods.flags;
            /***
	case MTTree.METHODDEF:
	    return ((JCMethodDecl) tree).mods.flags;
	case MTTree.CLASSDEF:
	    return ((JCClassDecl) tree).mods.flags;
             * ***/
	case MTTree.BLOCK:
	    return ((MTBlock) tree).flags;
	default:
	    return 0;
	}
    }

    /** Return first (smallest) flag in `flags':
     *  pre: flags != 0
     */
    public static long firstFlag(long flags) {
	int flag = 1;
        while ((flag & StandardFlags) != 0 && (flag & flags) == 0)
	    flag = flag << 1;
        return flag;
    }

    /** Return flags as a string, separated by " ".
     */
    public static String flagNames(long flags) {
        return Flags.toString(flags & StandardFlags).trim();
    }

    /** Map operators to their precedence levels.
     */
    public static int opPrec(int op) {
	switch(op) {
	case MTTree.ASSIGN: // Java distinguished, JavaFX doesn't -- Java-style
            return assignPrec;
	case MTTree.USR_ASG:
	case MTTree.PLUS_ASG:
	case MTTree.MINUS_ASG:
	case MTTree.MUL_ASG:
	case MTTree.DIV_ASG:
	case MTTree.MOD_ASG: 
            return assignopPrec;
        case MTTree.OR: 
        case MTTag.XOR: 
            return orPrec;
        case MTTree.AND: 
            return andPrec;
        case MTTree.EQ:
        case MTTree.NE: 
            return eqPrec;
        case MTTree.LT:
        case MTTree.GT:
        case MTTree.LE:
        case MTTree.GE: 
            return ordPrec;
        case MTTree.PLUS:
        case MTTree.MINUS: 
            return addPrec;
        case MTTree.MUL:
        case MTTree.DIV:
        case MTTree.MOD: 
            return mulPrec;
	case MTTree.TYPETEST: 
            return ordPrec;
	case MTTree.POS:
	case MTTree.NEG:
	case MTTree.NOT:
	case MTTree.COMPL:
	case MTTree.PREINC:
	case MTTree.PREDEC: 
            return prefixPrec;
	case MTTree.POSTINC:
	case MTTree.POSTDEC:
	case MTTree.NULLCHK: 
            return postfixPrec;
	default: throw new AssertionError();
	}
    }

    /** Find the declaration for a symbol, where
     *  that symbol is defined somewhere in the given tree. */
    public static MTTree declarationFor(final Symbol sym, final MTTree tree) {
	class DeclScanner extends MTTreeScanner {
            MTTree result = null;
            public void scan(MTTree tree) {
                if (tree!=null && result==null)
                    tree.accept(this);
            }
	    public void visitTopLevel(MTCompilationUnit that) {
		if (that.packge == sym) result = that;
		else super.visitTopLevel(that);
	    }
            /***
	    public void visitClassDef(JCClassDecl that) {
		if (that.sym == sym) result = that;
		else super.visitClassDef(that);
	    }
	    public void visitMethodDef(JCMethodDecl that) {
		if (that.sym == sym) result = that;
		else super.visitMethodDef(that);
	    }
             * ***/
	    public void visitVarDef(MTVariableDecl that) {
		if (that.sym == sym) result = that;
		else super.visitVarDef(that);
	    }
	}
	DeclScanner s = new DeclScanner();
	tree.accept(s);
	return s.result;
    }

    public static List<MTTree> pathFor(final MTTree node, final MTCompilationUnit unit) {
	class Result extends Error {
	    static final long serialVersionUID = -5942088234594905625L;
	    List<MTTree> path;
	    Result(List<MTTree> path) {
		this.path = path;
	    }
	}
	class PathFinder extends MTTreeScanner {
	    List<MTTree> path = List.nil();
	    public void scan(MTTree tree) {
		if (tree != null) {
		    path = path.prepend(tree);
		    if (tree == node)
			throw new Result(path);
		    super.scan(tree);
		    path = path.tail;
		}
	    }
	}
	try {
	    new PathFinder().scan(unit);
	} catch (Result result) {
	    return result.path;
	}
        return List.nil();
    }

    /** Operator precedences values.
     */
    /****  FX version
    public static final int
        notExpression = -1,   // not an expression
        noPrec = 0,           // no enclosing expression
        assignPrec = 1,
	assignopPrec = 2,
	orPrec = 3,
	andPrec = 4,
	eqPrec = 5,
	ordPrec = 6,
	addPrec = 7,
	mulPrec = 8,
	prefixPrec = 9,
	postfixPrec = 10,
	precCount = 11;
    ***/

    public static final int
        notExpression = -1,   // not an expression
        noPrec = 0,           // no enclosing expression
        assignPrec = 1,
	assignopPrec = 2,
	condPrec = 3,
	orPrec = 4,
	andPrec = 5,
	bitorPrec = 6,
	bitxorPrec = 7,
	bitandPrec = 8,
	eqPrec = 9,
	ordPrec = 10,
	shiftPrec = 11,
	addPrec = 12,
	mulPrec = 13,
	prefixPrec = 14,
	postfixPrec = 15,
	precCount = 16;

    static Tree.Kind tagToKind(int tag) {
        switch (tag) {
        // Postfix expressions
        case MTTree.POSTINC:           // _ ++
            return Tree.Kind.POSTFIX_INCREMENT;
        case MTTree.POSTDEC:           // _ --
            return Tree.Kind.POSTFIX_DECREMENT;

        // Unary operators
        case MTTree.PREINC:            // ++ _
            return Tree.Kind.PREFIX_INCREMENT;
        case MTTree.PREDEC:            // -- _
            return Tree.Kind.PREFIX_DECREMENT;
        case MTTree.POS:               // +
            return Tree.Kind.UNARY_PLUS;
        case MTTree.NEG:               // -
            return Tree.Kind.UNARY_MINUS;
        case MTTree.COMPL:             // ~
            return Tree.Kind.BITWISE_COMPLEMENT;
        case MTTree.NOT:               // !
            return Tree.Kind.LOGICAL_COMPLEMENT;

        // Binary operators

        // Multiplicative operators
        case MTTree.MUL:               // *
            return Tree.Kind.MULTIPLY;
        case MTTree.DIV:               // /
            return Tree.Kind.DIVIDE;
        case MTTree.MOD:               // %
            return Tree.Kind.REMAINDER;

        // Additive operators
        case MTTree.PLUS:              // +
            return Tree.Kind.PLUS;
        case MTTree.MINUS:             // -
            return Tree.Kind.MINUS;

        // Shift operators
        case MTTree.SL:                // <<
            return Tree.Kind.LEFT_SHIFT;
        case MTTree.SR:                // >>
            return Tree.Kind.RIGHT_SHIFT;
        case MTTree.USR:               // >>>
            return Tree.Kind.UNSIGNED_RIGHT_SHIFT;

        // Relational operators
        case MTTree.LT:                // <
            return Tree.Kind.LESS_THAN;
        case MTTree.GT:                // >
            return Tree.Kind.GREATER_THAN;
        case MTTree.LE:                // <=
            return Tree.Kind.LESS_THAN_EQUAL;
        case MTTree.GE:                // >=
            return Tree.Kind.GREATER_THAN_EQUAL;

        // Equality operators
        case MTTree.EQ:                // ==
            return Tree.Kind.EQUAL_TO;
        case MTTree.NE:                // !=
            return Tree.Kind.NOT_EQUAL_TO;

        // Bitwise and logical operators
        case MTTree.BITAND:            // &
            return Tree.Kind.AND;
        case MTTree.BITXOR:            // ^
            return Tree.Kind.XOR;
        case MTTree.BITOR:             // |
            return Tree.Kind.OR;

        // Conditional operators
        case MTTree.AND:               // &&
            return Tree.Kind.CONDITIONAL_AND;
        case MTTree.OR:                // ||
            return Tree.Kind.CONDITIONAL_OR;

        // Assignment operators
        case MTTree.MUL_ASG:           // *=
            return Tree.Kind.MULTIPLY_ASSIGNMENT;
        case MTTree.DIV_ASG:           // /=
            return Tree.Kind.DIVIDE_ASSIGNMENT;
        case MTTree.MOD_ASG:           // %=
            return Tree.Kind.REMAINDER_ASSIGNMENT;
        case MTTree.PLUS_ASG:          // +=
            return Tree.Kind.PLUS_ASSIGNMENT;
        case MTTree.MINUS_ASG:         // -=
            return Tree.Kind.MINUS_ASSIGNMENT;
        case MTTree.SL_ASG:            // <<=
            return Tree.Kind.LEFT_SHIFT_ASSIGNMENT;
        case MTTree.SR_ASG:            // >>=
            return Tree.Kind.RIGHT_SHIFT_ASSIGNMENT;
        case MTTree.USR_ASG:           // >>>=
            return Tree.Kind.UNSIGNED_RIGHT_SHIFT_ASSIGNMENT;
        case MTTree.BITAND_ASG:        // &=
            return Tree.Kind.AND_ASSIGNMENT;
        case MTTree.BITXOR_ASG:        // ^=
            return Tree.Kind.XOR_ASSIGNMENT;
        case MTTree.BITOR_ASG:         // |=
            return Tree.Kind.OR_ASSIGNMENT;

        // Null check (implementation detail), for example, __.getClass()
        case MTTree.NULLCHK:
            return Tree.Kind.OTHER;

        default:
            return null;
        }
    }
}



