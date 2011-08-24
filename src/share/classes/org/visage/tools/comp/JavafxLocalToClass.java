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

import org.visage.api.JavafxBindStatus;
import org.visage.api.tree.ForExpressionInClauseTree;
import org.visage.tools.code.JavafxClassSymbol;
import org.visage.tools.code.JavafxFlags;
import org.visage.tools.code.JavafxSymtab;
import org.visage.tools.code.JavafxTypes;
import org.visage.tools.code.JavafxVarSymbol;
import org.visage.tools.tree.*;
import org.visage.tools.tree.VisageExpression;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.code.TypeTags;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.Position;
import java.util.Stack;

/**
 * Convert local contexts into classes if need be.
 * This conversion is needed for local contexts containing binds,
 * on-replace, and some forms of object literal.
 *
 * Also, local context is created for if Pointer.make is called on a
 * local variable or if interpolation attribute is a local variable.
 *
 * This work is broken into chunks, where a chunk is a body of code
 * within which variables can be moved to the top (and initialized
 * in-place).  Chunk boundaries are:
 *
 *   Class Declaration
 *   function body
 *   for-expression body
 *   while-loop body
 *   catch body
 *   on-replace/instanciate body
 *
 * This code is FRAGILE.
 * DO NOT change this code without review from Robert.
 *
 * @author Robert Field
 */
public class JavafxLocalToClass {

    private final JavafxPreTranslationSupport preTrans;
    private final JavafxTreeMaker fxmake;
    private final JavafxDefs defs;
    private final Name.Table names;
    private final JavafxTypes types;
    private final JavafxSymtab syms;
    private final JavafxResolve rs;

    private JavafxEnv<JavafxAttrContext> env;
    private Symbol owner;
    private boolean isStatic;
    private Stack<Symbol> prevOwners = new Stack();
    private Stack<Boolean> prevIsStatics = new Stack();
    private Stack<Type> prevReturnTypes = new Stack<Type>();

    protected static final Context.Key<JavafxLocalToClass> localToClass =
            new Context.Key<JavafxLocalToClass>();

    public static JavafxLocalToClass instance(Context context) {
        JavafxLocalToClass instance = context.get(localToClass);
        if (instance == null) {
            instance = new JavafxLocalToClass(context);
        }
        return instance;
    }

    private JavafxLocalToClass(Context context) {
        context.put(localToClass, this);

        preTrans = JavafxPreTranslationSupport.instance(context);
        fxmake = JavafxTreeMaker.instance(context);
        defs = JavafxDefs.instance(context);
        names = Name.Table.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        rs = JavafxResolve.instance(context);
    }

    public void inflateAsNeeded(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.env = attrEnv;
        descend(attrEnv.tree);
    }

    enum BlockKind {
        FUNCTION,
        TRIGGER,
        LOOP,
        CATCH
    }

    /**
     * This class is subclassed by classes which wish to control their descent
     * into other chunks.
     *
     * Where a chunk in a block-expression and all the sharable contexts within.
     * Loop bodies (for and while) are not sharable (because they are multiple).
     * Class declarations, and function definition are
     * separate contexts (thus not sharable).
     *
     * This class should have NO semantics.  Its job it to define the boundaries
     * of chunks.  It is the sole place this is done.
     */
    private abstract class AbstractTreeChunker extends JavafxTreeScanner {

        abstract void blockWithin(VisageBlock block, BlockKind bkind);

        abstract void classWithin(VisageClassDeclaration block);

        @Override
        public void visitClassDeclaration(VisageClassDeclaration tree) {
            // The class is a new chunk
            pushOwner(tree.sym, false);
            classWithin(tree);
            popOwner();
        }

        @Override
        public void visitFunctionValue(VisageFunctionValue tree) {
            // Don't process parameters

            // The body of the function begins a new chunk
            pushOwner(tree.definition.sym, false);
            Type returnType = (tree.definition.type == null)?
                null : tree.definition.type.getReturnType();
            pushFunctionReturnType(returnType);
            blockWithin(tree.getBodyExpression(), BlockKind.FUNCTION);
            popFunctionReturnType();
            popOwner();
        }

