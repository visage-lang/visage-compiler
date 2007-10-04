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

package com.sun.tools.migrator.tree;

import com.sun.tools.migrator.tree.MTTree.*;

import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.util.*;

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;

import com.sun.tools.javafx.code.JavafxBindStatus;

/* JavaFX version of tree maker
 */
public class MTTreeMaker {

    /** The position at which subsequent trees will be created.
     */
    public int pos = Position.NOPOS;

    /** The toplevel tree to which created trees belong.
     */
    public MTCompilationUnit toplevel;

    /** The current name table. */
    protected Name.Table names;

    protected Types types;

    /** The current symbol table. */
    protected Symtab syms;


    /** The context key for the tree factory. */
    protected static final Context.Key<MTTreeMaker> treeMakerKey =
        new Context.Key<MTTreeMaker>();

    /** Get the MTTreeMaker instance.
     */
    public static MTTreeMaker instance(Context context) {
        MTTreeMaker instance = context.get(treeMakerKey);
        if (instance == null)
            instance = new MTTreeMaker(context);
        return instance;
    }
    
    public static void preRegister(final Context context) {
        context.put(treeMakerKey, new Context.Factory<MTTreeMaker>() {
            @Override
            public MTTreeMaker make() {
                return new MTTreeMaker(context);
            }
        });
    }
    
    /** Create a tree maker with null toplevel and NOPOS as initial position.
     */
    protected MTTreeMaker(Context context) {
        context.put(treeMakerKey, this);
        this.pos = Position.NOPOS;
        this.toplevel = null;
        this.names = Name.Table.instance(context);
        this.syms = Symtab.instance(context);
        this.types = Types.instance(context);
    }

    /** Create a tree maker with a given toplevel and FIRSTPOS as initial position.
     */
    protected MTTreeMaker(MTCompilationUnit toplevel, Name.Table names, Types types, Symtab syms) {
        this.pos = Position.FIRSTPOS;
        this.toplevel = toplevel;
        this.names = names;
        this.types = types;
        this.syms = syms;
    }

    /** Create a new tree maker for a given toplevel.
     */
    public MTTreeMaker forToplevel(MTCompilationUnit toplevel) {
        return new MTTreeMaker(toplevel, names, types, syms);
    }

    /** Reassign current position.
     */
    public MTTreeMaker at(int pos) {
        this.pos = pos;
        return this;
    }

    /** Reassign current position.
     */
    public MTTreeMaker at(MTDiagnosticPosition pos) {
        //this.pos = (pos == null ? Position.NOPOS : pos.getStartPosition());
        return this;
    }

    /**
     * Create given tree node at current position.
     * @param defs a list of ClassDef, Import, and Skip
     */
    public MTCompilationUnit TopLevel(MTExpression pid,
				      List<MTTree> defs) {
        for (MTTree node : defs)
            assert node instanceof MTClassDeclaration
		|| node instanceof MTImport
		|| node instanceof MTSkip
                || node instanceof MTErroneous
		|| (node instanceof MTExpressionStatement
		    && ((MTExpressionStatement)node).expr instanceof MTErroneous)
                 : node.getClass().getSimpleName();
        MTCompilationUnit tree = new MTCompilationUnit(pid, defs,
                                     null, null, null);
        tree.pos = pos;
        return tree;
    }

    public MTImport Import(MTTree qualid, boolean importStatic) {
        MTImport tree = new MTImport(qualid, importStatic);
        tree.pos = pos;
        return tree;
    }

    public MTSkip Skip() {
        MTSkip tree = new MTSkip();
        tree.pos = pos;
        return tree;
    }

    public MTBlock Block(long flags, List<MTStatement> stats) {
        MTBlock tree = new MTBlock(flags, stats);
        tree.pos = pos;
        return tree;
    }

    public MTWhileLoop WhileLoop(MTExpression cond, MTStatement body) {
        MTWhileLoop tree = new MTWhileLoop(cond, body);
        tree.pos = pos;
        return tree;
    }

