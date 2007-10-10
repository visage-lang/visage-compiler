/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.comp;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.tree.JCTree.*;

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;

import static com.sun.tools.javac.tree.JCTree.SELECT;

import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.code.JavafxBindStatus;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxVarSymbol;

import javax.tools.JavaFileObject;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;


/** This is the second phase of Enter, in which classes are completed
 *  by entering their members into the class scope using
 *  MemberEnter.complete().  See Enter for an overview.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxMemberEnter extends JavafxTreeScanner implements JavafxVisitor, Completer {
    protected static final Context.Key<JavafxMemberEnter> javafxMemberEnterKey =
        new Context.Key<JavafxMemberEnter>();
    
// JavaFX change
    protected
    final static boolean checkClash = true;

    private final Name.Table names;
    private final JavafxEnter enter;
    private final Log log;
    private final JavafxCheck chk;
    private final JavafxAttr attr;
    private final JavafxSymtab syms;
    private final JavafxTreeMaker make;
    private final ClassReader reader;
    private final JavafxTodo todo;
    private final JavafxAnnotate annotate;
    private final Types types;
    private final Target target;

    private final boolean skipAnnotations;
    private boolean isInMethodParamVars;
    private boolean isVarArgs;
    private List<MethodInferTypeHelper> methodsToInferReturnType;
    private Type methodReturnType;
    private JavafxEnv<JavafxAttrContext> localEnv;
    
    public static JavafxMemberEnter instance(Context context) {
        JavafxMemberEnter instance = context.get(javafxMemberEnterKey);
        if (instance == null)
            instance = new JavafxMemberEnter(context);
        return instance;
    }

    protected JavafxMemberEnter(Context context) {
        
        context.put(javafxMemberEnterKey, this);
        names = Name.Table.instance(context);
        enter = JavafxEnter.instance(context);
        log = Log.instance(context);
        chk = (JavafxCheck)JavafxCheck.instance(context);
        attr = JavafxAttr.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        reader = ClassReader.instance(context);
        todo = JavafxTodo.instance(context);
        annotate = JavafxAnnotate.instance(context);
        types = Types.instance(context);
        target = Target.instance(context);

        skipAnnotations =
            Options.instance(context).get("skipAnnotations") != null;
    }


    /** A queue for classes whose members still need to be entered into the
     *  symbol table.
     */
    ListBuffer<JavafxEnv<JavafxAttrContext>> halfcompleted = new ListBuffer<JavafxEnv<JavafxAttrContext>>();

    /** Set to true only when the first of a set of classes is
     *  processed from the halfcompleted queue.
     */
    boolean isFirst = true;

    /** A flag to disable completion from time to time during member
     *  enter, as we only need to look up types.  This avoids
     *  unnecessarily deep recursion.
     */
    boolean completionEnabled = true;

    /* ---------- Processing import clauses ----------------
     */

    /** Import all classes of a class or package on demand.
     *  @param pos           Position to be used for error reporting.
     *  @param tsym          The class or package the members of which are imported.
     *  @param toScope   The (import) scope in which imported classes
     *               are entered.
     */
