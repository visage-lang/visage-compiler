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

import org.visage.api.tree.Tree;
import java.util.Map;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.tree.JCTree;

import org.visage.tools.code.VisageFlags;

/** Utility class containing inspector methods for trees.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class VisageTreeInfo {

    /** The names of all operators.
     */
    protected Name[] opname = new Name[VisageTag.MOD.ordinal() - VisageTag.NEG.ordinal() + 1];

    protected static final Context.Key<VisageTreeInfo> visageTreeInfoKey =
        new Context.Key<VisageTreeInfo>();

    public static VisageTreeInfo instance(Context context) {
        VisageTreeInfo instance = context.get(visageTreeInfoKey);
        if (instance == null)
            instance = new VisageTreeInfo(context);
        return instance;
    }

     protected VisageTreeInfo(Context context) {
	Name.Table names = Name.Table.instance(context);
        int base = VisageTag.NEG.ordinal();
        opname = new Name[VisageTag.VISAGE_OP_LAST.ordinal() - base + 1];
	opname[VisageTag.NEG    .ordinal() - base] = names.hyphen;
	opname[VisageTag.NOT    .ordinal() - base] = names.fromString("!");
	opname[VisageTag.PREINC .ordinal() - base] = names.fromString("++");
	opname[VisageTag.PREDEC .ordinal() - base] = names.fromString("--");
	opname[VisageTag.POSTINC.ordinal() - base] = names.fromString("++");
	opname[VisageTag.POSTDEC.ordinal() - base] = names.fromString("--");
	opname[VisageTag.NULLCHK.ordinal() - base] = names.fromString("<*nullchk*>");
	opname[VisageTag.OR     .ordinal() - base] = names.fromString("or");
	opname[VisageTag.AND    .ordinal() - base] = names.fromString("and");
	opname[VisageTag.EQ     .ordinal() - base] = names.fromString("==");
	opname[VisageTag.NE     .ordinal() - base] = names.fromString("<>");
	opname[VisageTag.LT     .ordinal() - base] = names.fromString("<");
	opname[VisageTag.GT     .ordinal() - base] = names.fromString(">");
	opname[VisageTag.LE     .ordinal() - base] = names.fromString("<=");
	opname[VisageTag.GE     .ordinal() - base] = names.fromString(">=");
	opname[VisageTag.PLUS   .ordinal() - base] = names.fromString("+");
	opname[VisageTag.MINUS  .ordinal() - base] = names.hyphen;
	opname[VisageTag.MUL    .ordinal() - base] = names.asterisk;
	opname[VisageTag.DIV    .ordinal() - base] = names.slash;
	opname[VisageTag.MOD    .ordinal() - base] = names.fromString("%");
	opname[VisageTag.XOR    .ordinal() - base] = names.fromString("xor");
	opname[VisageTag.SIZEOF .ordinal() - base] = names.fromString("sizeof");
	opname[VisageTag.INDEXOF .ordinal() - base] = names.fromString("indexof");
	opname[VisageTag.REVERSE .ordinal() - base] = names.fromString("reverse");
    }    

    /** Return name of operator with given tree tag.
     */
    public Name operatorName(VisageTag tag) {
        return opname[tag.ordinal() - VisageTag.NEG.ordinal()];
    }

    /** A DiagnosticPosition with the preferred position set to the 
     *  end position of given tree, if it is a block with
     *  defined endpos.
     */
    public static DiagnosticPosition diagEndPos(final VisageTree tree) {
        final int endPos = VisageTreeInfo.endPos(tree);
        return new DiagnosticPosition() {
            public VisageTree getTree() { return tree; }
            public int getStartPosition() { return VisageTreeInfo.getStartPos(tree); }
            public int getPreferredPosition() { return endPos; }
            public int getEndPosition(Map<JCTree, Integer> endPosTable) { 
                return VisageTreeInfo.getEndPos(tree, endPosTable);
            }
        };
    }

    public static DiagnosticPosition diagnosticPositionFor(final Symbol sym, final VisageTree tree) {
        VisageTree decl = declarationFor(sym, tree);
        return ((decl != null) ? decl : tree).pos();
    }

    /** Find the declaration for a symbol, where
     *  that symbol is defined somewhere in the given tree. */
    public static VisageTree declarationFor(final Symbol sym, final VisageTree tree) {
        class DeclScanner extends VisageTreeScanner {

            VisageTree result = null;

            @Override
            public void scan(VisageTree tree) {
                if ( tree != null && result == null ) {
                    tree.accept(this);
                }
            }

            @Override
            public void visitClassDeclaration(VisageClassDeclaration that) {
                if (that.sym == sym) {
                    result = that;
                }
                else  {
                    super.visitClassDeclaration(that);
                }
            }

            @Override
            public void visitScript(VisageScript that) {
                if ( that.packge == sym ) {
                    result = that;
                }
                else {
                    super.visitScript(that);
                }
            }

            @Override
            public void visitFunctionDefinition(VisageFunctionDefinition that) {
                if ( that.sym == sym ) {
                    result = that;
                }
                else {
                    super.visitFunctionDefinition(that);
                }
            }

            @Override
            public void visitVar(VisageVar that) {
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

    public static List<VisageTree> pathFor(final VisageTree node, final VisageScript unit) {
	class Result extends Error {
	    static final long serialVersionUID = -5942088234594905625L;
	    List<VisageTree> path;
	    Result(List<VisageTree> path) {
		this.path = path;
	    }
	}
	class PathFinder extends VisageTreeScanner {
	    List<VisageTree> path = List.nil();
            @Override
	    public void scan(VisageTree tree) {
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
        if ((flags & VisageFlags.PACKAGE_ACCESS) != 0) {
            fsb.append("package ");
        }
        if (!pretty && (flags & VisageFlags.SCRIPT_PRIVATE) != 0) {
            fsb.append("script only (default) ");
        }
        if ((flags & VisageFlags.PUBLIC_READ) != 0) {
            fsb.append("public-read ");
        }
        if ((flags & VisageFlags.PUBLIC_INIT) != 0) {
            fsb.append("public-init ");
        }
        if ((flags & VisageFlags.DEFAULT) != 0) {
            fsb.append("default ");
        }
        if ((flags & VisageFlags.BOUND) != 0) {
            fsb.append("bound ");
        }
        if ((flags & VisageFlags.MIXIN) != 0) {
            fsb.append("mixin ");
        }
        if ((flags & VisageFlags.OVERRIDE) != 0) {
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
    public static int opPrec(VisageTag op) {
	switch(op) {
	case ASSIGN: // Java distinguished, Visage doesn't -- Java-style
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

    static Tree.VisageKind tagToKind(VisageTag tag) {
        switch (tag) {
        // Postfix expressions
        case POSTINC:           // _ ++
            return Tree.VisageKind.POSTFIX_INCREMENT;
        case POSTDEC:           // _ --
            return Tree.VisageKind.POSTFIX_DECREMENT;

        // Unary operators
        case PREINC:            // ++ _
            return Tree.VisageKind.PREFIX_INCREMENT;
        case PREDEC:            // -- _
            return Tree.VisageKind.PREFIX_DECREMENT;
        case NEG:               // -
            return Tree.VisageKind.UNARY_MINUS;
        case NOT:               // !
            return Tree.VisageKind.LOGICAL_COMPLEMENT;

        // Binary operators

        // Multiplicative operators
        case MUL:               // *
            return Tree.VisageKind.MULTIPLY;
        case DIV:               // /
            return Tree.VisageKind.DIVIDE;
        case MOD:               // %
            return Tree.VisageKind.REMAINDER;

        // Additive operators
        case PLUS:              // +
            return Tree.VisageKind.PLUS;
        case MINUS:             // -
            return Tree.VisageKind.MINUS;

         // Relational operators
        case LT:                // <
            return Tree.VisageKind.LESS_THAN;
        case GT:                // >
            return Tree.VisageKind.GREATER_THAN;
        case LE:                // <=
            return Tree.VisageKind.LESS_THAN_EQUAL;
        case GE:                // >=
            return Tree.VisageKind.GREATER_THAN_EQUAL;

        // Equality operators
        case EQ:                // ==
            return Tree.VisageKind.EQUAL_TO;
        case NE:                // !=
            return Tree.VisageKind.NOT_EQUAL_TO;

         // Conditional operators
        case AND:               // &&
            return Tree.VisageKind.CONDITIONAL_AND;
        case OR:                // ||
            return Tree.VisageKind.CONDITIONAL_OR;

        // Assignment operators
        case MUL_ASG:           // *=
            return Tree.VisageKind.MULTIPLY_ASSIGNMENT;
        case DIV_ASG:           // /=
            return Tree.VisageKind.DIVIDE_ASSIGNMENT;
        case PLUS_ASG:          // +=
            return Tree.VisageKind.PLUS_ASSIGNMENT;
        case MINUS_ASG:         // -=
            return Tree.VisageKind.MINUS_ASSIGNMENT;

        // Null check (implementation detail), for example, __.getClass()
        case NULLCHK:
            return Tree.VisageKind.OTHER;

        // Visage tags which are used in javac trees
        case SIZEOF:
            return Tree.VisageKind.OTHER;
        case REVERSE:
            return Tree.VisageKind.OTHER;

        default:
            return null;
        }
    }
    
    public static void setSymbol(VisageTree tree, Symbol sym) {
	tree = skipParens(tree);
	switch (tree.getVisageTag()) {
	case IDENT:
	    ((VisageIdent) tree).sym = sym; break;
	case SELECT:
	    ((VisageSelect) tree).sym = sym; break;
	}
    }

    /** If this tree is an identifier or a field, return its symbol,
     *  otherwise return null.
     */
    public static Symbol symbol(VisageTree tree) {
	tree = skipParens(tree);
	switch (tree.getVisageTag()) {
	case IDENT:
	    return ((VisageIdent) tree).sym;
	case SELECT:
	    return ((VisageSelect) tree).sym;
        case SEQUENCE_INDEXED:
            return symbol(((VisageSequenceIndexed) tree).getSequence());
        case SEQUENCE_SLICE:
            return symbol(((VisageSequenceSlice) tree).getSequence());
        case VAR_REF:
            return ((VisageVarRef)tree).getVarSymbol();
	default:
	    return null;
	}
    }

    /** Skip parens and return the enclosed expression
     */
    public static VisageTree skipParens(VisageTree tree) {

        if (tree == null) return tree;
        if (tree.getVisageTag() == VisageTag.PARENS)
            return skipParens(((VisageParens)tree).expr);
        else
            return tree;
    }

    /** If this tree is a qualified identifier, its return fully qualified name,
     *  otherwise return null.
     */
    public static Name fullName(VisageTree tree) {

        // Protect against a missing tree
        //
        if  (tree == null) return null;

        tree = skipParens(tree);
        switch (tree.getVisageTag()) {
        case IDENT:
            return ((VisageIdent) tree).getName();
        case SELECT:
            Name sname = fullName(((VisageSelect) tree).selected);
            return sname == null ? null : sname.append('.', name(tree));
        default:
            return null;
        }
    }

    /** If this tree is an identifier or a field or a parameterized type,
     *  return its name, otherwise return null.
     */
    public static Name name(VisageTree tree) {
        switch (tree.getVisageTag()) {
        case IDENT:
            return ((VisageIdent) tree).getName();
        case SELECT:
            return ((VisageSelect) tree).name;
        default:
            return null;
        }
    }

    public static Symbol symbolFor(VisageTree node) {
        if (node == null)
        {
            return null;
        }
        node = skipParens(node);

        switch (node.getVisageTag()) {
        case VAR_DEF:
            return ((VisageVar) node).sym;
        case VAR_SCRIPT_INIT:
            return ((VisageVarInit) node).getSymbol();
        case CLASS_DEF:
            return ((VisageClassDeclaration) node).sym;
        case FUNCTION_DEF:
            return ((VisageFunctionDefinition) node).sym;
        case FUNCTIONEXPRESSION:
            return symbolFor(((VisageFunctionValue) node).definition);
        case OBJECT_LITERAL_PART:
            return ((VisageObjectLiteralPart) node).sym;
        case TYPECLASS:
            return symbolFor(((VisageTypeClass) node).getTypeExpression());
        case IDENT:
            return ((VisageIdent) node).sym;
        case INDEXOF:
            VisageForExpressionInClause clause = ((VisageIndexof) node).clause;
            return clause == null ? null : clause.var.sym;
        case SELECT:
            return ((VisageSelect) node).sym;
        case APPLY:
            return symbolFor(((VisageFunctionInvocation) node).meth);
        case TOPLEVEL:
            return ((VisageScript) node).packge;
        case ON_REPLACE:
            return symbolFor(((VisageOnReplace) node).getOldValue());
        case OVERRIDE_ATTRIBUTE_DEF:
            return symbolFor(((VisageOverrideClassVar) node).getId());
        case INIT_DEF:
            return ((VisageInitDefinition) node).sym;
        case POSTINIT_DEF:
            return ((VisagePostInitDefinition) node).sym;
        default:
            return null;
        }
    }

    /** Get the start position for a tree node.  The start position is
     * defined to be the position of the first character of the first
     * token of the node's source text.
     * @param tree  The tree node
     */
    public static int getStartPos(VisageTree tree) {
        if (tree == null) {
            return Position.NOPOS;
        }

        switch (tree.getVisageTag()) {

            case APPLY:
                return getStartPos(((VisageFunctionInvocation) tree).meth);

            case ASSIGN:
                return getStartPos(((VisageAssign) tree).lhs);

            case PLUS_ASG:
            case MINUS_ASG:
            case MUL_ASG:
            case DIV_ASG:
                return getStartPos(((VisageAssignOp) tree).lhs);

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
                return getStartPos(((VisageBinary) tree).lhs);

            case SELECT:
                return getStartPos(((VisageSelect) tree).selected);

            case TYPETEST:
                return getStartPos(((VisageInstanceOf) tree).expr);

            case POSTINC:
            case POSTDEC:
                return getStartPos(((VisageUnary) tree).arg);

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
    public static int endPos(VisageTree tree) {
        if (tree.getVisageTag() == VisageTag.BLOCK_EXPRESSION && ((VisageBlock) tree).endpos != Position.NOPOS)
            return ((VisageBlock) tree).endpos;
        else if (tree.getVisageTag() == VisageTag.TRY) {
            VisageTry t = (VisageTry) tree;
            return endPos((t.finalizer != null)
                          ? t.finalizer
                          : t.catchers.last().body);
        } else
            return tree.pos;
    }

    /** The end position of given tree, given  a table of end positions generated by the parser
     */
    public static int getEndPos(VisageTree tree, Map<JCTree, Integer> endPositions) {
        if (tree == null)
            return Position.NOPOS;

        if (endPositions == null) {
            // fall back on limited info in the tree
            return tree instanceof VisageBlock ?
                ((VisageBlock)tree).endpos : VisageTreeInfo.endPos(tree);
        }

        Integer mapPos = endPositions.get(tree);
        if (mapPos != null)
            return mapPos;

        switch(tree.getVisageTag()) {
          case INIT_DEF:
            return getEndPos((VisageTree) ((VisageInitDefinition) tree).getBody(), endPositions);
          case POSTINIT_DEF:
            return getEndPos((VisageTree) ((VisagePostInitDefinition) tree).getBody(), endPositions);
          case OVERRIDE_ATTRIBUTE_DEF: {
            VisageOverrideClassVar t = (VisageOverrideClassVar)tree;
            if (t.getOnReplace() != null)
                return getEndPos(t.getOnReplace(), endPositions);
            return getEndPos(t.getInitializer(), endPositions);
          }
          case ON_REPLACE:
            return getEndPos(((VisageOnReplace) tree).getBody(), endPositions);
          case OBJECT_LITERAL_PART:
            return getEndPos(((VisageObjectLiteralPart) tree).getExpression(), endPositions);
          case STRING_EXPRESSION:
            return tree.pos + ((VisageStringExpression) tree).translationKey.length();
          case FOR_EXPRESSION:
            return getEndPos(((VisageForExpression) tree).getBodyExpression(), endPositions);
          case FOR_EXPRESSION_IN_CLAUSE:
            return getEndPos(((VisageForExpressionInClause) tree).getWhereExpression(), endPositions);
          case TYPECLASS:
            return getEndPos(((VisageTypeClass) tree).getClassName(), endPositions);
          case TIME_LITERAL:
            return tree.pos + tree.toString().length();
        }
        return VisageTreeInfo.getStartPos(tree);
    }
}