    public MTTry Try(MTBlock body, List<MTCatch> catchers, MTBlock finalizer) {
        MTTry tree = new MTTry(body, catchers, finalizer);
        tree.pos = pos;
        return tree;
    }

    public MTCatch Catch(MTVariableDecl param, MTBlock body) {
        MTCatch tree = new MTCatch(param, body);
        tree.pos = pos;
        return tree;
    }

    public MTConditional Conditional(MTExpression cond,
                                   MTExpression thenpart,
                                   MTExpression elsepart)
    {
        MTConditional tree = new MTConditional(cond, thenpart, elsepart);
        tree.pos = pos;
        return tree;
    }

    public MTIf If(MTExpression cond, MTStatement thenpart, MTStatement elsepart) {
        MTIf tree = new MTIf(cond, thenpart, elsepart);
        tree.pos = pos;
        return tree;
    }

    public MTExpressionStatement Exec(MTExpression expr) {
        MTExpressionStatement tree = new MTExpressionStatement(expr);
        tree.pos = pos;
        return tree;
    }

    public MTBreak Break(Name label) {
        MTBreak tree = new MTBreak(label, null);
        tree.pos = pos;
        return tree;
    }

    public MTContinue Continue(Name label) {
        MTContinue tree = new MTContinue(label, null);
        tree.pos = pos;
        return tree;
    }

    public MTReturn Return(MTExpression expr) {
        MTReturn tree = new MTReturn(expr);
        tree.pos = pos;
        return tree;
    }

    public MTThrow Throw(MTTree expr) {
        MTThrow tree = new MTThrow(expr);
        tree.pos = pos;
        return tree;
    }

    public MTAssert Assert(MTExpression cond, MTExpression detail) {
        MTAssert tree = new MTAssert(cond, detail);
        tree.pos = pos;
        return tree;
    }

    public MTMethodInvocation Apply(List<MTExpression> typeargs,
		       MTExpression fn,
		       List<MTExpression> args)
    {
        MTMethodInvocation tree = new MTMethodInvocation(typeargs, fn, args);
        tree.pos = pos;
        return tree;
    }

    public MTParens Parens(MTExpression expr) {
        MTParens tree = new MTParens(expr);
        tree.pos = pos;
        return tree;
    }

    public MTAssign Assign(MTExpression lhs, MTExpression rhs) {
        MTAssign tree = new MTAssign(lhs, rhs);
        tree.pos = pos;
        return tree;
    }

    public MTAssignOp Assignop(int opcode, MTTree lhs, MTTree rhs) {
        MTAssignOp tree = new MTAssignOp(opcode, lhs, rhs, null);
        tree.pos = pos;
        return tree;
    }

    public MTUnary Unary(int opcode, MTExpression arg) {
        MTUnary tree = new MTUnary(opcode, arg);
        tree.pos = pos;
        return tree;
    }

    public MTBinary Binary(int opcode, MTExpression lhs, MTExpression rhs) {
        MTBinary tree = new MTBinary(opcode, lhs, rhs, null);
        tree.pos = pos;
        return tree;
    }

    public MTTypeCast TypeCast(MTTree clazz, MTExpression expr) {
        MTTypeCast tree = new MTTypeCast(clazz, expr);
        tree.pos = pos;
        return tree;
    }

    public MTInstanceOf TypeTest(MTExpression expr, MTTree clazz) {
        MTInstanceOf tree = new MTInstanceOf(expr, clazz);
        tree.pos = pos;
        return tree;
    }

    public MTArrayAccess Indexed(MTExpression indexed, MTExpression index) {
        MTArrayAccess tree = new MTArrayAccess(indexed, index);
        tree.pos = pos;
        return tree;
    }

    public MTFieldAccess Select(MTExpression selected, Name selector) {
        MTFieldAccess tree = new MTFieldAccess(selected, selector, null);
        tree.pos = pos;
        return tree;
    }

    public MTIdent Ident(Name name) {
        MTIdent tree = new MTIdent(name, null);
        tree.pos = pos;
        return tree;
    }

    public MTLiteral Literal(int tag, Object value) {
        MTLiteral tree = new MTLiteral(tag, value);
        tree.pos = pos;
        return tree;
    }