        @Override
        public void visitForExpression(VisageForExpression tree) {
            for (ForExpressionInClauseTree cl : tree.getInClauses()) {
                VisageForExpressionInClause clause = (VisageForExpressionInClause) cl;
                // Don't process induction var
                scan(clause.getSequenceExpression());
                scan(clause.getWhereExpression());
            }
            // The body of the for-expression begins a new chunk
            // Lower has made the body a block-expression
            boolean prevStatic = isStatic;
            isStatic = false;
            pushOwner(preTrans.makeDummyMethodSymbol(owner, defs.boundForPartName), false);
            blockWithin((VisageBlock) tree.getBodyExpression(), BlockKind.LOOP);
            popOwner();
            isStatic = prevStatic;
        }

        @Override
        public void visitWhileLoop(VisageWhileLoop tree) {
            scan(tree.cond);
            // The body of the while-loop begins a new chunk
            // Lower has made the body a block-expression
            blockWithin((VisageBlock) tree.getBody(), BlockKind.LOOP);
        }

        @Override
        public void visitCatch(VisageCatch tree) {
            // Skip param
            // The body of the catch begins a new chunk
            blockWithin((VisageBlock) tree.getBlock(), BlockKind.CATCH);
        }

        @Override
        public void visitOnReplace(VisageOnReplace tree) {
            pushOwner(preTrans.makeDummyMethodSymbol(owner), false);
            blockWithin(tree.getBody(), BlockKind.TRIGGER);
            popOwner();
        }

        /****** Visit methods just to keep the owner straight ******/

        //TODO: should this just be on function-value?
        @Override
        public void visitFunctionDefinition(VisageFunctionDefinition tree) {
            pushOwner(tree.sym, false);
            super.visitFunctionDefinition(tree);
            popOwner();
        }

        @Override
        public void visitInitDefinition(VisageInitDefinition tree) {
            pushOwner(tree.sym, false);
            super.visitInitDefinition(tree);
            popOwner();
        }

        @Override
        public void visitPostInitDefinition(VisagePostInitDefinition tree) {
            pushOwner(tree.sym, false);
            super.visitPostInitDefinition(tree);
            popOwner();
        }

        @Override
        public void visitVar(VisageVar tree) {
            pushOwner(preTrans.makeDummyMethodSymbol(owner), tree.isStatic());
            scan(tree.getInitializer());
            popOwner();
            scan(tree.getOnReplace());
            scan(tree.getOnInvalidate());
        }
    }

    /**
     * Check if a local context needs to be inflated into a class.
     * If it has bound variables, variables with triggers, or
     * contexts that can reference locals, we need to inflate.
     * @param tree Expression to check
     * @return True if tree needs to be inflated
     */
    private boolean needsToBeInflatedToClass(VisageTree tree) {

        class InflationChecker extends AbstractTreeChunker {

            boolean needed = false;

            void blockWithin(VisageBlock block, BlockKind bkind) {
                // Do not descend -- this analysis is within the chunk
            }

            void classWithin(VisageClassDeclaration klass) {
                // Do not descend -- this analysis is within the chunk

                // If this block holds a class definition that references a local var that is
                // assigned to (and thus cannot be final), we need to inflate the block
                needed |= referencesMutatedLocal(klass);
            }

            @Override
            public void visitVar(VisageVar tree) {
                // Check for bound or triggered
                needed |= tree.isBound();
                needed |= tree.getOnReplace() != null;
                needed |= tree.getOnInvalidate() != null;
                needed |= hasSelfReference(tree);
                super.visitVar(tree);
            }

            @Override
            public void visitForExpression(VisageForExpression tree) {
                needed |= needsToBeInflatedToClass(tree.getBodyExpression()) && referencesMutatedLocal(tree);
                super.visitForExpression(tree);
            }

            @Override
            public void visitCatch(VisageCatch tree) {
                needed |= needsToBeInflatedToClass(tree.getBlock()) && referencesMutatedLocal(tree);
                super.visitCatch(tree);
            }

            @Override
            public void visitWhileLoop(VisageWhileLoop tree) {
                needed |= needsToBeInflatedToClass(tree.getBody()) && referencesMutatedLocal(tree);
                super.visitWhileLoop(tree);
            }

            @Override
            public void visitFunctionValue(VisageFunctionValue tree) {
                // Funtion value may reference (non-final) locals
                needed |= referencesLocal(tree);
                super.visitFunctionValue(tree);
            }

            @Override
            public void visitFunctionInvocation(VisageFunctionInvocation tree) {
                Symbol msym = JavafxTreeInfo.symbol(tree.meth);

                // If the function call is the magic Pointer.make(Object)
                // function and argument involves a local var, then make a local
                // context class.
                if (types.isSyntheticPointerFunction(msym)) {
                    Symbol sym = JavafxTreeInfo.symbol(tree.args.head);
                    if (sym.isLocal()) {
                        needed = true;
                    }
                } else if (msym != null && (msym.flags() & JavafxFlags.BOUND) != 0L) {
                    // This is a call to a bound function. If any of the arg involves
                    // a local  variable or literal value, then make a local context class
                    for (VisageExpression arg : tree.args) {
                        Symbol sym = JavafxTreeInfo.symbol(arg);
                        if (sym != null && sym.isLocal()) {
                            needed = true;
                            break;
                        }
                    }
                } else {
                    super.visitFunctionInvocation(tree);
                }
            }

            @Override
            public void visitBlockExpression(VisageBlock tree) {
                if (tree.isBound()) {
                    needed = true;
                }
                super.visitBlockExpression(tree);
            }

            @Override
            public void visitInterpolateValue(VisageInterpolateValue tree) {
                JavafxTag tag = JavafxTreeInfo.skipParens(tree.attribute).getFXTag();
                if (tag == JavafxTag.IDENT) {
                    Symbol sym = JavafxTreeInfo.symbol(tree.attribute);
                    if (sym.isLocal()) {
                        needed = true;
                    }
                } else {
                    super.visitInterpolateValue(tree);
                }
            }
        }
        InflationChecker ic = new InflationChecker();
        ic.scan(tree);
        return ic.needed;
    }

