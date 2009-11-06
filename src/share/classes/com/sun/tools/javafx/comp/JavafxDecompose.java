/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.comp;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.TypeTree.Cardinality;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.comp.JavafxTranslationSupport.NotYetImplementedException;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.ClassType;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.jvm.ClassReader;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;

/**
 * Decompose bind expressions into easily translated expressions
 *
 * @author Robert Field
 */
public class JavafxDecompose implements JavafxVisitor {
    protected static final Context.Key<JavafxDecompose> decomposeKey =
            new Context.Key<JavafxDecompose>();

    private JFXTree result;
    private JavafxBindStatus bindStatus = JavafxBindStatus.UNBOUND;
    private ListBuffer<JFXTree> lbVar;
    private int varCount = 0;
    private Symbol varOwner = null;
    private Symbol currentClass = null;
    private boolean inScriptLevel = true;

    protected final JavafxTreeMaker fxmake;
    protected final JavafxDefs defs;
    protected final Name.Table names;
    protected final JavafxResolve rs;
    protected final JavafxSymtab syms;
    protected final JavafxTypes types;
    protected final ClassReader reader;

    public static JavafxDecompose instance(Context context) {
        JavafxDecompose instance = context.get(decomposeKey);
        if (instance == null)
            instance = new JavafxDecompose(context);
        return instance;
    }

    JavafxDecompose(Context context) {
        context.put(decomposeKey, this);

        fxmake = JavafxTreeMaker.instance(context);
        names = Name.Table.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        rs = JavafxResolve.instance(context);
        defs = JavafxDefs.instance(context);
        reader = ClassReader.instance(context);
    }

    /**
     * External access: overwrite the top-level tree with the translated tree
     */
    public void decompose(JavafxEnv<JavafxAttrContext> attrEnv) {
        bindStatus = JavafxBindStatus.UNBOUND;
        lbVar = null;
        attrEnv.toplevel = decompose(attrEnv.toplevel);
        lbVar = null;
    }

    void TODO(String msg) {
        throw new NotYetImplementedException("Not yet implemented: " + msg);
    }

    @SuppressWarnings("unchecked")
    private <T extends JFXTree> T decompose(T tree) {
        if (tree == null)
            return null;
        tree.accept(this);
        result.type = tree.type;
        return (T)result;
    }

    private <T extends JFXTree> List<T> decompose(List<T> trees) {
        if (trees == null)
            return null;
        ListBuffer<T> lb = new ListBuffer<T>();
        for (T tree: trees)
            lb.append(decompose(tree));
        return lb.toList();
    }
    
    private boolean requiresShred(JFXExpression tree) {
        if (tree==null) {
            return false;
        }
        switch (tree.getFXTag()) {
            case APPLY:
            case SEQUENCE_EXPLICIT:
            case SEQUENCE_RANGE:
                return true;
        }
        return false;
    }

    private JFXExpression decomposeComponent(JFXExpression tree) {
        if (requiresShred(tree))
            return shred(tree);
        else
            return decompose(tree);
    }

    private List<JFXExpression> decomposeComponents(List<JFXExpression> trees) {
        if (trees == null)
            return null;
        ListBuffer<JFXExpression> lb = new ListBuffer<JFXExpression>();
        for (JFXExpression tree: trees)
            lb.append(decomposeComponent(tree));
        return lb.toList();
    }

    private JFXVar makeVar(DiagnosticPosition diagPos, String label, JFXExpression pose, JavafxBindStatus bindStatus, Type type) {
        Name vName = tempName(label);
        return makeVar(diagPos, vName, pose, bindStatus, type);
    }

    private JFXVar makeVar(DiagnosticPosition diagPos, Name vName, JFXExpression pose, JavafxBindStatus bindStatus, Type type) {
        long flags = JavafxFlags.SCRIPT_PRIVATE | (inScriptLevel ? Flags.STATIC | JavafxFlags.SCRIPT_LEVEL_SYNTH_STATIC : 0L);
        JFXModifiers mod = fxmake.at(diagPos).Modifiers(flags);
        JFXType fxType = fxmake.at(diagPos).TypeAny(Cardinality.ANY);
        JFXVar v = fxmake.at(diagPos).Var(vName, fxType, mod, pose, bindStatus, null, null);
        VarSymbol sym = new VarSymbol(flags, vName, type, varOwner);
        v.sym = sym;
        v.type = type;
        lbVar.append(v);
        return v;
    }

