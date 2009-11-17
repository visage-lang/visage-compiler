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

import com.sun.javafx.api.tree.ForExpressionInClauseTree;
import com.sun.tools.javafx.code.JavafxClassSymbol;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.tree.JFXExpression;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.ClassType;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import java.util.HashMap;
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
    private final JavafxTypes types;
    private final JavafxSymtab syms;

    private JavafxEnv<JavafxAttrContext> env;
    private Symbol owner;
    private boolean isStatic;
    private Stack<Symbol> prevOwners = new Stack();
    private Stack<Boolean> prevIsStatics = new Stack();

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
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
    }

    public void inflateAsNeeded(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.env = attrEnv;
        descendWasInflated(attrEnv.tree);
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

        abstract void blockWithin(JFXBlock block);

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
            blockWithin(tree.getBodyExpression());
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
            blockWithin((JFXBlock) tree.getBodyExpression());
        }

        @Override
        public void visitWhileLoop(JFXWhileLoop tree) {
            scan(tree.cond);
            // The body of the while-loop begins a new chunk
            // Lower has made the body a block-expression
            blockWithin((JFXBlock) tree.getBody());
        }

        @Override
        public void visitCatch(JFXCatch tree) {
            // Skip param

            // Scan catch body normally
            scan(tree.body);
        }

        @Override
        public void visitOnReplace(JFXOnReplace tree) {
            pushOwner(preTrans.makeDummyMethodSymbol(owner), false);
            blockWithin(tree.getBody());
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

            void blockWithin(JFXBlock block) {
                // Do not descend -- this analysis is within the chunk
            }

            void classWithin(JFXClassDeclaration block) {
                // Do not descend -- this analysis is within the chunk

                assert false : "if there was a class within, shouldn't be here testing inflation";
                needed = true;
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
            public void visitFunctionValue(JFXFunctionValue tree) {
                // Funtion value may reference (non-final) locals
                needed = true;
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

        void blockWithin(JFXBlock block) {
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
                while (classSym.members().lookup(var.sym.name).sym != null) {
                    var.sym.name = var.sym.name.append('$', var.sym.name);
                }
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
    private void inflateBlockToClass(JFXBlock block) {
        final Name funcName = preTrans.syntheticName("doit$");
        final Name className = preTrans.syntheticName("local_klass");

        final JavafxClassSymbol classSymbol = preTrans.makeClassSymbol(className, owner);

        final MethodType funcType = new MethodType(
                List.<Type>nil(),    // arg types
                block.type,          // return type
                List.<Type>nil(),    // Throws type
                classSymbol.type.tsym); // TypeSymbol
        final MethodSymbol funcSym = new MethodSymbol(0L, funcName, funcType, classSymbol);

        class BlockVarAndClassConverter extends VarAndClassConverter {
            boolean returnFound = false;
            Type returnType = null;
            BlockVarAndClassConverter() {
                super(classSymbol);
            }
            @Override
            public void visitReturn(JFXReturn tree) {
                // This is a return in a local context inflated to class
                // Handle it as a non-local return
                tree.nonLocalReturn = true;
                returnFound = true;
                if (tree.getExpression() != null)
                    returnType = tree.getExpression().type;
                scan(tree.expr);
            }

            @Override
            public void visitVar(JFXVar tree) {
                if ((tree.mods.flags & Flags.PARAMETER) == 0L) {
                    throw new AssertionError("all vars should have been processed in the block expression");
                }
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

        JFXFunctionDefinition doit = fxmake.FunctionDefinition(
                fxmake.Modifiers(JavafxFlags.SCRIPT_PRIVATE),
                funcName,
                fxtype,
                List.<JFXVar>nil(),
                body);
        doit.sym = funcSym;
        doit.type = funcType;

        final JFXClassDeclaration cdecl = fxmake.ClassDeclaration(
                fxmake.Modifiers(Flags.SYNTHETIC),
                className,
                List.<JFXExpression>nil(),
                vc.varsAsMembers().append(doit));
        cdecl.sym = classSymbol;
        cdecl.type = classSymbol.type;
        types.addFxClass(classSymbol, cdecl);
        cdecl.setDifferentiatedExtendingImplementing(List.<JFXExpression>nil(), List.<JFXExpression>nil(), List.<JFXExpression>nil());

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

        if (vc.returnFound) {
            // We have a non-local return -- wrap it in try-catch
            JFXBlock tryBody = fxmake.Block(0L, stats, value);
            JFXVar param = fxmake.Param(preTrans.syntheticName("expt"), null);
            param.setType(syms.javafx_NonLocalReturnExceptionType);
            param.sym = new VarSymbol(0L, param.name, param.type, owner);
            JFXExpression retValue = vc.returnType == null?
                null :
                fxmake.TypeCast(vc.returnType,
                    fxmake.Select(
                        fxmake.Ident(param),
                        defs.value_NonLocalReturnExceptionFieldName));
            stats = List.nil();
            value =
                fxmake.Try(
                    tryBody,
                    List.of(
                        fxmake.Catch(param,
                            fxmake.Block(0L,
                                List.<JFXExpression>nil(),
                                fxmake.Return(retValue)))
                    ),
                    null);
        }


        class NestedClassTypeLifter extends JavafxTreeScanner {

            @Override
            public void visitClassDeclaration(JFXClassDeclaration that) {
                super.visitClassDeclaration(that);
                if (that.sym != classSymbol &&
                        (that.type.getEnclosingType() == Type.noType ||
                        that.type.getEnclosingType().tsym == classSymbol.type.getEnclosingType().tsym)) {
                    Scope oldScope = preTrans.getEnclosingScope(that.sym);
                    if (oldScope != null)
                        oldScope.remove(that.sym);
                    ((ClassType)that.type).setEnclosingType(classSymbol.type);
                    that.sym.owner = funcSym;
                    classSymbol.members().enter(that.sym);
                }
            }
        }

        new NestedClassTypeLifter().scan(cdecl);

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
            vc.scan(mem);
        }
        klass.setMembers(klass.getMembers().appendList(vc.varsAsMembers()));
    }

    /**
     * Descend to the next level of chunk
     * @return true if that chunk was inflated
     */
    private boolean descendWasInflated(JFXTree tree) {
        BottomUpChunkWalker bucw = new BottomUpChunkWalker();
        bucw.scan(tree);
        return bucw.wasInflated;
    }

    private class BottomUpChunkWalker extends AbstractTreeChunker {

        boolean wasInflated = false;

        void blockWithin(JFXBlock block) {
            // Descend into inner chunks
            boolean innerWasInflated = descendWasInflated(block);

            // If inner was inflated, that can force outer to be inflated
            //TODO: refine that
            // otherwise check if the block needs inflation
            if (innerWasInflated || needsToBeInflatedToClass(block)) {
                inflateBlockToClass(block);
                wasInflated = true;
            }
        }

        void classWithin(JFXClassDeclaration klass) {
            for (JFXTree member : klass.getMembers()) {
                descendWasInflated(member);
            }
            flattenVarsIntoClass(klass);

            // Effectively pre-inflated
            wasInflated = true;
        }
    }


    /************************** Utilities ******************************/

    private boolean hasSelfReference(JFXVar checkedVar) {

        class SelfReferenceChecker extends JavafxTreeScanner {

            HashMap<Symbol, JFXFunctionValue> varDecls = new HashMap<Symbol, JFXFunctionValue>();
            JFXFunctionValue enclFunctionValue = null;
            boolean foundSelfRef;

            @Override
            public void visitIdent(JFXIdent tree) {
                if (varDecls.containsKey(tree.sym) &&
                        varDecls.get(tree.sym) != enclFunctionValue) {
                    foundSelfRef = true;
                }
            }

            @Override
            public void visitVar(JFXVar tree) {
                boolean prevFoundSelfRef = foundSelfRef;
                try {
                    varDecls.put(tree.sym, enclFunctionValue);
                    super.visitVar(tree);
                } finally {
                    varDecls.remove(tree.sym);
                    foundSelfRef = prevFoundSelfRef;
                }
            }

            @Override
            public void visitFunctionValue(JFXFunctionValue tree) {
                JFXFunctionValue prevEnclFunctionValue = enclFunctionValue;
                try {
                    enclFunctionValue = tree;
                    super.visitFunctionValue(tree);
                } finally {
                    enclFunctionValue = prevEnclFunctionValue;
                }
            }

            boolean checkSelfRef(JFXVar tree) {
                varDecls.put(tree.sym, null);
                scan(tree.getInitializer());
                return foundSelfRef;
            }
        }

        return new SelfReferenceChecker().checkSelfRef(checkedVar);
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
}

