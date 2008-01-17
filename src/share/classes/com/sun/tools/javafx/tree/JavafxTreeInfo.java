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

package com.sun.tools.javafx.tree;

import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.JCTree;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import java.util.Map;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.tree.JCTree.*;

import static com.sun.tools.javac.code.Flags.*;

/** Utility class containing inspector methods for trees.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxTreeInfo extends TreeInfo {

    public static void preRegister(final Context context) {
        context.put(treeInfoKey, new Context.Factory<TreeInfo>() {
	       public TreeInfo make() {
		   return new JavafxTreeInfo(context);
	       }
        });
    }

    protected JavafxTreeInfo(Context context) {
        super(context);

	Name.Table names = Name.Table.instance(context);
        opname = new Name[JavafxTag.JFX_OP_LAST - JCTree.POS + 1];
        opname[JCTree.POS     - JCTree.POS] = names.fromString("+");
	opname[JCTree.NEG     - JCTree.POS] = names.hyphen;
	opname[JCTree.NOT     - JCTree.POS] = names.fromString("!");
	opname[JCTree.COMPL   - JCTree.POS] = names.fromString("~");
	opname[JCTree.PREINC  - JCTree.POS] = names.fromString("++");
	opname[JCTree.PREDEC  - JCTree.POS] = names.fromString("--");
	opname[JCTree.POSTINC - JCTree.POS] = names.fromString("++");
	opname[JCTree.POSTDEC - JCTree.POS] = names.fromString("--");
	opname[JCTree.NULLCHK - JCTree.POS] = names.fromString("<*nullchk*>");
	opname[JCTree.OR      - JCTree.POS] = names.fromString("or");
	opname[JCTree.AND     - JCTree.POS] = names.fromString("and");
	opname[JCTree.EQ      - JCTree.POS] = names.fromString("==");
	opname[JCTree.NE      - JCTree.POS] = names.fromString("<>");
	opname[JCTree.LT      - JCTree.POS] = names.fromString("<");
	opname[JCTree.GT      - JCTree.POS] = names.fromString(">");
	opname[JCTree.LE      - JCTree.POS] = names.fromString("<=");
	opname[JCTree.GE      - JCTree.POS] = names.fromString(">=");
	opname[JCTree.SL      - JCTree.POS] = names.fromString("<<");
	opname[JCTree.SR      - JCTree.POS] = names.fromString(">>");
	opname[JCTree.USR     - JCTree.POS] = names.fromString(">>>");
	opname[JCTree.PLUS    - JCTree.POS] = names.fromString("+");
	opname[JCTree.MINUS   - JCTree.POS] = names.hyphen;
	opname[JCTree.MUL     - JCTree.POS] = names.asterisk;
	opname[JCTree.DIV     - JCTree.POS] = names.slash;
	opname[JCTree.MOD     - JCTree.POS] = names.fromString("%");
	opname[JavafxTag.XOR     - JCTree.POS] = names.fromString("xor");
	opname[JavafxTag.BINDOP  - JCTree.POS] = names.fromString("bind");
	opname[JavafxTag.LAZYOP  - JCTree.POS] = names.fromString("lazy");
    }    

    /** A DiagnosticPosition with the preferred position set to the 
     *  end position of given tree, if it is a block with
     *  defined endpos.
     */
    public static DiagnosticPosition diagEndPos(final JCTree tree) {
        final int endPos = JavafxTreeInfo.endPos(tree);
        return new DiagnosticPosition() {
            public JCTree getTree() { return tree; }
            public int getStartPosition() { return JavafxTreeInfo.getStartPos(tree); }
            public int getPreferredPosition() { return endPos; }
            public int getEndPosition(Map<JCTree, Integer> endPosTable) { 
                return JavafxTreeInfo.getEndPos(tree, endPosTable);
            }
        };
    }

    public static DiagnosticPosition diagnosticPositionFor(final Symbol sym, final JCTree tree) {
        JCTree decl = declarationFor(sym, tree);
        return ((decl != null) ? decl : tree).pos();
    }

    /** Find the declaration for a symbol, where
     *  that symbol is defined somewhere in the given tree. */
    public static JCTree declarationFor(final Symbol sym, final JCTree tree) {
	class DeclScanner extends JavafxTreeScanner {
            JCTree result = null;
            public void scan(JCTree tree) {
                if (tree!=null && result==null)
                    tree.accept(this);
            }
	    public void visitTopLevel(JCCompilationUnit that) {
		if (that.packge == sym) result = that;
		else super.visitTopLevel(that);
	    }
	    public void visitClassDef(JCClassDecl that) {
		if (that.sym == sym) result = that;
		else super.visitClassDef(that);
	    }
	    public void visitMethodDef(JCMethodDecl that) {
		if (that.sym == sym) result = that;
		else super.visitMethodDef(that);
	    }
	    public void visitVarDef(JCVariableDecl that) {
		if (that.sym == sym) result = that;
		else super.visitVarDef(that);
	    }
	}
	DeclScanner s = new DeclScanner();
	tree.accept(s);
	return s.result;
    }

    public static List<JCTree> pathFor(final JCTree node, final JCCompilationUnit unit) {
	class Result extends Error {
	    static final long serialVersionUID = -5942088234594905625L;
	    List<JCTree> path;
	    Result(List<JCTree> path) {
		this.path = path;
	    }
	}
	class PathFinder extends JavafxTreeScanner {
	    List<JCTree> path = List.nil();
	    public void scan(JCTree tree) {
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


    /** Map operators to their precedence levels.
     */
    public static int opPrec(int op) {
	switch(op) {
	case JCTree.ASSIGN: // Java distinguished, JavaFX doesn't -- Java-style
            return assignPrec;
	case JCTree.USR_ASG:
	case JCTree.PLUS_ASG:
	case JCTree.MINUS_ASG:
	case JCTree.MUL_ASG:
	case JCTree.DIV_ASG:
	case JCTree.MOD_ASG: 
            return assignopPrec;
        case JCTree.OR: 
        case JavafxTag.XOR: 
            return orPrec;
        case JCTree.AND: 
            return andPrec;
        case JCTree.EQ:
        case JCTree.NE: 
            return eqPrec;
        case JCTree.LT:
        case JCTree.GT:
        case JCTree.LE:
        case JCTree.GE: 
            return ordPrec;
        case JCTree.PLUS:
        case JCTree.MINUS: 
            return addPrec;
        case JCTree.MUL:
        case JCTree.DIV:
        case JCTree.MOD: 
            return mulPrec;
	case JCTree.TYPETEST: 
            return ordPrec;
	case JCTree.POS:
	case JCTree.NEG:
	case JCTree.NOT:
	case JCTree.COMPL:
	case JCTree.PREINC:
	case JCTree.PREDEC: 
            return prefixPrec;
	case JCTree.POSTINC:
	case JCTree.POSTDEC:
	case JCTree.NULLCHK: 
            return postfixPrec;
	default: throw new AssertionError();
	}
    }

    static Tree.Kind tagToKind(int tag) {
        switch (tag) {
        // Postfix expressions
        case JCTree.POSTINC:           // _ ++
            return Tree.Kind.POSTFIX_INCREMENT;
        case JCTree.POSTDEC:           // _ --
            return Tree.Kind.POSTFIX_DECREMENT;

        // Unary operators
        case JCTree.PREINC:            // ++ _
            return Tree.Kind.PREFIX_INCREMENT;
        case JCTree.PREDEC:            // -- _
            return Tree.Kind.PREFIX_DECREMENT;
        case JCTree.POS:               // +
            return Tree.Kind.UNARY_PLUS;
        case JCTree.NEG:               // -
            return Tree.Kind.UNARY_MINUS;
        case JCTree.COMPL:             // ~
            return Tree.Kind.BITWISE_COMPLEMENT;
        case JCTree.NOT:               // !
            return Tree.Kind.LOGICAL_COMPLEMENT;

        // Binary operators

        // Multiplicative operators
        case JCTree.MUL:               // *
            return Tree.Kind.MULTIPLY;
        case JCTree.DIV:               // /
            return Tree.Kind.DIVIDE;
        case JCTree.MOD:               // %
            return Tree.Kind.REMAINDER;

        // Additive operators
        case JCTree.PLUS:              // +
            return Tree.Kind.PLUS;
        case JCTree.MINUS:             // -
            return Tree.Kind.MINUS;

        // Shift operators
        case JCTree.SL:                // <<
            return Tree.Kind.LEFT_SHIFT;
        case JCTree.SR:                // >>
            return Tree.Kind.RIGHT_SHIFT;
        case JCTree.USR:               // >>>
            return Tree.Kind.UNSIGNED_RIGHT_SHIFT;

        // Relational operators
        case JCTree.LT:                // <
            return Tree.Kind.LESS_THAN;
        case JCTree.GT:                // >
            return Tree.Kind.GREATER_THAN;
        case JCTree.LE:                // <=
            return Tree.Kind.LESS_THAN_EQUAL;
        case JCTree.GE:                // >=
            return Tree.Kind.GREATER_THAN_EQUAL;

        // Equality operators
        case JCTree.EQ:                // ==
            return Tree.Kind.EQUAL_TO;
        case JCTree.NE:                // !=
            return Tree.Kind.NOT_EQUAL_TO;

        // Bitwise and logical operators
        case JCTree.BITAND:            // &
            return Tree.Kind.AND;
        case JCTree.BITXOR:            // ^
            return Tree.Kind.XOR;
        case JCTree.BITOR:             // |
            return Tree.Kind.OR;

        // Conditional operators
        case JCTree.AND:               // &&
            return Tree.Kind.CONDITIONAL_AND;
        case JCTree.OR:                // ||
            return Tree.Kind.CONDITIONAL_OR;

        // Assignment operators
        case JCTree.MUL_ASG:           // *=
            return Tree.Kind.MULTIPLY_ASSIGNMENT;
        case JCTree.DIV_ASG:           // /=
            return Tree.Kind.DIVIDE_ASSIGNMENT;
        case JCTree.MOD_ASG:           // %=
            return Tree.Kind.REMAINDER_ASSIGNMENT;
        case JCTree.PLUS_ASG:          // +=
            return Tree.Kind.PLUS_ASSIGNMENT;
        case JCTree.MINUS_ASG:         // -=
            return Tree.Kind.MINUS_ASSIGNMENT;
        case JCTree.SL_ASG:            // <<=
            return Tree.Kind.LEFT_SHIFT_ASSIGNMENT;
        case JCTree.SR_ASG:            // >>=
            return Tree.Kind.RIGHT_SHIFT_ASSIGNMENT;
        case JCTree.USR_ASG:           // >>>=
            return Tree.Kind.UNSIGNED_RIGHT_SHIFT_ASSIGNMENT;
        case JCTree.BITAND_ASG:        // &=
            return Tree.Kind.AND_ASSIGNMENT;
        case JCTree.BITXOR_ASG:        // ^=
            return Tree.Kind.XOR_ASSIGNMENT;
        case JCTree.BITOR_ASG:         // |=
            return Tree.Kind.OR_ASSIGNMENT;

        // Null check (implementation detail), for example, __.getClass()
        case JCTree.NULLCHK:
            return Tree.Kind.OTHER;

        default:
            return null;
        }
    }
    /** If this tree is an identifier or a field, return its symbol,
     *  otherwise return null.
     */
    public static Symbol symbol(JCTree tree) {
	tree = skipParens(tree);
	switch (tree.getTag()) {
	case JCTree.IDENT:
	    return ((JCIdent) tree).sym;
	case JCTree.SELECT:
	    return ((JCFieldAccess) tree).sym;
	case JCTree.TYPEAPPLY:
	    return symbol(((JCTypeApply) tree).clazz);
        case JCTree.INDEXED:
            return symbol(((JCArrayAccess) tree).indexed);
        case JavafxTag.SEQUENCE_INDEXED:
            return symbol(((JFXSequenceIndexed) tree).getSequence());
        case JavafxTag.SEQUENCE_SLICE:
            return symbol(((JFXSequenceSlice) tree).getSequence());
	default:
	    return null;
	}
    }
}



