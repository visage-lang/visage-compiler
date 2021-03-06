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

package org.visage.tools.comp;

import org.visage.api.VisageBindStatus;
import org.visage.tools.code.VisageFlags;
import org.visage.tools.code.VisageSymtab;
import org.visage.tools.code.VisageTypes;
import org.visage.tools.code.VisageVarSymbol;
import org.visage.tools.comp.VisageTranslationSupport.NotYetImplementedException;
import org.visage.tools.tree.*;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.code.TypeTags;
import com.sun.tools.mjavac.jvm.ClassReader;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

/**
 * Decompose bind expressions into easily translated expressions
 *
 * @author Robert Field
 */
public class VisageDecompose implements VisageVisitor {
    protected static final Context.Key<VisageDecompose> decomposeKey =
            new Context.Key<VisageDecompose>();

    private VisageTree result;
    private VisageBindStatus bindStatus = VisageBindStatus.UNBOUND;
    private ListBuffer<VisageTree> lbVar;
    private Set<String> synthNames;
    private Symbol varOwner = null;
    private VisageVarSymbol currentVarSymbol;
    private Symbol currentClass = null;
    private boolean inScriptLevel = true;
    private VisageVarInit varInitContext = null;
    private boolean allowDebinding = false;

    // Map of shreded (Ident) selectors in bound select expressions.
    // Used in shred optimization. We use two maps - one for instance level
    // expressions and one for script level expressions.
    private Map<Symbol, VisageExpression> shrededSelectors;
    private Map<Symbol, VisageExpression> scriptShrededSelectors;

    protected final VisageTreeMaker visagemake;
    protected final VisagePreTranslationSupport preTrans;
    protected final VisageDefs defs;
    protected final Name.Table names;
    protected final VisageResolve rs;
    protected final VisageSymtab syms;
    protected final VisageTypes types;
    protected final ClassReader reader;
    protected final VisageOptimizationStatistics optStat;

    public static VisageDecompose instance(Context context) {
        VisageDecompose instance = context.get(decomposeKey);
        if (instance == null)
            instance = new VisageDecompose(context);
        return instance;
    }

    VisageDecompose(Context context) {
        context.put(decomposeKey, this);

        visagemake = VisageTreeMaker.instance(context);
        preTrans = VisagePreTranslationSupport.instance(context);
        names = Name.Table.instance(context);
        types = VisageTypes.instance(context);
        syms = (VisageSymtab)VisageSymtab.instance(context);
        rs = VisageResolve.instance(context);
        defs = VisageDefs.instance(context);
        reader = ClassReader.instance(context);
        optStat = VisageOptimizationStatistics.instance(context);
    }

    /**
     * External access: overwrite the top-level tree with the translated tree
     */
    public void decompose(VisageEnv<VisageAttrContext> attrEnv) {
        bindStatus = VisageBindStatus.UNBOUND;
        lbVar = null;
        synthNames = new HashSet<String>();
        attrEnv.toplevel = inlineShreddedVarInits(decompose(attrEnv.toplevel));
        synthNames = null;
        lbVar = null;
    }

    void TODO(String msg) {
        throw new NotYetImplementedException("Not yet implemented: " + msg);
    }

    @SuppressWarnings("unchecked")
    private <T extends VisageTree> T decompose(T tree) {
        if (tree == null)
            return null;
        boolean ib = bindStatus != VisageBindStatus.UNBOUND;
        if (ib) optStat.recordDecomposeEnter(tree.getClass());
        tree.accept(this);
        if (ib) optStat.recordDecomposeExit();
        result.type = tree.type;
        return (T)result;
    }

    private <T extends VisageTree> List<T> decompose(List<T> trees) {
        if (trees == null)
            return null;
        ListBuffer<T> lb = new ListBuffer<T>();
        for (T tree: trees)
            lb.append(decompose(tree));
        return lb.toList();
    }

    private boolean requiresShred(VisageExpression tree) {
        if (tree==null) {
            return false;
        }
        switch (tree.getVisageTag()) {
            case APPLY:
            case OBJECT_LITERAL:
            case SEQUENCE_EXPLICIT:
            case SEQUENCE_RANGE:
            case FOR_EXPRESSION:
                return true;
            case CONDEXPR:
                return types.isSequence(tree.type);
        }
        return false;
    }

    private VisageExpression decomposeComponent(VisageExpression tree) {
        if (requiresShred(tree))
            return shred(tree);
        else
            return decompose(tree);
    }

    private List<VisageExpression> decomposeComponents(List<VisageExpression> trees) {
        if (trees == null)
            return null;
        ListBuffer<VisageExpression> lb =  ListBuffer.lb();
        for (VisageExpression tree: trees)
            lb.append(decomposeComponent(tree));
        return lb.toList();
    }
    
    private <T extends VisageTree> T inlineShreddedVarInits(T tree) {
        new VisageTreeScanner() {
            private void unwindVarInit(VisageVarInit vi, ListBuffer<VisageExpression> elb) {
                for (VisageVarInit cvi : vi.getShreddedVarInits()) {
                    unwindVarInit(cvi, elb);
                }
                elb.append(vi);
            }

            private List<VisageExpression> process(VisageExpression expr) {
                scan(expr);
                if (expr instanceof VisageVarInit) {
                    ListBuffer<VisageExpression> elb = ListBuffer.lb();
                    unwindVarInit((VisageVarInit) expr, elb);
                    return elb.toList();
                } else {
                    return List.of(expr);
                }
            }

            @Override
            public void visitBlockExpression(VisageBlock tree) {
                ListBuffer<VisageExpression> elb = ListBuffer.lb();
                for (VisageExpression expr : tree.stats) {
                    elb.appendList(process(expr));
                }
                if (tree.value != null) {
                    List<VisageExpression> val = process(tree.value);
                    for (int i = 0; i < (val.length() - 1); ++i) {
                        elb.append(val.get(i));
                    }
                }
                tree.stats = elb.toList();
            }
        }.scan(tree);
        return tree;
    }