// JavaFX change
    protected void importAll(int pos,
                           final TypeSymbol tsym,
                           JavafxEnv<JavafxAttrContext> env) {
        // Check that packages imported from exist (JLS ???).
        if (tsym.kind == PCK && tsym.members().elems == null && !tsym.exists()) {
            // If we can't find java.lang, exit immediately.
            if (((PackageSymbol)tsym).fullname.equals(names.java_lang)) {
                JCDiagnostic msg = JCDiagnostic.fragment("fatal.err.no.java.lang");
                throw new FatalError(msg);
            } else {
                log.error(pos, "doesnt.exist", tsym);
            }
        }
        final Scope fromScope = tsym.members();
        final Scope toScope = env.toplevel.starImportScope;
        for (Scope.Entry e = fromScope.elems; e != null; e = e.sibling) {
            if (e.sym.kind == TYP && !toScope.includes(e.sym))
                toScope.enter(e.sym, fromScope);
        }
    }

    /** Import all static members of a class or package on demand.
     *  @param pos           Position to be used for error reporting.
     *  @param tsym          The class or package the members of which are imported.
     *  @param toScope   The (import) scope in which imported classes
     *               are entered.
     */
    private void importStaticAll(int pos,
                                 final TypeSymbol tsym,
                                 JavafxEnv<JavafxAttrContext> env) {
        final JavaFileObject sourcefile = env.toplevel.sourcefile;
        final Scope toScope = env.toplevel.starImportScope;
        final PackageSymbol packge = env.toplevel.packge;
        final TypeSymbol origin = tsym;

        // enter imported types immediately
        new Object() {
            Set<Symbol> processed = new HashSet<Symbol>();
            void importFrom(TypeSymbol tsym) {
                if (tsym == null || !processed.add(tsym))
                    return;

                // also import inherited names
                importFrom(types.supertype(tsym.type).tsym);
                for (Type t : types.interfaces(tsym.type))
                    importFrom(t.tsym);

                final Scope fromScope = tsym.members();
                for (Scope.Entry e = fromScope.elems; e != null; e = e.sibling) {
                    Symbol sym = e.sym;
                    if (sym.kind == TYP &&
                        (sym.flags() & STATIC) != 0 &&
                        staticImportAccessible(sym, packge) &&
                        sym.isMemberOf(origin, types) &&
                        !toScope.includes(sym))
                        toScope.enter(sym, fromScope, origin.members());
                }
            }
        }.importFrom(tsym);

        // enter non-types before annotations that might use them
        annotate.earlier(new JavafxAnnotate.Annotator() {
            Set<Symbol> processed = new HashSet<Symbol>();

            public String toString() {
                return "import static " + tsym + ".*" + " in " + sourcefile;
            }
            void importFrom(TypeSymbol tsym) {
                if (tsym == null || !processed.add(tsym))
                    return;

                // also import inherited names
                importFrom(types.supertype(tsym.type).tsym);
                for (Type t : types.interfaces(tsym.type))
                    importFrom(t.tsym);

                final Scope fromScope = tsym.members();
                for (Scope.Entry e = fromScope.elems; e != null; e = e.sibling) {
                    Symbol sym = e.sym;
                    if (sym.isStatic() && sym.kind != TYP &&
                        staticImportAccessible(sym, packge) &&
                        !toScope.includes(sym) &&
                        sym.isMemberOf(origin, types)) {
                        toScope.enter(sym, fromScope, origin.members());
                    }
                }
            }
            public void enterAnnotation() {
                importFrom(tsym);
            }
        });
    }

    // is the sym accessible everywhere in packge?
    boolean staticImportAccessible(Symbol sym, PackageSymbol packge) {
        int flags = (int)(sym.flags() & AccessFlags);
        switch (flags) {
        default:
        case PUBLIC:
            return true;
        case PRIVATE:
            return false;
        case 0:
        case PROTECTED:
            return sym.packge() == packge;
        }
    }

    /** Import statics types of a given name.  Non-types are handled in Attr.
     *  @param pos           Position to be used for error reporting.
     *  @param tsym          The class from which the name is imported.
     *  @param name          The (simple) name being imported.
     *  @param env           The environment containing the named import
     *                  scope to add to.
     */
    private void importNamedStatic(final DiagnosticPosition pos,
                                   final TypeSymbol tsym,
                                   final Name name,
                                   final JavafxEnv<JavafxAttrContext> env) {
        if (tsym.kind != TYP) {
            log.error(pos, "static.imp.only.classes.and.interfaces");
            return;
        }

        final Scope toScope = env.toplevel.namedImportScope;
        final PackageSymbol packge = env.toplevel.packge;
        final TypeSymbol origin = tsym;

        // enter imported types immediately
        new Object() {
            Set<Symbol> processed = new HashSet<Symbol>();
            void importFrom(TypeSymbol tsym) {
                if (tsym == null || !processed.add(tsym))
                    return;

                // also import inherited names
                importFrom(types.supertype(tsym.type).tsym);
                for (Type t : types.interfaces(tsym.type))
                    importFrom(t.tsym);

                for (Scope.Entry e = tsym.members().lookup(name);
                     e.scope != null;
                     e = e.next()) {
                    Symbol sym = e.sym;
                    if (sym.isStatic() &&
                        sym.kind == TYP &&
                        staticImportAccessible(sym, packge) &&
                        sym.isMemberOf(origin, types) &&
                        chk.checkUniqueStaticImport(pos, sym, toScope))
                        toScope.enter(sym, sym.owner.members(), origin.members());
                }
            }
        }.importFrom(tsym);

        // enter non-types before annotations that might use them
        annotate.earlier(new JavafxAnnotate.Annotator() {
            Set<Symbol> processed = new HashSet<Symbol>();
            boolean found = false;

            public String toString() {
                return "import static " + tsym + "." + name;
            }
            void importFrom(TypeSymbol tsym) {
                if (tsym == null || !processed.add(tsym))
                    return;

                // also import inherited names
                importFrom(types.supertype(tsym.type).tsym);
                for (Type t : types.interfaces(tsym.type))
                    importFrom(t.tsym);

                for (Scope.Entry e = tsym.members().lookup(name);
                     e.scope != null;
                     e = e.next()) {
                    Symbol sym = e.sym;
                    if (sym.isStatic() &&
                        staticImportAccessible(sym, packge) &&
                        sym.isMemberOf(origin, types)) {
                        found = true;
                        if (sym.kind == MTH ||
                            sym.kind != TYP && chk.checkUniqueStaticImport(pos, sym, toScope))
                            toScope.enter(sym, sym.owner.members(), origin.members());
                    }
                }
            }
            public void enterAnnotation() {
                JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
                try {
                    importFrom(tsym);
                    if (!found) {
                        log.error(pos, "cant.resolve.location",
                                  JCDiagnostic.fragment("kindname.static"),
                                  name, "", "", JavafxResolve.typeKindName(tsym.type),
                                  tsym.type);
                    }
                } finally {
                    log.useSource(prev);
                }
            }
        });
    }

    /** Import given class.
     *  @param pos           Position to be used for error reporting.
     *  @param tsym          The class to be imported.
     *  @param env           The environment containing the named import
     *                  scope to add to.
     */
    void importNamed(DiagnosticPosition pos, Symbol tsym, JavafxEnv<JavafxAttrContext> env) {
        if (tsym.kind == TYP &&
            chk.checkUniqueImport(pos, tsym, env.toplevel.namedImportScope))
            env.toplevel.namedImportScope.enter(tsym, tsym.owner.members());
    }

    private Type signature(List<JFXVar> params,
                   Type restype,
                   JavafxEnv<JavafxAttrContext> env) {
        // Enter and attribute value parameters.
        ListBuffer<Type> argbuf = new ListBuffer<Type>();
        for (List<JFXVar> l = params; l.nonEmpty(); l = l.tail) {
            memberEnter(l.head, env);
            argbuf.append(l.head.getJFXType().type);
        }

        Type mtype = new MethodType(argbuf.toList(),
                                    restype,
                                    List.<Type>nil(),
                                    syms.methodClass);
        return mtype;
    }