    public MTPrimitiveTypeTree TypeIdent(int typetag) {
        MTPrimitiveTypeTree tree = new MTPrimitiveTypeTree(typetag);
        tree.pos = pos;
        return tree;
    }

    public MTArrayTypeTree TypeArray(MTExpression elemtype) {
        MTArrayTypeTree tree = new MTArrayTypeTree(elemtype);
        tree.pos = pos;
        return tree;
    }

    public MTTypeApply TypeApply(MTExpression clazz, List<MTExpression> arguments) {
        MTTypeApply tree = new MTTypeApply(clazz, arguments);
        tree.pos = pos;
        return tree;
    }

    public MTTypeParameter TypeParameter(Name name, List<MTExpression> bounds) {
        MTTypeParameter tree = new MTTypeParameter(name, bounds);
        tree.pos = pos;
        return tree;
    }

    public MTWildcard Wildcard(MTTypeBoundKind kind, MTTree type) {
        MTWildcard tree = new MTWildcard(kind, type);
        tree.pos = pos;
        return tree;
    }

    public MTTypeBoundKind TypeBoundKind(BoundKind kind) {
        MTTypeBoundKind tree = new MTTypeBoundKind(kind);
        tree.pos = pos;
        return tree;
    }

    public MTAnnotation Annotation(MTTree annotationType, List<MTExpression> args) {
        MTAnnotation tree = new MTAnnotation(annotationType, args);
        tree.pos = pos;
        return tree;
    }

    public MTModifiers Modifiers(long flags, List<MTAnnotation> annotations) {
        MTModifiers tree = new MTModifiers(flags, annotations);
        boolean noFlags = (flags & Flags.StandardFlags) == 0;
        tree.pos = (noFlags && annotations.isEmpty()) ? Position.NOPOS : pos;
        return tree;
    }

    public MTModifiers Modifiers(long flags) {
        return Modifiers(flags, List.<MTAnnotation>nil());
    }

    public MTErroneous Erroneous() {
        return Erroneous(List.<MTTree>nil());
    }

    public MTErroneous Erroneous(List<? extends MTTree> errs) {
        MTErroneous tree = new MTErroneous(errs);
        tree.pos = pos;
        return tree;
    }

    public MTLetExpr LetExpr(List<MTVariableDecl> defs, MTTree expr) {
        MTLetExpr tree = new MTLetExpr(defs, expr);
        tree.pos = pos;
        return tree;
    }

/* ***************************************************************************
 * Derived building blocks.
 ****************************************************************************/

    /** Create an identifier from a symbol.
     */
    public MTIdent Ident(Symbol sym) {
        return (MTIdent)new MTIdent((sym.name != names.empty)
				? sym.name
				: sym.flatName(), sym)
            .setPos(pos)
            .setType(sym.type);
    }

    /** Create a selection node from a qualifier tree and a symbol.
     *  @param base   The qualifier tree.
     */
    public MTExpression Select(MTExpression base, Symbol sym) {
        return new MTFieldAccess(base, sym.name, sym).setPos(pos).setType(sym.type);
    }

    /** Create a qualified identifier from a symbol, adding enough qualifications
     *  to make the reference unique.
     */
    public MTExpression QualIdent(Symbol sym) {
        return isUnqualifiable(sym)
            ? Ident(sym)
            : Select(QualIdent(sym.owner), sym);
    }

    /** Create an identifier that refers to the variable declared in given variable
     *  declaration.
     */
    public MTExpression Ident(MTVariableDecl param) {
        return Ident(param.sym);
    }

    /** Create a list of identifiers referring to the variables declared
     *  in given list of variable declarations.
     */
    public List<MTExpression> Idents(List<MTVariableDecl> params) {
        ListBuffer<MTExpression> ids = new ListBuffer<MTExpression>();
        for (List<MTVariableDecl> l = params; l.nonEmpty(); l = l.tail)
            ids.append(Ident(l.head));
        return ids.toList();
    }

