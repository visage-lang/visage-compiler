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

import org.visage.api.VisageBindStatus;
import org.visage.api.tree.TimeLiteralTree.Duration;
import org.visage.api.tree.TypeTree.Cardinality;
import org.visage.api.tree.Tree.VisageKind;
import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.code.Symbol.TypeSymbol;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Type.ClassType;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import org.visage.tools.code.VisageClassSymbol;
import org.visage.tools.code.VisageFlags;
import org.visage.tools.code.VisageSymtab;
import org.visage.tools.code.VisageTypes;
import org.visage.tools.code.VisageVarSymbol;
import org.visage.tools.comp.VisageDefs;
import static com.sun.tools.mjavac.code.Flags.*;
import static com.sun.tools.mjavac.code.Kinds.*;
import static com.sun.tools.mjavac.code.TypeTags.*;
import visage.lang.AngleUnit;
import visage.lang.LengthUnit;

/* Visage version of tree maker
 */
public class VisageTreeMaker implements VisageTreeFactory {

    /** The context key for the tree factory. */
    protected static final Context.Key<VisageTreeMaker> fxTreeMakerKey =
        new Context.Key<VisageTreeMaker>();

    /** Get the VisageTreeMaker instance. */
    public static VisageTreeMaker instance(Context context) {
        VisageTreeMaker instance = context.get(fxTreeMakerKey);
        if (instance == null)
            instance = new VisageTreeMaker(context);
        return instance;
    }

    /** The position at which subsequent trees will be created.
     */
    public int pos = Position.NOPOS;

    /** The toplevel tree to which created trees belong.
     */
    public VisageScript toplevel;

    /** The current name table. */
    protected Name.Table names;

    /** The current type table. */
    protected VisageTypes types;

    /** The current symbol table. */
    protected VisageSymtab syms;

    /** The current defs table. */
    protected VisageDefs defs;

    /** Create a tree maker with null toplevel and NOPOS as initial position.
     */
    protected VisageTreeMaker(Context context) {
        context.put(fxTreeMakerKey, this);
        this.pos = Position.NOPOS;
        this.toplevel = null;
        this.names = Name.Table.instance(context);
        this.syms = (VisageSymtab)VisageSymtab.instance(context);
        this.types = VisageTypes.instance(context);
        this.defs = VisageDefs.instance(context);
    }

    /** Create a tree maker with a given toplevel and FIRSTPOS as initial position.
     */
    protected VisageTreeMaker(VisageScript toplevel, Name.Table names, VisageTypes types, VisageSymtab syms) {
        this.pos = Position.FIRSTPOS;
        this.toplevel = toplevel;
        this.names = names;
        this.types = types;
        this.syms = syms;
    }

    /** Create a new tree maker for a given toplevel.
     */
    public VisageTreeMaker forToplevel(VisageScript toplevel) {
        return new VisageTreeMaker(toplevel, names, types, syms);
    }

    /** Reassign current position.
     */
    public VisageTreeMaker at(int pos) {
        this.pos = pos;
        return this;
    }

    /** Reassign current position.
     */
    public VisageTreeMaker at(DiagnosticPosition pos) {
        this.pos = (pos == null ? Position.NOPOS : pos.getStartPosition());
        return this;
    }

    public VisageImport Import(VisageExpression qualid) {
        VisageImport tree = new VisageImport(qualid);
        tree.pos = pos;
        return tree;
    }

    public VisageSkip Skip() {
        VisageSkip tree = new VisageSkip();
        tree.pos = pos;
        return tree;
    }

    public VisageWhileLoop WhileLoop(VisageExpression cond, VisageExpression body) {
        VisageWhileLoop tree = new VisageWhileLoop(cond, body);
        tree.pos = pos;
        return tree;
    }

    public VisageTry Try(VisageBlock body, List<VisageCatch> catchers, VisageBlock finalizer) {
        VisageTry tree = new VisageTry(body, catchers, finalizer);
        tree.pos = pos;
        return tree;
    }

    public VisageCatch ErroneousCatch(List<? extends VisageTree> errs) {
        VisageCatch tree = new VisageErroneousCatch(errs);
        tree.pos = pos;
        return tree;
    }
    
    public VisageCatch Catch(VisageVar param, VisageBlock body) {
        VisageCatch tree = new VisageCatch(param, body);
        tree.pos = pos;
        return tree;
    }

    public VisageIfExpression Conditional(VisageExpression cond,
                                   VisageExpression thenpart,
                                   VisageExpression elsepart)
    {
        VisageIfExpression tree = new VisageIfExpression(cond, thenpart, elsepart);
        tree.pos = pos;
        return tree;
    }

    public VisageBreak Break(Name label) {
        VisageBreak tree = new VisageBreak(label, null);
        tree.pos = pos;
        return tree;
    }

    public VisageContinue Continue(Name label) {
        VisageContinue tree = new VisageContinue(label, null);
        tree.pos = pos;
        return tree;
    }

    public VisageReturn Return(VisageExpression expr) {
        VisageReturn tree = new VisageReturn(expr);
        tree.pos = pos;
        return tree;
    }