    private JFXVar shredVar(String label, JFXExpression pose, Type type) {
        Name tmpName = tempName(label);
        // If this shred var initialized with a call to a bound function?
        JFXVar ptrVar = makeTempBoundResultName(tmpName, pose);
        if (ptrVar != null) {
            return makeVar(pose.pos(), tmpName, id(ptrVar), JavafxBindStatus.UNIDIBIND, type);
        } else {
            return makeVar(pose.pos(), tmpName, pose, JavafxBindStatus.UNIDIBIND, type);
        }
    }

    private JFXExpression id(JFXVar v) {
        JFXIdent id = fxmake.at(v.pos).Ident(v.getName());
        id.sym = v.sym;
        id.type = v.type;
        return id;
    }

    /**
     * If we are in a bound expression, break this expression out into a separate synthetic bound variable.
     */
    private JFXExpression shred(JFXExpression tree, Type contextType) {
        if (tree == null) {
            return null;
        }
        JFXExpression pose = decompose(tree);
        if (bindStatus.isBound()) {
            Type varType = tree.type;
            if (tree.type == syms.botType && contextType != null) {
                // If the tree type is bottom, try to use contextType
                varType = contextType;
            }
            JFXVar v = shredVar("", pose, varType);
            return id(v);
        } else {
            return pose;
        }
    }

    private JFXExpression shred(JFXExpression tree) {
        return shred(tree, null);
    }

    private List<JFXExpression> shred(List<JFXExpression> trees, List<Type> paramTypes) {
        if (trees == null)
            return null;
        ListBuffer<JFXExpression> lb = new ListBuffer<JFXExpression>();
        Type paramType = paramTypes != null? paramTypes.head : null;
        for (JFXExpression tree: trees) {
            lb.append(shred(tree, paramType));
            if (paramTypes != null) {
                paramTypes = paramTypes.tail;
                paramType = paramTypes.head;
            }
        }
        return lb.toList();
    }

    private Name tempName(String label) {
        return names.fromString("$" + label + "$" + varCount++);
    }

    private Name tempBoundResultName(Name name) {
        return names.fromString(JavafxDefs.boundFunctionResult + name);
    }

    private JFXVar makeTempBoundResultName(Name varName, JFXExpression initExpr) {
        JFXVar ptrVar = null;
        if (initExpr instanceof JFXFunctionInvocation) {
            Symbol meth = JavafxTreeInfo.symbol(((JFXFunctionInvocation)initExpr).meth);
            if (meth != null && (meth.flags() & JavafxFlags.BOUND) != 0L) {
                Name tmpBoundResName = tempBoundResultName(varName);
                /*
                 * Introduce a Pointer synthetic variable which will be used to cache
                 * bound function's return value. The name of the sythetic Pointer
                 * variable is derived from the given varName.
                 */
                ptrVar = makeVar(initExpr.pos(), tmpBoundResName, initExpr, JavafxBindStatus.UNIDIBIND, syms.javafx_PointerType);
                ptrVar.sym.flags_field |= Flags.SYNTHETIC;
            }
        }
        return ptrVar;
    }
    
    Name syntheticClassName(Name superclass) {
        return names.fromString(superclass.toString() + "$anonD" + ++varCount);
    }

    ClassSymbol syntheticScriptClass(Symbol sym) {
        ClassSymbol cs = new ClassSymbol(0L, sym.name.append(defs.scriptClassSuffixName), sym);
        cs.type = new ClassType(Type.noType, List.<Type>nil(), cs);
        return cs;
    }

