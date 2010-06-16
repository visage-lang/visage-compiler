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
import com.sun.javafx.api.tree.ForExpressionInClauseTree;
import com.sun.tools.javafx.code.JavafxClassSymbol;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.tree.JFXExpression;
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

        abstract void blockWithin(JFXBlock block, BlockKind bkind);

        abstract void classWithin(JFXClassDeclaration block);

        @Override
        public void visitClassDeclaration(JFXClassDeclaration tree) {
            // The class is a new chunk
            pushOwner(tree.sym, false);
            classWithin(tree);
            popOwner();
        }

        @Override
        public void visitFunctionValue(JFXFunctionValue tree) {
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
        public void visitForExpression(JFXForExpression tree) {
            for (ForExpressionInClauseTree cl : tree.getInClauses()) {
                JFXForExpressionInClause clause = (JFXForExpressionInClause) cl;
                // Don't process induction var
                scan(clause.getSequenceExpression());
                scan(clause.getWhereExpression());
            }
            // The body of the for-expression begins a new chunk
            // Lower has made the body a block-expression
            boolean prevStatic = isStatic;
            isStatic = false;
            pushOwner(preTrans.makeDummyMethodSymbol(owner, defs.boundForPartName), false);
            blockWithin((JFXBlock) tree.getBodyExpression(), BlockKind.LOOP);
            popOwner();
            isStatic = prevStatic;
        }

        @Override
        public void visitWhileLoop(JFXWhileLoop tree) {
            scan(tree.cond);
            // The body of the while-loop begins a new chunk
            // Lower has made the body a block-expression
            blockWithin((JFXBlock) tree.getBody(), BlockKind.LOOP);
        }

        @Override
        public void visitCatch(JFXCatch tree) {
            // Skip param
            // The body of the catch begins a new chunk
            blockWithin((JFXBlock) tree.getBlock(), BlockKind.CATCH);
        }

        @Override
        public void visitOnReplace(JFXOnReplace tree) {
            pushOwner(preTrans.makeDummyMethodSymbol(owner), false);
            blockWithin(tree.getBody(), BlockKind.TRIGGER);
            popOwner();
        }

        /****** Visit methods just to keep the owner straight ******/

        //TODO: should this just be on function-value?
        @Override
        public void visitFunctionDefinition(JFXFunctionDefinition tree) {
            pushOwner(tree.sym, false);
            super.visitFunctionDefinition(tree);
            popOwner();
        }

        @Override
        public void visitInitDefinition(JFXInitDefinition tree) {
            pushOwner(tree.sym, false);
            super.visitInitDefinition(tree);
            popOwner();
        }

        @Override
        public void visitPostInitDefinition(JFXPostInitDefinition tree) {
            pushOwner(tree.sym, false);
            super.visitPostInitDefinition(tree);
            popOwner();
        }

        @Override
        public void visitVar(JFXVar tree) {
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
    private boolean needsToBeInflatedToClass(JFXTree tree) {

        class InflationChecker extends AbstractTreeChunker {

            boolean needed = false;

            void blockWithin(JFXBlock block, BlockKind bkind) {
                // Do not descend -- this analysis is within the chunk
            }

            void classWithin(JFXClassDeclaration klass) {
                // Do not descend -- this analysis is within the chunk

                // If this block holds a class definition that references a local var that is
                // assigned to (and thus cannot be final), we need to inflate the block
                needed |= referencesMutatedLocal(klass);
            }

            @Override
            public void visitVar(JFXVar tree) {
                // Check for bound or triggered
                needed |= tree.isBound();
                needed |= tree.getOnReplace() != null;
                needed |= tree.getOnInvalidate() != null;
                needed |= hasSelfReference(tree);
                super.visitVar(tree);
            }

            @Override
            public void visitForExpression(JFXForExpression tree) {
                needed |= needsToBeInflatedToClass(tree.getBodyExpression()) && referencesMutatedLocal(tree);
                super.visitForExpression(tree);
            }

            @Override
            public void visitCatch(JFXCatch tree) {
                needed |= needsToBeInflatedToClass(tree.getBlock()) && referencesMutatedLocal(tree);
                super.visitCatch(tree);
            }

            @Override
            public void visitWhileLoop(JFXWhileLoop tree) {
                needed |= needsToBeInflatedToClass(tree.getBody()) && referencesMutatedLocal(tree);
                super.visitWhileLoop(tree);
            }

            @Override
            public void visitFunctionValue(JFXFunctionValue tree) {
                // Funtion value may reference (non-final) locals
                needed |= referencesLocal(tree);
                super.visitFunctionValue(tree);
            }

            @Override
            public void visitFunctionInvocation(JFXFunctionInvocation tree) {
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
                    for (JFXExpression arg : tree.args) {
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
            public void visitBlockExpression(JFXBlock tree) {
                if (tree.isBound()) {
                    needed = true;
                }
                super.visitBlockExpression(tree);
            }

            @Override
            public void visitInterpolateValue(JFXInterpolateValue tree) {
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

        ListBuffer<JFXVar> vars = ListBuffer.lb();

        List<JFXTree> varsAsMembers() {
            return List.convert(JFXTree.class, vars.toList());
        }

        void blockWithin(JFXBlock block, BlockKind bkind) {
            // Do not descend -- this inflation is within the chunk
        }

        void classWithin(JFXClassDeclaration block) {
            // Do not descend -- this inflation is within the chunk
        }

        /**
         * Convert any variables with the local context into a VarInit
         */
        private JFXExpression convertExprAndScan(JFXExpression expr) {
            if (expr instanceof JFXVar) {
                JFXVar var = (JFXVar) expr;
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
                JFXExpression vi = fxmake.at(var).VarInit(var);
                vi.type = var.type;
                return vi;
            } else {
                // Not a var, just pass through
                scan(expr);
                return expr;
            }
        }

        @Override
        public void visitBlockExpression(JFXBlock tree) {
            ListBuffer<JFXExpression> stmts = ListBuffer.lb();
            for (JFXExpression stat : tree.stats) {
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
    private void inflateBlockToClass(JFXBlock block, BlockKind bkind) {
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
            List<JFXCatch> nonLocalCatchers = List.nil();
            Type returnType = null;

            BlockVarAndClassConverter() {
                super(classSymbol);
            }
            @Override
            public void visitReturn(JFXReturn tree) {
                // This is a return in a local context inflated to class
                // Handle it as a non-local return
                tree.nonLocalReturn = true;
                returnType = tree.getExpression() != null ?
                    topFunctionReturnType() :
                    syms.voidType;
                if (!nonLocalExprTags.contains(tree.getFXTag())) {
                    JFXVar param = makeExceptionParameter(syms.javafx_NonLocalReturnExceptionType);
                    JFXReturn catchBody = fxmake.Return(null);
                    if (returnType.tag != TypeTags.VOID) {
                        JFXIdent nlParam = fxmake.Ident(param);
                        nlParam.type = param.type;
                        nlParam.sym = param.sym;
                        JFXSelect nlValue = fxmake.Select(nlParam,
                        defs.value_NonLocalReturnExceptionFieldName);
                        nlValue.type = syms.objectType;
                        nlValue.sym = rs.findIdentInType(env, syms.javafx_NonLocalReturnExceptionType, nlValue.name, Kinds.VAR);
                        catchBody.expr = fxmake.TypeCast(preTrans.makeTypeTree(returnType), nlValue).setType(returnType);
                    }
                    nonLocalExprTags = nonLocalExprTags.append(tree.getFXTag());
                    nonLocalCatchers = nonLocalCatchers.append(makeCatchExpression(param, catchBody, returnType));
                }
                scan(tree.expr);
            }
            @Override
            public void visitBreak(JFXBreak tree) {
                // This is a break in a local context inflated to class
                // Handle it as a non-local break
                tree.nonLocalBreak = true;
                if (!nonLocalExprTags.contains(tree.getFXTag())) {
                    JFXVar param = makeExceptionParameter(syms.javafx_NonLocalBreakExceptionType);
                    JFXBreak catchBody = fxmake.Break(tree.label);
                    nonLocalExprTags = nonLocalExprTags.append(tree.getFXTag());
                    nonLocalCatchers = nonLocalCatchers.append(makeCatchExpression(param, catchBody, syms.unreachableType));
                }
            }
            @Override
            public void visitContinue(JFXContinue tree) {
                // This is a continue in a local context inflated to class
                // Handle it as a non-local continue
                tree.nonLocalContinue = true;
                if (!nonLocalExprTags.contains(tree.getFXTag())) {
                    JFXVar param = makeExceptionParameter(syms.javafx_NonLocalContinueExceptionType);
                    JFXContinue catchBody = fxmake.Continue(tree.label);
                    nonLocalExprTags = nonLocalExprTags.append(tree.getFXTag());
                    nonLocalCatchers = nonLocalCatchers.append(makeCatchExpression(param, catchBody, syms.unreachableType));
                }
            }

            @Override
            public void visitVar(JFXVar tree) {
                if ((tree.mods.flags & Flags.PARAMETER) == 0L) {
                    throw new AssertionError("all vars should have been processed in the block expression");
                }
            }

            private JFXVar makeExceptionParameter(Type exceptionType) {
                JFXVar param = fxmake.Param(preTrans.syntheticName(defs.exceptionDollarNamePrefix()), preTrans.makeTypeTree(exceptionType));
                param.setType(exceptionType);
                param.sym = new JavafxVarSymbol(types, names, 0L, param.name, param.type, owner);
                return param;
            }

            private JFXCatch makeCatchExpression(JFXVar param, JFXExpression body, Type bodyType) {
                return fxmake.Catch(param,
                                (JFXBlock)fxmake.Block(0L,
                                    List.<JFXExpression>nil(),
                                    body).setType(bodyType));
            }
        }
        BlockVarAndClassConverter vc = new BlockVarAndClassConverter();
        vc.scan(block);

        // set position of class etc as block-expression position
        fxmake.at(block.pos());

        // Create whose vars are the block's vars and having a doit function with the content

        JFXType fxtype = fxmake.TypeUnknown();
        fxtype.type = block.type;

        JFXBlock body = fxmake.Block(block.flags, block.getStmts(), block.getValue());
        body.type = block.type;
        body.pos = Position.NOPOS;

        JFXFunctionDefinition doit = fxmake.FunctionDefinition(
                fxmake.Modifiers(JavafxFlags.SCRIPT_PRIVATE),
                funcName,
                fxtype,
                List.<JFXVar>nil(),
                body);
        doit.pos = Position.NOPOS;
        doit.sym = funcSym;
        doit.type = funcType;

        final JFXClassDeclaration cdecl = fxmake.ClassDeclaration(
                fxmake.Modifiers(Flags.FINAL | Flags.SYNTHETIC),
                className,
                List.<JFXExpression>nil(),
                vc.varsAsMembers().append(doit));
        cdecl.sym = classSymbol;
        cdecl.type = classSymbol.type;
        types.addFxClass(classSymbol, cdecl);
        cdecl.setDifferentiatedExtendingImplementingMixing(List.<JFXExpression>nil(), List.<JFXExpression>nil(), List.<JFXExpression>nil());

        JFXIdent classId = fxmake.Ident(className);
        classId.sym = classSymbol;
        classId.type = classSymbol.type;

        JFXInstanciate inst = fxmake.InstanciateNew(classId, null);
        inst.sym = classSymbol;
        inst.type = classSymbol.type;

        JFXSelect select = fxmake.Select(inst, funcName);
        select.sym = funcSym;
        select.type = funcSym.type;

        JFXFunctionInvocation apply = fxmake.Apply(null, select, null);
        apply.type = block.type;

        List<JFXExpression> stats = List.<JFXExpression>of(cdecl);
        JFXExpression value = apply;

        if (vc.nonLocalCatchers.size() > 0) {

            JFXBlock tryBody = (JFXBlock)fxmake.Block(0L, stats, value).setType(block.type);
            
            stats = List.<JFXExpression>of(
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
                JFXVar resVar = fxmake.Var(resVarSym.name,
                    preTrans.makeTypeTree(resVarSym.type),
                    fxmake.Modifiers(resVarSym.flags_field),
                    null,
                    JavafxBindStatus.UNBOUND,
                    null, null);
                resVar.type = resVarSym.type;
                resVar.sym = resVarSym;
                JFXIdent resVarRef = fxmake.Ident(resVarSym);
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
    private void flattenVarsIntoClass(JFXClassDeclaration klass) {
        final JavafxClassSymbol classSym = (JavafxClassSymbol) klass.sym;
        VarAndClassConverter vc = new VarAndClassConverter(classSym);
        for (JFXTree mem : klass.getMembers()) {
            if (needsToBeInflatedToClass(mem)) {
                vc.scan(mem);
            }
        }
        klass.setMembers(klass.getMembers().appendList(vc.varsAsMembers()));
    }

    /**
     * Descend to the next level of chunk
     */
    private void descend(JFXTree tree) {
        BottomUpChunkWalker bucw = new BottomUpChunkWalker();
        bucw.scan(tree);
    }

    private class BottomUpChunkWalker extends AbstractTreeChunker {

        void blockWithin(JFXBlock block, BlockKind bkind) {
            // Descend into inner chunks
            descend(block);

            // check if the block needs inflation, if so, inflate
            if (needsToBeInflatedToClass(block)) {
                inflateBlockToClass(block, bkind);
            }
        }

        void classWithin(JFXClassDeclaration klass) {
            for (JFXTree member : klass.getMembers()) {
                descend(member);
            }
            flattenVarsIntoClass(klass);
        }
    }


    /************************** Utilities ******************************/

    private boolean referencesMutatedLocal(JFXTree tree) {

        class ReferenceChecker extends JavafxTreeScanner {

            boolean hasMutatedLocal = false;

            @Override
            public void visitIdent(JFXIdent tree) {
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

    private boolean referencesLocal(JFXTree tree) {

        class ReferenceChecker extends JavafxTreeScanner {

            boolean hasLocal = false;

            @Override
            public void visitIdent(JFXIdent tree) {
                if (tree.sym instanceof VarSymbol) {
                    JavafxVarSymbol vsym = (JavafxVarSymbol) tree.sym;
                    if (vsym.isLocal()) {
                        hasLocal = true;
                    }
                }
                
                super.visitIdent(tree);
            }
            
            @Override
            public void visitIndexof(JFXIndexof tree) {
                hasLocal = true;
                super.visitIndexof(tree);
            }
        }
        ReferenceChecker rc = new ReferenceChecker();
        rc.scan(tree);
        return rc.hasLocal;
    }

    private boolean hasSelfReference(JFXVar checkedVar) {
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