    private VisageVar makeVar(DiagnosticPosition diagPos, String label, VisageExpression initExpr, VisageBindStatus bindStatus, Type type) {
        VisageVar var = preTrans.SynthVar(diagPos, currentVarSymbol, label, initExpr, bindStatus, type, inScriptLevel, varOwner);
        lbVar.append(var);
        return var;
    }

    private VisageVar makeVar(DiagnosticPosition diagPos, Name vName, VisageExpression pose, VisageBindStatus bindStatus, Type type) {
        optStat.recordSynthVar("synth");
        long flags = VisageFlags.SCRIPT_PRIVATE | Flags.SYNTHETIC | (inScriptLevel ? Flags.STATIC | VisageFlags.SCRIPT_LEVEL_SYNTH_STATIC : 0L);
        VisageVar var = preTrans.Var(diagPos, flags, types.normalize(type), vName, bindStatus, pose, varOwner);
        varOwner.members().enter(var.sym);
        lbVar.append(var);
        return var;
    }

    private VisageVar shredVar(String label, VisageExpression pose, Type type) {
        return shredVar(label, pose, type, VisageBindStatus.UNIDIBIND);
    }
    
    private VisageVar shredVar(String label, VisageExpression pose, Type type, VisageBindStatus bindStatus) {
        optStat.recordShreds();
        Name tmpName = tempName(label);
        // If this shred var initialized with a call to a bound function?
        VisageVar ptrVar = makeTempBoundResultName(tmpName, pose);
        
        if (ptrVar != null) {
            return makeVar(pose.pos(), tmpName, id(ptrVar), bindStatus, type);
        } else {
            return makeVar(pose.pos(), label, pose, bindStatus, type);
        }
    }

    private VisageIdent id(VisageVar v) {
        VisageIdent id = visagemake.at(v.pos).Ident(v.getName());
        id.sym = v.sym;
        id.type = v.type;
        return id;
    }

    /**
     * If we are in a bound expression, break this expression out into a separate synthetic bound variable.
     */
    private VisageExpression shred(VisageExpression tree, Type contextType) {
        if (tree == null) {
            return null;
        }
        if (bindStatus.isBound()) {
            VisageVarInit prevVarInitContext = varInitContext;
            VisageVarInit ourVarInit = null;
            VisageBindStatus prevBindStatus = bindStatus;
            if (false & allowDebinding && preTrans.isImmutable(tree)) {
                bindStatus = VisageBindStatus.UNBOUND;
                if (prevVarInitContext != null) {
                    ourVarInit = visagemake.VarInit(null);
                    varInitContext = ourVarInit;
                }
            }
            VisageExpression pose = decompose(tree);
            Type varType = tree.type;
            if (tree.type == syms.botType && contextType != null) {
                // If the tree type is bottom, try to use contextType
                varType = contextType;
            }
            VisageVar v = shredVar("", pose, varType, bindStatus);
            if (ourVarInit != null) {
                ourVarInit.resetVar(v);
                prevVarInitContext.addShreddedVarInit(ourVarInit);
            }
            varInitContext = prevVarInitContext;
            VisageExpression shred = id(v);
            bindStatus = prevBindStatus;
            return shred;
        } else {
            return decompose(tree);
        }
    }

    private VisageExpression shred(VisageExpression tree) {
        return shred(tree, null);
    }

    private VisageExpression shredUnlessIdent(VisageExpression tree) {
        if (tree instanceof VisageIdent) {
            return decompose(tree);
        }
        return shred(tree);
    }

    private List<VisageExpression> shred(List<VisageExpression> trees, List<Type> paramTypes) {
        if (trees == null)
            return null;
        ListBuffer<VisageExpression> lb = new ListBuffer<VisageExpression>();
        Type paramType = paramTypes != null? paramTypes.head : null;
        for (VisageExpression tree: trees) {
            if (false/*disable-VSGC-4079*/ && tree != null && preTrans.isImmutable(tree)) {
                lb.append(tree);
            } else {
                lb.append(shred(tree, paramType));
            }
            if (paramTypes != null) {
                paramTypes = paramTypes.tail;
                paramType = paramTypes.head;
            }
        }
        return lb.toList();
    }

    private Name tempName(String label) {
        String name = currentVarSymbol != null ? currentVarSymbol.toString() : "";
        name += "$" + label + "$";
        if (synthNames.contains(name)) {
            for (int i = 0; true; i++) {
                String numbered = name + i;
                if (!synthNames.contains(numbered)) {
                    name = numbered;
                    break;
                }
            }
        }

        // name += defs.internalNameMarker;
        synthNames.add(name);

        return names.fromString(name);
    }

    private Name tempBoundResultName(Name name) {
        return names.fromString(VisageDefs.boundFunctionResult + name);
    }

    //TODO: clean-up this whole mess
    private boolean isBoundFunctionResult(VisageExpression initExpr) {
        if (initExpr instanceof VisageFunctionInvocation) {
            Symbol meth = VisageTreeInfo.symbol(((VisageFunctionInvocation)initExpr).meth);
            return meth != null && (meth.flags() & VisageFlags.BOUND) != 0L;
        } else {
            return false;
        }
    }

    private VisageVar makeTempBoundResultName(Name varName, VisageExpression initExpr) {
        VisageVar ptrVar = null;
        if (isBoundFunctionResult(initExpr)) {
                Name tmpBoundResName = tempBoundResultName(varName);
                /*
                 * Introduce a Pointer synthetic variable which will be used to cache
                 * bound function's return value. The name of the sythetic Pointer
                 * variable is derived from the given varName.
                 */
                ptrVar = makeVar(initExpr.pos(), tmpBoundResName, initExpr, VisageBindStatus.UNIDIBIND, syms.visage_PointerType);
                ptrVar.sym.flags_field |= Flags.SYNTHETIC | VisageFlags.VARUSE_BIND_ACCESS;
        }
        return ptrVar;
    }

    private <T extends VisageTree> List<T> decomposeContainer(List<T> trees) {
        if (trees == null)
            return null;
        ListBuffer<T> lb = new ListBuffer<T>();
        for (T tree: trees)
            lb.append(decompose(tree));
        return lb.toList();
    }

    public void visitScript(VisageScript tree) {
        bindStatus = VisageBindStatus.UNBOUND;
        tree.defs = decomposeContainer(tree.defs);
        result = tree;
    }