    MethodSymbol syntheticScriptMethod(Symbol staticsHolder, Symbol scriptClazz) {
        MethodSymbol msym = new MethodSymbol(Flags.STATIC, defs.scriptLevelAccessMethod(names, staticsHolder), Type.noType, scriptClazz.owner);
        msym.type = new MethodType(List.<Type>nil(), scriptClazz.type, List.<Type>nil(), syms.methodClass);
        return msym;
    }

    JFXExpression syntheticScriptMethodCall(Symbol sym) {
        Symbol cs = syntheticScriptClass(sym);
        Symbol msym = syntheticScriptMethod(sym, cs);
        JFXExpression  meth = fxmake.Apply(null, fxmake.Ident(msym), null);
        meth.type = cs.type;
        return meth;
    }

    private <T extends JFXTree> List<T> decomposeContainer(List<T> trees) {
        if (trees == null)
            return null;
        ListBuffer<T> lb = new ListBuffer<T>();
        for (T tree: trees)
            lb.append(decompose(tree));
        return lb.toList();
    }

    public void visitScript(JFXScript tree) {
        bindStatus = JavafxBindStatus.UNBOUND;
        tree.defs = decomposeContainer(tree.defs);
        result = tree;
    }

    public void visitImport(JFXImport tree) {
        result = tree;
    }

    public void visitSkip(JFXSkip tree) {
        result = tree;
    }

    public void visitWhileLoop(JFXWhileLoop tree) {
        JFXExpression cond = decompose(tree.cond);
        JFXExpression body = decompose(tree.body);
        result = fxmake.at(tree.pos).WhileLoop(cond, body);
    }

    public void visitTry(JFXTry tree) {
        JFXBlock body = decompose(tree.body);
        List<JFXCatch> catchers = decompose(tree.catchers);
        JFXBlock finalizer = decompose(tree.finalizer);
        result = fxmake.at(tree.pos).Try(body, catchers, finalizer);
    }

    public void visitCatch(JFXCatch tree) {
        JFXVar param = decompose(tree.param);
        JFXBlock body = decompose(tree.body);
        result = fxmake.at(tree.pos).Catch(param, body);
    }

    public void visitIfExpression(JFXIfExpression tree) {
        JFXExpression cond = decomposeComponent(tree.cond);
        JFXExpression truepart = decomposeComponent(tree.truepart);
        JFXExpression falsepart = decomposeComponent(tree.falsepart);
        result = fxmake.at(tree.pos).Conditional(cond, truepart, falsepart);
    }

    public void visitBreak(JFXBreak tree) {
        result = tree;
    }

    public void visitContinue(JFXContinue tree) {
        result = tree;
    }

    public void visitReturn(JFXReturn tree) {
        tree.expr = decompose(tree.expr);
        result = tree;
    }