    public VisageThrow Throw(VisageExpression expr) {
        VisageThrow tree = new VisageThrow(expr);
        tree.pos = pos;
        return tree;
    }
    public VisageThrow ErroneousThrow() {
        VisageThrow tree = new VisageErroneousThrow();
        tree.pos = pos;
        return tree;
    }
    public VisageFunctionInvocation Apply(List<VisageExpression> typeargs,
                       VisageExpression fn,
                       List<VisageExpression> args)
    {
        VisageFunctionInvocation tree = new VisageFunctionInvocation(
                typeargs != null? typeargs : List.<VisageExpression>nil(),
                fn,
                args != null? args : List.<VisageExpression>nil());
        tree.pos = pos;
        return tree;
    }

    public VisageParens Parens(VisageExpression expr) {
        VisageParens tree = new VisageParens(expr);
        tree.pos = pos;
        return tree;
    }

    public VisageAssign Assign(VisageExpression lhs, VisageExpression rhs) {
        VisageAssign tree = new VisageAssign(lhs, rhs);
        tree.pos = pos;
        return tree;
    }

    public VisageAssignOp Assignop(VisageTag opcode, VisageExpression lhs, VisageExpression rhs) {
        VisageAssignOp tree = new VisageAssignOp(opcode, lhs, rhs, null);
        tree.pos = pos;
        return tree;
    }

    public VisageBinary Binary(VisageTag opcode, VisageExpression lhs, VisageExpression rhs) {
        VisageBinary tree = new VisageBinary(opcode, lhs, rhs, null);
        tree.pos = pos;
        return tree;
    }

    public VisageTypeCast TypeCast(VisageTree clazz, VisageExpression expr) {
        VisageTypeCast tree = new VisageTypeCast(clazz, expr);
        tree.pos = pos;
        return tree;
    }

    public VisageInstanceOf TypeTest(VisageExpression expr, VisageTree clazz) {
        VisageInstanceOf tree = new VisageInstanceOf(expr, clazz);
        tree.pos = pos;
        return tree;
    }

    public VisageSelect Select(VisageExpression selected, Name selector, boolean nullCheck) {
        VisageSelect tree = new VisageSelect(selected, selector, null, nullCheck);
        tree.pos = pos;
        return tree;
    }

    public VisageIdentSequenceProxy IdentSequenceProxy(Name name, Symbol sym, VisageVarSymbol boundSizeSym) {
        VisageIdentSequenceProxy tree = new VisageIdentSequenceProxy(name, sym, boundSizeSym);
        tree.pos = pos;
        return tree;
    }

    public VisageIdent Ident(Name name) {
        VisageIdent tree = new VisageIdent(name, null);
        tree.pos = pos;
        return tree;
    }

    public VisageErroneousIdent ErroneousIdent() {
        VisageErroneousIdent tree = new VisageErroneousIdent(List.<VisageTree>nil());
        tree.pos = pos;
        return tree;
    }


    public VisageLiteral Literal(int tag, Object value) {
        VisageLiteral tree = new VisageLiteral(tag, value);
        tree.pos = pos;
        return tree;
    }

    public VisageModifiers Modifiers(long flags) {
        VisageModifiers tree = new VisageModifiers(flags);
        boolean noFlags = (flags & Flags.StandardFlags) == 0;
        tree.pos = (noFlags) ? Position.NOPOS : pos;
        return tree;
    }

    public VisageErroneous Erroneous() {
        return Erroneous(List.<VisageTree>nil());
    }

    public VisageErroneous Erroneous(List<? extends VisageTree> errs) {
        VisageErroneous tree = new VisageErroneous(errs);
        tree.pos = pos;
        return tree;
    }

/* ***************************************************************************
 * Derived building blocks.
 ****************************************************************************/

    /** Create an identifier from a symbol.
     */
    public VisageIdent Ident(Symbol sym) {
        VisageIdent id = new VisageIdent(
                (sym.name != names.empty)
                                ? sym.name
                                : sym.flatName(), sym);
        id.setPos(pos);
        id.setType(sym.type);
        id.sym = sym;
        return id;
    }

    /** Create a selection node from a qualifier tree and a symbol.
     *  @param base   The qualifier tree.
     */
    public VisageExpression Select(VisageExpression base, Symbol sym, boolean nullCheck) {
        return new VisageSelect(base, sym.name, sym, nullCheck).setPos(pos).setType(sym.type);
    }

    /** Create an identifier that refers to the variable declared in given variable
     *  declaration.
     */
    public VisageIdent Ident(VisageVar param) {
        return Ident(param.sym);
    }

    /** Create a list of identifiers referring to the variables declared
     *  in given list of variable declarations.
     */
    public List<VisageExpression> Idents(List<VisageVar> params) {
        ListBuffer<VisageExpression> ids = new ListBuffer<VisageExpression>();
        for (List<VisageVar> l = params; l.nonEmpty(); l = l.tail)
            ids.append(Ident(l.head));
        return ids.toList();
    }

    /** Create a tree representing the script class of a given enclosing class.
     */
    public VisageClassSymbol ScriptSymbol(Symbol sym) {
        VisageClassSymbol owner = (VisageClassSymbol)sym;
        
        if (owner.scriptSymbol == null) {
            Name scriptName = owner.name.append(defs.scriptClassSuffixName);
            owner.scriptSymbol = new VisageClassSymbol(Flags.STATIC | Flags.PUBLIC, scriptName, owner);
            owner.scriptSymbol.type = new ClassType(Type.noType, List.<Type>nil(), owner.scriptSymbol);
        }
        
        return owner.scriptSymbol;
    }
    public VisageIdent Script(Symbol sym) {
        return Ident(ScriptSymbol(sym));
    }

