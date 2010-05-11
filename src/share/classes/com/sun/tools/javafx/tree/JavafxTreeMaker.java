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

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.TimeLiteralTree.Duration;
import com.sun.javafx.api.tree.TypeTree.Cardinality;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.code.Symbol.TypeSymbol;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Type.ClassType;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;

import com.sun.tools.javafx.code.JavafxClassSymbol;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.comp.JavafxDefs;
import static com.sun.tools.mjavac.code.Flags.*;
import static com.sun.tools.mjavac.code.Kinds.*;
import static com.sun.tools.mjavac.code.TypeTags.*;


/* JavaFX version of tree maker
 */
public class JavafxTreeMaker implements JavafxTreeFactory {

    /** The context key for the tree factory. */
    protected static final Context.Key<JavafxTreeMaker> fxTreeMakerKey =
        new Context.Key<JavafxTreeMaker>();

    /** Get the JavafxTreeMaker instance. */
    public static JavafxTreeMaker instance(Context context) {
        JavafxTreeMaker instance = context.get(fxTreeMakerKey);
        if (instance == null)
            instance = new JavafxTreeMaker(context);
        return instance;
    }

    /** The position at which subsequent trees will be created.
     */
    public int pos = Position.NOPOS;

    /** The toplevel tree to which created trees belong.
     */
    public JFXScript toplevel;

    /** The current name table. */
    protected Name.Table names;

    /** The current type table. */
    protected JavafxTypes types;

    /** The current symbol table. */
    protected JavafxSymtab syms;

    /** The current defs table. */
    protected JavafxDefs defs;

    /** Create a tree maker with null toplevel and NOPOS as initial position.
     */
    protected JavafxTreeMaker(Context context) {
        context.put(fxTreeMakerKey, this);
        this.pos = Position.NOPOS;
        this.toplevel = null;
        this.names = Name.Table.instance(context);
        this.syms = (JavafxSymtab)JavafxSymtab.instance(context);
        this.types = JavafxTypes.instance(context);
        this.defs = JavafxDefs.instance(context);
    }

    /** Create a tree maker with a given toplevel and FIRSTPOS as initial position.
     */
    protected JavafxTreeMaker(JFXScript toplevel, Name.Table names, JavafxTypes types, JavafxSymtab syms) {
        this.pos = Position.FIRSTPOS;
        this.toplevel = toplevel;
        this.names = names;
        this.types = types;
        this.syms = syms;
    }

    /** Create a new tree maker for a given toplevel.
     */
    public JavafxTreeMaker forToplevel(JFXScript toplevel) {
        return new JavafxTreeMaker(toplevel, names, types, syms);
    }

    /** Reassign current position.
     */
    public JavafxTreeMaker at(int pos) {
        this.pos = pos;
        return this;
    }

    /** Reassign current position.
     */
    public JavafxTreeMaker at(DiagnosticPosition pos) {
        this.pos = (pos == null ? Position.NOPOS : pos.getStartPosition());
        return this;
    }

    public JFXImport Import(JFXExpression qualid) {
        JFXImport tree = new JFXImport(qualid);
        tree.pos = pos;
        return tree;
    }

    public JFXSkip Skip() {
        JFXSkip tree = new JFXSkip();
        tree.pos = pos;
        return tree;
    }

    public JFXWhileLoop WhileLoop(JFXExpression cond, JFXExpression body) {
        JFXWhileLoop tree = new JFXWhileLoop(cond, body);
        tree.pos = pos;
        return tree;
    }

    public JFXTry Try(JFXBlock body, List<JFXCatch> catchers, JFXBlock finalizer) {
        JFXTry tree = new JFXTry(body, catchers, finalizer);
        tree.pos = pos;
        return tree;
    }

    public JFXCatch ErroneousCatch(List<? extends JFXTree> errs) {
        JFXCatch tree = new JFXErroneousCatch(errs);
        tree.pos = pos;
        return tree;
    }
    