    private class VarAndClassConverter extends AbstractTreeChunker {

        final JavafxClassSymbol classSym;

        VarAndClassConverter(JavafxClassSymbol classSym) {
            this.classSym = classSym;
        }

        ListBuffer<VisageVar> vars = ListBuffer.lb();

        List<VisageTree> varsAsMembers() {
            return List.convert(VisageTree.class, vars.toList());
        }

        void blockWithin(VisageBlock block, BlockKind bkind) {
            // Do not descend -- this inflation is within the chunk
        }

        void classWithin(VisageClassDeclaration block) {
            // Do not descend -- this inflation is within the chunk
        }

        /**
         * Convert any variables with the local context into a VarInit
         */
        private VisageExpression convertExprAndScan(VisageExpression expr) {
            if (expr instanceof VisageVar) {
                VisageVar var = (VisageVar) expr;
                vars.append(var);
                Scope oldScope = preTrans.getEnclosingScope(var.sym);
                if (oldScope != null) {
                    oldScope.remove(var.sym);
                }
                
                var.sym.name = preTrans.makeUniqueVarNameIn(var.sym.name, classSym);
                
                if (isStatic) {
                    var.sym.flags_field |= Flags.STATIC;
                    var.mods.flags |= Flags.STATIC;
                }
                classSym.members().enter(var.sym);
                var.sym.owner = classSym;
                pushOwner(preTrans.makeDummyMethodSymbol(classSym), isStatic);
                scan(var.getInitializer());
                popOwner();
                scan(var.getOnReplace());
                scan(var.getOnInvalidate());

                // Do the init in-line
                VisageExpression vi = fxmake.at(var).VarInit(var);
                vi.type = var.type;
                return vi;
            } else {
                // Not a var, just pass through
                scan(expr);
                return expr;
            }
        }

        @Override
        public void visitBlockExpression(VisageBlock tree) {
            ListBuffer<VisageExpression> stmts = ListBuffer.lb();
            for (VisageExpression stat : tree.stats) {
                stmts.append(convertExprAndScan(stat));
            }
            // Replace the guts of the block-expression with the var-converted versions
            tree.stats = stmts.toList();
            tree.value = convertExprAndScan(tree.value);
        }
    }