    /** Create a tree representing `this', given its type.
     */
    public MTExpression This(Type t) {
        return Ident(new VarSymbol(FINAL, names._this, t, t.tsym));
    }

    /** Create a tree representing a class literal.
     */
    public MTExpression ClassLiteral(ClassSymbol clazz) {
        return ClassLiteral(clazz.type);
    }

    /** Create a tree representing a class literal.
     */
    public MTExpression ClassLiteral(Type t) {
        VarSymbol lit = new VarSymbol(STATIC | PUBLIC | FINAL,
                                      names._class,
                                      t,
                                      t.tsym);
        return Select(Type(t), lit);
    }

    /** Create a tree representing `super', given its type and owner.
     */
    public MTIdent Super(Type t, TypeSymbol owner) {
        return Ident(new VarSymbol(FINAL, names._super, t, owner));
    }

    /**
     * Create a method invocation from a method tree and a list of
     * argument trees.
     */
    public MTMethodInvocation App(MTExpression meth, List<MTExpression> args) {
        return Apply(null, meth, args).setType(meth.type.getReturnType());
    }

    /**
     * Create a no-arg method invocation from a method tree
     */
    public MTMethodInvocation App(MTExpression meth) {
        return Apply(null, meth, List.<MTExpression>nil()).setType(meth.type.getReturnType());
    }

    /** Create a tree representing given type.
     */
    public MTExpression Type(Type t) {
        if (t == null) return null;
        MTExpression tp;
        switch (t.tag) {
        case BYTE: case CHAR: case SHORT: case INT: case LONG: case FLOAT:
        case DOUBLE: case BOOLEAN: case VOID:
            tp = TypeIdent(t.tag);
            break;
        case TYPEVAR:
            tp = Ident(t.tsym);
            break;
        case WILDCARD: {
            WildcardType a = ((WildcardType) t);
            tp = Wildcard(TypeBoundKind(a.kind), Type(a.type));
            break;
        }
        case CLASS:
            Type outer = t.getEnclosingType();
            MTExpression clazz = outer.tag == CLASS && t.tsym.owner.kind == TYP
                ? Select(Type(outer), t.tsym)
                : QualIdent(t.tsym);
            tp = t.getTypeArguments().isEmpty()
                ? clazz
                : TypeApply(clazz, Types(t.getTypeArguments()));
            break;
        case ARRAY:
            tp = TypeArray(Type(types.elemtype(t)));
            break;
        case ERROR:
            tp = TypeIdent(ERROR);
            break;
        default:
            throw new AssertionError("unexpected type: " + t);
        }
        return tp.setType(t);
    }
//where
        private MTExpression Selectors(MTExpression base, Symbol sym, Symbol limit) {
            if (sym == limit) return base;
            else return Select(Selectors(base, sym.owner, limit), sym);
        }

    /** Create a list of trees representing given list of types.
     */
    public List<MTExpression> Types(List<Type> ts) {
        ListBuffer<MTExpression> types = new ListBuffer<MTExpression>();
        for (List<Type> l = ts; l.nonEmpty(); l = l.tail)
            types.append(Type(l.head));
        return types.toList();
    }

    public MTLiteral Literal(Object value) {
        MTLiteral result = null;
        if (value instanceof String) {
            result = Literal(CLASS, value).
                setType(syms.stringType.constType(value));
        } else if (value instanceof Integer) {
            result = Literal(INT, value).
                setType(syms.intType.constType(value));
        } else if (value instanceof Long) {
            result = Literal(LONG, value).
                setType(syms.longType.constType(value));
        } else if (value instanceof Byte) {
            result = Literal(BYTE, value).
                setType(syms.byteType.constType(value));
        } else if (value instanceof Character) {
            result = Literal(CHAR, value).
                setType(syms.charType.constType(value));
        } else if (value instanceof Double) {
            result = Literal(DOUBLE, value).
                setType(syms.doubleType.constType(value));
        } else if (value instanceof Float) {
            result = Literal(FLOAT, value).
                setType(syms.floatType.constType(value));
        } else if (value instanceof Short) {
            result = Literal(SHORT, value).
                setType(syms.shortType.constType(value));
        } else {
            throw new AssertionError(value);
        }
        return result;
    }