    public void visitImport(VisageImport tree) {
        result = tree;
    }

    public void visitSkip(VisageSkip tree) {
        result = tree;
    }

    public void visitWhileLoop(VisageWhileLoop tree) {
        VisageExpression cond = decompose(tree.cond);
        VisageExpression body = decompose(tree.body);
        result = visagemake.at(tree.pos).WhileLoop(cond, body);
    }

    public void visitTry(VisageTry tree) {
        VisageBlock body = decompose(tree.body);
        List<VisageCatch> catchers = decompose(tree.catchers);
        VisageBlock finalizer = decompose(tree.finalizer);
        result = visagemake.at(tree.pos).Try(body, catchers, finalizer);
    }

    public void visitCatch(VisageCatch tree) {
        VisageVar param = decompose(tree.param);
        VisageBlock body = decompose(tree.body);
        result = visagemake.at(tree.pos).Catch(param, body);
    }

    public void visitIfExpression(VisageIfExpression tree) {
        VisageExpression cond = decomposeComponent(tree.cond);
        VisageExpression truepart = decomposeComponent(tree.truepart);
        VisageExpression falsepart = decomposeComponent(tree.falsepart);
        VisageIfExpression res = visagemake.at(tree.pos).Conditional(cond, truepart, falsepart);
        if (bindStatus.isBound() && types.isSequence(tree.type)) {
            res.boundCondVar = synthVar(defs.condNamePrefix(), cond, cond.type, false);
            res.boundThenVar = synthVar(defs.thenNamePrefix(), truepart, truepart.type, false);
            res.boundElseVar = synthVar(defs.elseNamePrefix(), falsepart, falsepart.type, false);
            // Add a size field to hold the previous size on condition switch
            VisageVar v = makeSizeVar(tree.pos(), VisageDefs.UNDEFINED_MARKER_INT);
            res.boundSizeVar = v;
        }
        result = res;
    }

    public void visitBreak(VisageBreak tree) {
        if (tree.nonLocalBreak) {
            // A non-local break gets turned into an exception
            VisageIdent nonLocalExceptionClass = visagemake.Ident(names.fromString(VisageDefs.cNonLocalBreakException));
            nonLocalExceptionClass.sym = syms.visage_NonLocalBreakExceptionType.tsym;
            nonLocalExceptionClass.type = syms.visage_NonLocalBreakExceptionType;
            VisageInstanciate expInst = visagemake.InstanciateNew(nonLocalExceptionClass, List.<VisageExpression>nil());
            expInst.sym = (ClassSymbol)syms.visage_NonLocalBreakExceptionType.tsym;
            expInst.type = syms.visage_NonLocalBreakExceptionType;
            result = visagemake.Throw(expInst).setType(syms.unreachableType);
        } else {
            result = tree;
        }
    }

    public void visitContinue(VisageContinue tree) {
        if (tree.nonLocalContinue) {
            // A non-local continue gets turned into an exception
            VisageIdent nonLocalExceptionClass = visagemake.Ident(names.fromString(VisageDefs.cNonLocalContinueException));
            nonLocalExceptionClass.sym = syms.visage_NonLocalContinueExceptionType.tsym;
            nonLocalExceptionClass.type = syms.visage_NonLocalContinueExceptionType;
            VisageInstanciate expInst = visagemake.InstanciateNew(nonLocalExceptionClass, List.<VisageExpression>nil());
            expInst.sym = (ClassSymbol)syms.visage_NonLocalContinueExceptionType.tsym;
            expInst.type = syms.visage_NonLocalContinueExceptionType;
            result = visagemake.Throw(expInst).setType(syms.unreachableType);
        } else {
            result = tree;
        }
    }

    public void visitReturn(VisageReturn tree) {
        tree.expr = decompose(tree.expr);
        if (tree.nonLocalReturn) {
            // A non-local return gets turned into an exception
            VisageIdent nonLocalExceptionClass = visagemake.Ident(names.fromString(VisageDefs.cNonLocalReturnException));
            nonLocalExceptionClass.sym = syms.visage_NonLocalReturnExceptionType.tsym;
            nonLocalExceptionClass.type = syms.visage_NonLocalReturnExceptionType;
            List<VisageExpression> valueArg = tree.expr==null? List.<VisageExpression>nil() : List.of(tree.expr);
            VisageInstanciate expInst = visagemake.InstanciateNew(
                    nonLocalExceptionClass,
                    valueArg);
            expInst.sym = (ClassSymbol)syms.visage_NonLocalReturnExceptionType.tsym;
            expInst.type = syms.visage_NonLocalReturnExceptionType;
            result = visagemake.Throw(expInst);
        } else {
            result = tree;
        }
    }

    public void visitThrow(VisageThrow tree) {
        result = tree;
    }