    /**
     * Inflate a block-expression into a class:
     *   {
     *     var x = 4;
     *     ++x;
     *     var y = x + 100;
     *     def z = bind y + 1;
     *     println(z);
     *     x + z
     *   }
     * Should become:
     *   {
     *     class local_klass44 {
     *       var x = 4;
     *       var y = x + 100;
     *       def z = bind y + 1;
     *       function doit$23(0 {
     *         VarInit x;
     *         ++x;
     *         VarInit y;
     *         ;
     *         println(z);
     *         x + z
     *       }
     *     }
     *     (new local_klass44()).doit$();
     *   }
     */
    private void inflateBlockToClass(VisageBlock block, BlockKind bkind) {
        final Name funcName = preTrans.syntheticName(defs.doitDollarNamePrefix());

        String classNamePrefix;
        if (owner instanceof MethodSymbol && (owner.flags() & JavafxFlags.BOUND) != 0L) {
            classNamePrefix = JavafxDefs.boundFunctionClassPrefix;
        } else if (owner instanceof MethodSymbol && owner.name == defs.boundForPartName) {
            classNamePrefix = JavafxDefs.boundForPartClassPrefix;
        } else {
            classNamePrefix = JavafxDefs.localContextClassPrefix;
        }
        final Name className = preTrans.syntheticName(classNamePrefix);

        final JavafxClassSymbol classSymbol = preTrans.makeClassSymbol(className, owner);
        classSymbol.flags_field |= Flags.FINAL;

        final MethodType funcType = new MethodType(
                List.<Type>nil(),    // arg types
                types.normalize(block.type),  // return type
                List.<Type>nil(),    // Throws type
                syms.methodClass);   // TypeSymbol
        final MethodSymbol funcSym = new MethodSymbol(JavafxFlags.FUNC_SYNTH_LOCAL_DOIT, funcName, funcType, classSymbol);

        class BlockVarAndClassConverter extends VarAndClassConverter {
            List<JavafxTag> nonLocalExprTags = List.nil();
            List<VisageCatch> nonLocalCatchers = List.nil();
            Type returnType = null;

            BlockVarAndClassConverter() {
                super(classSymbol);
            }
            @Override
            public void visitReturn(VisageReturn tree) {
                // This is a return in a local context inflated to class
                // Handle it as a non-local return
                tree.nonLocalReturn = true;
                returnType = tree.getExpression() != null ?
                    topFunctionReturnType() :
                    syms.voidType;
                if (!nonLocalExprTags.contains(tree.getFXTag())) {
                    VisageVar param = makeExceptionParameter(syms.visage_NonLocalReturnExceptionType);
                    VisageReturn catchBody = fxmake.Return(null);
                    if (returnType.tag != TypeTags.VOID) {
                        VisageIdent nlParam = fxmake.Ident(param);
                        nlParam.type = param.type;
                        nlParam.sym = param.sym;
                        VisageSelect nlValue = fxmake.Select(nlParam,
                        defs.value_NonLocalReturnExceptionFieldName, false);
                        nlValue.type = syms.objectType;
                        nlValue.sym = rs.findIdentInType(env, syms.visage_NonLocalReturnExceptionType, nlValue.name, Kinds.VAR);
                        catchBody.expr = fxmake.TypeCast(preTrans.makeTypeTree(returnType), nlValue).setType(returnType);
                    }
                    nonLocalExprTags = nonLocalExprTags.append(tree.getFXTag());
                    nonLocalCatchers = nonLocalCatchers.append(makeCatchExpression(param, catchBody, returnType));
                }
                scan(tree.expr);
            }
            @Override
            public void visitBreak(VisageBreak tree) {
                // This is a break in a local context inflated to class
                // Handle it as a non-local break
                tree.nonLocalBreak = true;
                if (!nonLocalExprTags.contains(tree.getFXTag())) {
                    VisageVar param = makeExceptionParameter(syms.visage_NonLocalBreakExceptionType);
                    VisageBreak catchBody = fxmake.Break(tree.label);
                    nonLocalExprTags = nonLocalExprTags.append(tree.getFXTag());
                    nonLocalCatchers = nonLocalCatchers.append(makeCatchExpression(param, catchBody, syms.unreachableType));
                }
            }
            @Override
            public void visitContinue(VisageContinue tree) {
                // This is a continue in a local context inflated to class
                // Handle it as a non-local continue
                tree.nonLocalContinue = true;
                if (!nonLocalExprTags.contains(tree.getFXTag())) {
                    VisageVar param = makeExceptionParameter(syms.visage_NonLocalContinueExceptionType);
                    VisageContinue catchBody = fxmake.Continue(tree.label);
                    nonLocalExprTags = nonLocalExprTags.append(tree.getFXTag());
                    nonLocalCatchers = nonLocalCatchers.append(makeCatchExpression(param, catchBody, syms.unreachableType));
                }
            }

            @Override
            public void visitVar(VisageVar tree) {
                if ((tree.mods.flags & Flags.PARAMETER) == 0L) {
                    throw new AssertionError("all vars should have been processed in the block expression");
                }
            }

            private VisageVar makeExceptionParameter(Type exceptionType) {
                VisageVar param = fxmake.Param(preTrans.syntheticName(defs.exceptionDollarNamePrefix()), preTrans.makeTypeTree(exceptionType));
                param.setType(exceptionType);
                param.sym = new JavafxVarSymbol(types, names, 0L, param.name, param.type, owner);
                return param;
            }

            private VisageCatch makeCatchExpression(VisageVar param, VisageExpression body, Type bodyType) {
                return fxmake.Catch(param,
                                (VisageBlock)fxmake.Block(0L,
                                    List.<VisageExpression>nil(),
                                    body).setType(bodyType));
            }
        }
        BlockVarAndClassConverter vc = new BlockVarAndClassConverter();
        vc.scan(block);

        // set position of class etc as block-expression position
        fxmake.at(block.pos());

        // Create whose vars are the block's vars and having a doit function with the content

        VisageType fxtype = fxmake.TypeUnknown();
        fxtype.type = block.type;

        VisageBlock body = fxmake.Block(block.flags, block.getStmts(), block.getValue());
        body.type = block.type;
        body.pos = Position.NOPOS;

        VisageFunctionDefinition doit = fxmake.FunctionDefinition(
                fxmake.Modifiers(JavafxFlags.SCRIPT_PRIVATE),
                funcName,
                fxtype,
                List.<VisageVar>nil(),
                body);
        doit.pos = Position.NOPOS;
        doit.sym = funcSym;
        doit.type = funcType;

        final VisageClassDeclaration cdecl = fxmake.ClassDeclaration(
                fxmake.Modifiers(Flags.FINAL | Flags.SYNTHETIC),
                className,
                List.<VisageExpression>nil(),
                vc.varsAsMembers().append(doit));
        cdecl.sym = classSymbol;
        cdecl.type = classSymbol.type;
        types.addFxClass(classSymbol, cdecl);
        cdecl.setDifferentiatedExtendingImplementingMixing(List.<VisageExpression>nil(), List.<VisageExpression>nil(), List.<VisageExpression>nil());

        VisageIdent classId = fxmake.Ident(className);
        classId.sym = classSymbol;
        classId.type = classSymbol.type;

        VisageInstanciate inst = fxmake.InstanciateNew(classId, null);
        inst.sym = classSymbol;
        inst.type = classSymbol.type;

        VisageSelect select = fxmake.Select(inst, funcName, false);
        select.sym = funcSym;
        select.type = funcSym.type;

        VisageFunctionInvocation apply = fxmake.Apply(null, select, null);
        apply.type = block.type;

        List<VisageExpression> stats = List.<VisageExpression>of(cdecl);
        VisageExpression value = apply;

        if (vc.nonLocalCatchers.size() > 0) {

            VisageBlock tryBody = (VisageBlock)fxmake.Block(0L, stats, value).setType(block.type);
            
            stats = List.<VisageExpression>of(
                fxmake.Try(
                    tryBody,
                    vc.nonLocalCatchers,
                    null));

            if (block.type != syms.voidType) {
                JavafxVarSymbol resVarSym = new JavafxVarSymbol(types,
                        names,
                        0L,
                        preTrans.syntheticName(defs.resDollarNamePrefix()),
                        types.normalize(block.type),
                        doit.sym);
                VisageVar resVar = fxmake.Var(resVarSym.name,
                    preTrans.makeTypeTree(resVarSym.type),
                    fxmake.Modifiers(resVarSym.flags_field),
                    null,
                    JavafxBindStatus.UNBOUND,
                    null, null);
                resVar.type = resVarSym.type;
                resVar.sym = resVarSym;
                VisageIdent resVarRef = fxmake.Ident(resVarSym);
                resVarRef.sym = resVarSym;
                resVarRef.type = resVar.type;
                tryBody.value = fxmake.Assign(resVarRef, apply).setType(block.type);

                value = (bkind == BlockKind.FUNCTION &&
                        vc.returnType != null &&
                        vc.returnType.tag != TypeTags.VOID) ?
                        fxmake.Return(
                            fxmake.TypeCast(
                                preTrans.makeTypeTree(vc.returnType),
                                resVarRef
                            ).setType(vc.returnType)
                        ).setType(syms.unreachableType) :
                    resVarRef;
                stats = stats.prepend(resVar);
            }
            else {
                value = null;
            }
        }

        preTrans.liftTypes(cdecl, classSymbol.type, funcSym);

        // Replace the guts of the block-expression with the class wrapping the previous body
        // and a call to the doit function of that class.
        block.stats = stats;
        block.value = value;
    }