/* ********************************************************************
 * Visitor methods for member enter
 *********************************************************************/

    /** Visitor argument: the current environment
     */
    protected JavafxEnv<JavafxAttrContext> env;

    /** Enter field and method definitions and process import
     *  clauses, catching any completion failure exceptions.
     */
    void memberEnter(JCTree tree, JavafxEnv<JavafxAttrContext> env) {
        JavafxEnv<JavafxAttrContext> prevEnv = this.env;
        try {
            this.env = env;
            tree.accept(this);
        }  catch (CompletionFailure ex) {
            chk.completionError(tree.pos(), ex);
        } finally {
            this.env = prevEnv;
        }
    }

    /** Enter members from a list of trees.
     */
    void memberEnter(List<? extends JCTree> trees, JavafxEnv<JavafxAttrContext> env) {
        for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail)
            memberEnter(l.head, env);
    }

    /** Create a fresh environment for a variable's initializer.
     *  If the variable is a field, the owner of the environment's scope
     *  is be the variable itself, otherwise the owner is the method
     *  enclosing the variable definition.
     *
     *  @param tree     The variable definition.
     *  @param env      The environment current outside of the variable definition.
     */
    JavafxEnv<JavafxAttrContext> initEnv(JCVariableDecl tree, JavafxEnv<JavafxAttrContext> env) {
        JavafxEnv<JavafxAttrContext> localEnv = env.dupto(new JavafxAttrContextEnv(tree, env.info.dup()));
        if (tree.sym.owner.kind == TYP) {
            localEnv.info.scope = new Scope.DelegatedScope(env.info.scope);
            localEnv.info.scope.owner = tree.sym;
        }
        if ((tree.mods.flags & STATIC) != 0 ||
            (env.enclClass.sym.flags() & INTERFACE) != 0)
            localEnv.info.staticLevel++;
        return localEnv;
    }

    @Override
    public void visitTree(JCTree tree) {
        if (tree instanceof JFXBlockExpression)
            visitBlockExpression((JFXBlockExpression) tree);
        else
            super.visitTree(tree);
    }

    @Override
    public void visitErroneous(JCErroneous tree) {
        memberEnter(tree.errs, env);
    }
    
    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        if (tree.starImportScope.elems != null) {
            // we must have already processed this toplevel
            return;
        }

        // check that no class exists with same fully qualified name as
        // toplevel package
        if (checkClash && tree.pid != null) {
            Symbol p = tree.packge;
            while (p.owner != syms.rootPackage) {
                p.owner.complete(); // enter all class members of p
                if (syms.classes.get(p.getQualifiedName()) != null) {
                    log.error(tree.pos,
                              "pkg.clashes.with.class.of.same.name",
                              p);
                }
                p = p.owner;
            }
        }

        // process package annotations
        annotateLater(tree.packageAnnotations, env, tree.packge);

        // Import-on-demand the JavaFX types 
        importNamed(tree.pos(), syms.javafx_StringType.tsym, env);
        importNamed(tree.pos(), syms.javafx_IntegerType.tsym, env);
        importNamed(tree.pos(), syms.javafx_BooleanType.tsym, env);
        importNamed(tree.pos(), syms.javafx_NumberType.tsym, env);

        // Process all import clauses.
        memberEnter(tree.defs, env);
    }

    @Override
    public void visitImport(JCImport tree) {
        if (!tree.isStatic()) {
            if (tree.qualid.getTag() == SELECT) {
                if (((JCFieldAccess)tree.qualid).name == names.fromString("Integer")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.integer.primitive.type");
                }
                else if (((JCFieldAccess)tree.qualid).name == names.fromString("Number")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.number.primitive.type");
                }
                else if (((JCFieldAccess)tree.qualid).name == names.fromString("Boolean")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.boolean.primitive.type");
                }
                else if (((JCFieldAccess)tree.qualid).name == names.fromString("String")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.string.primitive.type");
                }
            }
        }
        
        JCTree imp = tree.qualid;
        Name name = TreeInfo.name(imp);
        TypeSymbol p;

        // Create a local environment pointing to this tree to disable
        // effects of other imports in Resolve.findGlobalType
        JavafxEnv<JavafxAttrContext> localEnv = env.dup(tree);

        // Attribute qualifying package or class.
        JCFieldAccess s = (JCFieldAccess) imp;
        p = attr.
            attribTree(s.selected,
                       localEnv,
                       tree.staticImport ? TYP : (TYP | PCK),
                       Type.noType).tsym;
        if (name == names.asterisk) {
            // Import on demand.
            chk.checkCanonical(s.selected);
            if (tree.staticImport)
                importStaticAll(tree.pos, p, env);
            else
                importAll(tree.pos, p, env);
        } else {
            // Named type import.
            if (tree.staticImport) {
                importNamedStatic(tree.pos(), p, name, localEnv);
                chk.checkCanonical(s.selected);
            } else {
                TypeSymbol c = attribImportType(imp, localEnv).tsym;
                chk.checkCanonical(imp);
                importNamed(tree.pos(), c, env);
            }
        }
    }
    
    protected void finishClass(JCClassDecl tree, JavafxEnv<JavafxAttrContext> env) {
        List<MethodInferTypeHelper> prevMethodsToInferReturnType;
        prevMethodsToInferReturnType = methodsToInferReturnType;
        methodsToInferReturnType = List.nil();
        //JavaFX remove
        memberEnter(tree.defs, env);
        inferMethodReturnTypes();
        methodsToInferReturnType = prevMethodsToInferReturnType;
    }

    /** Create a fresh environment for method bodies.
     *  @param tree     The method definition.
     *  @param env      The environment current outside of the method definition.
     */
    JavafxEnv<JavafxAttrContext> methodEnv(JFXOperationDefinition tree, JavafxEnv<JavafxAttrContext> env) {
        JavafxEnv<JavafxAttrContext> localEnv =
            env.dup(tree, env.info.dup(env.info.scope.dupUnshared()));
        localEnv.enclMethod = tree;
        localEnv.info.scope.owner = tree.sym;
        if ((tree.mods.flags & STATIC) != 0) localEnv.info.staticLevel++;
        return localEnv;
    }

    @Override
    public void visitVarDef(JCVariableDecl tree) {
        assert false : "should not be here";
    }
    
    @Override
    public void visitVar(JFXVar tree) {
        JavafxEnv<JavafxAttrContext> localEnv = env;
        if ((tree.mods.flags & STATIC) != 0 ||
            (env.info.scope.owner.flags() & INTERFACE) != 0) {
            localEnv = env.dup(tree, env.info.dup());
            localEnv.info.staticLevel++;
        }
        attr.attribType(tree.getJFXType(), localEnv);

        Scope enclScope = enter.enterScope(env);
        VarSymbol v = new JavafxVarSymbol(0, tree.name, tree.getJFXType().type, 
                    tree.isBound(), tree.isLazy(), enclScope.owner);

        v.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, v, tree);
        tree.sym = v;
        if (tree.init != null) {
            v.flags_field |= HASINIT;
            if (tree.getJFXType().type == syms.javafx_AnyType) {
                JavafxEnv<JavafxAttrContext> initEnv = initEnv(tree, env);
                tree.getJFXType().type = attr.attribExpr(tree.init, initEnv, Type.noType);
                tree.sym.type = tree.getJFXType().type;
            }
        }
        if (chk.checkUnique(tree.pos(), v, enclScope)) {
            chk.checkTransparentVar(tree.pos(), v, enclScope);
            enclScope.enter(v);
        }
        annotateLater(tree.mods.annotations, localEnv, v);
        v.pos = tree.pos;
    }

    public void visitMethodDef(JCMethodDecl tree) {
        assert false;
    }
    
    @Override
    public void visitOperationDefinition(JFXOperationDefinition tree) {
            Scope enclScope = enter.enterScope(env);
            MethodSymbol m = new MethodSymbol(0, tree.name, null, enclScope.owner);
            m.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, m, tree);
            tree.sym = m;
            JavafxEnv<JavafxAttrContext> localEnv = methodEnv(tree, env);

            m.type = attrMethodType(tree, localEnv);
        
            // If types were not set, set them to syms.javafx_AnyType
            // TODO: Can we do some type inference in here?
            if (m != null && m.type != null && ((MethodType)m.type).argtypes != null) {
                for (List<Type> mTypes = ((MethodType)m.type).argtypes; mTypes.nonEmpty(); mTypes = mTypes.tail) {
                    if (mTypes.head == null) {
                        mTypes.head = syms.javafx_AnyType;
                    } 
                }
            }

            // mark the method varargs, if necessary
            if (isVarArgs)
                m.flags_field |= Flags.VARARGS;

            localEnv.info.scope.leave();
            if (chk.checkUnique(tree.pos(), m, enclScope)) {
                enclScope.enter(m);
            }
    }

    @Override
    public void visitReturn(JCReturn tree) {
        super.visitReturn(tree);
        if (localEnv != null) {
            attr.attribStat(tree, localEnv);
            if (tree.expr == null) {
                methodReturnType = syms.voidType;
            }
            else {
                methodReturnType = tree.expr.type;
            }
        }
    }

    private Type attrMethodType(JFXOperationDefinition opDef, JavafxEnv<JavafxAttrContext> lEnv) {
        Type returnType;
        JFXOperationValue opVal = opDef.operation;
        // Create a new environment with local scope
        // for attributing the method.
        JavafxEnv<JavafxAttrContext> localEnv = methodEnv(opDef, env);

        // Attribute all value parameters.
        boolean prevIsInMethodLocalVars = isInMethodParamVars;
        isInMethodParamVars = true;
        for (List<JFXVar> l = opVal.funParams; l.nonEmpty(); l = l.tail) {
            attr.attribStat(l.head, localEnv);
        }
        isInMethodParamVars = prevIsInMethodLocalVars;

        if (opVal.getJFXReturnType().getTag() == JavafxTag.TYPEUNKNOWN) {
            if (opVal.getBodyExpression() == null) {
                // no body, can't infer, assume Any
                returnType = syms.javafx_AnyType;
            } else {
                // infer the type from the body
                if (opVal.getBodyExpression().value == null) {
                    returnType = syms.javafx_VoidType;
                } else {
                    returnType = attr.attribExpr(opVal.getBodyExpression(), localEnv);
                }
            }
            methodsToInferReturnType = methodsToInferReturnType.append(new MethodInferTypeHelper(opDef, lEnv));
        } else {
            returnType = attr.attribType(opDef.getJFXReturnType(), localEnv);
        }
        localEnv.info.scope.leave();

        Type methType = signature(opDef.getParameters(), returnType, lEnv);

        return methType;
    }

    private void inferMethodReturnTypes() {
        if (methodsToInferReturnType != null) { 
            for (MethodInferTypeHelper methodDeclHelper : methodsToInferReturnType) {
                JFXOperationValue operation = methodDeclHelper.method.operation;
                if (operation.rettype.type == syms.javafx_AnyType &&
                          operation.getBodyExpression() != null) {
                      Type prevMethodReturnType = methodReturnType;
                      methodReturnType = null;
                      Type prevrettype = operation.rettype.type;
                      operation.rettype.type = Type.noType;
                      JavafxEnv<JavafxAttrContext> prevLocalEnv = localEnv;
                      localEnv = methodDeclHelper.lEnv;
                      if (operation.getBodyExpression() != null) {
                          memberEnter(operation.getBodyExpression(), localEnv);
                      }
                      localEnv = prevLocalEnv;
                      if (methodReturnType == null) {
                          operation.rettype.type = syms.voidType;
                          if (methodDeclHelper.method.sym != null && methodDeclHelper.method.sym.type != null &&
                              methodDeclHelper.method.sym.kind == MTH) {
                              ((MethodType)methodDeclHelper.method.sym.type).restype = syms.voidType;
                          }
                      }
                      else {
                          operation.rettype.type = methodReturnType;
                          if (methodDeclHelper.method.sym != null && methodDeclHelper.method.sym.type != null &&
                              methodDeclHelper.method.sym.kind == MTH) {
                              ((MethodType)methodDeclHelper.method.sym.type).restype = methodReturnType;
                          }
                      }
                      
                      if (operation.rettype.type == Type.noType) {
                          operation.rettype.type = prevrettype;
                      }
  
                      methodReturnType = prevMethodReturnType;
                }
            }
        }
    }

    static class MethodInferTypeHelper {
        JFXOperationDefinition method;
        JavafxEnv<JavafxAttrContext> lEnv;
        MethodInferTypeHelper(JFXOperationDefinition method,  JavafxEnv<JavafxAttrContext> lEnv) {
            this.method = method;
            this.lEnv = lEnv;
        }
    }