    public void visitFunctionInvocation(VisageFunctionInvocation tree) {
        VisageExpression fn = decompose(tree.meth);
        Symbol msym = VisageTreeInfo.symbol(tree.meth);
        /*
         * Do *not* shred select expression if it is passed to intrinsic function
         * Pointer.make(Object). Shred only the "selected" portion of it. If
         * we shred the whole select expr, then a temporary shred variable will
         * be used to create Pointer. That temporary is a bound variable and so
         * Pointer.set() on that would throw assign-to-bind-variable exception.
         */
        List<VisageExpression> args;
        if (types.isSyntheticPointerFunction(msym)) {
            VisageVarRef varRef = (VisageVarRef)tree.args.head;
            if (varRef.getReceiver() != null) {
                varRef.setReceiver(shred(varRef.getReceiver()));
            }
            args = tree.args;
        } else {
            List<Type> paramTypes = tree.meth.type.getParameterTypes();
            Symbol sym = VisageTreeInfo.symbolFor(tree.meth);
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
        VisageExpression res = visagemake.at(tree.pos).Apply(tree.typeargs, fn, args);
        res.type = tree.type;
        if (bindStatus.isBound() && types.isSequence(tree.type) && !isBoundFunctionResult(tree)) {
            VisageVar v = shredVar(defs.functionResultNamePrefix(), res, tree.type);
            VisageVar sz = makeSizeVar(v.pos(), VisageDefs.UNDEFINED_MARKER_INT);
            res = visagemake.IdentSequenceProxy(v.name, v.sym, sz.sym);
        }
        result = res;
    }

    public void visitParens(VisageParens tree) {
        VisageExpression expr = decomposeComponent(tree.expr);
        result = visagemake.at(tree.pos).Parens(expr);
    }

    public void visitAssign(VisageAssign tree) {
        VisageExpression lhs = decompose(tree.lhs);
        VisageExpression rhs = decompose(tree.rhs);
        result = visagemake.at(tree.pos).Assign(lhs, rhs);
    }

    public void visitAssignop(VisageAssignOp tree) {
        VisageExpression lhs = decompose(tree.lhs);
        VisageExpression rhs = decompose(tree.rhs);
        VisageTag tag = tree.getVisageTag();
        VisageAssignOp res = visagemake.at(tree.pos).Assignop(tag, lhs, rhs);
        res.operator = tree.operator;
        result = res;
    }

    public void visitUnary(VisageUnary tree) {
        VisageTag tag = tree.getVisageTag();
        VisageExpression arg = tag == VisageTag.REVERSE ||
                            tag == VisageTag.SIZEOF ?
            shredUnlessIdent(tree.arg) :
            decomposeComponent(tree.arg);
        VisageUnary res = visagemake.at(tree.pos).Unary(tag, arg);
        res.operator = tree.operator;
        result = res;
    }

    public void visitBinary(VisageBinary tree) {
        VisageTag tag = tree.getVisageTag();
        boolean cutOff = tag==VisageTag.AND || tag==VisageTag.OR;
        VisageExpression lhs = decomposeComponent(tree.lhs);
        VisageExpression rhs = cutOff?
            shredUnlessIdent(tree.rhs) :  // If cut-off operation, preface code must be evaluated separately
            decomposeComponent(tree.rhs);
        VisageBinary res = visagemake.at(tree.pos).Binary(tag, lhs, rhs);
        res.operator = tree.operator;
        result = res;
    }

    public void visitTypeCast(VisageTypeCast tree) {
        boolean isBoundSequence = bindStatus.isBound() && types.isSequence(tree.type);
        boolean isCastingArray = types.isArray(tree.expr.type);
        VisageTree clazz = decompose(tree.clazz);
        VisageExpression expr = isBoundSequence?
            isCastingArray?
                shred(tree.expr) : // can't smash invalidation logic of user var
                shredUnlessIdent(tree.expr) :
            decomposeComponent(tree.expr);
        VisageTypeCast res = visagemake.at(tree.pos).TypeCast(clazz, expr);
        if (isBoundSequence && isCastingArray) {
            // Add a size field to hold the previous size of nativearray
            VisageVar v = makeSizeVar(tree.pos(), 0);
            res.boundArraySizeSym = v.sym;
        }
        result = res;
    }

    public void visitInstanceOf(VisageInstanceOf tree) {
        VisageExpression expr = decomposeComponent(tree.expr);
        VisageTree clazz = decompose(tree.clazz);
        result = visagemake.at(tree.pos).TypeTest(expr, clazz);
    }

    public void visitSelect(VisageSelect tree) {
        DiagnosticPosition diagPos = tree.pos();
        Symbol sym = tree.sym;
        Symbol selectSym = VisageTreeInfo.symbolFor(tree.selected);
        if (selectSym != null
                && ((selectSym.kind == Kinds.TYP && sym.kind != Kinds.MTH)
                || selectSym.name == names._this)) {
            // Select is just via "this" -- make it a simple Ident
            //TODO: move this to lower
            VisageIdent res = visagemake.at(diagPos).Ident(sym.name);
            res.sym = sym;
            result = res;
        } else {
            VisageExpression selected;
            if ((selectSym != null && (selectSym.kind == Kinds.TYP || selectSym.name == names._super || selectSym.name == names._class))) {
                // Referenced is static, or qualified super access
                // then selected is a class reference
                selected = decompose(tree.selected);
            } else {
                VisageBindStatus oldBindStatus = bindStatus;
                if (bindStatus == VisageBindStatus.BIDIBIND) bindStatus = VisageBindStatus.UNIDIBIND;

                /**
                 * Avoding shreding as an optimization: if the select expression's selected part
                 * is a VisageIdent and that identifier is an instance var of current class, then we
                 * don't have to shred it.
                 *
                 * Example:
                 *
                 * class Person {
                 *     var name : String;
                 *     var age: Integer;
                 * }
                 *
                 * class Test {
                 *     var p : Person;
                 *     var name = bind p.name; // instance var "p" in bind-select
                 *     var age = bind p.age;   // same instance var "p" in bind-select
                 * }
                 *
                 * In this case we can avoid shreding and generating two synthetic variables for
                 * bind select expressions p.name, p.age.
                 *
                 * Special cases:
                 * 
                 *     (1) sequences are always shreded
                 *     (2) non-variable access (eg. select expression selects method)
                 *
                 * TODO: for some reason this optimization does not work if the same selected part is
                 * used by a unidirectional and bidirectional bind expressions. For now, filtering out
                 * bidirectional cases. We need to revisit that mystery. Also. I've to oldBindStatus
                 * because bindStatus has been set to UNIDIBIND in the previous statement.
                 */
                if (oldBindStatus == VisageBindStatus.UNIDIBIND &&
                    tree.selected instanceof VisageIdent &&
                    !types.isSequence(tree.type) &&
                    sym instanceof VarSymbol) {
                    if (selectSym.owner == currentClass && !(selectSym.isStatic() ^ inScriptLevel)) {
                        selected = tree.selected;
                    } else {
                        Map<Symbol, VisageExpression> shredMap = inScriptLevel? scriptShrededSelectors : shrededSelectors;
                        if (shredMap.containsKey(selectSym)) {
                            selected = shredMap.get(selectSym);
                        } else {
                            selected = shred(tree.selected);
                            shredMap.put(selectSym, selected);
                        }
                    }
                } else {
                    selected = shred(tree.selected);
                }
                bindStatus = oldBindStatus;
            }
            VisageSelect res = visagemake.at(diagPos).Select(selected, sym.name, tree.nullCheck);
            res.sym = sym;
            if (bindStatus.isBound() && types.isSequence(tree.type)) {
                // Add a size field to hold the previous size on selector switch
                VisageVar v = makeSizeVar(diagPos, 0);
                res.boundSize = v;
            }
            result = res;
        }
    }

    public void visitIdent(VisageIdent tree) {
        VisageIdent res = visagemake.at(tree.pos).Ident(tree.getName());
        res.sym = tree.sym;
        result = res;
    }

    public void visitLiteral(VisageLiteral tree) {
        result = tree;
    }

    public void visitModifiers(VisageModifiers tree) {
        result = tree;
    }

    public void visitErroneous(VisageErroneous tree) {
        result = tree;
    }

    public void visitClassDeclaration(VisageClassDeclaration tree) {
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = VisageBindStatus.UNBOUND;
        Symbol prevVarOwner = varOwner;
        Symbol prevClass = currentClass;
        Map<Symbol, VisageExpression> prevShredExprs = shrededSelectors;
        Map<Symbol, VisageExpression> prevScriptShredExprs = scriptShrededSelectors;
        shrededSelectors = new HashMap<Symbol, VisageExpression>();
        scriptShrededSelectors = new HashMap<Symbol, VisageExpression>();
        currentClass = varOwner = tree.sym;
        ListBuffer<VisageTree> prevLbVar = lbVar;
        lbVar = ListBuffer.<VisageTree>lb();
        for (VisageTree mem : tree.getMembers()) {
            lbVar.append(decompose(mem));
        }
        tree.setMembers(lbVar.toList());
        lbVar = prevLbVar;
        varOwner = prevVarOwner;
        currentClass = prevClass;
        shrededSelectors = prevShredExprs;
        scriptShrededSelectors = prevScriptShredExprs;
        result = tree;
        bindStatus = prevBindStatus;
    }

    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
        boolean wasInScriptLevel = inScriptLevel;
        // Bound functions are handled by local variable bind facility.
        // The return value is transformed already in VisageLocalToClass.
        // So, we are not changing bind state "inBind".
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = VisageBindStatus.UNBOUND;
        inScriptLevel = tree.isStatic();
        Symbol prevVarOwner = varOwner;
        varOwner = null;
        VisageModifiers mods = tree.mods;
        Name name = tree.getName();
        VisageType restype = tree.getVisageReturnType();
        List<VisageVar> params = decompose(tree.getParams());
        VisageBlock bodyExpression = decompose(tree.getBodyExpression());
        VisageFunctionDefinition res = visagemake.at(tree.pos).FunctionDefinition(mods, name, restype, params, bodyExpression);
        res.sym = tree.sym;
        result = res;
        bindStatus = prevBindStatus;
        inScriptLevel = wasInScriptLevel;
        varOwner = prevVarOwner;
    }