    /**
     * Flatten any vars defined in var initializer block expressions into the class level
     */
    private void flattenVarsIntoClass(VisageClassDeclaration klass) {
        final JavafxClassSymbol classSym = (JavafxClassSymbol) klass.sym;
        VarAndClassConverter vc = new VarAndClassConverter(classSym);
        for (VisageTree mem : klass.getMembers()) {
            if (needsToBeInflatedToClass(mem)) {
                vc.scan(mem);
            }
        }
        klass.setMembers(klass.getMembers().appendList(vc.varsAsMembers()));
    }

    /**
     * Descend to the next level of chunk
     */
    private void descend(VisageTree tree) {
        BottomUpChunkWalker bucw = new BottomUpChunkWalker();
        bucw.scan(tree);
    }

    private class BottomUpChunkWalker extends AbstractTreeChunker {

        void blockWithin(VisageBlock block, BlockKind bkind) {
            // Descend into inner chunks
            descend(block);

            // check if the block needs inflation, if so, inflate
            if (needsToBeInflatedToClass(block)) {
                inflateBlockToClass(block, bkind);
            }
        }

        void classWithin(VisageClassDeclaration klass) {
            for (VisageTree member : klass.getMembers()) {
                descend(member);
            }
            flattenVarsIntoClass(klass);
        }
    }