    /** Create a type parameter tree from its name and type.
     */
    public MTTypeParameter TypeParam(Name name, TypeVar tvar) {
        return (MTTypeParameter)
            TypeParameter(name, Types(types.getBounds(tvar))).setPos(pos).setType(tvar);
    }

    /** Create a list of type parameter trees from a list of type variables.
     */
    public List<MTTypeParameter> TypeParams(List<Type> typarams) {
        ListBuffer<MTTypeParameter> tparams = new ListBuffer<MTTypeParameter>();
        int i = 0;
        for (List<Type> l = typarams; l.nonEmpty(); l = l.tail)
            tparams.append(TypeParam(l.head.tsym.name, (TypeVar)l.head));
        return tparams.toList();
    }

    /** Wrap a method invocation in an expression statement or return statement,
     *  depending on whether the method invocation expression's type is void.
     */
    public MTStatement Call(MTExpression apply) {
        return apply.type.tag == VOID ? Exec(apply) : Return(apply);
    }

    /** Construct an assignment from a variable symbol and a right hand side.
     */
    public MTStatement Assignment(Symbol v, MTExpression rhs) {
        return Exec(Assign(Ident(v), rhs).setType(v.type));
    }

    /** Construct an index expression from a variable and an expression.
     */
    public MTArrayAccess Indexed(Symbol v, MTExpression index) {
        MTArrayAccess tree = new MTArrayAccess(QualIdent(v), index);
        tree.type = ((ArrayType)v.type).elemtype;
        return tree;
    }

    /** Make an attributed type cast expression.
     */
    public MTTypeCast TypeCast(Type type, MTExpression expr) {
        return (MTTypeCast)TypeCast(Type(type), expr).setType(type);
    }

/* ***************************************************************************
 * Helper methods.
 ****************************************************************************/

    /** Can given symbol be referred to in unqualified form?
     */
    boolean isUnqualifiable(Symbol sym) {
        if (sym.name == names.empty ||
            sym.owner == null ||
            sym.owner.kind == MTH || sym.owner.kind == VAR) {
            return true;
        } else if (sym.kind == TYP && toplevel != null) {
            Scope.Entry e;
            e = toplevel.namedImportScope.lookup(sym.name);
            if (e.scope != null) {
                return
                  e.sym == sym &&
                  e.next().scope == null;
            }
            e = toplevel.packge.members().lookup(sym.name);
            if (e.scope != null) {
                return
                  e.sym == sym &&
                  e.next().scope == null;
            }
            e = toplevel.starImportScope.lookup(sym.name);
            if (e.scope != null) {
                return
                  e.sym == sym &&
                  e.next().scope == null;
            }
        }
        return false;
    }

    /** The name of synthetic parameter number `i'.
     */
    public Name paramName(int i)   { return names.fromString("x" + i); }

    /** The name of synthetic type parameter number `i'.
     */
    public Name typaramName(int i) { return names.fromString("A" + i); }

    public MTClassDeclaration ClassDeclaration(MTModifiers mods,
            Name name,
            List<MTExpression> supertypes,
            List<MTTree> declarations) {
        MTClassDeclaration tree = new MTClassDeclaration(mods,
                name,
                supertypes,
                declarations,
                null);
        tree.pos = pos;
        return tree;
    }

    public MTBlockExpression BlockExpression(long flags, List<MTStatement> stats, MTExpression value) {
        MTBlockExpression tree = new MTBlockExpression(flags, stats, value);
        tree.pos = pos;
        return tree;
    }
    
    public MTOperationDefinition OperationDefinition(
            MTModifiers modifiers,
            Name name,
            MTType restype,
            List<MTVar> params, 
            MTBlockExpression bodyExpression) {
        MTOperationDefinition tree = new MTOperationDefinition(
                modifiers,
                name,
                restype,
                params,
                bodyExpression);
        tree.pos = pos;
        return tree;
    }
    