    /** Create a tree representing `this', given its type.
     */
    public VisageVarSymbol ThisSymbol(Type t) {
        VisageClassSymbol owner = (VisageClassSymbol)t.tsym;

        if (owner.thisSymbol == null) {
            long flags = FINAL | HASINIT | VisageFlags.VARUSE_SPECIAL;
            owner.thisSymbol = new VisageVarSymbol(types, names, flags, names._this, t, owner);
        }
        
        return owner.thisSymbol;
    }
    public VisageIdent This(Type t) {
        return Ident(ThisSymbol(t));
    }

    /** Create a tree representing `super', given its type and owner.
     */
    public VisageVarSymbol SuperSymbol(Type t, TypeSymbol sym) {
        VisageClassSymbol owner = (VisageClassSymbol)sym;

        if (owner.superSymbol == null) {
            long flags = FINAL | HASINIT | VisageFlags.VARUSE_SPECIAL;
            owner.superSymbol = new VisageVarSymbol(types, names, flags, names._super, t, owner);
        }
        
        return owner.superSymbol;
    }
    public VisageIdent Super(Type t, TypeSymbol owner) {
        return Ident(SuperSymbol(t, owner));
    }

    /** Create a tree representing the script instance of the enclosing class.
     */
    public VisageVarSymbol ScriptAccessSymbol(Symbol sym) {
        VisageClassSymbol owner = (VisageClassSymbol)sym;

        if (owner.scriptAccessSymbol == null) {
            Name scriptLevelAccessName = defs.scriptLevelAccessField(names, sym);
            VisageClassSymbol script = ScriptSymbol(sym);
            long flags = FINAL | STATIC | PUBLIC | HASINIT | VisageFlags.VARUSE_SPECIAL;
            owner.scriptAccessSymbol = new VisageVarSymbol(types, names, flags, scriptLevelAccessName, script.type, owner);
        }
        
        return owner.scriptAccessSymbol;
    }
    public VisageIdent ScriptAccess(Symbol sym) {
        return Ident(ScriptAccessSymbol(sym));
    }
    
    /**
     * Create a method invocation from a method tree and a list of
     * argument trees.
     */
    public VisageFunctionInvocation App(VisageExpression meth, List<VisageExpression> args) {
        return Apply(null, meth, args).setType(meth.type.getReturnType());
    }

    /**
     * Create a no-arg method invocation from a method tree
     */
    public VisageFunctionInvocation App(VisageExpression meth) {
        return Apply(null, meth, List.<VisageExpression>nil()).setType(meth.type.getReturnType());
    }

    /** Create a tree representing given type.
     */
    public VisageExpression Type(Type t) {
        if (t == null) {
            return null;
        }
        VisageExpression tp;
        switch (t.tag) {
            case FLOAT: 
                tp = Ident(syms.floatTypeName);
                break;
            case DOUBLE:
                tp = Ident(syms.doubleTypeName);
                break;
            case CHAR:
                tp = Ident(syms.charTypeName);
                break;
            case BYTE:
                tp = Ident(syms.byteTypeName);
                break;
            case SHORT:
                tp = Ident(syms.shortTypeName);
                break;
            case INT:
                tp = Ident(syms.integerTypeName);
                break;
            case LONG:
                tp = Ident(syms.longTypeName);
                break;
            case BOOLEAN:
                tp = Ident(syms.booleanTypeName);
                break;
            case VOID:
                tp = Ident(syms.voidTypeName);
                break;
            case ARRAY:
                VisageExpression elem = Type(types.elemtype(t));
                elem = elem instanceof VisageType ? elem : TypeClass(elem, Cardinality.SINGLETON);
                tp = TypeArray((VisageType)elem);
                break;
            case CLASS:
                Type outer = t.getEnclosingType();
                tp = outer.tag == CLASS && t.tsym.owner.kind == TYP
                        ? Select(Type(outer), t.tsym, false)
                        : QualIdent(t.tsym);
                break;
            default:
                throw new AssertionError("unexpected type: " + t);
        }
        return tp.setType(t);
    }

    /** Create a list of trees representing given list of types.
     */
    public List<VisageExpression> Types(List<Type> ts) {
        ListBuffer<VisageExpression> typeList = new ListBuffer<VisageExpression>();
        for (List<Type> l = ts; l.nonEmpty(); l = l.tail)
            typeList.append(Type(l.head));
        return typeList.toList();
    }

    public VisageLiteral LiteralInteger(String text, int radix) {
        long longVal = Convert.string2long(text, radix);
        // For decimal, allow Integer negative numbers
        if ((radix==10)? (longVal <= Integer.MAX_VALUE && longVal >= Integer.MIN_VALUE) : ((longVal & ~0xFFFFFFFFL) == 0L))
            return Literal(TypeTags.INT, Integer.valueOf((int) longVal));
        else
            return Literal(TypeTags.LONG, Long.valueOf(longVal));
    }

