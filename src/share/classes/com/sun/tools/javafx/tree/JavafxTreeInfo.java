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

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.Tree;
import java.util.Map;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.tree.JCTree;

import com.sun.tools.javafx.code.JavafxFlags;

/** Utility class containing inspector methods for trees.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxTreeInfo {

    /** The names of all operators.
     */
    protected Name[] opname = new Name[JavafxTag.MOD.ordinal() - JavafxTag.NEG.ordinal() + 1];

    protected static final Context.Key<JavafxTreeInfo> fxTreeInfoKey =
        new Context.Key<JavafxTreeInfo>();

    public static JavafxTreeInfo instance(Context context) {
        JavafxTreeInfo instance = context.get(fxTreeInfoKey);
        if (instance == null)
            instance = new JavafxTreeInfo(context);
        return instance;
    }

     protected JavafxTreeInfo(Context context) {
	Name.Table names = Name.Table.instance(context);
        int base = JavafxTag.NEG.ordinal();
        opname = new Name[JavafxTag.JFX_OP_LAST.ordinal() - base + 1];
	opname[JavafxTag.NEG    .ordinal() - base] = names.hyphen;
	opname[JavafxTag.NOT    .ordinal() - base] = names.fromString("!");
	opname[JavafxTag.PREINC .ordinal() - base] = names.fromString("++");
	opname[JavafxTag.PREDEC .ordinal() - base] = names.fromString("--");
	opname[JavafxTag.POSTINC.ordinal() - base] = names.fromString("++");
	opname[JavafxTag.POSTDEC.ordinal() - base] = names.fromString("--");
	opname[JavafxTag.NULLCHK.ordinal() - base] = names.fromString("<*nullchk*>");
	opname[JavafxTag.OR     .ordinal() - base] = names.fromString("or");
	opname[JavafxTag.AND    .ordinal() - base] = names.fromString("and");
	opname[JavafxTag.EQ     .ordinal() - base] = names.fromString("==");
	opname[JavafxTag.NE     .ordinal() - base] = names.fromString("<>");
	opname[JavafxTag.LT     .ordinal() - base] = names.fromString("<");
	opname[JavafxTag.GT     .ordinal() - base] = names.fromString(">");
	opname[JavafxTag.LE     .ordinal() - base] = names.fromString("<=");
	opname[JavafxTag.GE     .ordinal() - base] = names.fromString(">=");
	opname[JavafxTag.PLUS   .ordinal() - base] = names.fromString("+");
	opname[JavafxTag.MINUS  .ordinal() - base] = names.hyphen;
	opname[JavafxTag.MUL    .ordinal() - base] = names.asterisk;
	opname[JavafxTag.DIV    .ordinal() - base] = names.slash;
	opname[JavafxTag.MOD    .ordinal() - base] = names.fromString("%");
	opname[JavafxTag.XOR    .ordinal() - base] = names.fromString("xor");
	opname[JavafxTag.SIZEOF .ordinal() - base] = names.fromString("sizeof");
	opname[JavafxTag.INDEXOF .ordinal() - base] = names.fromString("indexof");
	opname[JavafxTag.REVERSE .ordinal() - base] = names.fromString("reverse");
    }    

    /** Return name of operator with given tree tag.
     */
    public Name operatorName(JavafxTag tag) {
        return opname[tag.ordinal() - JavafxTag.NEG.ordinal()];
    }

    /** A DiagnosticPosition with the preferred position set to the 
     *  end position of given tree, if it is a block with
     *  defined endpos.
     */
    public static DiagnosticPosition diagEndPos(final JFXTree tree) {
        final int endPos = JavafxTreeInfo.endPos(tree);
        return new DiagnosticPosition() {
            public JFXTree getTree() { return tree; }
            public int getStartPosition() { return JavafxTreeInfo.getStartPos(tree); }
            public int getPreferredPosition() { return endPos; }
            public int getEndPosition(Map<JCTree, Integer> endPosTable) { 
                return JavafxTreeInfo.getEndPos(tree, endPosTable);
            }
        };
    }

    public static DiagnosticPosition diagnosticPositionFor(final Symbol sym, final JFXTree tree) {
        JFXTree decl = declarationFor(sym, tree);
        return ((decl != null) ? decl : tree).pos();
    }

    /** Find the declaration for a symbol, where
     *  that symbol is defined somewhere in the given tree. */
    public static JFXTree declarationFor(final Symbol sym, final JFXTree tree) {
        class DeclScanner extends JavafxTreeScanner {

            JFXTree result = null;

            @Override
            public void scan(JFXTree tree) {
                if ( tree != null && result == null ) {
                    tree.accept(this);
                }
            }

            @Override
            public void visitClassDeclaration(JFXClassDeclaration that) {
                if (that.sym == sym) {
                    result = that;
                }
                else  {
                    super.visitClassDeclaration(that);
                }
            }

            @Override
            public void visitScript(JFXScript that) {
                if ( that.packge == sym ) {
                    result = that;
                }
                else {
                    super.visitScript(that);
                }
            }

            @Override
            public void visitFunctionDefinition(JFXFunctionDefinition that) {
                if ( that.sym == sym ) {
                    result = that;
                }
                else {
                    super.visitFunctionDefinition(that);
                }
            }

            @Override
            public void visitVar(JFXVar that) {
                if ( that.sym == sym ) {
                    result = that;
                }
                else {
                    super.visitVar(that);
                }
            }
        }
        DeclScanner s = new DeclScanner();
        tree.accept(s);
        return s.result;
    }

    public static List<JFXTree> pathFor(final JFXTree node, final JFXScript unit) {
	class Result extends Error {
	    static final long serialVersionUID = -5942088234594905625L;
	    List<JFXTree> path;
	    Result(List<JFXTree> path) {
		this.path = path;
	    }
	}
	class PathFinder extends JavafxTreeScanner {
	    List<JFXTree> path = List.nil();
            @Override
	    public void scan(JFXTree tree) {
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

    /** Return first (smallest) flag in `flags':
     *  pre: flags != 0
     */
    public static long firstFlag(long flags) {
        for (int i = 0; i < 63; ++i) {
            long flag = 1L << i;
            if ((flag & flags) != 0) {
                return flag;
            }
        }
        throw new AssertionError();
    }

    /** Return flags as a string, separated by " ".
     */
    public static String flagNames(long flags) {
        return flagNames(flags, false);
    }

    /** Return flags as a string, separated by " ".
     */
    public static String flagNames(long flags, boolean pretty) {
        StringBuffer fsb = new StringBuffer(Flags.toString(flags));
        if ((flags & JavafxFlags.PACKAGE_ACCESS) != 0) {
            fsb.append("package ");
        }
        if (!pretty && (flags & JavafxFlags.SCRIPT_PRIVATE) != 0) {
            fsb.append("script only (default) ");
        }
        if ((flags & JavafxFlags.PUBLIC_READ) != 0) {
            fsb.append("public-read ");
        }
        if ((flags & JavafxFlags.PUBLIC_INIT) != 0) {
            fsb.append("public-init ");
        }
        if ((flags & JavafxFlags.BOUND) != 0) {
            fsb.append("bound ");
        }
        if ((flags & JavafxFlags.MIXIN) != 0) {
            fsb.append("mixin ");
        }
        if ((flags & JavafxFlags.OVERRIDE) != 0) {
            fsb.append("override ");
        }
        return fsb.toString().trim();
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
    public static int opPrec(JavafxTag op) {
	switch(op) {
	case ASSIGN: // Java distinguished, JavaFX doesn't -- Java-style
            return assignPrec;
	case PLUS_ASG:
	case MINUS_ASG:
	case MUL_ASG:
	case DIV_ASG:
            return assignopPrec;
        case OR: 
        case XOR: 
            return orPrec;
        case AND: 
            return andPrec;
        case EQ:
        case NE: 
            return eqPrec;
        case LT:
        case GT:
        case LE:
        case GE: 
            return ordPrec;
        case PLUS:
        case MINUS: 
            return addPrec;
        case MUL:
        case DIV:
        case MOD: 
            return mulPrec;
	case TYPETEST: 
            return ordPrec;
	case NEG:
	case NOT:
	case PREINC:
	case PREDEC:
	case REVERSE:
	case INDEXOF:
	case SIZEOF:
            return prefixPrec;
	case POSTINC:
	case POSTDEC:
	case NULLCHK: 
            return postfixPrec;
            default: throw new AssertionError("Unexpected operator precidence request: " + op);
	}
    }

    static Tree.JavaFXKind tagToKind(JavafxTag tag) {
        switch (tag) {
        // Postfix expressions
        case POSTINC:           // _ ++
            return Tree.JavaFXKind.POSTFIX_INCREMENT;
        case POSTDEC:           // _ --
            return Tree.JavaFXKind.POSTFIX_DECREMENT;

        // Unary operators
        case PREINC:            // ++ _
            return Tree.JavaFXKind.PREFIX_INCREMENT;
        case PREDEC:            // -- _
            return Tree.JavaFXKind.PREFIX_DECREMENT;
        case NEG:               // -
            return Tree.JavaFXKind.UNARY_MINUS;
        case NOT:               // !
            return Tree.JavaFXKind.LOGICAL_COMPLEMENT;

        // Binary operators

        // Multiplicative operators
        case MUL:               // *
            return Tree.JavaFXKind.MULTIPLY;
        case DIV:               // /
            return Tree.JavaFXKind.DIVIDE;
        case MOD:               // %
            return Tree.JavaFXKind.REMAINDER;

        // Additive operators
        case PLUS:              // +
            return Tree.JavaFXKind.PLUS;
        case MINUS:             // -
            return Tree.JavaFXKind.MINUS;

         // Relational operators
        case LT:                // <
            return Tree.JavaFXKind.LESS_THAN;
        case GT:                // >
            return Tree.JavaFXKind.GREATER_THAN;
        case LE:                // <=
            return Tree.JavaFXKind.LESS_THAN_EQUAL;
        case GE:                // >=
            return Tree.JavaFXKind.GREATER_THAN_EQUAL;

        // Equality operators
        case EQ:                // ==
            return Tree.JavaFXKind.EQUAL_TO;
        case NE:                // !=
            return Tree.JavaFXKind.NOT_EQUAL_TO;

         // Conditional operators
        case AND:               // &&
            return Tree.JavaFXKind.CONDITIONAL_AND;
        case OR:                // ||
            return Tree.JavaFXKind.CONDITIONAL_OR;

        // Assignment operators
        case MUL_ASG:           // *=
            return Tree.JavaFXKind.MULTIPLY_ASSIGNMENT;
        case DIV_ASG:           // /=
            return Tree.JavaFXKind.DIVIDE_ASSIGNMENT;
        case PLUS_ASG:          // +=
            return Tree.JavaFXKind.PLUS_ASSIGNMENT;
        case MINUS_ASG:         // -=
            return Tree.JavaFXKind.MINUS_ASSIGNMENT;

        // Null check (implementation detail), for example, __.getClass()
        case NULLCHK:
            return Tree.JavaFXKind.OTHER;

        // JavaFX tags which are used in javac trees
        case SIZEOF:
            return Tree.JavaFXKind.OTHER;
        case REVERSE:
            return Tree.JavaFXKind.OTHER;

        default:
            return null;
        }
    }
    
    public static void setSymbol(JFXTree tree, Symbol sym) {
	tree = skipParens(tree);
	switch (tree.getFXTag()) {
	case IDENT:
	    ((JFXIdent) tree).sym = sym; break;
	case SELECT:
	    ((JFXSelect) tree).sym = sym; break;
	}
    }

    /** If this tree is an identifier or a field, return its symbol,
     *  otherwise return null.
     */
    public static Symbol symbol(JFXTree tree) {
	tree = skipParens(tree);
	switch (tree.getFXTag()) {
	case IDENT:
	    return ((JFXIdent) tree).sym;
	case SELECT:
	    return ((JFXSelect) tree).sym;
        case SEQUENCE_INDEXED:
            return symbol(((JFXSequenceIndexed) tree).getSequence());
        case SEQUENCE_SLICE:
            return symbol(((JFXSequenceSlice) tree).getSequence());
        case VAR_REF:
            return ((JFXVarRef)tree).getVarSymbol();
	default:
	    return null;
	}
    }

    /** Skip parens and return the enclosed expression
     */
    public static JFXTree skipParens(JFXTree tree) {

        if (tree == null) return tree;
        if (tree.getFXTag() == JavafxTag.PARENS)
            return skipParens(((JFXParens)tree).expr);
        else
            return tree;
    }

    /** If this tree is a qualified identifier, its return fully qualified name,
     *  otherwise return null.
     */
    public static Name fullName(JFXTree tree) {

        // Protect against a missing tree
        //
        if  (tree == null) return null;

        tree = skipParens(tree);
        switch (tree.getFXTag()) {
        case IDENT:
            return ((JFXIdent) tree).getName();
        case SELECT:
            Name sname = fullName(((JFXSelect) tree).selected);
            return sname == null ? null : sname.append('.', name(tree));
        default:
            return null;
        }
    }

    /** If this tree is an identifier or a field or a parameterized type,
     *  return its name, otherwise return null.
     */
    public static Name name(JFXTree tree) {
        switch (tree.getFXTag()) {
        case IDENT:
            return ((JFXIdent) tree).getName();
        case SELECT:
            return ((JFXSelect) tree).name;
        default:
            return null;
        }
    }

    public static Symbol symbolFor(JFXTree node) {
        if (node == null)
        {
            return null;
        }
        node = skipParens(node);

        switch (node.getFXTag()) {
        case VAR_DEF:
            return ((JFXVar) node).sym;
        case VAR_SCRIPT_INIT:
            return ((JFXVarInit) node).getSymbol();
        case CLASS_DEF:
            return ((JFXClassDeclaration) node).sym;
        case FUNCTION_DEF:
            return ((JFXFunctionDefinition) node).sym;
        case FUNCTIONEXPRESSION:
            return symbolFor(((JFXFunctionValue) node).definition);
        case OBJECT_LITERAL_PART:
            return ((JFXObjectLiteralPart) node).sym;
        case TYPECLASS:
            return symbolFor(((JFXTypeClass) node).getTypeExpression());
        case IDENT:
            return ((JFXIdent) node).sym;
        case INDEXOF:
            JFXForExpressionInClause clause = ((JFXIndexof) node).clause;
            return clause == null ? null : clause.var.sym;
        case SELECT:
            return ((JFXSelect) node).sym;
        case APPLY:
            return symbolFor(((JFXFunctionInvocation) node).meth);
        case TOPLEVEL:
            return ((JFXScript) node).packge;
        case ON_REPLACE:
            return symbolFor(((JFXOnReplace) node).getOldValue());
        case OVERRIDE_ATTRIBUTE_DEF:
            return symbolFor(((JFXOverrideClassVar) node).getId());
        case INIT_DEF:
            return ((JFXInitDefinition) node).sym;
        case POSTINIT_DEF:
            return ((JFXPostInitDefinition) node).sym;
        default:
            return null;
        }
    }

    /** Get the start position for a tree node.  The start position is
     * defined to be the position of the first character of the first
     * token of the node's source text.
     * @param tree  The tree node
     */
    public static int getStartPos(JFXTree tree) {
        if (tree == null) {
            return Position.NOPOS;
        }

        switch (tree.getFXTag()) {

            case APPLY:
                return getStartPos(((JFXFunctionInvocation) tree).meth);

            case ASSIGN:
                return getStartPos(((JFXAssign) tree).lhs);

            case PLUS_ASG:
            case MINUS_ASG:
            case MUL_ASG:
            case DIV_ASG:
                return getStartPos(((JFXAssignOp) tree).lhs);

            case OR:
            case AND:
            case EQ:
            case NE:
            case LT:
            case GT:
            case LE:
            case GE:
            case PLUS:
            case MINUS:
            case MUL:
            case DIV:
            case MOD:
                return getStartPos(((JFXBinary) tree).lhs);

            case SELECT:
                return getStartPos(((JFXSelect) tree).selected);

            case TYPETEST:
                return getStartPos(((JFXInstanceOf) tree).expr);

            case POSTINC:
            case POSTDEC:
                return getStartPos(((JFXUnary) tree).arg);

            case ERRONEOUS:

                // Erroneous nodes are created with the correct start
                // position in the source as their pos position, so we do
                // not need to interrogate the list.
                //
                return tree.pos;

            default:

                return tree.pos;
        }
    }

    /** The end position of given tree, if it is a block with
     *  defined endpos.
     */
    public static int endPos(JFXTree tree) {
        if (tree.getFXTag() == JavafxTag.BLOCK_EXPRESSION && ((JFXBlock) tree).endpos != Position.NOPOS)
            return ((JFXBlock) tree).endpos;
        else if (tree.getFXTag() == JavafxTag.TRY) {
            JFXTry t = (JFXTry) tree;
            return endPos((t.finalizer != null)
                          ? t.finalizer
                          : t.catchers.last().body);
        } else
            return tree.pos;
    }

    /** The end position of given tree, given  a table of end positions generated by the parser
     */
    public static int getEndPos(JFXTree tree, Map<JCTree, Integer> endPositions) {
        if (tree == null)
            return Position.NOPOS;

        if (endPositions == null) {
            // fall back on limited info in the tree
            return tree instanceof JFXBlock ?
                ((JFXBlock)tree).endpos : JavafxTreeInfo.endPos(tree);
        }

        Integer mapPos = endPositions.get(tree);
        if (mapPos != null)
            return mapPos;

        switch(tree.getFXTag()) {
          case INIT_DEF:
            return getEndPos((JFXTree) ((JFXInitDefinition) tree).getBody(), endPositions);
          case POSTINIT_DEF:
            return getEndPos((JFXTree) ((JFXPostInitDefinition) tree).getBody(), endPositions);
          case OVERRIDE_ATTRIBUTE_DEF: {
            JFXOverrideClassVar t = (JFXOverrideClassVar)tree;
            if (t.getOnReplace() != null)
                return getEndPos(t.getOnReplace(), endPositions);
            return getEndPos(t.getInitializer(), endPositions);
          }
          case ON_REPLACE:
            return getEndPos(((JFXOnReplace) tree).getBody(), endPositions);
          case OBJECT_LITERAL_PART:
            return getEndPos(((JFXObjectLiteralPart) tree).getExpression(), endPositions);
          case STRING_EXPRESSION:
            return tree.pos + ((JFXStringExpression) tree).translationKey.length();
          case FOR_EXPRESSION:
            return getEndPos(((JFXForExpression) tree).getBodyExpression(), endPositions);
          case FOR_EXPRESSION_IN_CLAUSE:
            return getEndPos(((JFXForExpressionInClause) tree).getWhereExpression(), endPositions);
          case TYPECLASS:
            return getEndPos(((JFXTypeClass) tree).getClassName(), endPositions);
          case TIME_LITERAL:
            return tree.pos + tree.toString().length();
        }
        return JavafxTreeInfo.getStartPos(tree);
    }
}