    public void visitInitDefinition(VisageInitDefinition tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.sym.isStatic();
        VisageBlock body = decompose(tree.body);
        VisageInitDefinition res = visagemake.at(tree.pos).InitDefinition(body);
        res.sym = tree.sym;
        result = res;
        inScriptLevel = wasInScriptLevel;
    }

    public void visitPostInitDefinition(VisagePostInitDefinition tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.sym.isStatic();
        VisageBlock body = decompose(tree.body);
        VisagePostInitDefinition res = visagemake.at(tree.pos).PostInitDefinition(body);
        res.sym = tree.sym;
        result = res;
        inScriptLevel = wasInScriptLevel;
    }

    public void visitStringExpression(VisageStringExpression tree) {
        List<VisageExpression> parts = decomposeComponents(tree.parts);
        result = visagemake.at(tree.pos).StringExpression(parts, tree.translationKey);
    }

   public void visitInstanciate(VisageInstanciate tree) {
       VisageExpression klassExpr = tree.getIdentifier();
       List<VisageObjectLiteralPart> dparts = decompose(tree.getParts());
       VisageClassDeclaration dcdel = decompose(tree.getClassBody());
       List<VisageExpression> dargs = decomposeComponents(tree.getArgs());

       VisageInstanciate res = visagemake.at(tree.pos).Instanciate(tree.getVisageKind(), klassExpr, dcdel, dargs, dparts, tree.getLocalvars());
       res.sym = tree.sym;
       res.constructor = tree.constructor;
       res.varDefinedByThis = tree.varDefinedByThis;

       long anonTestFlags = Flags.SYNTHETIC | Flags.FINAL;
       if (dcdel != null && (dcdel.sym.flags_field & anonTestFlags) == anonTestFlags) {
           ListBuffer<VisageVarSymbol> objInitSyms = ListBuffer.lb();
           for (VisageObjectLiteralPart olp : dparts) {
              objInitSyms.append((VisageVarSymbol)olp.sym);
           }

           if (objInitSyms.size() > 1) {
              dcdel.setObjInitSyms(objInitSyms.toList());
           }
       }

       result = res;
   }

    public void visitObjectLiteralPart(VisageObjectLiteralPart tree) {
        VisageVarSymbol prevVarSymbol = currentVarSymbol;
        currentVarSymbol = (VisageVarSymbol)tree.sym;
        if (tree.isExplicitlyBound())
            throw new AssertionError("bound parts should have been converted to overrides");
        VisageExpression expr = shred(tree.getExpression(), tree.sym.type);
        VisageObjectLiteralPart res = visagemake.at(tree.pos).ObjectLiteralPart(tree.name, expr, tree.getExplicitBindStatus());
        res.markBound(bindStatus);
        res.sym = tree.sym;
        currentVarSymbol = prevVarSymbol;
        result = res;
    }

    public void visitTypeAny(VisageTypeAny tree) {
        result = tree;
    }

    public void visitTypeClass(VisageTypeClass tree) {
        result = tree;
    }

    public void visitTypeFunctional(VisageTypeFunctional tree) {
        result = tree;
    }

    public void visitTypeArray(VisageTypeArray tree) {
        result = tree;
    }