    public MTOperationValue OperationValue(
            MTType restype,
             List<MTVar> params, 
            MTBlockExpression bodyExpression) {
        MTOperationValue tree = new MTOperationValue(
                restype,
                params,
                bodyExpression);
        tree.pos = pos;
        return tree;
    }

    public MTInitDefinition InitDefinition(
            MTBlock body) {
        MTInitDefinition tree = new MTInitDefinition(
                body);
        tree.pos = pos;
        return tree;
    }
    
    public MTDoLater  DoLater(MTBlock body) {
        MTDoLater tree = new MTDoLater(body);
        tree.pos = pos;
        return tree;
    }
    
    public MTMemberSelector  MemberSelector(Name className,
            Name name) {
        MTMemberSelector tree = new MTMemberSelector(className, name);
        tree.pos = pos;
        return tree;
    }
    
    public MTSequenceEmpty EmptySequence() {
        MTSequenceEmpty tree = new MTSequenceEmpty();
        tree.pos = pos;
        return tree;
    }

    public MTSequenceRange RangeSequence(MTExpression lower, MTExpression upper) {
        MTSequenceRange tree = new MTSequenceRange(lower, upper);
        tree.pos = pos;
        return tree;
    }

    public MTSequenceExplicit ExplicitSequence(List<MTExpression> items) {
        MTSequenceExplicit tree = new MTSequenceExplicit(items);
        tree.pos = pos;
        return tree;
    }
    
    public MTSequenceIndexed SequenceIndexed(MTExpression sequence, MTExpression index) {
        MTSequenceIndexed tree = new MTSequenceIndexed(sequence, index);
        tree.pos = pos;
        return tree;
    }

    public MTSequenceInsert SequenceInsert(MTExpression sequence, MTExpression element) {
        MTSequenceInsert tree = new MTSequenceInsert(sequence, element);
        tree.pos = pos;
        return tree;
    }

    public MTSequenceDelete SequenceDelete(MTExpression sequence) {
        MTSequenceDelete tree = new MTSequenceDelete(sequence, null);
        tree.pos = pos;
        return tree;
    }

    public MTSequenceDelete SequenceDelete(MTExpression sequence, MTExpression element) {
        MTSequenceDelete tree = new MTSequenceDelete(sequence, element);
        tree.pos = pos;
        return tree;
    }

    public MTStringExpression StringExpression(List<MTExpression> parts) {
        MTStringExpression tree = new MTStringExpression(parts);
        tree.pos = pos;
        return tree;
    }
    
    public MTPureObjectLiteral PureObjectLiteral(MTExpression ident,
            List<MTStatement> parts) {
        MTPureObjectLiteral tree = new MTPureObjectLiteral(ident, parts, null);
        tree.pos = pos;
        return tree;
    }
    
    public MTSetAttributeToObjectBeingInitialized SetAttributeToObjectBeingInitialized(Name name) {
        MTSetAttributeToObjectBeingInitialized tree = new MTSetAttributeToObjectBeingInitialized(name, null);
        tree.pos = pos;
        return tree;
    }
    
    public MTObjectLiteralPart ObjectLiteralPart(
            Name attrName,
            MTExpression expr,
            JavafxBindStatus bindStatus) {
        MTObjectLiteralPart tree = new MTObjectLiteralPart(attrName, expr,
                bindStatus, null);
        tree.pos = pos;
        return tree;
    }
    
    public MTType  TypeAny(int cardinality) {
        MTType tree = new MTTypeAny(cardinality);
        tree.pos = pos;
        return tree;
    }
    
    public MTType  TypeUnknown() {
        MTType tree = new MTTypeUnknown();
        tree.pos = pos;
        return tree;
    }
    
    public MTType  TypeClass(MTExpression className,int cardinality) {
        MTType tree = new MTTypeClass(className, cardinality, null);
        tree.pos = pos;
        return tree;
    }
    