    public JFXCatch Catch(JFXVar param, JFXBlock body) {
        JFXCatch tree = new JFXCatch(param, body);
        tree.pos = pos;
        return tree;
    }

    public JFXIfExpression Conditional(JFXExpression cond,
                                   JFXExpression thenpart,
                                   JFXExpression elsepart)
    {
        JFXIfExpression tree = new JFXIfExpression(cond, thenpart, elsepart);
        tree.pos = pos;
        return tree;
    }

    public JFXBreak Break(Name label) {
        JFXBreak tree = new JFXBreak(label, null);
        tree.pos = pos;
        return tree;
    }

    public JFXContinue Continue(Name label) {
        JFXContinue tree = new JFXContinue(label, null);
        tree.pos = pos;
        return tree;
    }

    public JFXReturn Return(JFXExpression expr) {
        JFXReturn tree = new JFXReturn(expr);
        tree.pos = pos;
        return tree;
    }

    public JFXThrow Throw(JFXExpression expr) {
        JFXThrow tree = new JFXThrow(expr);
        tree.pos = pos;
        return tree;
    }
    public JFXThrow ErroneousThrow() {
        JFXThrow tree = new JFXErroneousThrow();
        tree.pos = pos;
        return tree;
    }
    public JFXFunctionInvocation Apply(List<JFXExpression> typeargs,
                       JFXExpression fn,
                       List<JFXExpression> args)
    {
        JFXFunctionInvocation tree = new JFXFunctionInvocation(
                typeargs != null? typeargs : List.<JFXExpression>nil(),
                fn,
                args != null? args : List.<JFXExpression>nil());
        tree.pos = pos;
        return tree;
    }

    public JFXParens Parens(JFXExpression expr) {
        JFXParens tree = new JFXParens(expr);
        tree.pos = pos;
        return tree;
    }

    public JFXAssign Assign(JFXExpression lhs, JFXExpression rhs) {
        JFXAssign tree = new JFXAssign(lhs, rhs);
        tree.pos = pos;
        return tree;
    }

    public JFXAssignOp Assignop(JavafxTag opcode, JFXExpression lhs, JFXExpression rhs) {
        JFXAssignOp tree = new JFXAssignOp(opcode, lhs, rhs, null);
        tree.pos = pos;
        return tree;
    }

    public JFXBinary Binary(JavafxTag opcode, JFXExpression lhs, JFXExpression rhs) {
        JFXBinary tree = new JFXBinary(opcode, lhs, rhs, null);
        tree.pos = pos;
        return tree;
    }

    public JFXTypeCast TypeCast(JFXTree clazz, JFXExpression expr) {
        JFXTypeCast tree = new JFXTypeCast(clazz, expr);
        tree.pos = pos;
        return tree;
    }

    public JFXInstanceOf TypeTest(JFXExpression expr, JFXTree clazz) {
        JFXInstanceOf tree = new JFXInstanceOf(expr, clazz);
        tree.pos = pos;
        return tree;
    }

    public JFXSelect Select(JFXExpression selected, Name selector) {
        JFXSelect tree = new JFXSelect(selected, selector, null);
        tree.pos = pos;
        return tree;
    }

    public JFXIdentSequenceProxy IdentSequenceProxy(Name name, Symbol sym, JavafxVarSymbol boundSizeSym) {
        JFXIdentSequenceProxy tree = new JFXIdentSequenceProxy(name, sym, boundSizeSym);
        tree.pos = pos;
        return tree;
    }

    public JFXIdent Ident(Name name) {
        JFXIdent tree = new JFXIdent(name, null);
        tree.pos = pos;
        return tree;
    }

    public JFXErroneousIdent ErroneousIdent() {
        JFXErroneousIdent tree = new JFXErroneousIdent(List.<JFXTree>nil());
        tree.pos = pos;
        return tree;
    }


    public JFXLiteral Literal(int tag, Object value) {
        JFXLiteral tree = new JFXLiteral(tag, value);
        tree.pos = pos;
        return tree;
    }