    public void visitTypeUnknown(VisageTypeUnknown tree) {
        result = tree;
    }

    public void visitVarInit(VisageVarInit tree) {
        // Handled in visitVar
        result = tree;
    }

    public void visitVarRef(VisageVarRef tree) {
        result = tree;
    }

    public void visitVar(VisageVar tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.isStatic();
        VisageVarSymbol prevVarSymbol = currentVarSymbol;
        currentVarSymbol = tree.sym;

        VisageBindStatus prevBindStatus = bindStatus;
        // for on-replace, decompose as unbound
        bindStatus = VisageBindStatus.UNBOUND;
        VisageOnReplace onReplace = decompose(tree.getOnReplace());
        VisageOnReplace onInvalidate = decompose(tree.getOnInvalidate());
        // bound if was bind context or is bound variable
        bindStatus = tree.isBound()?
                            tree.getBindStatus() :
                            prevBindStatus;

        VisageVarInit vsi = tree.getVarInit();
        VisageVarInit prevVarInitContext = varInitContext;
        boolean prevAllowDebinding = allowDebinding;
        varInitContext = vsi;
        allowDebinding = !tree.sym.hasForwardReference(); // No debinding for forward referenced var

        VisageExpression initExpr = decompose(tree.getInitializer());
        // Is this a bound var and initialized with a Pointer result
        // from a bound function call? If so, we need to create Pointer
        // synthetic var here.
        VisageVar ptrVar = bindStatus.isBound()? makeTempBoundResultName(tree.name, initExpr) : null;

        VisageVar res = visagemake.at(tree.pos).Var(
                    tree.name,
                    tree.getVisageType(),
                    tree.getModifiers(),
                    (ptrVar != null)? id(ptrVar) : initExpr,
                    tree.getBindStatus(),
                    onReplace,
                    onInvalidate);
        res.sym = tree.sym;
        res.type = tree.type;
        if (vsi != null) {
            // update the var in the var-init
            vsi.resetVar(res);
        }

        allowDebinding = prevAllowDebinding;
        varInitContext = prevVarInitContext;
        bindStatus = prevBindStatus;
        inScriptLevel = wasInScriptLevel;
        currentVarSymbol = prevVarSymbol;
        result = res;
    }

    public void visitOnReplace(VisageOnReplace tree) {
        VisageVar oldValue = tree.getOldValue();
        VisageVar firstIndex = tree.getFirstIndex();
        VisageVar lastIndex = tree.getLastIndex();
        VisageVar newElements = tree.getNewElements();
        VisageVar saveVar = null;
        if (oldValue != null && types.isSequence(oldValue.type)) {
            VisageVarSymbol sym = oldValue.sym;
            // FIXME OPTIMIZATION:
            // if sym.isUsedInSizeof() && ! sym.isUsedOutsideSizeof())
            // then we can save just the old size in the save-var.
            // We also have to translate 'sizeof oldVar' to the saved size.
            if (sym.isUsedInSizeof() || sym.isUsedOutsideSizeof())
                saveVar = makeSaveVar(tree.pos(), oldValue.type);
        }
        VisageBlock body = decompose(tree.getBody());
        result = visagemake.at(tree.pos).OnReplace(oldValue, firstIndex, lastIndex, tree.getEndKind(), newElements, saveVar, body);
    }

    /**
     * Block-expressions
     *
     * For bound sequence block-expressions, get the initialization right by
     *   The block vars have already been made into VarInits.
     *   Making block value into a synthetic var, and add a VarInit for it
     *   to the block vars
     */
    public void visitBlockExpression(VisageBlock tree) {
        List<VisageExpression> stats;
        VisageExpression value;
        if (bindStatus.isBound() && types.isSequence(tree.type)) {
            for (VisageExpression stat : tree.stats) {
                if (!(stat instanceof VisageVarInit)) {
                    throw new AssertionError("the statements in a bound block should already be just VarInit");
                }
            }
            VisageVar v = shredVar(defs.valueNamePrefix(), decompose(tree.value), tree.type);
            VisageVarInit vi = visagemake.at(tree.value.pos()).VarInit(v);
            vi.type = tree.type;
            stats = tree.stats.append(vi);
            VisageIdent val = id(v);
            val.sym = v.sym;
            val.type = tree.type;
            value = val;
        } else {
            stats = decomposeContainer(tree.stats);
            value = decompose(tree.value);
        }
        VisageBlock res = visagemake.at(tree.pos()).Block(tree.flags, stats, value);
        res.endpos = tree.endpos;
        result = res;
    }

    public void visitFunctionValue(VisageFunctionValue tree) {
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = VisageBindStatus.UNBOUND;
        tree.bodyExpression = decompose(tree.bodyExpression);
        result = tree;
        bindStatus = prevBindStatus;
    }

    public void visitSequenceEmpty(VisageSequenceEmpty tree) {
        result = tree;
    }

    private VisageVar synthVar(String label, VisageExpression tree, Type type) {
        return synthVar(label, tree, type, true);
    }

    private VisageVar synthVar(String label, VisageExpression tree, Type type, boolean decompose) {
        if (tree == null) {
            return null;
        }
        VisageExpression expr = decompose ? decompose(tree) : tree;

        visagemake.at(tree.pos()); // set position

        if (!types.isSameType(tree.type, type)) {
            // cast to desired type
            VisageIdent tp = (VisageIdent) visagemake.Type(type);
            tp.sym = type.tsym;
            expr = visagemake.TypeCast(tp, expr);
        }

        VisageVar v = shredVar(label, expr, type);
        v.sym.flags_field |= VisageFlags.VARMARK_BARE_SYNTH;
        return v;
    }

    private VisageVar makeSizeVar(DiagnosticPosition diagPos, int initial) {
        return makeIntVar(diagPos, defs.sizeNamePrefix(), initial);
    }

    private VisageVar makeIntVar(DiagnosticPosition diagPos, String label, int initial) {
        VisageExpression initialSize = visagemake.at(diagPos).Literal(initial);
        initialSize.type = syms.intType;
        VisageVar v = makeVar(diagPos, label, initialSize, VisageBindStatus.UNBOUND, syms.intType);
        return v;
    }