    public MTType TypeFunctional(List<MTType> params,
            MTType restype,
            int cardinality) {
        MTType tree = new MTTypeFunctional(params,
                restype,
                cardinality);
        tree.pos = pos;
        return tree;
    }
    
    
    public MTOnReplace OnReplace(MTVar oldValue, MTBlock body) {
         MTOnReplace tree = new MTOnReplace(oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
    public MTOnReplaceElement OnReplaceElement(MTVar index, MTVar oldValue, MTBlock body) {
         MTOnReplaceElement tree = new MTOnReplaceElement(index, oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
    public MTOnInsertElement OnInsertElement(MTVar index, MTVar oldValue, MTBlock body) {
         MTOnInsertElement tree = new MTOnInsertElement(index, oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
    public MTOnDeleteElement OnDeleteElement(MTVar index, MTVar oldValue, MTBlock body) {
         MTOnDeleteElement tree = new MTOnDeleteElement(index, oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
    public MTVar Var(Name name,
            MTType type,
            MTModifiers mods,
            MTExpression initializer,
            JavafxBindStatus bindStatus) {
        MTVar tree = new MTVar(name, type, 
                mods, initializer, bindStatus, List.<MTAbstractOnChange>nil(), null);
        tree.pos = pos;
        return tree;
    }
    
    public MTVar Var(Name name,
            MTType type,
            MTModifiers mods,
            MTExpression initializer,
            JavafxBindStatus bindStatus,
            List<MTAbstractOnChange> onChanges) {
        MTVar tree = new MTVar(name, type, 
                mods, initializer, bindStatus, onChanges, null);
        tree.pos = pos;
        return tree;
    }
    
    public MTVar Param(Name name,
            MTType type) {
        MTVar tree = new MTVar(name, type, 
                Modifiers(Flags.PARAMETER), null, JavafxBindStatus.UNBOUND, 
                List.<MTAbstractOnChange>nil(), null);
        tree.pos = pos;
        return tree;
    }
    
    public MTForExpression ForExpression(
            List<MTForExpressionInClause> inClauses,
            MTExpression bodyExpr) {
        MTForExpression tree = new MTForExpression(inClauses, bodyExpr);       
        tree.pos = pos;
        return tree;
    }
    
    public MTForExpressionInClause InClause(
            MTVar var, 
            MTExpression seqExpr,
            MTExpression whereExpr) {
        MTForExpressionInClause tree = new MTForExpressionInClause(var, seqExpr, whereExpr);       
        tree.pos = pos;
        return tree;
    }
    
    public MTInstanciate Instanciate(MTExpression encl,
                             MTExpression clazz,
                             List<MTExpression> args,
                             MTClassDeclaration def)
    {
        MTInstanciate tree = new MTInstanciate(encl, clazz, args, def);
        tree.pos = pos;
        return tree;
    }

    public MTExpression Identifier(Name name) {
        String str = name.toString();
        if (str.indexOf('.') < 0 && str.indexOf('<') < 0) {
            return Ident(name);
        }
        return Identifier(str);
    }
    
    public MTExpression Identifier(String str) {
        MTExpression tree = null;
        int inx;
        int lastInx = 0;
        do {
            inx = str.indexOf('.', lastInx);
            int endInx;
            if (inx < 0) {
                endInx = str.length();
                int ltInx = str.indexOf('<', lastInx);
                if (ltInx >= 0) {
                    // proof of concept only
                    String part = str.substring(lastInx, ltInx);
                    Name partName = Name.fromString(names, part);
                    tree = tree==null? Ident(partName) : Select(tree, partName);
                    tree.pos = pos;
                    ListBuffer<MTExpression> generic = ListBuffer.lb();
                    int gtInx = str.indexOf('>', ltInx);
                    String tpart = str.substring(ltInx+1, gtInx);
                    Name tpartName = Name.fromString(names, tpart);
                    MTExpression texp = Ident(tpartName);
                    generic.append(texp);
                    tree = TypeApply(tree, generic.toList());
                    tree.pos = pos;
                    break;
                }
            } else {
                endInx = inx;
            }
            String part = str.substring(lastInx, endInx);
            Name partName = Name.fromString(names, part);
            tree = tree==null? Ident(partName) : Select(tree, partName);
            tree.pos = pos;
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }
}