    public void visitThrow(JFXThrow tree) {
        result = tree;
    }

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        JFXExpression fn = decompose(tree.meth);
        Symbol msym = JavafxTreeInfo.symbol(tree.meth);
        boolean magicPointerMakeFunction = (msym != null) &&
                    (msym.flags() & JavafxFlags.FUNC_POINTER_MAKE) != 0;
        /*
         * Do *not* shred select expression if it is passed to intrinsic function
         * Pointer.make(Object). Shred only the "selected" portion of it. If
         * we shred the whole select expr, then a temporary shred variable will
         * be used to create Pointer. That temporary is a bound variable and so
         * Pointer.set() on that would throw assign-to-bind-variable exception.
         */
        List<JFXExpression> args;
        if (magicPointerMakeFunction) {
            if (tree.args.head.getFXTag() == JavafxTag.SELECT) {
                JFXSelect select = (JFXSelect) tree.args.head;
                select.selected = shred(select.selected);
            }
            args = tree.args;
        } else {
            List<Type> paramTypes = tree.meth.type.getParameterTypes();
            Symbol sym = JavafxTreeInfo.symbolFor(tree.meth);
            if (sym instanceof MethodSymbol &&
                ((MethodSymbol)sym).isVarArgs()) {
                Type varargType = paramTypes.reverse().head;
                paramTypes = paramTypes.reverse().tail.reverse(); //remove last formal
                while (paramTypes.size() < tree.args.size()) {
                    paramTypes = paramTypes.append(types.elemtype(varargType));
                }
            }
            args = shred(tree.args, paramTypes);
        }
        result = fxmake.at(tree.pos).Apply(tree.typeargs, fn, args);
    }

    public void visitParens(JFXParens tree) {
        JFXExpression expr = decomposeComponent(tree.expr);
        result = fxmake.at(tree.pos).Parens(expr);
    }

    public void visitAssign(JFXAssign tree) {
        JFXExpression lhs = decompose(tree.lhs);
        JFXExpression rhs = decompose(tree.rhs);
        result = fxmake.at(tree.pos).Assign(lhs, rhs);
    }

    public void visitAssignop(JFXAssignOp tree) {
        JFXExpression lhs = decompose(tree.lhs);
        JFXExpression rhs = decompose(tree.rhs);
        JavafxTag tag = tree.getFXTag();
        JFXAssignOp res = fxmake.at(tree.pos).Assignop(tag, lhs, rhs);
        res.operator = tree.operator;
        result = res;
    }

    public void visitUnary(JFXUnary tree) {
        JFXExpression arg = decomposeComponent(tree.arg);
        JavafxTag tag = tree.getFXTag();
        JFXUnary res = fxmake.at(tree.pos).Unary(tag, arg);
        res.operator = tree.operator;
        result = res;
    }

    public void visitBinary(JFXBinary tree) {
        JFXExpression lhs = decomposeComponent(tree.lhs);
        JFXExpression rhs = decomposeComponent(tree.rhs);
        JavafxTag tag = tree.getFXTag();
        JFXBinary res = fxmake.at(tree.pos).Binary(tag, lhs, rhs);
        res.operator = tree.operator;
        result = res;
    }

    public void visitTypeCast(JFXTypeCast tree) {
        JFXTree clazz = decompose(tree.clazz);
        JFXExpression expr = decomposeComponent(tree.expr);
        result = fxmake.at(tree.pos).TypeCast(clazz, expr);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        JFXExpression expr = decomposeComponent(tree.expr);
        JFXTree clazz = decompose(tree.clazz);
        result = fxmake.at(tree.pos).TypeTest(expr, clazz);
    }

    public void visitSelect(JFXSelect tree) {
        JFXExpression selected;
        if (tree.sym.isStatic() &&
                tree.sym.kind == Kinds.VAR &&
                (tree.sym.flags() & JavafxFlags.IS_DEF) == 0 &&
                types.isJFXClass(tree.sym.owner) &&
                !currentClass.isSubClass(tree.sym.owner, types) &&
                bindStatus.isBound() &&
                tree.name != names._class) {
            //referenced is static script var - if in bind context need shredding
            JFXExpression meth = syntheticScriptMethodCall(tree.sym.owner);
            selected = shred(meth);
        }
        else if (tree.sym.isStatic() || !types.isJFXClass(tree.sym.owner)) {
            // Referenced is static, then selected is a class reference
            selected = decompose(tree.selected);
        } else {
            selected = shred(tree.selected);
        }
        setSelectResult(tree, selected, tree.sym);
    }

    void setSelectResult(JFXExpression tree, JFXExpression selector, Symbol sym) {
        DiagnosticPosition diagPos = tree.pos();
        JFXSelect res = fxmake.at(diagPos).Select(selector, sym.name);
        res.sym = sym;
        if (bindStatus.isBound() && types.isSequence(tree.type)) {
            // Add a size field to hold the previous size on selector switch
            JFXExpression zero = fxmake.at(diagPos).Literal(0);
            zero.type = syms.intType;
            JFXVar v = makeVar(diagPos, "size"+sym.name, zero, JavafxBindStatus.UNIDIBIND, syms.intType);
            v.sym.flags_field |= JavafxFlags.VARUSE_BARE_SYNTH;
            res.boundSize = v;
        }
        result = res;
    }

    public void visitIdent(JFXIdent tree) {
        if (tree.sym.kind == Kinds.VAR &&
                (tree.sym.flags() & JavafxFlags.IS_DEF) == 0 &&
                types.isJFXClass(tree.sym.owner) &&
                !(tree.getName().startsWith(defs.scriptLevelAccess_FXObjectMethodPrefixName)) &&
                bindStatus.isBound()) {
            if (tree.sym.isStatic()) {
                if (!inScriptLevel) {
                    //referenced is static script var - if in bind context need shredding
                    JFXExpression meth = syntheticScriptMethodCall(tree.sym.owner);
                    meth = shred(meth);
                    setSelectResult(tree, meth, tree.sym);
                    return;
                }
            } else if (tree.sym.name != names._this && tree.sym.name != names._super && !currentClass.isSubClass(tree.sym.owner, types)) {
                // instance field from outer class. We transform "foo" as "this.foo"
                // and shred "this" part so that local classes generated for local
                // binds will have proper dependency.
                JFXIdent thisExpr = fxmake.Ident(names._this);
                thisExpr.sym = new VarSymbol(0L, names._this, tree.sym.owner.type, tree.sym.owner.type.tsym);
                thisExpr.type = thisExpr.sym.type;
                setSelectResult(tree, shred(thisExpr), tree.sym);
                return;
            }
        }

        JFXIdent res = fxmake.at(tree.pos).Ident(tree.getName());
        res.sym = tree.sym;
        result = res;
    }

    public void visitLiteral(JFXLiteral tree) {
        result = tree;
    }

    public void visitModifiers(JFXModifiers tree) {
        result = tree;
    }

    public void visitErroneous(JFXErroneous tree) {
        result = tree;
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        JavafxBindStatus prevBindStatus = bindStatus;
        bindStatus = JavafxBindStatus.UNBOUND;
        Symbol prevVarOwner = varOwner;
        Symbol prevClass = currentClass;
        currentClass = varOwner = tree.sym;
        ListBuffer<JFXTree> prevLbVar = lbVar;
        lbVar = ListBuffer.<JFXTree>lb();
        for (JFXTree mem : tree.getMembers()) {
            lbVar.append(decompose(mem));
        }
        tree.setMembers(lbVar.toList());
        lbVar = prevLbVar;
        varOwner = prevVarOwner;
        currentClass = prevClass;
        result = tree;
        bindStatus = prevBindStatus;
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        boolean wasInScriptLevel = inScriptLevel;
        // Bound functions are handled by local variable bind facility.
        // The return value is transformed already in JavafxLocalToClass.
        // So, we are not changing bind state "inBind".
        inScriptLevel = tree.isStatic();
        Symbol prevVarOwner = varOwner;
        varOwner = null;
        JFXModifiers mods = tree.mods;
        Name name = tree.getName();
        JFXType restype = tree.getJFXReturnType();
        List<JFXVar> params = decompose(tree.getParams());
        JFXBlock bodyExpression = decompose(tree.getBodyExpression());
        JFXFunctionDefinition res = fxmake.at(tree.pos).FunctionDefinition(mods, name, restype, params, bodyExpression);
        res.sym = tree.sym;
        result = res;
        inScriptLevel = wasInScriptLevel;
        varOwner = prevVarOwner;
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.sym.isStatic();
        JFXBlock body = decompose(tree.body);
        JFXInitDefinition res = fxmake.at(tree.pos).InitDefinition(body);
        res.sym = tree.sym;
        result = res;
        inScriptLevel = wasInScriptLevel;
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.sym.isStatic();
        JFXBlock body = decompose(tree.body);
        JFXPostInitDefinition res = fxmake.at(tree.pos).PostInitDefinition(body);
        res.sym = tree.sym;
        result = res;
        inScriptLevel = wasInScriptLevel;
    }

    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = decomposeComponents(tree.parts);
        result = fxmake.at(tree.pos).StringExpression(parts, tree.translationKey);
    }

   public void visitInstanciate(JFXInstanciate tree) {
       JFXExpression klassExpr = tree.getIdentifier();
       List<JFXObjectLiteralPart> parts = tree.getParts();
       ListBuffer<JFXTree> newOverrides = ListBuffer.<JFXTree>lb();
       ListBuffer<JFXObjectLiteralPart> dparts = ListBuffer.<JFXObjectLiteralPart>lb();
       for (JFXObjectLiteralPart part : parts) {
           if (part.isBound()) {
               fxmake.at(part.pos());  // create at part position
               JFXIdent id = fxmake.Ident(part.name);
               id.sym = part.sym;
               id.type = part.sym.type;
               JFXOverrideClassVar ocv =
                  fxmake.OverrideClassVar(
                       part.name,
                       fxmake.Modifiers(part.sym.flags_field),
                       id,
                       part.getExpression(),
                       part.getBindStatus(),
                       null,
                       null);
               ocv.sym = (VarSymbol)part.sym;
               ocv.type = part.sym.type;
               newOverrides.append(ocv);
           } else {
               dparts.append(decompose(part));
           }
       }
       JFXClassDeclaration cdecl = tree.getClassBody();
       if (newOverrides.nonEmpty()) {
           cdecl.setMembers(cdecl.getMembers().appendList(newOverrides));
       }
       JFXClassDeclaration dcdel = decompose(cdecl);
       List<JFXExpression> dargs = decomposeComponents(tree.getArgs());
       
       JFXInstanciate res = fxmake.at(tree.pos).Instanciate(tree.getJavaFXKind(), klassExpr, dcdel, dargs, dparts.toList(), tree.getLocalvars());
       res.sym = tree.sym;
       res.constructor = tree.constructor;
       res.varDefinedByThis = tree.varDefinedByThis;
       result = res;
   }

    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        JavafxBindStatus prevBindStatus = bindStatus;

        // bind doesn't permiate object literals, but...
        // ... it does make it a dependent context
        bindStatus = tree.isBound()?
                            tree.getBindStatus() :
                            prevBindStatus == JavafxBindStatus.UNIDIBIND?
                                JavafxBindStatus.DEPENDENT :
                                prevBindStatus;
        JFXExpression expr = decompose(tree.getExpression());
        JFXObjectLiteralPart res = fxmake.at(tree.pos).ObjectLiteralPart(tree.name, expr, bindStatus);
        res.sym = tree.sym;
        result = res;
        bindStatus = prevBindStatus;
    }

    public void visitTypeAny(JFXTypeAny tree) {
        result = tree;
    }

    public void visitTypeClass(JFXTypeClass tree) {
        result = tree;
    }

    public void visitTypeFunctional(JFXTypeFunctional tree) {
        result = tree;
    }

    public void visitTypeArray(JFXTypeArray tree) {
        result = tree;
    }

    public void visitTypeUnknown(JFXTypeUnknown tree) {
        result = tree;
    }

    public void visitVarInit(JFXVarInit tree) {
        // Handled in visitVar
        result = tree;
    }

    public void visitVar(JFXVar tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.isStatic();
        JavafxBindStatus prevBindStatus = bindStatus;
        // for on-replace, decompose as unbound
        bindStatus = JavafxBindStatus.UNBOUND;
        JFXOnReplace onReplace = decompose(tree.getOnReplace());
        JFXOnReplace onInvalidate = decompose(tree.getOnInvalidate());
        // bound if was bind context or is bound variable
        bindStatus = tree.isBound()?
                            JavafxBindStatus.UNIDIBIND :
                            prevBindStatus;

        JFXExpression initExpr = decompose(tree.getInitializer());
        // Is this a bound var and initialized with a Pointer result
        // from a bound function call? If so, we need to create Pointer
        // synthetic var here.
        JFXVar ptrVar = bindStatus.isBound()? makeTempBoundResultName(tree.name, initExpr) : null;

        JFXVar res = fxmake.at(tree.pos).Var(
                    tree.name,
                    tree.getJFXType(),
                    tree.getModifiers(),
                    (ptrVar != null)? id(ptrVar) : initExpr,
                    tree.getBindStatus(),
                    onReplace,
                    onInvalidate);
        res.sym = tree.sym;
        res.type = tree.type;
        JFXVarInit vsi = tree.getVarInit();
        if (vsi != null) {
            // update the var in the var-init
            vsi.resetVar(res);
        }

        bindStatus = prevBindStatus;
        inScriptLevel = wasInScriptLevel;
        result = res;
    }

    public void visitOnReplace(JFXOnReplace tree) {
        JFXVar oldValue = tree.getOldValue();
        JFXVar firstIndex = tree.getFirstIndex();
        JFXVar lastIndex = tree.getLastIndex();
        JFXVar newElements = tree.getNewElements();
        JFXBlock body = decompose(tree.getBody());
        result = fxmake.at(tree.pos).OnReplace(oldValue, firstIndex, lastIndex, tree.getEndKind(), newElements, body);
    }

    public void visitBlockExpression(JFXBlock tree) {
        tree.stats = decomposeContainer(tree.stats);
        tree.value = decompose(tree.value);
        result = tree;
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        JavafxBindStatus prevBindStatus = bindStatus;
        bindStatus = JavafxBindStatus.UNBOUND;
        tree.bodyExpression = decompose(tree.bodyExpression);
        result = tree;
        bindStatus = prevBindStatus;
    }

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        result = tree;
    }

    private JFXVar synthVar(String label, JFXExpression tree, Type type) {
        if (tree == null) {
            return null;
        }
        JFXExpression expr = decompose(tree);

        fxmake.at(tree.pos()); // set position

        if (!types.isSameType(tree.type, type)) {
            // cast to desired type
            JFXIdent tp = (JFXIdent) fxmake.Type(type);
            tp.sym = type.tsym;
            expr = fxmake.TypeCast(tp, expr);
        }

        JFXVar v = shredVar(label, expr, type);
        v.sym.flags_field |= JavafxFlags.VARUSE_BARE_SYNTH;
        return v;
    }

    private JFXVar makeSizeVar(DiagnosticPosition diagPos) {
        JFXExpression dummy = fxmake.at(diagPos).Literal(-99);
        dummy.type = syms.intType;
        JFXVar v = makeVar(diagPos, "size", dummy, JavafxBindStatus.UNIDIBIND, syms.intType);
        return v;
    }

    /**
     * Add synthetic variables, and attach them to the reconstituted range.
     *    def range = bind [rb .. re step st]
     * adds:
     *
     * def lower = bind rb; // marked BARE_SYNTH
     * def upper = bind re; // marked BARE_SYNTH
     * def step = bind st; // marked BARE_SYNTH
     * def size = bind -99;
     */
    public void visitSequenceRange(JFXSequenceRange tree) {
        JFXExpression lower;
        JFXExpression upper;
        JFXExpression stepOrNull;
        if (bindStatus.isBound()) {
            Type elemType = types.elementType(tree.type);
            lower = synthVar("lower", tree.getLower(), elemType);
            upper = synthVar("upper", tree.getUpper(), elemType);
            stepOrNull = synthVar("step", tree.getStepOrNull(), elemType);
        } else {
            lower = decomposeComponent(tree.getLower());
            upper = decomposeComponent(tree.getUpper());
            stepOrNull = decomposeComponent(tree.getStepOrNull());
        }
        JFXSequenceRange res = fxmake.at(tree.pos).RangeSequence(lower, upper, stepOrNull, tree.isExclusive());
        res.type = tree.type;
        if (bindStatus.isBound()) {
            // now add a size temp var
            res.boundSizeVar = makeSizeVar(tree.pos());
        }
        result = res;
    }

    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        List<JFXExpression> items = null; // bound should not use items
        if (!bindStatus.isBound()) {
            items = decomposeComponents(tree.getItems());
        }
        JFXSequenceExplicit res = fxmake.at(tree.pos).ExplicitSequence(items);
        res.type = tree.type;
        if (bindStatus.isBound()) {
            // Generate bare synth vars for the items
            ListBuffer<JFXVar> vb = ListBuffer.lb();
            for (JFXExpression item : tree.getItems()) {
                vb.append(synthVar("item", item, item.type));
            }
            res.boundItemsVars = vb.toList();

            // now add a size temp var
            res.boundSizeVar = makeSizeVar(tree.pos());
        }
        result = res;
    }

    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        JFXExpression sequence = decomposeComponent(tree.getSequence());
        JFXExpression index = decomposeComponent(tree.getIndex());
        result = fxmake.at(tree.pos).SequenceIndexed(sequence, index);
    }

    public void visitSequenceSlice(JFXSequenceSlice tree) {
        JFXExpression sequence = decomposeComponent(tree.getSequence());
        JFXExpression firstIndex = decomposeComponent(tree.getFirstIndex());
        JFXExpression lastIndex = decomposeComponent(tree.getLastIndex());
        result = fxmake.at(tree.pos).SequenceSlice(sequence, firstIndex, lastIndex, tree.getEndKind());
    }

    public void visitSequenceInsert(JFXSequenceInsert tree) {
        JFXExpression sequence = decompose(tree.getSequence());
        JFXExpression element = decompose(tree.getElement());
        JFXExpression position = decompose(tree.getPosition());
        result = fxmake.at(tree.pos).SequenceInsert(sequence, element, position, tree.shouldInsertAfter());
    }

    public void visitSequenceDelete(JFXSequenceDelete tree) {
        JFXExpression sequence = decompose(tree.getSequence());
        JFXExpression element = decompose(tree.getElement());
        result = fxmake.at(tree.pos).SequenceDelete(sequence, element);
    }

    public void visitInvalidate(JFXInvalidate tree) {
        JFXExpression variable = decompose(tree.getVariable());
        result = fxmake.at(tree.pos).Invalidate(variable);
    }

    public void visitForExpression(JFXForExpression tree) {
        List<JFXForExpressionInClause> inClauses = decompose(tree.inClauses);
        JFXExpression bodyExpr = decompose(tree.bodyExpr);
        result = fxmake.at(tree.pos).ForExpression(inClauses, bodyExpr);
    }

    public void visitForExpressionInClause(JFXForExpressionInClause tree) {
        JFXVar var = tree.var;
        JFXExpression seqExpr = decompose(tree.seqExpr);
        JFXExpression whereExpr = decompose(tree.whereExpr);
        JFXForExpressionInClause res = fxmake.at(tree.pos).InClause(var, seqExpr, whereExpr);
        res.setIndexUsed(tree.getIndexUsed());
        result = res;
    }

    public void visitIndexof(JFXIndexof tree) {
        result = tree;
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        result = tree;
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.isStatic();
        JavafxBindStatus prevBindStatus = bindStatus;
        // on-replace is always unbound
        bindStatus = JavafxBindStatus.UNBOUND;
        JFXOnReplace onReplace = decompose(tree.getOnReplace());
        JFXOnReplace onInvalidate = decompose(tree.getOnInvalidate());
        // bound if was bind context or is bound variable
        bindStatus = tree.isBound()?
                            JavafxBindStatus.UNIDIBIND :
                            prevBindStatus;
        JFXExpression initializer = decompose(tree.getInitializer());
        JFXOverrideClassVar res = fxmake.at(tree.pos).OverrideClassVar(tree.getName(),
                tree.getModifiers(),
                tree.getId(),
                initializer,
                tree.getBindStatus(),
                onReplace,
                onInvalidate);
        res.sym = tree.sym;
        bindStatus = prevBindStatus;
        inScriptLevel = wasInScriptLevel;
        result = res;
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        JavafxBindStatus prevBindStatus = bindStatus;
        bindStatus = JavafxBindStatus.UNBOUND;
        JFXExpression attr = decompose(tree.attribute);
        JFXExpression funcValue = decompose(tree.funcValue);
        JFXExpression interpolation = decompose(tree.interpolation);
        // Note: funcValue takes the place of value
        JFXInterpolateValue res = fxmake.at(tree.pos).InterpolateValue(attr, funcValue, interpolation);
        res.sym = tree.sym;
        result = res;
        bindStatus = prevBindStatus;
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        JFXExpression start = decompose(tree.start);
        List<JFXExpression> values = decomposeComponents(tree.values);
        JFXExpression trigger = decompose(tree.trigger);
        result = fxmake.at(tree.pos).KeyFrameLiteral(start, values, trigger);
    }
}