    public VisageLiteral Literal(Object value) {
        VisageLiteral result = null;
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
        } else if (value instanceof Boolean) {
            result = Literal(BOOLEAN, ((Boolean) value).booleanValue() ? 1 : 0).
                setType(syms.booleanType.constType(value));
        } else {
            throw new AssertionError(value);
        }
        return result;
    }

    /** Make an attributed type cast expression.
     */
    public VisageTypeCast TypeCast(Type type, VisageExpression expr) {
        return (VisageTypeCast)TypeCast(Type(type), expr).setType(type);
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

    public VisageClassDeclaration ClassDeclaration(VisageModifiers mods,
            Name name,
            List<VisageExpression> supertypes,
            List<VisageTree> declarations) {
        VisageClassDeclaration tree = new VisageClassDeclaration(mods,
                name,
                supertypes,
                declarations,
                null);
        tree.pos = pos;

        return tree;
    }

    public VisageBlock Block(long flags, List<VisageExpression> stats, VisageExpression value) {
        VisageBlock tree = new VisageBlock(flags, stats, value);
        tree.pos = pos;
        return tree;
    }

    public VisageBlock ErroneousBlock() {
        VisageErroneousBlock tree = new VisageErroneousBlock(List.<VisageTree>nil());
        tree.pos = pos;
        return tree;
    }
    public VisageBlock ErroneousBlock(List<? extends VisageTree> errs) {
        VisageErroneousBlock tree = new VisageErroneousBlock(errs);
        tree.pos = pos;
        return tree;
    }
        
    public VisageFunctionDefinition FunctionDefinition(
            VisageModifiers modifiers,
            Name name,
            VisageType restype,
            List<VisageVar> params,
            VisageBlock bodyExpression) {
        VisageFunctionDefinition tree = new VisageFunctionDefinition(
                modifiers,
                name,
                restype,
                params,
                bodyExpression);
        tree.operation.definition = tree;
        tree.pos = pos;
        return tree;
    }

    public VisageFunctionValue FunctionValue(
            VisageType restype,
             List<VisageVar> params,
            VisageBlock bodyExpression) {
        VisageFunctionValue tree = new VisageFunctionValue(
                restype,
                params,
                bodyExpression);
        tree.pos = pos;
        return tree;
    }

    public VisageInitDefinition InitDefinition(
            VisageBlock body) {
        VisageInitDefinition tree = new VisageInitDefinition(
                body);
        tree.pos = pos;
        return tree;
    }

    public VisagePostInitDefinition PostInitDefinition(VisageBlock body) {
        VisagePostInitDefinition tree = new VisagePostInitDefinition(body);
        tree.pos = pos;
        return tree;
    }

    public VisageSequenceEmpty EmptySequence() {
        VisageSequenceEmpty tree = new VisageSequenceEmpty();
        tree.pos = pos;
        return tree;
    }

    public VisageSequenceRange RangeSequence(VisageExpression lower, VisageExpression upper, VisageExpression stepOrNull, boolean exclusive) {
        VisageSequenceRange tree = new VisageSequenceRange(lower, upper,  stepOrNull,  exclusive);
        tree.pos = pos;
        return tree;
    }

    public VisageSequenceExplicit ExplicitSequence(List<VisageExpression> items) {
        VisageSequenceExplicit tree = new VisageSequenceExplicit(items);
        tree.pos = pos;
        return tree;
    }

    public VisageSequenceIndexed SequenceIndexed(VisageExpression sequence, VisageExpression index) {
        VisageSequenceIndexed tree = new VisageSequenceIndexed(sequence, index);
        tree.pos = pos;
        return tree;
    }

    public VisageSequenceSlice SequenceSlice(VisageExpression sequence, VisageExpression firstIndex, VisageExpression lastIndex, int endKind) {
        VisageSequenceSlice tree = new VisageSequenceSlice(sequence, firstIndex,
                lastIndex, endKind);
        tree.pos = pos;
        return tree;
    }

    public VisageSequenceInsert SequenceInsert(VisageExpression sequence, VisageExpression element, VisageExpression position, boolean after) {
        VisageSequenceInsert tree = new VisageSequenceInsert(sequence, element, position, after);
        tree.pos = pos;
        return tree;
    }

    public VisageSequenceDelete SequenceDelete(VisageExpression sequence) {
        VisageSequenceDelete tree = new VisageSequenceDelete(sequence, null);
        tree.pos = pos;
        return tree;
    }

    public VisageSequenceDelete SequenceDelete(VisageExpression sequence, VisageExpression element) {
        VisageSequenceDelete tree = new VisageSequenceDelete(sequence, element);
        tree.pos = pos;
        return tree;
    }

    public VisageStringExpression StringExpression(List<VisageExpression> parts,
                                        String translationKey) {
        VisageStringExpression tree = new VisageStringExpression(parts, translationKey);
        tree.pos = pos;
        return tree;
    }

    public VisageInstanciate Instanciate(VisageKind kind, VisageExpression clazz, VisageClassDeclaration def, List<VisageExpression> args, List<VisageObjectLiteralPart> parts, List<VisageVar> localVars) {
        VisageInstanciate tree = new VisageInstanciate(kind, clazz, def, args, parts, localVars, null);
        tree.pos = pos;
        return tree;
    }

    public VisageInstanciate ObjectLiteral(VisageExpression ident,
            List<VisageTree> defs) {
        return Instanciate(VisageKind.INSTANTIATE_OBJECT_LITERAL,
                ident,
                List.<VisageExpression>nil(),
                defs);
    }

    public VisageInstanciate InstanciateNew(VisageExpression ident,
            List<VisageExpression> args) {
        return Instanciate(VisageKind.INSTANTIATE_NEW,
                ident,
                args != null? args : List.<VisageExpression>nil(),
                List.<VisageTree>nil());
    }

   public VisageInstanciate Instanciate(VisageKind kind, VisageExpression ident,
           List<VisageExpression> args,
           List<VisageTree> defs) {

       // Don't try and process object literals that have erroneous elements
       //
       if  (ident instanceof VisageErroneous) return null;

       ListBuffer<VisageObjectLiteralPart> partsBuffer = ListBuffer.lb();
       ListBuffer<VisageTree> defsBuffer = ListBuffer.lb();
       ListBuffer<VisageExpression> varsBuffer = ListBuffer.lb();
       boolean boundParts = false;
       if (defs != null) {
           for (VisageTree def : defs) {
               if (def instanceof VisageObjectLiteralPart) {
                   VisageObjectLiteralPart olp = (VisageObjectLiteralPart) def;
                   partsBuffer.append(olp);
                   boundParts |= olp.isExplicitlyBound();
               } else if (def instanceof VisageVar /* && ((VisageVar)def).isLocal()*/) {
                   // for now, at least, assume any var declaration inside an object literal is local
                   varsBuffer.append((VisageVar) def);
               } else {
                   defsBuffer.append(def);
               }
           }
       }
       VisageClassDeclaration klass = null;
       if (defsBuffer.size() > 0 || boundParts) {
           VisageExpression id = ident;
           while (id instanceof VisageSelect) id = ((VisageSelect)id).getExpression();
           Name cname = objectLiteralClassName(((VisageIdent)id).getName());
           long innerClassFlags = Flags.SYNTHETIC | Flags.FINAL; // to enable, change to Flags.FINAL
           
           klass = this.ClassDeclaration(this.Modifiers(innerClassFlags), cname, List.<VisageExpression>of(ident), defsBuffer.toList());
       }

       VisageInstanciate tree = new VisageInstanciate(kind, ident,
               klass,
               args==null? List.<VisageExpression>nil() : args,
               partsBuffer.toList(),
               List.convert(VisageVar.class, varsBuffer.toList()),
               null);
       tree.pos = pos;
       return tree;
   }

    public VisageObjectLiteralPart ObjectLiteralPart(
            Name attrName,
            VisageExpression expr,
            VisageBindStatus bindStatus) {
        VisageObjectLiteralPart tree =
                new VisageObjectLiteralPart(attrName, expr, bindStatus, null);
        tree.pos = pos;
        return tree;
    }

    public VisageType  TypeAny(Cardinality cardinality) {
        VisageType tree = new VisageTypeAny(cardinality);
        tree.pos = pos;
        return tree;
    }
    
    public VisageType  ErroneousType() {
        VisageType tree = new VisageErroneousType(List.<VisageTree>nil());
        tree.pos = pos;
        return tree;
    }

    public VisageType  ErroneousType(List<? extends VisageTree> errs) {
        VisageType tree = new VisageErroneousType(errs);
        tree.pos = pos;
        return tree;
    }

    public VisageType  TypeUnknown() {
        VisageType tree = new VisageTypeUnknown();
        tree.pos = pos;
        return tree;
    }

    public VisageType  TypeClass(VisageExpression className,Cardinality cardinality) {
        return TypeClass(className, cardinality, null);
    }

    public VisageType  TypeClass(VisageExpression className,Cardinality cardinality, ClassSymbol sym) {
        VisageType tree = new VisageTypeClass(className, cardinality, sym);
        tree.pos = pos;
        return tree;
    }

    public VisageType TypeFunctional(List<VisageType> params,
            VisageType restype,
            Cardinality cardinality) {
        VisageType tree = new VisageTypeFunctional(params,
                restype,
                cardinality);
        tree.pos = pos;
        return tree;
    }

    public VisageType TypeArray(VisageType elementType) {
        VisageType tree = new VisageTypeArray(elementType);
        tree.pos = pos;
        return tree;
    }

    public VisageOverrideClassVar TriggerWrapper(VisageIdent expr, VisageOnReplace onReplace, VisageOnReplace onInvalidate) {
        VisageOverrideClassVar tree = new VisageOverrideClassVar(null, null, null, expr, null, null, onReplace, onInvalidate, null);
        tree.pos = pos;
        return tree;
    }
    
   public VisageOnReplace ErroneousOnReplace(List<? extends VisageTree> errs) {
        VisageOnReplace tree = new VisageErroneousOnReplace(errs, VisageOnReplace.Kind.ONREPLACE);
        tree.pos = pos;
        return tree;
    }

   public VisageOnReplace ErroneousOnInvalidate(List<? extends VisageTree> errs) {
        VisageOnReplace tree = new VisageErroneousOnReplace(errs, VisageOnReplace.Kind.ONINVALIDATE);
        tree.pos = pos;
        return tree;
    }

    public VisageOnReplace OnReplace(VisageVar oldValue, VisageBlock body) {
        VisageOnReplace tree = new VisageOnReplace(oldValue, body, VisageOnReplace.Kind.ONREPLACE);
        tree.pos = pos;
        return tree;
    }

     public VisageOnReplace OnReplace(VisageVar oldValue, VisageVar firstIndex,
             VisageVar lastIndex, VisageVar newElements, VisageBlock body) {
         return OnReplace(oldValue, firstIndex, lastIndex,
                 VisageSequenceSlice.END_INCLUSIVE, newElements, body);
    }

     public VisageOnReplace OnReplace(VisageVar oldValue, VisageVar firstIndex,
             VisageVar lastIndex, int endKind, VisageVar newElements, VisageBlock body) {
         VisageOnReplace tree = OnReplace(oldValue, firstIndex, lastIndex,
                 endKind, newElements, null, body);
        tree.pos = pos;
        return tree;
     }

     public VisageOnReplace OnReplace(VisageVar oldValue, VisageVar firstIndex,
             VisageVar lastIndex, int endKind, VisageVar newElements, VisageVar saveVar, VisageBlock body) {
         VisageOnReplace tree = new VisageOnReplace(oldValue, firstIndex, lastIndex,
                 endKind, newElements, saveVar, body, VisageOnReplace.Kind.ONREPLACE);
        tree.pos = pos;
        return tree;
     }

     public VisageOnReplace OnInvalidate(VisageBlock body) {
        VisageOnReplace tree = new VisageOnReplace(null, body, VisageOnReplace.Kind.ONINVALIDATE);
        tree.pos = pos;
        return tree;
    }

     public VisageVarInit VarInit(VisageVar var) {
         VisageVarInit tree = new VisageVarInit(var);
         tree.pos = (var==null) ? Position.NOPOS : var.pos;
         return tree;
     }

     public VisageVarRef VarRef(VisageExpression expr, VisageVarRef.RefKind kind) {
         VisageVarRef tree = new VisageVarRef(expr, kind);
         tree.pos = pos;
         return tree;
     }

    public VisageVar Var(Name name,
            VisageType type,
            VisageModifiers mods,
            VisageExpression initializer,
            VisageBindStatus bindStatus,
            VisageOnReplace onReplace,
            VisageOnReplace onInvalidate) {
            VisageVar tree = new VisageVar(name, type,
               mods, initializer, bindStatus, onReplace, onInvalidate, null);
        tree.pos = pos;
        return tree;
    }
    public VisageOverrideClassVar OverrideClassVar(Name name, VisageType type, VisageModifiers mods, VisageIdent expr,
            VisageExpression initializer,
            VisageBindStatus bindStatus,
            VisageOnReplace onReplace,
            VisageOnReplace onInvalidate) {
        VisageOverrideClassVar tree = new VisageOverrideClassVar(name, type, mods, expr, initializer,
                bindStatus, onReplace, onInvalidate, null);
        tree.pos = pos;
        return tree;
    }

    public VisageVar Param(Name name,
            VisageType type) {
        VisageVar tree = new VisageVar(name, type,
                Modifiers(Flags.PARAMETER), null, VisageBindStatus.UNBOUND, null, null, null);
        tree.pos = pos;
        return tree;
    }

    public VisageForExpression ForExpression(
            List<VisageForExpressionInClause> inClauses,
            VisageExpression bodyExpr) {
        VisageForExpression tree = new VisageForExpression(VisageKind.FOR_EXPRESSION_FOR, inClauses, bodyExpr);
        tree.pos = pos;
        return tree;
    }

    public VisageForExpression Predicate(
            List<VisageForExpressionInClause> inClauses,
            VisageExpression bodyExpr) {
        VisageForExpression tree = new VisageForExpression(VisageKind.FOR_EXPRESSION_PREDICATE, inClauses, bodyExpr);
        tree.pos = pos;
        return tree;
    }

    public VisageForExpressionInClause InClause(
            VisageVar var,
            VisageExpression seqExpr,
            VisageExpression whereExpr) {
        VisageForExpressionInClause tree = new VisageForExpressionInClause(var, seqExpr, whereExpr);
        tree.pos = pos;
        return tree;
    }
    
    public VisageErroneousForExpressionInClause ErroneousInClause(List<? extends VisageTree> errs) {

        VisageErroneousForExpressionInClause tree = new VisageErroneousForExpressionInClause(errs);
        tree.pos = pos;
        return tree;
    }

    public VisageExpression Identifier(Name name) {
        String str = name.toString();
        if (str.indexOf('.') < 0 && str.indexOf('<') < 0) {
            return Ident(name);
        }
        return Identifier(str);
    }

    public VisageExpression Identifier(String str) {
        assert str.indexOf('<') < 0 : "attempt to parse a type with 'Identifier'.  Use TypeTree";
        VisageExpression tree = null;
        int inx;
        int lastInx = 0;
        do {
            inx = str.indexOf('.', lastInx);
            int endInx;
            if (inx < 0) {
                endInx = str.length();
            } else {
                endInx = inx;
            }
            String part = str.substring(lastInx, endInx);
            Name partName = names.fromString(part);
            tree = tree == null?
                Ident(partName) :
                Select(tree, partName, false);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }

    public VisageInterpolateValue InterpolateValue(VisageExpression attr, VisageExpression v, VisageExpression interp) {
        VisageInterpolateValue tree = new VisageInterpolateValue(attr, v, interp);
        tree.pos = pos;
        return tree;
    }

    public VisageInvalidate Invalidate(VisageExpression var) {
        VisageInvalidate tree = new VisageInvalidate(var);
        tree.pos = pos;
        return tree;
    }
     
    public VisageIndexof Indexof (VisageIdent name) {
        VisageIndexof tree = new VisageIndexof(name);
        tree.pos = pos;
        return tree;
    }

    public VisageTimeLiteral TimeLiteral(String str) {
        int i = 0;
        char[] buf = str.toCharArray();

        // Locate the duration specifier.
        //
        while (i < buf.length && (Character.isDigit(buf[i]) || buf[i] == '.' || buf[i] == 'e' || buf[i] == 'E'))
            i++;

        assert i > 0;               // lexer should only pass valid time strings
        assert buf.length - i > 0;  // lexer should only pass valid time strings

        String dur = str.substring(i);
        Duration duration =
                dur.equals("ms") ? Duration.MILLIS :
                dur.equals("s") ? Duration.SECONDS :
                dur.equals("m") ? Duration.MINUTES :
                dur.equals("h") ? Duration.HOURS : null;
        assert duration != null;
        Object timeVal;
        Double value;
        try {

            // Extract the literal value up to but excluding the duration
            // specifier.
            //
            String s = str.substring(0, i);

            // Even though the number of hours/mounts/etc may be specified
            // as an integer, we still need to use a double value always because
            // durations such as 999999m will overflow an integer.
            //
            value = Double.valueOf(s) * duration.getMultiplier();

            // Now use an integer if we will not overflow the maximum vlaue
            // for an integer and the value is an integer number.
            //
            if  (value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE && value == value.intValue()) {
                timeVal = new Integer(value.intValue());
            }
            else
            {
                // Has to stay as a double or it would overflow, or it was
                // not an integer vlaue, such as 5.5m
                //
                timeVal = value;
            }
        }
        catch (NumberFormatException ex) {
            // error already reported in scanner
            timeVal = Double.NaN;
        }
        VisageLiteral literal = Literal(timeVal);
        VisageTimeLiteral tree = new VisageTimeLiteral(literal, duration);
        tree.pos = pos;
        return tree;
    }

    public VisageTimeLiteral TimeLiteral(VisageLiteral literal, Duration duration) {
        VisageTimeLiteral tree = new VisageTimeLiteral(literal, duration);
        tree.pos = pos;
        return tree;
    }

    public VisageErroneousTimeLiteral ErroneousTimeLiteral() {
        VisageErroneousTimeLiteral tree = new VisageErroneousTimeLiteral(List.<VisageTree>nil());
        tree.pos = pos;

        return tree;
    }

    public VisageLengthLiteral LengthLiteral(String str) {
        int i = 0;
        char[] buf = str.toCharArray();

        // Locate the length specifier. (also swallows the 'e' in "em")
        //
        while (i < buf.length && (Character.isDigit(buf[i]) || buf[i] == '.' || buf[i] == 'e' || buf[i] == 'E'))
            i++;
        
        if (str.substring(i).equals("m")) {
            // ate the 'e' in "em", backup one.
            i--;
        }

        assert i > 0;               // lexer should only pass valid length strings
        assert buf.length - i > 0;  // lexer should only pass valid length strings

        String u = str.substring(i);
        LengthUnit units =
                u.equals("in") ? LengthUnit.INCH :
                u.equals("cm") ? LengthUnit.CENTIMETER :
                u.equals("mm") ? LengthUnit.MILLIMETER :
                u.equals("pt") ? LengthUnit.POINT :
                u.equals("pc") ? LengthUnit.PICA :
                u.equals("em") ? LengthUnit.EM :
                u.equals("px") ? LengthUnit.PIXEL :
                u.equals("dp") ? LengthUnit.DENSITY_INDEPENDENT_PIXEL :
                u.equals("sp") ? LengthUnit.SCALE_INDEPENDENT_PIXEL :
                u.equals("%") ? LengthUnit.PERCENTAGE : null;
        assert units != null : "unknown unit: '" + u + "'";
        Object lengthVal;
        Double value;
        try {

            // Extract the literal value up to but excluding the length
            // specifier.
            //
            String s = str.substring(0, i);
            value = Double.valueOf(s);

            // Now use an integer if we will not overflow the maximum vlaue
            // for an integer and the value is an integer number.
            //
            if  (value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE && value == value.intValue()) {
                lengthVal = new Integer(value.intValue());
            }
            else
            {
                // Has to stay as a double or it would overflow, or it was
                // not an integer vlaue, such as 5.5mm
                //
                lengthVal = value;
            }
        }
        catch (NumberFormatException ex) {
            // error already reported in scanner
            lengthVal = Double.NaN;
        }
        VisageLiteral literal = Literal(lengthVal);
        VisageLengthLiteral tree = new VisageLengthLiteral(literal, units);
        tree.pos = pos;
        return tree;
    }

    public VisageLengthLiteral LengthLiteral(VisageLiteral literal, LengthUnit units) {
        VisageLengthLiteral tree = new VisageLengthLiteral(literal, units);
        tree.pos = pos;
        return tree;
    }

    public VisageErroneousLengthLiteral ErroneousLengthLiteral() {
        VisageErroneousLengthLiteral tree = new VisageErroneousLengthLiteral(List.<VisageTree>nil());
        tree.pos = pos;

        return tree;
    }

    public VisageAngleLiteral AngleLiteral(String str) {
        int i = 0;
        char[] buf = str.toCharArray();

        // Locate the angle specifier.
        //
        while (i < buf.length && (Character.isDigit(buf[i]) || buf[i] == '.' || buf[i] == 'e' || buf[i] == 'E'))
            i++;

        assert i > 0;               // lexer should only pass valid angle strings
        assert buf.length - i > 0;  // lexer should only pass valid angle strings

        String u = str.substring(i);
        AngleUnit units =
                u.equals("deg") ? AngleUnit.DEGREE :
                u.equals("rad") ? AngleUnit.RADIAN :
                u.equals("turn") ? AngleUnit.TURN : null;
        assert units != null;
        Object angleVal;
        Double value;
        try {

            // Extract the literal value up to but excluding the angle
            // specifier.
            //
            String s = str.substring(0, i);
            value = Double.valueOf(s);

            // Now use an integer if we will not overflow the maximum vlaue
            // for an integer and the value is an integer number.
            //
            if  (value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE && value == value.intValue()) {
                angleVal = new Integer(value.intValue());
            }
            else
            {
                // Has to stay as a double or it would overflow, or it was
                // not an integer vlaue, such as 5.5mm
                //
                angleVal = value;
            }
        }
        catch (NumberFormatException ex) {
            // error already reported in scanner
            angleVal = Double.NaN;
        }
        VisageLiteral literal = Literal(angleVal);
        VisageAngleLiteral tree = new VisageAngleLiteral(literal, units);
        tree.pos = pos;
        return tree;
    }

    public VisageAngleLiteral AngleLiteral(VisageLiteral literal, AngleUnit units) {
        VisageAngleLiteral tree = new VisageAngleLiteral(literal, units);
        tree.pos = pos;
        return tree;
    }

    public VisageErroneousAngleLiteral ErroneousAngleLiteral() {
        VisageErroneousAngleLiteral tree = new VisageErroneousAngleLiteral(List.<VisageTree>nil());
        tree.pos = pos;

        return tree;
    }

    public VisageColorLiteral ColorLiteral(String str) {
        // valid strings: #rgb, #rrggbb, #rgb|a, or #rrggbb|aa
        String color;
        String alpha = null;
        switch (str.length()) {
            case 4:
                color = str.substring(1);
                break;
            case 7:
                color = str.substring(1);
                break;
            case 6:
                color = str.substring(1, 4);
                alpha = str.substring(5);
                break;
            case 10:
                color = str.substring(1, 7);
                alpha = str.substring(8);
                break;
            default:
                throw new IllegalStateException("malformed color literal string: " + str);
        }
        int colorVal = Integer.parseInt(color, 16);
        int alphaVal = alpha == null ? 0xFF : Integer.parseInt(alpha, 16);
        switch (str.length()) {
            case 6:
                alphaVal |= alphaVal << 4;
            case 4:
                int r = (colorVal >> 8) & 0xF; r |= r << 4;
                int g = (colorVal >> 4) & 0xF; g |= g << 4;
                int b = colorVal & 0xF; b |= b << 4;
                colorVal = r << 16 | g << 8 | b;
        }
        VisageLiteral literal = Literal(colorVal | alphaVal << 24);
        VisageColorLiteral tree = new VisageColorLiteral(literal);
        tree.pos = pos;
        return tree;
    }

    public VisageColorLiteral ColorLiteral(VisageLiteral literal) {
        VisageColorLiteral tree = new VisageColorLiteral(literal);
        tree.pos = pos;
        return tree;
    }

    public VisageErroneousColorLiteral ErroneousColorLiteral() {
        VisageErroneousColorLiteral tree = new VisageErroneousColorLiteral(List.<VisageTree>nil());
        tree.pos = pos;

        return tree;
    }

    public VisageKeyFrameLiteral KeyFrameLiteral(VisageExpression start, List<VisageExpression> values, VisageExpression trigger) {
        VisageKeyFrameLiteral tree = new VisageKeyFrameLiteral(start, values, trigger);
        tree.pos = pos;
        return tree;
    }

    public VisageUnary Unary(VisageTag opcode, VisageExpression arg) {
        VisageUnary tree = new VisageUnary(opcode, arg);
        tree.pos = pos;
        return tree;
    }

    private int syntheticClassNumber = 0;

    Name syntheticClassName(Name superclass, String infix) {
        return names.fromString(superclass.toString() + infix + ++syntheticClassNumber);
    }

    Name objectLiteralClassName(Name superclass) {
        return syntheticClassName(superclass, VisageDefs.objectLiteralClassInfix);
    }

    /**
     * Clone of javac's VisageTreeMaker.Script, minus the assertion check of defs types.
     */
    public VisageScript Script(VisageExpression pid,
                                      List<VisageTree> defs) {
        VisageScript tree = new VisageScript(pid, defs,
                                     null, null, null, null);
        tree.pos = pos;
        return tree;
    }

    public VisageExpression QualIdent(Symbol sym) {
        if (sym.kind ==Kinds.PCK && sym.owner == syms.rootPackage)
            return Ident(sym);
        return isUnqualifiable(sym)
            ? Ident(sym)
            : Select(QualIdent(sym.owner), sym, false);
    }
}
