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
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.tree.JFXExpression;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.PackageSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.ClassType;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.jvm.ClassReader;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;

/**
 * Convert local contexts into classes if need be.
 * This conversion is needed for local contexts containing binds,
 * on-replace, and some forms of object literal.
 *
 * Also, local context is created for if Pointer.make is callled on a
 * local variable or if interpolation attribute is a local variable.
 *
 * @author Robert Field
 */
public class JavafxLocalToClass {

    private final JavafxTreeMaker fxmake;
    private final JavafxDefs defs;
    private final Name.Table names;
    private final JavafxCheck chk;
    private final ClassReader reader;
    private final JavafxTypes types;
    private final JavafxSymtab syms;

    private JavafxEnv<JavafxAttrContext> env;
    private Symbol owner;
    private int tmpCount = 0;

    // assigned lazily on the first usage. This is symbol of
    // Pointer.make(Object) method.
    private MethodSymbol pointerMakeMethodSym;

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

        fxmake = JavafxTreeMaker.instance(context);
        defs = JavafxDefs.instance(context);
        names = Name.Table.instance(context);
        chk = JavafxCheck.instance(context);
        reader = ClassReader.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
    }

    public void inflateAsNeeded(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.env = attrEnv;
        descendThroughChunksReportIfThisChunkInflated(attrEnv.tree);
    }

    private MethodSymbol pointerMakeMethodSym() {
        if (pointerMakeMethodSym == null) {
            Symbol pointerSym = syms.javafx_PointerType.tsym;
            for (Scope.Entry e1 = pointerSym.members().lookup(defs.make_PointerMethodName);
                e1 != null;
                e1 = e1.sibling) {
                Symbol sym = e1.sym;
                MethodSymbol msym = (MethodSymbol) sym;
                if (msym.params().size() == 1) {
                    pointerMakeMethodSym = msym;
                    break;
                }
            }
        }

        assert pointerMakeMethodSym != null : "can't find Pointer.make(Object) method";
        return pointerMakeMethodSym;
    }

    /**
     * This class subclassed by classes which should not descend into other chunks.
     * Where a chunk in a block-expression and all the sharable contexts within.
     * Loop bodies (for and while) are not sharable (because they are multiple).
     * Object literal parts, class declarations, and function definition are
     * separate contexts (thus not sharable).
     */
    private abstract class TreeScannerWithinChunk extends JavafxTreeScanner {

        @Override
        public void visitForExpression(JFXForExpression that) {
            for (ForExpressionInClauseTree cl : that.getInClauses()) {
                JFXForExpressionInClause clause = (JFXForExpressionInClause) cl;
                // Don't process induction var
                scan(clause.getSequenceExpression());
                scan(clause.getWhereExpression());
            }
            // skip body
            }

        @Override
        public void visitCatch(JFXCatch tree) {
            // Skip param
            scan(tree.body);
        }

        @Override
        public void visitWhileLoop(JFXWhileLoop tree) {
            scan(tree.cond);
            // skip body
        }

        @Override
        public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        }

        @Override
        public void visitClassDeclaration(JFXClassDeclaration that) {
        }

        @Override
        public void visitFunctionValue(JFXFunctionValue that) {
        }

        @Override
        public void visitFunctionDefinition(JFXFunctionDefinition that) {
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
        class InflationChecker extends TreeScannerWithinChunk {

            boolean needed = false;

            @Override
            public void visitVar(JFXVar that) {
                // Check for bound or triggered
                needed |= that.isBound();
                needed |= that.getOnReplace() != null;
                needed |= that.getOnInvalidate() != null;
                super.visitVar(that);
            }

            @Override
            public void visitClassDeclaration(JFXClassDeclaration that) {
                // Class declaration may reference (non-final) locals
                needed = true;
            }

            @Override
            public void visitFunctionValue(JFXFunctionValue that) {
                // Funtion value may reference (non-final) locals
                needed = true;
            }

            @Override
            public void visitFunctionDefinition(JFXFunctionDefinition that) {
                // Function declaration may reference (non-final) locals
                needed = true;
            }


            @Override
            public void visitFunctionInvocation(JFXFunctionInvocation tree) {
                Symbol msym = JavafxTreeInfo.symbol(tree.meth);

                // If the function call is the magic Pointer.make(Object)
                // function and argument involves a local var, then make a local
                // context class.
                boolean magicPointerMakeFunction = (msym != null) &&
                    (msym.flags_field & JavafxFlags.FUNC_POINTER_MAKE) != 0;
                if (magicPointerMakeFunction) {
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

    private Scope getEnclosingScope(Symbol s) {
        if (s.owner.kind == Kinds.TYP) {
            return ((ClassSymbol)s.owner).members();
        }
        else if (s.owner.kind == Kinds.PCK) {
            return ((PackageSymbol)s.owner).members();
        }
        else
            return null;
    }

    private JavafxClassSymbol makeClassSymbol(Name name) {
        JavafxClassSymbol classSym = (JavafxClassSymbol)reader.defineClass(name, owner);
        classSym.flatname = chk.localClassName(classSym);
        chk.compiled.put(classSym.flatname, classSym);

        // we may be able to get away without any scope stuff
        //  s.enter(sym);

        // Fill out class fields.
        classSym.completer = null;
        /*
         * These class symbol flags of the local classes are used in translation.
         * For FX_SYNTHETIC_LOCAL_CLASS classes, initialize$ is not called from
         * Java entry constructor so that function code executes in source order.
         * See comment in JavafxInitializationBuilder.makeJavaEntryConstructor().
         * FX_BOUND_FUNCTION_CLASS classes are treated specially for many aspects
         * like handling Pointer/FXObject+varNum registration stuff for bound
         * function implementation.
         */
        classSym.flags_field = JavafxFlags.FX_SYNTHETIC_LOCAL_CLASS;
        if (classSym.owner instanceof MethodSymbol &&
            (classSym.owner.flags() & JavafxFlags.BOUND) != 0L) {
            classSym.flags_field |= JavafxFlags.FX_BOUND_FUNCTION_CLASS;
        }
        classSym.sourcefile = env.toplevel.sourcefile;
        classSym.members_field = new Scope(classSym);

        ClassType ct = (ClassType) classSym.type;
        // We are seeing a local or inner class.
        // Set outer_field of this class to closest enclosing class
        // which contains this class in a non-static context
        // (its "enclosing instance class"), provided such a class exists.
        Symbol owner1 = owner;
        while ((owner1.kind & (Kinds.VAR | Kinds.MTH)) != 0 &&
                (owner1.flags_field & Flags.STATIC) == 0) {
            owner1 = owner1.owner;
        }
        if (owner1.kind == Kinds.TYP) {
            ct.setEnclosingType(owner1.type);
        }

        ct.supertype_field = syms.javafx_FXBaseType;
        classSym.addSuperType(syms.javafx_FXBaseType);

        return classSym;
    }

    private MethodSymbol makeDummyMethodSymbol(Symbol owner) {
        return new MethodSymbol(Flags.BLOCK, names.empty, null, owner);
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
    private void inflateToClass(JFXBlock tree) {
        final Name funcName = syntheticName("doit$");
        final Name className = syntheticName("local_klass");

        final JavafxClassSymbol classSym = makeClassSymbol(className);

        final MethodType funcType = new MethodType(
                List.<Type>nil(),    // arg types
                tree.type,           // return type
                List.<Type>nil(),    // Throws type
                classSym.type.tsym); // TypeSymbol
        final MethodSymbol funcSym = new MethodSymbol(0L, funcName, funcType, classSym);

        class VarAndClassConverter extends TreeScannerWithinChunk {

            ListBuffer<JFXTree> vars = ListBuffer.lb();
            boolean inInstanciateLocalVars = false;

            /**
             * Convert any variables with the local context into a VarInit
             */
            private JFXExpression convertExprAndScan(JFXExpression expr) {
                if (expr instanceof JFXVar) {
                    JFXVar var = (JFXVar) expr;
                    vars.append(var);
                    Scope oldScope = getEnclosingScope(var.sym);
                    if (oldScope != null)
                        oldScope.remove(var.sym);
                    while (classSym.members().lookup(var.sym.name).sym != null) {
                        var.sym.name = var.sym.name.append('$', var.sym.name);
                    }
                    classSym.members().enter(var.sym);
                    var.sym.owner = classSym;
                    Symbol prevOwner = owner;
                    owner = makeDummyMethodSymbol(classSym);
                    scan(var.getInitializer());
                    scan(var.getOnReplace());
                    scan(var.getOnInvalidate());
                    owner = prevOwner;

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
            public void visitInstanciate(JFXInstanciate tree) {
                scan(tree.getIdentifier());
                scan(tree.getArgs());
                scan(tree.getParts());
                inInstanciateLocalVars = true;
                scan(tree.getLocalvars());
                inInstanciateLocalVars = false;
                scan(tree.getClassBody());
            }

            @Override
            public void visitVar(JFXVar that) {
                if (!inInstanciateLocalVars && (that.mods.flags & Flags.PARAMETER) == 0L)
                    throw new AssertionError("all vars should have been processed in the block expression");
            }

            @Override
            public void visitClassDeclaration(JFXClassDeclaration that) {
                Scope oldScope = getEnclosingScope(that.sym);
                if (oldScope != null)
                    oldScope.remove(that.sym);
                ((ClassType)that.type).setEnclosingType(classSym.type);
                that.sym.owner = funcSym;
                classSym.members().enter(that.sym);
            }

            @Override
            public void visitBlockExpression(JFXBlock that) {
                ListBuffer<JFXExpression> stmts = ListBuffer.lb();
                for (JFXExpression stat : that.stats) {
                    stmts.append(convertExprAndScan(stat));
                }
                // Replace the guts of the block-expression with the var-converted versions
                that.stats = stmts.toList();
                that.value = convertExprAndScan(that.value);
            }
        }
        VarAndClassConverter vc = new VarAndClassConverter();
        vc.scan(tree);

        // set position of class etc as block-expression position
        fxmake.at(tree.pos());  

        // Create whose vars are the block's vars and having a doit function with the content

        JFXType fxtype = fxmake.TypeUnknown();
        fxtype.type = tree.type;

        JFXBlock body = fxmake.Block(tree.flags, tree.getStmts(), tree.getValue());
        body.type = tree.type;

        JFXFunctionDefinition doit = fxmake.FunctionDefinition(
                fxmake.Modifiers(JavafxFlags.SCRIPT_PRIVATE),
                funcName,
                fxtype,
                List.<JFXVar>nil(),
                body);
        doit.sym = funcSym;
        doit.type = funcType;

        JFXClassDeclaration cdecl = fxmake.ClassDeclaration(
                fxmake.Modifiers(Flags.SYNTHETIC),
                className,
                List.<JFXExpression>nil(),
                vc.vars.append(doit).toList());
        cdecl.sym = classSym;
        cdecl.type = classSym.type;
        types.addFxClass(classSym, cdecl);
        cdecl.setDifferentiatedExtendingImplementing(List.<JFXExpression>nil(), List.<JFXExpression>nil(), List.<JFXExpression>nil());

        JFXIdent classId = fxmake.Ident(className);
        classId.sym = classSym;
        classId.type = classSym.type;

        JFXInstanciate inst = fxmake.InstanciateNew(classId, null);
        inst.sym = classSym;
        inst.type = classSym.type;

        JFXSelect select = fxmake.Select(inst, funcName);
        select.sym = funcSym;
        select.type = funcSym.type;

        JFXFunctionInvocation apply = fxmake.Apply(null, select, null);
        apply.type = tree.type;


        // Replace the guts of the block-expression with the class wrapping the previous body
        // and a call to the doit function of that class.
        tree.stats = List.<JFXExpression>of(cdecl);
        tree.value = apply;
    }

    private abstract class ChunkBreakingTreeScanner extends JavafxTreeScanner {
        @Override
        public void visitInterpolateValue(JFXInterpolateValue that) {
            //TODO: this probably needs special processing
            super.visitInterpolateValue(that);
        }

        @Override
        public void visitKeyFrameLiteral(JFXKeyFrameLiteral that) {
            //TODO: this probably needs special processing
            super.visitKeyFrameLiteral(that);
        }

        @Override
        public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
            // Process separately, but it has its own context and
            // we don't care if it was inflated
            descendThroughChunksReportIfThisChunkInflated(that.getExpression());
        }

        @Override
        public void visitClassDeclaration(JFXClassDeclaration that) {
            Symbol prevOwner = owner;
            owner = that.sym;
            for (JFXTree member : that.getMembers()) {
                descendThroughChunksReportIfThisChunkInflated(member);
            }
            owner = prevOwner;
        }

        @Override
        public void visitFunctionValue(JFXFunctionValue that) {
            descendThroughChunksReportIfThisChunkInflated(that.getBodyExpression());
        }

        @Override
        public void visitFunctionDefinition(JFXFunctionDefinition that) {
            Symbol prevOwner = owner;
            owner = that.sym;
            if (that.isBound()) {
                MethodSymbol oldSym = that.sym;
                MethodType oldFuncType = oldSym.type.asMethodType();
                MethodType newFuncType = new MethodType(
                    oldFuncType.getParameterTypes(), // arg types
                    syms.javafx_PointerType,         // return type
                    oldFuncType.getThrownTypes(),    // Throws type
                    oldFuncType.tsym);               // TypeSymbol
                that.sym = new MethodSymbol(oldSym.flags(), oldSym.name, newFuncType, oldSym.owner);

                /*
                 * For bound functions, make a synthetic bound variable with
                 * initialization expression to be the return expression and return
                 * the Pointer of the synthetic variable as the result.
                 */
                JFXBlock blk = that.getBodyExpression();
                if (blk != null) {
                    JFXExpression returnExpr = (blk.value instanceof JFXReturn) ? ((JFXReturn) blk.value).getExpression() : blk.value;
                    if (returnExpr != null) {
                        fxmake.at(blk.value.pos);
                        ListBuffer<JFXExpression> stmts = ListBuffer.lb();
                        /*
                         * Generate a local variable for each parameter. We will later
                         * transform each param as FXObject+varNum pair during translation.
                         * These locals will be converted into instance variables of the
                         * local context class.
                         */
                        for (JFXVar fxVar : that.getParams()) {
                            JFXVar localVar = fxmake.Var(
                                fxVar.name,
                                fxVar.getJFXType(),
                                fxVar.mods,
                                null,
                                JavafxBindStatus.UNIDIBIND, null, null);
                            localVar.type = fxVar.type;
                            localVar.sym = fxVar.sym;
                            stmts.append(localVar);
                        }

                        stmts.appendList(blk.stats);

                        // is return expression a variable declaration?
                        boolean returnExprIsVar = (returnExpr.getFXTag() == JavafxTag.VAR_DEF);
                        if (returnExprIsVar) {
                            stmts.append(returnExpr);
                        }
                        JFXVar returnVar = fxmake.Var(
                                defs.boundFunctionResultName,
                                fxmake.TypeUnknown(),
                                fxmake.Modifiers(0), 
                                returnExprIsVar? fxmake.Ident((JFXVar)returnExpr) : returnExpr,
                                JavafxBindStatus.UNIDIBIND, null, null);
                        returnVar.type = oldSym.type.getReturnType();
                        returnVar.sym = new VarSymbol(0L, defs.boundFunctionResultName, returnVar.type, that.sym);
                        returnVar.markBound(JavafxBindStatus.UNIDIBIND);
                        stmts.append(returnVar);

                        // find the symbol of Pointer.make(Object) method.
                        // The select expression Pointer.make
                        Type pointerMakeType = pointerMakeMethodSym().type;
                        JFXSelect select = fxmake.Select(fxmake.Type(syms.javafx_PointerType), defs.make_PointerMethodName);
                        select.sym = pointerMakeMethodSym();
                        select.sym.flags_field |= JavafxFlags.FUNC_POINTER_MAKE;
                        select.type = pointerMakeType;


                        // args for Pointer.make(Object)
                        JFXExpression ident = fxmake.Ident(returnVar);
                        ident.type = returnVar.type;
                        ListBuffer<JFXExpression> pointerMakeArgs = ListBuffer.lb();
                        pointerMakeArgs.append(ident);

                        // call Pointer.make($$bound$result$)
                        JFXFunctionInvocation apply = fxmake.Apply(null, select, pointerMakeArgs.toList());
                        apply.type = syms.javafx_PointerType;

                        blk.stats = stmts.toList();
                        blk.value = apply;
                        blk.type = apply.type;
                    }
                }
            }
            descendThroughChunksReportIfThisChunkInflated(that.operation);
            owner = prevOwner;
        }

        @Override
        public void visitOnReplace(JFXOnReplace that) {
            Symbol prevOwner = owner;
            owner = makeDummyMethodSymbol(owner);
            descendThroughChunksReportIfThisChunkInflated(that.getBody());
            owner = prevOwner;
        }

        @Override
        public void visitInitDefinition(JFXInitDefinition that) {
            Symbol prevOwner = owner;
            owner = that.sym;
            descendThroughChunksReportIfThisChunkInflated(that.getBody());
            owner = prevOwner;
        }

        @Override
        public void visitPostInitDefinition(JFXPostInitDefinition that) {
            Symbol prevOwner = owner;
            owner = that.sym;
            descendThroughChunksReportIfThisChunkInflated(that.getBody());
            owner = prevOwner;
        }

        @Override
        public void visitVar(JFXVar that) {
            Symbol prevOwner = owner;
            owner = makeDummyMethodSymbol(owner);
            descendThroughChunksReportIfThisChunkInflated(that.getInitializer());
            owner = prevOwner;
            descendThroughChunksReportIfThisChunkInflated(that.getOnReplace());
            descendThroughChunksReportIfThisChunkInflated(that.getOnInvalidate());
        }
    }

    /**
     * Process all contained chunks (see above).  Ones outside our context (other
     * classes, functions, etc) get inflated, but we don't care.
     * Those inside our context (loop bodies, etc) force us to be inflated, as does
     * our (independent) need for inflation (bound vars, etc).
     * Call the inflator (if needed).
     * @return True if the chunk was inflated
     */
    private boolean processBlockChunk(JFXBlock tree) {
        class Chunker extends ChunkBreakingTreeScanner {

            boolean containedChunkInflated = false;

            @Override
            public void visitForExpression(JFXForExpression that) {
                for (ForExpressionInClauseTree cl : that.getInClauses()) {
                    JFXForExpressionInClause clause = (JFXForExpressionInClause) cl;
                    scan(clause);
                }
                containedChunkInflated |= descendThroughChunksReportIfThisChunkInflated(that.getBodyExpression());
            }

            @Override
            public void visitWhileLoop(JFXWhileLoop that) {
                scan(that.cond);
                containedChunkInflated |= descendThroughChunksReportIfThisChunkInflated(that.body);
            }
        }
        Chunker chunk = new Chunker();
        chunk.scan(tree);
        if (needsToBeInflatedToClass(tree) || chunk.containedChunkInflated) {
            inflateToClass(tree);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Recurse all the way down the AST, breaking into chunks.
     * Skip any non-var containing cruft, and report if anything within this
     * chunk gets inflated.
     * @param tree
     * @return True if something inside (but within this chunk) gets inflated.
     */
    private boolean descendThroughChunksReportIfThisChunkInflated(JFXTree tree) {
        class Skipper extends ChunkBreakingTreeScanner {

            boolean containedChunkInflated = false;

            @Override
            public void visitBlockExpression(JFXBlock that) {
                containedChunkInflated |= processBlockChunk(that);
            }
        }
        Skipper skp = new Skipper();
        skp.scan(tree);
        return skp.containedChunkInflated;
    }

    private Name syntheticName(String prefix) {
        return names.fromString(prefix + ++tmpCount);
    }

}