// Javafx modification
    // Begin JavaFX trees
    @Override
    public void visitClassDeclaration(JFXClassDeclaration that) {
    }
    
/* ********************************************************************
 * Type completion
 *********************************************************************/

    Type attribImportType(JCTree tree, JavafxEnv<JavafxAttrContext> env) {
        assert completionEnabled;
        try {
            // To prevent deep recursion, suppress completion of some
            // types.
            completionEnabled = false;
            return attr.attribType(tree, env);
        } finally {
            completionEnabled = true;
        }
    }

/* ********************************************************************
 * Annotation processing
 *********************************************************************/

    /** Queue annotations for later processing. */
// JavaFX change
    protected
// JavaFX change
    void annotateLater(final List<JCAnnotation> annotations,
                       final JavafxEnv<JavafxAttrContext> localEnv,
                       final Symbol s) {
        if (annotations.isEmpty()) return;
        if (s.kind != PCK) s.attributes_field = null; // mark it incomplete for now
        annotate.later(new JavafxAnnotate.Annotator() {
                public String toString() {
                    return "annotate " + annotations + " onto " + s + " in " + s.owner;
                }
                public void enterAnnotation() {
                    assert s.kind == PCK || s.attributes_field == null;
                    JavaFileObject prev = log.useSource(localEnv.toplevel.sourcefile);
                    try {
                        if (s.attributes_field != null &&
                            s.attributes_field.nonEmpty() &&
                            annotations.nonEmpty())
                            log.error(annotations.head.pos,
                                      "already.annotated",
                                      JavafxResolve.kindName(s), s);
                        enterAnnotations(annotations, localEnv, s);
                    } finally {
                        log.useSource(prev);
                    }
                }
            });
    }

    /**
     * Check if a list of annotations contains a reference to
     * java.lang.Deprecated.
     **/
    private boolean hasDeprecatedAnnotation(List<JCAnnotation> annotations) {
        for (List<JCAnnotation> al = annotations; al.nonEmpty(); al = al.tail) {
            JCAnnotation a = al.head;
            if (a.annotationType.type == syms.deprecatedType && a.args.isEmpty())
                return true;
        }
        return false;
    }

    /** Enter a set of annotations. */
    private void enterAnnotations(List<JCAnnotation> annotations,
                          JavafxEnv<JavafxAttrContext> env,
                          Symbol s) {
        ListBuffer<Attribute.Compound> buf =
            new ListBuffer<Attribute.Compound>();
        Set<TypeSymbol> annotated = new HashSet<TypeSymbol>();
        if (!skipAnnotations)
        for (List<JCAnnotation> al = annotations; al.nonEmpty(); al = al.tail) {
            JCAnnotation a = al.head;
            Attribute.Compound c = annotate.enterAnnotation(a,
                                                            syms.annotationType,
                                                            env);
            if (c == null) continue;
            buf.append(c);
            // Note: @Deprecated has no effect on local variables and parameters
            if (!c.type.isErroneous()
                && s.owner.kind != MTH
                && types.isSameType(c.type, syms.deprecatedType))
                s.flags_field |= Flags.DEPRECATED;
            if (!annotated.add(a.type.tsym))
                log.error(a.pos, "duplicate.annotation");
        }
        s.attributes_field = buf.toList();
    }