    public JFXModifiers Modifiers(long flags) {
        JFXModifiers tree = new JFXModifiers(flags);
        boolean noFlags = (flags & Flags.StandardFlags) == 0;
        tree.pos = (noFlags) ? Position.NOPOS : pos;
        return tree;
    }

    public JFXErroneous Erroneous() {
        return Erroneous(List.<JFXTree>nil());
    }

    public JFXErroneous Erroneous(List<? extends JFXTree> errs) {
        JFXErroneous tree = new JFXErroneous(errs);
        tree.pos = pos;
        return tree;
    }

/* ***************************************************************************
 * Derived building blocks.
 ****************************************************************************/

    /** Create an identifier from a symbol.
     */
    public JFXIdent Ident(Symbol sym) {
        JFXIdent id = new JFXIdent(
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
    public JFXExpression Select(JFXExpression base, Symbol sym) {
        return new JFXSelect(base, sym.name, sym).setPos(pos).setType(sym.type);
    }

    /** Create an identifier that refers to the variable declared in given variable
     *  declaration.
     */
    public JFXIdent Ident(JFXVar param) {
        return Ident(param.sym);
    }

    /** Create a list of identifiers referring to the variables declared
     *  in given list of variable declarations.
     */
    public List<JFXExpression> Idents(List<JFXVar> params) {
        ListBuffer<JFXExpression> ids = new ListBuffer<JFXExpression>();
        for (List<JFXVar> l = params; l.nonEmpty(); l = l.tail)
            ids.append(Ident(l.head));
        return ids.toList();
    }

    /** Create a tree representing the script class of a given enclosing class.
     */
    public JavafxClassSymbol ScriptSymbol(Symbol sym) {
        JavafxClassSymbol owner = (JavafxClassSymbol)sym;
        
        if (owner.scriptSymbol == null) {
            Name scriptName = owner.name.append(defs.scriptClassSuffixName);
            owner.scriptSymbol = new JavafxClassSymbol(Flags.STATIC | Flags.PUBLIC, scriptName, owner);
            owner.scriptSymbol.type = new ClassType(Type.noType, List.<Type>nil(), owner.scriptSymbol);
        }
        
        return owner.scriptSymbol;
    }
    public JFXIdent Script(Symbol sym) {
        return Ident(ScriptSymbol(sym));
    }

    /** Create a tree representing `this', given its type.
     */
    public JavafxVarSymbol ThisSymbol(Type t) {
        JavafxClassSymbol owner = (JavafxClassSymbol)t.tsym;

        if (owner.thisSymbol == null) {
            long flags = FINAL | HASINIT | JavafxFlags.VARUSE_SPECIAL;
            owner.thisSymbol = new JavafxVarSymbol(types, names, flags, names._this, t, owner);
        }
        
        return owner.thisSymbol;
    }
    public JFXIdent This(Type t) {
        return Ident(ThisSymbol(t));
    }

    /** Create a tree representing `super', given its type and owner.
     */
    public JavafxVarSymbol SuperSymbol(Type t, TypeSymbol sym) {
        JavafxClassSymbol owner = (JavafxClassSymbol)sym;

        if (owner.superSymbol == null) {
            long flags = FINAL | HASINIT | JavafxFlags.VARUSE_SPECIAL;
            owner.superSymbol = new JavafxVarSymbol(types, names, flags, names._super, t, owner);
        }
        
        return owner.superSymbol;
    }
    public JFXIdent Super(Type t, TypeSymbol owner) {
        return Ident(SuperSymbol(t, owner));
    }

    /** Create a tree representing the script instance of the enclosing class.
     */
    public JavafxVarSymbol ScriptAccessSymbol(Symbol sym) {
        JavafxClassSymbol owner = (JavafxClassSymbol)sym;

        if (owner.scriptAccessSymbol == null) {
            Name scriptLevelAccessName = defs.scriptLevelAccessField(names, sym);
            JavafxClassSymbol script = ScriptSymbol(sym);
            long flags = FINAL | STATIC | PUBLIC | HASINIT | JavafxFlags.VARUSE_SPECIAL;
            owner.scriptAccessSymbol = new JavafxVarSymbol(types, names, flags, scriptLevelAccessName, script.type, owner);
        }
        
        return owner.scriptAccessSymbol;
    }
    public JFXIdent ScriptAccess(Symbol sym) {
        return Ident(ScriptAccessSymbol(sym));
    }
    
    /**
     * Create a method invocation from a method tree and a list of
     * argument trees.
     */
    public JFXFunctionInvocation App(JFXExpression meth, List<JFXExpression> args) {
        return Apply(null, meth, args).setType(meth.type.getReturnType());
    }

    /**
     * Create a no-arg method invocation from a method tree
     */
    public JFXFunctionInvocation App(JFXExpression meth) {
        return Apply(null, meth, List.<JFXExpression>nil()).setType(meth.type.getReturnType());
    }

    /** Create a tree representing given type.
     */
    public JFXExpression Type(Type t) {
        if (t == null) {
            return null;
        }
        JFXExpression tp;
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
                JFXExpression elem = Type(types.elemtype(t));
                elem = elem instanceof JFXType ? elem : TypeClass(elem, Cardinality.SINGLETON);
                tp = TypeArray((JFXType)elem);
                break;
            case CLASS:
                Type outer = t.getEnclosingType();
                tp = outer.tag == CLASS && t.tsym.owner.kind == TYP
                        ? Select(Type(outer), t.tsym)
                        : QualIdent(t.tsym);
                break;
            default:
                throw new AssertionError("unexpected type: " + t);
        }
        return tp.setType(t);
    }

    /** Create a list of trees representing given list of types.
     */
    public List<JFXExpression> Types(List<Type> ts) {
        ListBuffer<JFXExpression> typeList = new ListBuffer<JFXExpression>();
        for (List<Type> l = ts; l.nonEmpty(); l = l.tail)
            typeList.append(Type(l.head));
        return typeList.toList();
    }

    public JFXLiteral LiteralInteger(String text, int radix) {
        long longVal = Convert.string2long(text, radix);
        // For decimal, allow Integer negative numbers
        if ((radix==10)? (longVal <= Integer.MAX_VALUE && longVal >= Integer.MIN_VALUE) : ((longVal & ~0xFFFFFFFFL) == 0L))
            return Literal(TypeTags.INT, Integer.valueOf((int) longVal));
        else
            return Literal(TypeTags.LONG, Long.valueOf(longVal));
    }

    public JFXLiteral Literal(Object value) {
        JFXLiteral result = null;
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
    public JFXTypeCast TypeCast(Type type, JFXExpression expr) {
        return (JFXTypeCast)TypeCast(Type(type), expr).setType(type);
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

    public JFXClassDeclaration ClassDeclaration(JFXModifiers mods,
            Name name,
            List<JFXExpression> supertypes,
            List<JFXTree> declarations) {
        JFXClassDeclaration tree = new JFXClassDeclaration(mods,
                name,
                supertypes,
                declarations,
                null);
        tree.pos = pos;

        return tree;
    }

    public JFXBlock Block(long flags, List<JFXExpression> stats, JFXExpression value) {
        JFXBlock tree = new JFXBlock(flags, stats, value);
        tree.pos = pos;
        return tree;
    }

    public JFXBlock ErroneousBlock() {
        JFXErroneousBlock tree = new JFXErroneousBlock(List.<JFXTree>nil());
        tree.pos = pos;
        return tree;
    }
    public JFXBlock ErroneousBlock(List<? extends JFXTree> errs) {
        JFXErroneousBlock tree = new JFXErroneousBlock(errs);
        tree.pos = pos;
        return tree;
    }
        
    public JFXFunctionDefinition FunctionDefinition(
            JFXModifiers modifiers,
            Name name,
            JFXType restype,
            List<JFXVar> params,
            JFXBlock bodyExpression) {
        JFXFunctionDefinition tree = new JFXFunctionDefinition(
                modifiers,
                name,
                restype,
                params,
                bodyExpression);
        tree.operation.definition = tree;
        tree.pos = pos;
        return tree;
    }

    public JFXFunctionValue FunctionValue(
            JFXType restype,
             List<JFXVar> params,
            JFXBlock bodyExpression) {
        JFXFunctionValue tree = new JFXFunctionValue(
                restype,
                params,
                bodyExpression);
        tree.pos = pos;
        return tree;
    }

    public JFXInitDefinition InitDefinition(
            JFXBlock body) {
        JFXInitDefinition tree = new JFXInitDefinition(
                body);
        tree.pos = pos;
        return tree;
    }

    public JFXPostInitDefinition PostInitDefinition(JFXBlock body) {
        JFXPostInitDefinition tree = new JFXPostInitDefinition(body);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceEmpty EmptySequence() {
        JFXSequenceEmpty tree = new JFXSequenceEmpty();
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceRange RangeSequence(JFXExpression lower, JFXExpression upper, JFXExpression stepOrNull, boolean exclusive) {
        JFXSequenceRange tree = new JFXSequenceRange(lower, upper,  stepOrNull,  exclusive);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceExplicit ExplicitSequence(List<JFXExpression> items) {
        JFXSequenceExplicit tree = new JFXSequenceExplicit(items);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceIndexed SequenceIndexed(JFXExpression sequence, JFXExpression index) {
        JFXSequenceIndexed tree = new JFXSequenceIndexed(sequence, index);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceSlice SequenceSlice(JFXExpression sequence, JFXExpression firstIndex, JFXExpression lastIndex, int endKind) {
        JFXSequenceSlice tree = new JFXSequenceSlice(sequence, firstIndex,
                lastIndex, endKind);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceInsert SequenceInsert(JFXExpression sequence, JFXExpression element, JFXExpression position, boolean after) {
        JFXSequenceInsert tree = new JFXSequenceInsert(sequence, element, position, after);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceDelete SequenceDelete(JFXExpression sequence) {
        JFXSequenceDelete tree = new JFXSequenceDelete(sequence, null);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceDelete SequenceDelete(JFXExpression sequence, JFXExpression element) {
        JFXSequenceDelete tree = new JFXSequenceDelete(sequence, element);
        tree.pos = pos;
        return tree;
    }

    public JFXStringExpression StringExpression(List<JFXExpression> parts,
                                        String translationKey) {
        JFXStringExpression tree = new JFXStringExpression(parts, translationKey);
        tree.pos = pos;
        return tree;
    }

    public JFXInstanciate Instanciate(JavaFXKind kind, JFXExpression clazz, JFXClassDeclaration def, List<JFXExpression> args, List<JFXObjectLiteralPart> parts, List<JFXVar> localVars) {
        JFXInstanciate tree = new JFXInstanciate(kind, clazz, def, args, parts, localVars, null);
        tree.pos = pos;
        return tree;
    }

    public JFXInstanciate ObjectLiteral(JFXExpression ident,
            List<JFXTree> defs) {
        return Instanciate(JavaFXKind.INSTANTIATE_OBJECT_LITERAL,
                ident,
                List.<JFXExpression>nil(),
                defs);
    }

    public JFXInstanciate InstanciateNew(JFXExpression ident,
            List<JFXExpression> args) {
        return Instanciate(JavaFXKind.INSTANTIATE_NEW,
                ident,
                args != null? args : List.<JFXExpression>nil(),
                List.<JFXTree>nil());
    }

   public JFXInstanciate Instanciate(JavaFXKind kind, JFXExpression ident,
           List<JFXExpression> args,
           List<JFXTree> defs) {

       // Don't try and process object literals that have erroneous elements
       //
       if  (ident instanceof JFXErroneous) return null;

       ListBuffer<JFXObjectLiteralPart> partsBuffer = ListBuffer.lb();
       ListBuffer<JFXTree> defsBuffer = ListBuffer.lb();
       ListBuffer<JFXExpression> varsBuffer = ListBuffer.lb();
       boolean boundParts = false;
       if (defs != null) {
           for (JFXTree def : defs) {
               if (def instanceof JFXObjectLiteralPart) {
                   JFXObjectLiteralPart olp = (JFXObjectLiteralPart) def;
                   partsBuffer.append(olp);
                   boundParts |= olp.isExplicitlyBound();
               } else if (def instanceof JFXVar /* && ((JFXVar)def).isLocal()*/) {
                   // for now, at least, assume any var declaration inside an object literal is local
                   varsBuffer.append((JFXVar) def);
               } else {
                   defsBuffer.append(def);
               }
           }
       }
       JFXClassDeclaration klass = null;
       if (defsBuffer.size() > 0 || boundParts) {
           JFXExpression id = ident;
           while (id instanceof JFXSelect) id = ((JFXSelect)id).getExpression();
           Name cname = objectLiteralClassName(((JFXIdent)id).getName());
           long innerClassFlags = Flags.SYNTHETIC | Flags.FINAL; // to enable, change to Flags.FINAL
           
           klass = this.ClassDeclaration(this.Modifiers(innerClassFlags), cname, List.<JFXExpression>of(ident), defsBuffer.toList());
       }

       JFXInstanciate tree = new JFXInstanciate(kind, ident,
               klass,
               args==null? List.<JFXExpression>nil() : args,
               partsBuffer.toList(),
               List.convert(JFXVar.class, varsBuffer.toList()),
               null);
       tree.pos = pos;
       return tree;
   }

    public JFXObjectLiteralPart ObjectLiteralPart(
            Name attrName,
            JFXExpression expr,
            JavafxBindStatus bindStatus) {
        JFXObjectLiteralPart tree =
                new JFXObjectLiteralPart(attrName, expr, bindStatus, null);
        tree.pos = pos;
        return tree;
    }

    public JFXType  TypeAny(Cardinality cardinality) {
        JFXType tree = new JFXTypeAny(cardinality);
        tree.pos = pos;
        return tree;
    }
    
    public JFXType  ErroneousType() {
        JFXType tree = new JFXErroneousType(List.<JFXTree>nil());
        tree.pos = pos;
        return tree;
    }

    public JFXType  ErroneousType(List<? extends JFXTree> errs) {
        JFXType tree = new JFXErroneousType(errs);
        tree.pos = pos;
        return tree;
    }

    public JFXType  TypeUnknown() {
        JFXType tree = new JFXTypeUnknown();
        tree.pos = pos;
        return tree;
    }

    public JFXType  TypeClass(JFXExpression className,Cardinality cardinality) {
        return TypeClass(className, cardinality, null);
    }

    public JFXType  TypeClass(JFXExpression className,Cardinality cardinality, ClassSymbol sym) {
        JFXType tree = new JFXTypeClass(className, cardinality, sym);
        tree.pos = pos;
        return tree;
    }

    public JFXType TypeFunctional(List<JFXType> params,
            JFXType restype,
            Cardinality cardinality) {
        JFXType tree = new JFXTypeFunctional(params,
                restype,
                cardinality);
        tree.pos = pos;
        return tree;
    }

    public JFXType TypeArray(JFXType elementType) {
        JFXType tree = new JFXTypeArray(elementType);
        tree.pos = pos;
        return tree;
    }

    public JFXOverrideClassVar TriggerWrapper(JFXIdent expr, JFXOnReplace onReplace, JFXOnReplace onInvalidate) {
        JFXOverrideClassVar tree = new JFXOverrideClassVar(null, null, null, expr, null, null, onReplace, onInvalidate, null);
        tree.pos = pos;
        return tree;
    }
    
   public JFXOnReplace ErroneousOnReplace(List<? extends JFXTree> errs) {
        JFXOnReplace tree = new JFXErroneousOnReplace(errs, JFXOnReplace.Kind.ONREPLACE);
        tree.pos = pos;
        return tree;
    }

   public JFXOnReplace ErroneousOnInvalidate(List<? extends JFXTree> errs) {
        JFXOnReplace tree = new JFXErroneousOnReplace(errs, JFXOnReplace.Kind.ONINVALIDATE);
        tree.pos = pos;
        return tree;
    }

    public JFXOnReplace OnReplace(JFXVar oldValue, JFXBlock body) {
        JFXOnReplace tree = new JFXOnReplace(oldValue, body, JFXOnReplace.Kind.ONREPLACE);
        tree.pos = pos;
        return tree;
    }

     public JFXOnReplace OnReplace(JFXVar oldValue, JFXVar firstIndex,
             JFXVar lastIndex, JFXVar newElements, JFXBlock body) {
         return OnReplace(oldValue, firstIndex, lastIndex,
                 JFXSequenceSlice.END_INCLUSIVE, newElements, body);
    }

     public JFXOnReplace OnReplace(JFXVar oldValue, JFXVar firstIndex,
             JFXVar lastIndex, int endKind, JFXVar newElements, JFXBlock body) {
         JFXOnReplace tree = OnReplace(oldValue, firstIndex, lastIndex,
                 endKind, newElements, null, body);
        tree.pos = pos;
        return tree;
     }

     public JFXOnReplace OnReplace(JFXVar oldValue, JFXVar firstIndex,
             JFXVar lastIndex, int endKind, JFXVar newElements, JFXVar saveVar, JFXBlock body) {
         JFXOnReplace tree = new JFXOnReplace(oldValue, firstIndex, lastIndex,
                 endKind, newElements, saveVar, body, JFXOnReplace.Kind.ONREPLACE);
        tree.pos = pos;
        return tree;
     }

     public JFXOnReplace OnInvalidate(JFXBlock body) {
        JFXOnReplace tree = new JFXOnReplace(null, body, JFXOnReplace.Kind.ONINVALIDATE);
        tree.pos = pos;
        return tree;
    }

     public JFXVarInit VarInit(JFXVar var) {
         JFXVarInit tree = new JFXVarInit(var);
         tree.pos = (var==null) ? Position.NOPOS : var.pos;
         return tree;
     }

     public JFXVarRef VarRef(JFXExpression expr, JFXVarRef.RefKind kind) {
         JFXVarRef tree = new JFXVarRef(expr, kind);
         tree.pos = pos;
         return tree;
     }

    public JFXVar Var(Name name,
            JFXType type,
            JFXModifiers mods,
            JFXExpression initializer,
            JavafxBindStatus bindStatus,
            JFXOnReplace onReplace,
            JFXOnReplace onInvalidate) {
            JFXVar tree = new JFXVar(name, type,
               mods, initializer, bindStatus, onReplace, onInvalidate, null);
        tree.pos = pos;
        return tree;
    }
    public JFXOverrideClassVar OverrideClassVar(Name name, JFXType type, JFXModifiers mods, JFXIdent expr,
            JFXExpression initializer,
            JavafxBindStatus bindStatus,
            JFXOnReplace onReplace,
            JFXOnReplace onInvalidate) {
        JFXOverrideClassVar tree = new JFXOverrideClassVar(name, type, mods, expr, initializer,
                bindStatus, onReplace, onInvalidate, null);
        tree.pos = pos;
        return tree;
    }

    public JFXVar Param(Name name,
            JFXType type) {
        JFXVar tree = new JFXVar(name, type,
                Modifiers(Flags.PARAMETER), null, JavafxBindStatus.UNBOUND, null, null, null);
        tree.pos = pos;
        return tree;
    }

    public JFXForExpression ForExpression(
            List<JFXForExpressionInClause> inClauses,
            JFXExpression bodyExpr) {
        JFXForExpression tree = new JFXForExpression(JavaFXKind.FOR_EXPRESSION_FOR, inClauses, bodyExpr);
        tree.pos = pos;
        return tree;
    }

    public JFXForExpression Predicate(
            List<JFXForExpressionInClause> inClauses,
            JFXExpression bodyExpr) {
        JFXForExpression tree = new JFXForExpression(JavaFXKind.FOR_EXPRESSION_PREDICATE, inClauses, bodyExpr);
        tree.pos = pos;
        return tree;
    }

    public JFXForExpressionInClause InClause(
            JFXVar var,
            JFXExpression seqExpr,
            JFXExpression whereExpr) {
        JFXForExpressionInClause tree = new JFXForExpressionInClause(var, seqExpr, whereExpr);
        tree.pos = pos;
        return tree;
    }
    
    public JFXErroneousForExpressionInClause ErroneousInClause(List<? extends JFXTree> errs) {

        JFXErroneousForExpressionInClause tree = new JFXErroneousForExpressionInClause(errs);
        tree.pos = pos;
        return tree;
    }

    public JFXExpression Identifier(Name name) {
        String str = name.toString();
        if (str.indexOf('.') < 0 && str.indexOf('<') < 0) {
            return Ident(name);
        }
        return Identifier(str);
    }

    public JFXExpression Identifier(String str) {
        assert str.indexOf('<') < 0 : "attempt to parse a type with 'Identifier'.  Use TypeTree";
        JFXExpression tree = null;
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
                Select(tree, partName);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }

    public JFXInterpolateValue InterpolateValue(JFXExpression attr, JFXExpression v, JFXExpression interp) {
        JFXInterpolateValue tree = new JFXInterpolateValue(attr, v, interp);
        tree.pos = pos;
        return tree;
    }

    public JFXInvalidate Invalidate(JFXExpression var) {
        JFXInvalidate tree = new JFXInvalidate(var);
        tree.pos = pos;
        return tree;
    }
     
    public JFXIndexof Indexof (JFXIdent name) {
        JFXIndexof tree = new JFXIndexof(name);
        tree.pos = pos;
        return tree;
    }

    public JFXTimeLiteral TimeLiteral(String str) {
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
            // durecations such as 999999m will overflow an integer.
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
        JFXLiteral literal = Literal(timeVal);
        JFXTimeLiteral tree = new JFXTimeLiteral(literal, duration);
        tree.pos = pos;
        return tree;
    }

    public JFXTimeLiteral TimeLiteral(JFXLiteral literal, Duration duration) {
        JFXTimeLiteral tree = new JFXTimeLiteral(literal, duration);
        tree.pos = pos;
        return tree;
    }

    public JFXErroneousTimeLiteral ErroneousTimeLiteral() {
        JFXErroneousTimeLiteral tree = new JFXErroneousTimeLiteral(List.<JFXTree>nil());
        tree.pos = pos;

        return tree;
    }
    public JFXKeyFrameLiteral KeyFrameLiteral(JFXExpression start, List<JFXExpression> values, JFXExpression trigger) {
        JFXKeyFrameLiteral tree = new JFXKeyFrameLiteral(start, values, trigger);
        tree.pos = pos;
        return tree;
    }

    public JFXUnary Unary(JavafxTag opcode, JFXExpression arg) {
        JFXUnary tree = new JFXUnary(opcode, arg);
        tree.pos = pos;
        return tree;
    }

    private int syntheticClassNumber = 0;

    Name syntheticClassName(Name superclass, String infix) {
        return names.fromString(superclass.toString() + infix + ++syntheticClassNumber);
    }

    Name objectLiteralClassName(Name superclass) {
        return syntheticClassName(superclass, JavafxDefs.objectLiteralClassInfix);
    }

    /**
     * Clone of javac's JavafxTreeMaker.Script, minus the assertion check of defs types.
     */
    public JFXScript Script(JFXExpression pid,
                                      List<JFXTree> defs) {
        JFXScript tree = new JFXScript(pid, defs,
                                     null, null, null, null);
        tree.pos = pos;
        return tree;
    }

    public JFXExpression QualIdent(Symbol sym) {
        if (sym.kind ==Kinds.PCK && sym.owner == syms.rootPackage)
            return Ident(sym);
        return isUnqualifiable(sym)
            ? Ident(sym)
            : Select(QualIdent(sym.owner), sym);
    }
}