    /************************** Utilities ******************************/

    private boolean referencesMutatedLocal(VisageTree tree) {

        class ReferenceChecker extends JavafxTreeScanner {

            boolean hasMutatedLocal = false;

            @Override
            public void visitIdent(VisageIdent tree) {
                if (tree.sym instanceof VarSymbol) {
                    JavafxVarSymbol vsym = (JavafxVarSymbol) tree.sym;
                    if (vsym.isMutatedLocal()) {
                        hasMutatedLocal = true;
                    }
                }
            }
        }
        ReferenceChecker rc = new ReferenceChecker();
        rc.scan(tree);
        return rc.hasMutatedLocal;
    }

    private boolean referencesLocal(VisageTree tree) {

        class ReferenceChecker extends JavafxTreeScanner {

            boolean hasLocal = false;

            @Override
            public void visitIdent(VisageIdent tree) {
                if (tree.sym instanceof VarSymbol) {
                    JavafxVarSymbol vsym = (JavafxVarSymbol) tree.sym;
                    if (vsym.isLocal()) {
                        hasLocal = true;
                    }
                }
                
                super.visitIdent(tree);
            }
            
            @Override
            public void visitIndexof(VisageIndexof tree) {
                hasLocal = true;
                super.visitIndexof(tree);
            }
        }
        ReferenceChecker rc = new ReferenceChecker();
        rc.scan(tree);
        return rc.hasLocal;
    }

    private boolean hasSelfReference(VisageVar checkedVar) {
        return checkedVar.sym.hasSelfReference();
    }

    private void pushOwner(Symbol newOwner, boolean newIsStatic) {
        prevOwners.push(owner);
        prevIsStatics.push(isStatic);
        owner = newOwner;
        isStatic = newIsStatic;
    }

    private void popOwner() {
       owner = prevOwners.pop();
       isStatic = prevIsStatics.pop();
    }

    private void pushFunctionReturnType(Type returnType) {
        prevReturnTypes.push(returnType);
    }

    private void popFunctionReturnType() {
        prevReturnTypes.pop();
    }

    private Type topFunctionReturnType() {
        return prevReturnTypes.peek();
    }
}