// Javafx change
    protected
// Javafx change
    /** Queue processing of an attribute default value. */
    void annotateDefaultValueLater(final JCExpression defaultValue,
                                   final JavafxEnv<JavafxAttrContext> localEnv,
                                   final MethodSymbol m) {
        annotate.later(new JavafxAnnotate.Annotator() {
                public String toString() {
                    return "annotate " + m.owner + "." +
                        m + " default " + defaultValue;
                }
                public void enterAnnotation() {
                    JavaFileObject prev = log.useSource(localEnv.toplevel.sourcefile);
                    try {
                        enterDefaultValue(defaultValue, localEnv, m);
                    } finally {
                        log.useSource(prev);
                    }
                }
            });
    }

    /** Enter a default value for an attribute method. */
    private void enterDefaultValue(final JCExpression defaultValue,
                                   final JavafxEnv<JavafxAttrContext> localEnv,
                                   final MethodSymbol m) {
        m.defaultValue = annotate.enterAttributeValue(m.type.getReturnType(),
                                                      defaultValue,
                                                      localEnv);
    }

/* ********************************************************************
 * Source completer
 *********************************************************************/

    /** Complete entering a class.
     *  @param sym         The symbol of the class to be completed.
     */
    public void complete(Symbol sym) throws CompletionFailure {
        // Suppress some (recursive) MemberEnter invocations
        if (!completionEnabled) {
            // Re-install same completer for next time around and return.
            assert (sym.flags() & Flags.COMPOUND) == 0;
            sym.completer = this;
            return;
        }

        ClassSymbol c = (ClassSymbol)sym;
        ClassType ct = (ClassType)c.type;
        JavafxEnv<JavafxAttrContext> env = enter.typeEnvs.get(c);
        JCClassDecl tree = (JCClassDecl)env.tree;
        boolean wasFirst = isFirst;
        isFirst = false;

        JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
        try {
            // Save class environment for later member enter (2) processing.
            halfcompleted.append(env);

            // If this is a toplevel-class, make sure any preceding import
            // clauses have been seen.
            if (c.owner.kind == PCK) {
                memberEnter(env.toplevel, env.enclosing(JCTree.TOPLEVEL));
                todo.append(env);
            }

            // Mark class as not yet attributed.
            c.flags_field |= UNATTRIBUTED;

            if (c.owner.kind == TYP)
                c.owner.complete();

            // create an environment for evaluating the base clauses
            JavafxEnv<JavafxAttrContext> baseEnv = baseEnv(tree, env);

            // Determine supertype.
            Type supertype =
                (tree.extending != null)
                ? attr.attribBase(tree.extending, baseEnv, true, false, true)
                : ((tree.mods.flags & Flags.ENUM) != 0 && !target.compilerBootstrap(c))
                ? attr.attribBase(enumBase(tree.pos, c), baseEnv,
                                  true, false, false)
                : (c.fullname == names.java_lang_Object)
                ? Type.noType
                : syms.objectType;
            ct.supertype_field = supertype;

            // Determine interfaces.
            ListBuffer<Type> interfaces = new ListBuffer<Type>();
            Set<Type> interfaceSet = new HashSet<Type>();
            List<JCExpression> interfaceTrees = tree.implementing;
            if ((tree.mods.flags & Flags.ENUM) != 0 && target.compilerBootstrap(c)) {
                // add interface Comparable<T>
                interfaceTrees =
                    interfaceTrees.prepend(make.Type(new ClassType(syms.comparableType.getEnclosingType(),
                                                                   List.of(c.type),
                                                                   syms.comparableType.tsym)));
                // add interface Serializable
                interfaceTrees =
                    interfaceTrees.prepend(make.Type(syms.serializableType));
            }
            for (JCExpression iface : interfaceTrees) {
                Type i = attr.attribBase(iface, baseEnv, false, true, true);
                if (i.tag == CLASS) {
                    interfaces.append(i);
                    chk.checkNotRepeated(iface.pos(), types.erasure(i), interfaceSet);
                }
            }
            if ((c.flags_field & ANNOTATION) != 0)
                ct.interfaces_field = List.of(syms.annotationType);
            else
                ct.interfaces_field = interfaces.toList();

            if (c.fullname == names.java_lang_Object) {
                if (tree.extending != null) {
                    chk.checkNonCyclic(tree.extending.pos(),
                                       supertype);
                    ct.supertype_field = Type.noType;
                }
                else if (tree.implementing.nonEmpty()) {
                    chk.checkNonCyclic(tree.implementing.head.pos(),
                                       ct.interfaces_field.head);
                    ct.interfaces_field = List.nil();
                }
            }

            // Annotations.
            // In general, we cannot fully process annotations yet,  but we
            // can attribute the annotation types and then check to see if the
            // @Deprecated annotation is present.
            attr.attribAnnotationTypes(tree.mods.annotations, baseEnv);
            if (hasDeprecatedAnnotation(tree.mods.annotations))
                c.flags_field |= DEPRECATED;
            annotateLater(tree.mods.annotations, baseEnv, c);

            attr.attribTypeVariables(tree.typarams, baseEnv);

            chk.checkNonCyclic(tree.pos(), c.type);

            // If this is a class, enter symbols for this and super into
            // current scope.
            if ((c.flags_field & INTERFACE) == 0) {
                VarSymbol thisSym =
                    new VarSymbol(FINAL | HASINIT, names._this, c.type, c);
                thisSym.pos = Position.FIRSTPOS;
                env.info.scope.enter(thisSym);
                if (ct.supertype_field.tag == CLASS) {
                    VarSymbol superSym =
                        new VarSymbol(FINAL | HASINIT, names._super,
                                      ct.supertype_field, c);
                    superSym.pos = Position.FIRSTPOS;
                    env.info.scope.enter(superSym);
                }
            }

            // check that no package exists with same fully qualified name,
            // but admit classes in the unnamed package which have the same
            // name as a top-level package.
            if (checkClash &&
                c.owner.kind == PCK && c.owner != syms.unnamedPackage &&
                reader.packageExists(c.fullname))
                {
                    log.error(tree.pos, "clash.with.pkg.of.same.name", c);
                }

        } catch (CompletionFailure ex) {
            chk.completionError(tree.pos(), ex);
        } finally {
            log.useSource(prev);
        }

        // Enter all member fields and methods of a set of half completed
        // classes in a second phase.
        if (wasFirst) {
            try {
                while (halfcompleted.nonEmpty()) {
                    finish(halfcompleted.next());
                }
            } finally {
                isFirst = true;
            }

            // commit pending annotations
            annotate.flush();
        }
    }

        private JavafxEnv<JavafxAttrContext> baseEnv(JCClassDecl tree, JavafxEnv<JavafxAttrContext> env) {
        Scope typaramScope = new Scope(tree.sym);
        if (tree.typarams != null)
            for (List<JCTypeParameter> typarams = tree.typarams;
                 typarams.nonEmpty();
                 typarams = typarams.tail)
                typaramScope.enter(typarams.head.type.tsym);
        JavafxEnv<JavafxAttrContext> outer = env.outer; // the base clause can't see members of this class
        JavafxEnv<JavafxAttrContext> localEnv = outer.dup(tree, outer.info.dup(typaramScope));
        localEnv.baseClause = true;
        localEnv.outer = outer;
        localEnv.info.isSelfCall = false;
        return localEnv;
    }

    /** Enter member fields and methods of a class
     *  @param env        the environment current for the class block.
     */
    private void finish(JavafxEnv<JavafxAttrContext> env) {
        JavaFileObject prev = log.useSource(env.toplevel.sourcefile);
        try {
            JCClassDecl tree = (JCClassDecl)env.tree;
            finishClass(tree, env);
        } finally {
            log.useSource(prev);
        }
    }

    /** Generate a base clause for an enum type.
     *  @param pos              The position for trees and diagnostics, if any
     *  @param c                The class symbol of the enum
     */
    private JCExpression enumBase(int pos, ClassSymbol c) {
        JCExpression result = make.at(pos).
            TypeApply(make.QualIdent(syms.enumSym),
                      List.<JCExpression>of(make.Type(c.type)));
        return result;
    }


}