    private VisageVar makeSaveVar(DiagnosticPosition diagPos, Type type) {
        VisageVar v = makeVar(diagPos, defs.saveNamePrefix(), null, VisageBindStatus.UNBOUND, type);
        v.sym.flags_field |= VisageFlags.VARMARK_BARE_SYNTH;
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
    public void visitSequenceRange(VisageSequenceRange tree) {
        VisageExpression lower;
        VisageExpression upper;
        VisageExpression stepOrNull;
        if (bindStatus.isBound()) {
            Type elemType = types.elementType(tree.type);
            lower = synthVar(defs.lowerNamePrefix(), tree.getLower(), elemType);
            upper = synthVar(defs.upperNamePrefix(), tree.getUpper(), elemType);
            stepOrNull = synthVar(defs.stepNamePrefix(), tree.getStepOrNull(), elemType);
        } else {
            lower = decomposeComponent(tree.getLower());
            upper = decomposeComponent(tree.getUpper());
            stepOrNull = decomposeComponent(tree.getStepOrNull());
        }
        VisageSequenceRange res = visagemake.at(tree.pos).RangeSequence(lower, upper, stepOrNull, tree.isExclusive());
        res.type = tree.type;
        if (bindStatus.isBound()) {
            // now add a size var
            res.boundSizeVar = makeSizeVar(tree.pos(), VisageDefs.UNDEFINED_MARKER_INT);
        }
        result = res;
    }

    public void visitSequenceExplicit(VisageSequenceExplicit tree) {
        DiagnosticPosition diagPos = tree.pos();
        VisageSequenceExplicit res;
        if (bindStatus.isBound()) {
            // bound should not use items - non-null for pretty-printing
            res = visagemake.at(diagPos).ExplicitSequence(List.<VisageExpression>nil());
            boolean hasNullable = false;
            int n = 0;
            ListBuffer<VisageVarSymbol> vb = ListBuffer.lb();
            ListBuffer<VisageVarSymbol> vblen = ListBuffer.lb();
            for (VisageExpression item : tree.getItems()) {
                vb.append(synthVar(defs.itemNamePrefix()+n, item, item.type).sym);
                VisageVarSymbol lenSym = null;
                if (preTrans.isNullable(item)) {
                    lenSym = makeIntVar(item.pos(), defs.lengthNamePrefix()+n, 0).sym;
                    hasNullable = true;
                }
                vblen.append(lenSym);
                ++n;
            }
            res.boundItemsSyms = vb.toList();
            res.boundItemLengthSyms = vblen.toList();

            // now add synth vars
            if (tree.getItems().length() > 1  || types.isArrayOrSequenceType(res.boundItemsSyms.get(0).type)) {
                res.boundLowestInvalidPartSym = makeIntVar(diagPos, defs.lowNamePrefix(), VisageDefs.UNDEFINED_MARKER_INT).sym;
                res.boundHighestInvalidPartSym = makeIntVar(diagPos, defs.highNamePrefix(), VisageDefs.UNDEFINED_MARKER_INT).sym;
                res.boundPendingTriggersSym = makeIntVar(diagPos, defs.pendingNamePrefix(), 0).sym;
                if (hasNullable) {
                    res.boundSizeSym = makeSizeVar(diagPos, VisageDefs.UNDEFINED_MARKER_INT).sym;
                    res.boundDeltaSym = makeIntVar(diagPos, defs.deltaNamePrefix(), 0).sym;
                    res.boundChangeStartPosSym = makeIntVar(diagPos, defs.cngStartNamePrefix(), 0).sym;
                    res.boundChangeEndPosSym = makeIntVar(diagPos, defs.cngEndNamePrefix(), 0).sym;
                }
            }
            VisageExpression falseLit = visagemake.Literal(TypeTags.BOOLEAN, 0);
            falseLit.type = syms.booleanType;
            res.boundIgnoreInvalidationsSym = makeVar(diagPos, defs.ignoreNamePrefix(), falseLit, VisageBindStatus.UNBOUND, syms.booleanType).sym;
        } else {
            List<VisageExpression> items = decomposeComponents(tree.getItems());
            res = visagemake.at(diagPos).ExplicitSequence(items);
        }
        res.type = tree.type;
        result = res;
    }

    public void visitSequenceIndexed(VisageSequenceIndexed tree) {
        VisageExpression sequence = null;
        if (bindStatus.isBound()) {
            sequence = shredUnlessIdent(tree.getSequence());
        } else {
            sequence = decomposeComponent(tree.getSequence());
        }
        VisageExpression index = decomposeComponent(tree.getIndex());
        result = visagemake.at(tree.pos).SequenceIndexed(sequence, index);
    }

    public void visitSequenceSlice(VisageSequenceSlice tree) {
        VisageExpression sequence = shred(tree.getSequence());
        VisageExpression firstIndex = shred(tree.getFirstIndex());
        VisageExpression lastIndex = shred(tree.getLastIndex());
        result = visagemake.at(tree.pos).SequenceSlice(sequence, firstIndex, lastIndex, tree.getEndKind());
    }

    public void visitSequenceInsert(VisageSequenceInsert tree) {
        VisageExpression sequence = decompose(tree.getSequence());
        VisageExpression element = decompose(tree.getElement());
        VisageExpression position = decompose(tree.getPosition());
        result = visagemake.at(tree.pos).SequenceInsert(sequence, element, position, tree.shouldInsertAfter());
    }

    public void visitSequenceDelete(VisageSequenceDelete tree) {
        VisageExpression sequence = decompose(tree.getSequence());
        VisageExpression element = decompose(tree.getElement());
        result = visagemake.at(tree.pos).SequenceDelete(sequence, element);
    }

    public void visitInvalidate(VisageInvalidate tree) {
        VisageExpression variable = decompose(tree.getVariable());
        result = visagemake.at(tree.pos).Invalidate(variable);
    }

    public void visitForExpression(VisageForExpression tree) {
        if (bindStatus.isBound()) {
            VisageForExpressionInClause clause = tree.inClauses.head;
            clause.seqExpr = shred(clause.seqExpr, null);
            // clause.whereExpr = decompose(clause.whereExpr);

            // Create the BoundForHelper variable:
            Type inductionType = types.boxedTypeOrType(clause.inductionVarSym.type);
            VisageBlock body = (VisageBlock) tree.getBodyExpression();
            Type helperType = types.applySimpleGenericType(
                    types.isSequence(body.type)?
                        syms.visage_BoundForOverSequenceType :
                        (preTrans.isNullable(body) || clause.hasWhereExpression())?
                            syms.visage_BoundForOverNullableSingletonType :
                            syms.visage_BoundForOverSingletonType,
                    types.boxedElementType(tree.type),
                    inductionType);
            VisageExpression init = visagemake.Literal(TypeTags.BOT, null);
            init.type = helperType;
            Name helperName = preTrans.makeUniqueVarNameIn(names.fromString(defs.helperDollarNamePrefix()+currentVarSymbol.name), varOwner);
            VisageVar helper = makeVar(tree, helperName, init, VisageBindStatus.UNBOUND, helperType);
            //helper.sym.flags_field |= VisageFlags.VARMARK_BARE_SYNTH;
            clause.boundHelper = helper;

            // Fix up the class
            VisageClassDeclaration cdecl = (VisageClassDeclaration) decompose(body.stats.head);
            body.stats.head = cdecl;

            // Patch the type of the doit function
            patchDoitFunction(cdecl);

            // Patch the type of the anon{}.doit() call
            body.value.type = cdecl.type;  //TODO: probably need to go deeper

            // Add VisageForPart as implemented interface -- VisageForPart<T>
            Type intfc = types.applySimpleGenericType(types.erasure(syms.visage_ForPartInterfaceType), inductionType);
            cdecl.setDifferentiatedExtendingImplementingMixing(
                    List.<VisageExpression>nil(),
                    List.<VisageExpression>of(visagemake.Type(intfc)),  // implement interface
                    List.<VisageExpression>nil());

            result = visagemake.at(tree.pos).ForExpression(List.of(clause), body);
        } else {
            List<VisageForExpressionInClause> inClauses = decompose(tree.inClauses);
            VisageExpression bodyExpr = decompose(tree.bodyExpr);
            result = visagemake.at(tree.pos).ForExpression(inClauses, bodyExpr);
        }
    }

    private void patchDoitFunction(VisageClassDeclaration cdecl) {
        Type ctype = cdecl.type;
        for (VisageTree mem : cdecl.getMembers()) {
            if (mem.getVisageTag() == VisageTag.FUNCTION_DEF) {
                VisageFunctionDefinition func = (VisageFunctionDefinition) mem;
                if ((func.sym.flags() & VisageFlags.FUNC_SYNTH_LOCAL_DOIT) != 0L) {
                    // Change the value to be "this"
                    VisageBlock body = func.getBodyExpression();
                    body.value = visagemake.This(ctype);
                    body.type = ctype;

                    // Adjust function to return class type
                    final MethodType funcType = new MethodType(
                            List.<Type>nil(), // arg types
                            ctype, // return type
                            List.<Type>nil(), // Throws type
                            syms.methodClass);   // TypeSymbol
                    func.sym.type = funcType;
                    func.type = funcType;
               }
            }
        }

    }

    public void visitForExpressionInClause(VisageForExpressionInClause tree) {
        tree.seqExpr = decompose(tree.seqExpr);
        tree.setWhereExpr(decompose(tree.getWhereExpression()));
        result = tree;
    }

    public void visitIndexof(VisageIndexof tree) {
        result = tree.clause.indexVarSym == null ? tree : visagemake.Ident(tree.clause.indexVarSym);
    }

    public void visitTimeLiteral(VisageTimeLiteral tree) {
        result = tree;
    }

    public void visitLengthLiteral(VisageLengthLiteral tree) {
        result = tree;
    }

    public void visitAngleLiteral(VisageAngleLiteral tree) {
        result = tree;
    }

    public void visitColorLiteral(VisageColorLiteral tree) {
        result = tree;
    }

    public void visitOverrideClassVar(VisageOverrideClassVar tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.isStatic();
        VisageBindStatus prevBindStatus = bindStatus;
        VisageVarSymbol prevVarSymbol = currentVarSymbol;
        currentVarSymbol = tree.sym;
        // on-replace is always unbound
        bindStatus = VisageBindStatus.UNBOUND;
        VisageOnReplace onReplace = decompose(tree.getOnReplace());
        VisageOnReplace onInvalidate = decompose(tree.getOnInvalidate());
        // bound if was bind context or is bound variable
        bindStatus = tree.isBound()?
                            tree.getBindStatus() :
                            prevBindStatus;
        VisageExpression initializer = shredUnlessIdent(tree.getInitializer());
        VisageOverrideClassVar res = visagemake.at(tree.pos).OverrideClassVar(tree.getName(),
                tree.getVisageType(),
                tree.getModifiers(),
                tree.getId(),
                initializer,
                tree.getBindStatus(),
                onReplace,
                onInvalidate);
        res.sym = tree.sym;
        bindStatus = prevBindStatus;
        currentVarSymbol = prevVarSymbol;
        inScriptLevel = wasInScriptLevel;
        result = res;
    }

    public void visitInterpolateValue(VisageInterpolateValue tree) {
        VisageBindStatus prevBindStatus = bindStatus;
        bindStatus = VisageBindStatus.UNBOUND;
        VisageExpression attr = decompose(tree.attribute);
        VisageExpression funcValue = decompose(tree.funcValue);
        VisageExpression interpolation = decompose(tree.interpolation);
        // Note: funcValue takes the place of value
        VisageInterpolateValue res = visagemake.at(tree.pos).InterpolateValue(attr, funcValue, interpolation);
        res.sym = tree.sym;
        result = res;
        bindStatus = prevBindStatus;
    }

    public void visitKeyFrameLiteral(VisageKeyFrameLiteral tree) {
        VisageExpression start = decompose(tree.start);
        List<VisageExpression> values = decomposeComponents(tree.values);
        VisageExpression trigger = decompose(tree.trigger);
        result = visagemake.at(tree.pos).KeyFrameLiteral(start, values, trigger);
    }
}
