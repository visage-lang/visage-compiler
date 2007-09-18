/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
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
public class JavafxMemberEnter extends JCTree.Visitor implements JavafxVisitor, Completer {
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

    private final Name numberTypeName;
    private final Name integerTypeName;
    private final Name booleanTypeName;
    private final Name voidTypeName;  // possibly temporary

    private final boolean skipAnnotations;
    private boolean isInMethodParamVars;
    private boolean isVarArgs;
    private JCMethodDecl currentMethodDecl = null;
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
        
        numberTypeName  = names.fromString("Number");
        integerTypeName = names.fromString("Integer");
        booleanTypeName = names.fromString("Boolean");
        voidTypeName = names.fromString("Void");

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
// JavaFX change
    protected
    /*private*/ void importNamed(DiagnosticPosition pos, Symbol tsym, JavafxEnv<JavafxAttrContext> env) {
        if (tsym.kind == TYP &&
            chk.checkUniqueImport(pos, tsym, env.toplevel.namedImportScope))
            env.toplevel.namedImportScope.enter(tsym, tsym.owner.members());
    }

// Javafx change
    protected
// Javafx change
    /** Construct method type from method signature.
     *  @param typarams    The method's type parameters.
     *  @param params      The method's value parameters.
     *  @param res             The method's result type,
     *                 null if it is a constructor.
     *  @param thrown      The method's thrown exceptions.
     *  @param env             The method's (local) environment.
     */
    Type signature(List<JCTypeParameter> typarams,
                   List<JCVariableDecl> params,
                   JCTree res,
                   List<JCExpression> thrown,
                   JavafxEnv<JavafxAttrContext> env) {

        // Enter and attribute type parameters.
        List<Type> tvars = enter.classEnter(typarams, env);
        attr.attribTypeVariables(typarams, env);

        // Enter and attribute value parameters.
        ListBuffer<Type> argbuf = new ListBuffer<Type>();
        for (List<JCVariableDecl> l = params; l.nonEmpty(); l = l.tail) {
            memberEnter(l.head, env);
            argbuf.append(l.head.vartype.type);
        }

        // Attribute result type, if one is given.
        Type restype = res == null ? syms.voidType : attr.attribType(res, env);

        // Attribute thrown exceptions.
        ListBuffer<Type> thrownbuf = new ListBuffer<Type>();
        for (List<JCExpression> l = thrown; l.nonEmpty(); l = l.tail) {
            Type exc = attr.attribType(l.head, env);
            if (exc.tag != TYPEVAR)
                exc = chk.checkClassType(l.head.pos(), exc);
            thrownbuf.append(exc);
        }
        Type mtype = new MethodType(argbuf.toList(),
                                    restype,
                                    thrownbuf.toList(),
                                    syms.methodClass);
        return tvars.isEmpty() ? mtype : new ForAll(tvars, mtype);
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
// JavaFX change
    public
// JavaFX change
    /*protected*/ void memberEnter(JCTree tree, JavafxEnv<JavafxAttrContext> env) {
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
// JavaFX change
    public
    void memberEnter(List<? extends JCTree> trees, JavafxEnv<JavafxAttrContext> env) {
        for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail)
            memberEnter(l.head, env);
    }

    /** Add the implicit members for an enum type
     *  to the symbol table.
     */
    private void addEnumMembers(JCClassDecl tree, JavafxEnv<JavafxAttrContext> env) {
        JCExpression valuesType = make.Type(new ArrayType(tree.sym.type, syms.arrayClass));

        // public static T[] values() { return ???; }
        JCMethodDecl values = make.
            MethodDef(make.Modifiers(Flags.PUBLIC|Flags.STATIC),
                      names.values,
                      valuesType,
                      List.<JCTypeParameter>nil(),
                      List.<JCVariableDecl>nil(),
                      List.<JCExpression>nil(), // thrown
                      null, //make.Block(0, Tree.emptyList.prepend(make.Return(make.Ident(names._null)))),
                      null);
        memberEnter(values, env);

        // public static T valueOf(String name) { return ???; }
        JCMethodDecl valueOf = make.
            MethodDef(make.Modifiers(Flags.PUBLIC|Flags.STATIC),
                      names.valueOf,
                      make.Type(tree.sym.type),
                      List.<JCTypeParameter>nil(),
                      List.of(make.VarDef(make.Modifiers(Flags.PARAMETER),
                                            names.fromString("name"),
                                            make.Type(syms.stringType), null)),
                      List.<JCExpression>nil(), // thrown
                      null, //make.Block(0, Tree.emptyList.prepend(make.Return(make.Ident(names._null)))),
                      null);
        memberEnter(valueOf, env);

        // the remaining members are for bootstrapping only
        if (!target.compilerBootstrap(tree.sym)) return;

        // public final int ordinal() { return ???; }
        JCMethodDecl ordinal = make.at(tree.pos).
            MethodDef(make.Modifiers(Flags.PUBLIC|Flags.FINAL),
                      names.ordinal,
                      make.Type(syms.intType),
                      List.<JCTypeParameter>nil(),
                      List.<JCVariableDecl>nil(),
                      List.<JCExpression>nil(),
                      null,
                      null);
        memberEnter(ordinal, env);

        // public final String name() { return ???; }
        JCMethodDecl name = make.
            MethodDef(make.Modifiers(Flags.PUBLIC|Flags.FINAL),
                      names._name,
                      make.Type(syms.stringType),
                      List.<JCTypeParameter>nil(),
                      List.<JCVariableDecl>nil(),
                      List.<JCExpression>nil(),
                      null,
                      null);
        memberEnter(name, env);

        // public int compareTo(E other) { return ???; }
        MethodSymbol compareTo = new
            MethodSymbol(Flags.PUBLIC,
                         names.compareTo,
                         new MethodType(List.of(tree.sym.type),
                                        syms.intType,
                                        List.<Type>nil(),
                                        syms.methodClass),
                         tree.sym);
        memberEnter(make.MethodDef(compareTo, null), env);
    }

    @Override
    public void visitBlock(JCBlock block) {
        for (JCStatement tree : block.stats) {
            tree.accept(this);
        }
    }
     
    /** Create a fresh environment for a variable's initializer.
     *  If the variable is a field, the owner of the environment's scope
     *  is be the variable itself, otherwise the owner is the method
     *  enclosing the variable definition.
     *
     *  @param tree     The variable definition.
     *  @param env      The environment current outside of the variable definition.
     */
// JavaFX change
    public
// JavaFX change
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
        if ((tree.mods.flags & Flags.ENUM) != 0 &&
            (types.supertype(tree.sym.type).tsym.flags() & Flags.ENUM) == 0) {
            addEnumMembers(tree, env);
        }
        memberEnter(tree.defs, env);
        inferMethodReturnTypes();
        methodsToInferReturnType = prevMethodsToInferReturnType;
    }

    /** Create a fresh environment for method bodies.
     *  @param tree     The method definition.
     *  @param env      The environment current outside of the method definition.
     */
// Javafx modification
public
// Javafx modification
    JavafxEnv<JavafxAttrContext> methodEnv(JCMethodDecl tree, JavafxEnv<JavafxAttrContext> env) {
        JavafxEnv<JavafxAttrContext> localEnv =
            env.dup(tree, env.info.dup(env.info.scope.dupUnshared()));
        localEnv.enclMethod = tree;
        localEnv.info.scope.owner = tree.sym;
        if ((tree.mods.flags & STATIC) != 0) localEnv.info.staticLevel++;
        return localEnv;
    }

    @Override
    public void visitVarDef(JCVariableDecl tree) {
        JavafxEnv<JavafxAttrContext> localEnv = env;
        if ((tree.mods.flags & STATIC) != 0 ||
            (env.info.scope.owner.flags() & INTERFACE) != 0) {
            localEnv = env.dup(tree, env.info.dup());
            localEnv.info.staticLevel++;
        }
// Javafx change
        if (tree.vartype == null) {
            tree.vartype = make.TypeIdent(CLASS);
            tree.vartype.type = syms.javafx_AnyType;
        }
        
        if (tree.vartype.type != syms.javafx_AnyType) {
// Javafx change
            attr.attribType(tree.vartype, localEnv);
// Javafx change
        }
// Javafx change
        Scope enclScope = enter.enterScope(env);
        VarSymbol v;
        if (tree instanceof JFXVar) {
            JFXVar decl = (JFXVar)tree;
            v = new JavafxVarSymbol(0, tree.name, tree.vartype.type, 
                    decl.isBound(), decl.isLazy(), enclScope.owner);
        } else {
            v = new VarSymbol(0, tree.name, tree.vartype.type, enclScope.owner);
        }

        v.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, v, tree);
        tree.sym = v;
        if (tree.init != null) {
            v.flags_field |= HASINIT;
            /** don't want const value -- and env doesn't match
            if ((v.flags_field & FINAL) != 0 && tree.init.getTag() != JCTree.NEWCLASS)
                v.setLazyConstValue(initEnv(tree, env), log, attr, tree.init);
             **/
// Javafx change
            if (tree.vartype.type == syms.javafx_AnyType) {
                JavafxEnv<JavafxAttrContext> initEnv = initEnv(tree, env);
                tree.vartype.type = attr.attribExpr(tree.init, initEnv, Type.noType);
                tree.sym.type = tree.vartype.type;
            }
// Javafx change

        }
        if (chk.checkUnique(tree.pos(), v, enclScope)) {
            chk.checkTransparentVar(tree.pos(), v, enclScope);
            enclScope.enter(v);
        }
        annotateLater(tree.mods.annotations, localEnv, v);
        v.pos = tree.pos;
    }

    @Override
    public void visitMethodDef(JCMethodDecl tree) {
        JCMethodDecl prevMethodDecl = currentMethodDecl;
        currentMethodDecl = tree;
        try {
            Scope enclScope = enter.enterScope(env);
            MethodSymbol m = new MethodSymbol(0, tree.name, null, enclScope.owner);
            m.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, m, tree);
            tree.sym = m;
            JavafxEnv<JavafxAttrContext> localEnv = methodEnv(tree, env);

            m.type = attrMethodType(tree, localEnv);
        
// Javafx modification
            // If types were not set, set them to syms.javafx_AnyType
            // TODO: Can we do some type inference in here?
            if (m != null && m.type != null && ((MethodType)m.type).argtypes != null) {
                for (List<Type> mTypes = ((MethodType)m.type).argtypes; mTypes.nonEmpty(); mTypes = mTypes.tail) {
                    if (mTypes.head == null) {
                        mTypes.head = syms.javafx_AnyType;
                    } 
                }
            }
// Javafx modification

            // mark the method varargs, if necessary
            if (isVarArgs)
                m.flags_field |= Flags.VARARGS;

            localEnv.info.scope.leave();
            if (chk.checkUnique(tree.pos(), m, enclScope)) {
                enclScope.enter(m);
            }
            annotateLater(tree.mods.annotations, localEnv, m);
            if (tree.defaultValue != null)
                annotateDefaultValueLater(tree.defaultValue, localEnv, m);
        }
        finally {
            currentMethodDecl = prevMethodDecl;
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
// Javafx modification    
    private List<Type> checkParameterTypes(List<JCTree> defParams, List<JCTree> declParams, JavafxJCMethodDecl methodDecl) {
        isVarArgs = false;
        ListBuffer<Type> ret = new ListBuffer<Type>();
        if (declParams == null || defParams == null || declParams.size() != defParams.size()) {
            return null;
        }
        
        Iterator<JCTree> defParamIter = defParams.iterator();
        Iterator<JCTree> declParamIter = declParams.iterator();
        
        while (defParamIter.hasNext()) {
            JCTree defTree = defParamIter.next();
            JCTree declTree = declParamIter.next();
            
            if (defTree.type == null) {
                defTree.type = syms.javafx_AnyType;
            }
            
            if (declTree.type == null) {
                declTree.type = syms.javafx_AnyType;
            }

            Symbol defTypeSym = defTree.type.tsym;
            Symbol declTypeSym = declTree.type.tsym;
            
            if (defTypeSym == declTypeSym) {
                ret.append(defTree.type);
                continue;
            }
            else if (defTree.type == syms.javafx_AnyType) {
                defTree.type = declTree.type;
                ret.append(defTree.type);
            }
            else if (declTree.type == syms.javafx_AnyType) {
                declTree.type = defTree.type;
                ret.append(declTree.type);
            }
            else {
                log.error(methodDecl.pos, "javafx.different.types.of.parameters.for.definition.and.declaration");
                ret.append(syms.javafx_AnyType);
            }
        }
        
        return ret.toList();
    }
    
    private Type checkReturnTypes(JCTree defRestype, JCTree declRestype, JavafxJCMethodDecl methodDecl) {
        Type res = syms.javafx_AnyType;
        if (defRestype == null || declRestype == null) {
            return syms.javafx_AnyType;
        }
        
        if (defRestype.type == null) {
            defRestype.type = syms.javafx_AnyType;
        }
        
        if (declRestype.type == null) {
            declRestype.type = syms.javafx_AnyType;
        }

        Symbol defTypeSym = defRestype.type.tsym;
        Symbol declTypeSym = declRestype.type.tsym;
        if (defTypeSym == declTypeSym) {
            return defRestype.type;
        }
        else if (defRestype.type == syms.javafx_AnyType) {
            defRestype.type = declRestype.type;
            res = defRestype.type;
        }
        else if (declRestype.type == syms.javafx_AnyType) {
            declRestype.type = defRestype.type;
            res = declRestype.type;
        }
        else {
            log.error(methodDecl.pos, "javafx.different.types.of.result.for.definition.and.declaration");
            res = syms.javafx_AnyType;
        }
        
        return res;
    }

    private Type attrMethodType(JCMethodDecl tree, JavafxEnv<JavafxAttrContext> lEnv) {
        Type res = null;
        List<JCTree> declParams = null;
        JCTree declRestype = null;

        List<JCTree> defParams = null;
        JCTree defRestype = null;

        JavafxJCMethodDecl jfxTree = null;

        if (tree instanceof JavafxJCMethodDecl) {
            // First attribute the definition
            jfxTree = (JavafxJCMethodDecl)tree;

            List<JCTree> params = null;
            JCTree restype = null;
            JFXStatement methodDef = jfxTree.definition;

            if (methodDef != null &&
                    jfxTree.getJavafxMethodType() >= JavafxFlags.OPERATION && 
                    jfxTree.getJavafxMethodType() <= JavafxFlags.LOCAL_FUNCTION) {
                /**** TODO: RETRO code, may need to be converted, REMOVE
                switch (methodDef.getTag()) {
                    case JavafxTag.RETROOPERATIONDEF: {
                        JFXRetroOperationMemberDefinition operationMemberDef = (JFXRetroOperationMemberDefinition)methodDef;
                        params = operationMemberDef.params;
                        restype = operationMemberDef.getType();
                        break;
                    }
                    case JavafxTag.RETROFUNCTIONDEF: {
                        JFXRetroFunctionMemberDefinition functionMemberDef = (JFXRetroFunctionMemberDefinition)methodDef;
                        params = functionMemberDef.params;
                        restype = functionMemberDef.getType();
                        break;
                    }                    case JavafxTag.RETROOPERATIONLOCALDEF: {
                        JFXRetroOperationLocalDefinition operationLocalDef = (JFXRetroOperationLocalDefinition)methodDef;
                        params = operationLocalDef.params;
                        restype = operationLocalDef.restype;
                        break;
                    }

                    case JavafxTag.RETROFUNCTIONLOCALDEF: {
                        JFXRetroFunctionLocalDefinition functionLocalDef = (JFXRetroFunctionLocalDefinition)methodDef;
                        params = functionLocalDef.params;
                        restype = functionLocalDef.restype;
                        break;
                    }

                    default : {
                        throw new AssertionError("Unexpected JFXMethodDecl definition type!");
                    }
                }
                 * ***/

                if (params == null) {
                    params = List.nil();
                }

                if (restype == null) {
                    restype = make.TypeIdent(CLASS);
                    restype.type = syms.javafx_AnyType;
                }

                // Create a new environment with local scope
                // for attributing the method.
                JavafxEnv<JavafxAttrContext> localEnv = methodEnv(tree, env);

                // Attribute all value parameters.
                boolean prevIsInMethodLocalVars = isInMethodParamVars;
                isInMethodParamVars = true;
                for (List<JCTree> l = params; l.nonEmpty(); l = l.tail) {
                    attr.attribStat(l.head, localEnv);
                }
                isInMethodParamVars = prevIsInMethodLocalVars;
                defParams = params;

                // Check that result type is well-formed.
                chk.validate(restype);
                restype = restype.getTag() == JavafxTag.TYPECLASS ? ((JFXTypeClass)restype).getClassName() : restype;
                attr.attribType(restype, localEnv);

                defRestype = restype;

                localEnv.info.scope.leave();
            }

            // Then attribute the declaration
            JFXStatement methodDecl = jfxTree.declaration;

            if (methodDecl != null &&
                    jfxTree.getJavafxMethodType() >= JavafxFlags.OPERATION && 
                    jfxTree.getJavafxMethodType() <= JavafxFlags.LOCAL_FUNCTION) {
                /**** TODO: RETRO code, may need to be converted, REMOVE
                switch (methodDecl.getTag()) {
                case JavafxTag.RETROOPERATIONDECL: {
                        JFXRetroOperationMemberDeclaration operationMemberDecl = (JFXRetroOperationMemberDeclaration)methodDecl;
                        params = operationMemberDecl.params;
                        restype = operationMemberDecl.getType();
                        break;
                    }
                case JavafxTag.RETROFUNCTIONDECL: {
                        JFXRetroFunctionMemberDeclaration functionMemberDecl = (JFXRetroFunctionMemberDeclaration)methodDecl;
                        params = functionMemberDecl.params;
                        restype = functionMemberDecl.getType();
                        break;
                    }
                default : {
                        throw new AssertionError("Unexpected JFXMethodDecl declaration type!");
                    }
                }
                 * ****/

                if (params == null) {
                    params = List.nil();
                }

                if (restype == null) {
                    restype = make.TypeIdent(CLASS);
                    restype.type = syms.javafx_AnyType;
                }

                // Create a new environment with local scope
                // for attributing the method.
                JavafxEnv<JavafxAttrContext> localEnv = methodEnv(tree, env);

                // Attribute all value parameters.
                boolean prevIsInMethodLocalVars = isInMethodParamVars;
                isInMethodParamVars = true;
                for (List<JCTree> l = params; l.nonEmpty(); l = l.tail) {
                    attr.attribStat(l.head, localEnv);
                }
                isInMethodParamVars = prevIsInMethodLocalVars;

                declParams = params;

                // Check that result type is well-formed.
                chk.validate(restype);
                restype = restype.getTag() == JavafxTag.TYPECLASS ? ((JFXTypeClass)restype).getClassName() : restype;
                attr.attribType(restype, localEnv);

                declRestype = restype;

                localEnv.info.scope.leave();
            }

            if (defParams != null && declParams != null) {
                if (declParams.size() != defParams.size()) {
                    log.error(tree.pos, "javafx.different.number.of.parameters.for.definition.and.declaration");
                }
            }

            List<Type> params1 = null;
            if (jfxTree != null) {
                params1 = checkParameterTypes(defParams, declParams, jfxTree);
                Type temp = checkReturnTypes(defRestype, declRestype, jfxTree);
                if (tree.restype == null || tree.restype.type == Type.noType || tree.restype.type == syms.javafx_AnyType) {
                    if (temp.tag <= TypeTags.VOID) {
                        tree.restype = make.TypeIdent(temp.tag);
                    }
                    else {
                        tree.restype = make.Ident(temp.tsym);
                    }
                }
            }

            if (params1 != null && params.length() != params1.length()) {
                throw new AssertionError("Different number of params!");
            }
            
            if (params != null) {
                for (JCVariableDecl param : tree.params) {
                    param.type = params1 != null && params1.head != null ? params1.head : syms.javafx_AnyType;
                    if (params1 != null) {
                        params1 = params1.tail;
                    }
                }
            }
        }

        if (tree instanceof JFXOperationDefinition) {
            JFXOperationDefinition opDef = (JFXOperationDefinition)tree;
            
            List<JCTree> params = null;
            JCTree restype = null;
            if (params == null) {
                params = List.nil();
            }

            if (restype == null) {
                restype = make.TypeIdent(CLASS);
                restype.type = syms.javafx_AnyType;
            }

            // Create a new environment with local scope
            // for attributing the method.
            JavafxEnv<JavafxAttrContext> localEnv = methodEnv(tree, env);

            // Attribute all value parameters.
            boolean prevIsInMethodLocalVars = isInMethodParamVars;
            isInMethodParamVars = true;
            for (List<JCTree> l = opDef.funParams; l.nonEmpty(); l = l.tail) {
                attr.attribStat(l.head, localEnv);
            }
            isInMethodParamVars = prevIsInMethodLocalVars;

            declParams = opDef.funParams;

            // Check that result type is well-formed.
            chk.validate(restype);
            restype = restype.getTag() == JavafxTag.TYPECLASS ? ((JFXTypeClass)restype).getClassName() : restype;
            attr.attribType(restype, localEnv);

            declRestype = restype;

            if (opDef.rettype == null) {
                if (opDef.bodyExpression != null &&
                    opDef.bodyExpression.value != null) {
                    Type tp = attr.attribExpr(opDef.bodyExpression, localEnv, Type.noType);
                    opDef.restype = make.Ident(tp.tsym);
                }
                else {
                    opDef.restype = make.TypeIdent(VOID);
                }
            }
            else {
                opDef.restype = (JCExpression)opDef.rettype.getJCTypeTree();
            }
            localEnv.info.scope.leave();
        }
        
       res = signature(tree.typarams, tree.params,
                           tree.restype, tree.thrown,
                           lEnv);
       
        if (tree.restype != null && tree.restype.type == syms.javafx_AnyType) {
            methodsToInferReturnType = methodsToInferReturnType.append(new MethodInferTypeHelper(tree, lEnv));
        }
        return res;
    }

    private void inferMethodReturnTypes() {
        if (methodsToInferReturnType != null) { 
            for (MethodInferTypeHelper methodDeclHelper : methodsToInferReturnType) {
                if (methodDeclHelper.method.restype != null &&
                          methodDeclHelper.method.restype.type == syms.javafx_AnyType &&
                          methodDeclHelper.method.body != null) {
                      Type prevMethodReturnType = methodReturnType;
                      methodReturnType = null;
                      Type prevRestype = methodDeclHelper.method.restype.type;
                      methodDeclHelper.method.restype.type = Type.noType;
                      JavafxEnv<JavafxAttrContext> prevLocalEnv = localEnv;
                      localEnv = methodDeclHelper.lEnv;
                      memberEnter(methodDeclHelper.method.body, localEnv);
                      localEnv = prevLocalEnv;
                      if (methodReturnType == null) {
                          methodDeclHelper.method.restype = make.TypeIdent(VOID);
                          methodDeclHelper.method.restype.type = syms.voidType;
                          if (methodDeclHelper.method.sym != null && methodDeclHelper.method.sym.type != null &&
                              methodDeclHelper.method.sym.kind == MTH) {
                              ((MethodType)methodDeclHelper.method.sym.type).restype = syms.voidType;
                          }
                      }
                      else {
                          methodDeclHelper.method.restype = make.Type(methodReturnType);
                          methodDeclHelper.method.restype.type = methodReturnType;
                          if (methodDeclHelper.method.sym != null && methodDeclHelper.method.sym.type != null &&
                              methodDeclHelper.method.sym.kind == MTH) {
                              ((MethodType)methodDeclHelper.method.sym.type).restype = methodReturnType;
                          }
                      }
                      
                      if (methodDeclHelper.method.restype.type == Type.noType) {
                          methodDeclHelper.method.restype.type = prevRestype;
                      }
  
                      methodReturnType = prevMethodReturnType;
                }
            }
        }
    }

    static class MethodInferTypeHelper {
        JCMethodDecl method;
        JavafxEnv<JavafxAttrContext> lEnv;
        MethodInferTypeHelper(JCMethodDecl method,  JavafxEnv<JavafxAttrContext> lEnv) {
            this.method = method;
            this.lEnv = lEnv;
        }
    }
// Javafx modification
    // Begin JavaFX trees
    @Override
    public void visitClassDeclaration(JFXClassDeclaration that) {
    }
    
    @Override
    public void visitAbstractMember(JFXAbstractMember that) {
        that.modifiers.accept(this);
        if (that.getType() != null) {
            that.getType().accept((JavafxVisitor)this);
        }
    }
    
    @Override
    public void visitAbstractFunction(JFXAbstractFunction that) {
        visitAbstractMember(that);
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
    }
    
    @Override
    public void visitAttributeDefinition(JFXAttributeDefinition tree) {
        visitVar(tree);
    }
    
    @Override
    public void visitOperationDefinition(JFXOperationDefinition that) {
        visitType(that.rettype);

        that.params = buildParams(that.funParams);
        visitMethodDef(that);
    }

    @Override
    public void visitFunctionDefinitionStatement(JFXFunctionDefinitionStatement that) {
        visitOperationDefinition(that.funcDef);
    }

    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        that.getBody().accept(this);
    }

    @Override
    public void visitDoLater(JFXDoLater that) {
        that.getBody().accept(this);
    }

    @Override
    public void visitMemberSelector(JFXMemberSelector that) {
    }
    
    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty that) {
    }
    
    @Override
    public void visitSequenceRange(JFXSequenceRange that) {
        that.getLower().accept(this);
        that.getUpper().accept(this);
    }
    
    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        for (JCExpression expr : that.getItems()) {
            expr.accept(this);
        }
    }

    @Override
    public void visitStringExpression(JFXStringExpression that) {
        List<JCExpression> parts = that.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            parts.head.accept(this);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    @Override
    public void visitPureObjectLiteral(JFXPureObjectLiteral that) {
        that.getIdentifier().accept(this);
        for (JCStatement part : that.getParts()) {
            part.accept(this);
        }
    }
    
    @Override
    public void visitVarIsObjectBeingInitialized(JFXVarIsObjectBeingInitialized that) {
        visitVar(that);
    }
    
    @Override
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
    }
    
    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        that.getExpression().accept(this);
    }  
    
    @Override
    public void visitTypeAny(JFXTypeAny that) {
        visitType(that);
    }
    
    @Override
    public void visitTypeClass(JFXTypeClass that) {
        visitType(that);
    }
    
    @Override
    public void visitTypeFunctional(JFXTypeFunctional that) {
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
        that.getReturnType().accept((JavafxVisitor)this);
        visitType(that);
    }
    
    @Override
    public void visitTypeUnknown(JFXTypeUnknown that) {
        visitType(that);
    }
    
    @Override
    public void visitType(JFXType that) {
    }
    
    @Override
    public void visitVar(JFXVar that) {
        visitType(that.getJFXType());
        visitVarDef(that);
   }
    
    @Override
    public void visitForExpression(JFXForExpression that) {
        for (JFXForExpressionInClause clause : that.getInClauses()) {
            clause.accept((JavafxVisitor)this);
        }
        that.getBodyExpression().accept((JavafxVisitor)this);
    }
    
    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        that.getVar().accept((JavafxVisitor)this);
        that.getSequenceExpression().accept(this);
        if (that.getWhereExpression() != null) {
            that.getWhereExpression().accept(this);
        }
    }
    
    @Override
    public boolean shouldVisitRemoved() {
        return false;
    }
    
    @Override
    public boolean shouldVisitSynthetic() {
        return true;
    }
    
    @Override
    public void visitBlockExpression(JFXBlockExpression tree) {
        for (JCStatement stmt : tree.stats) {
            stmt.accept(this);
        }
        if (tree.value != null) {
            tree.value.accept(this);
        }
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

            // We need default constructor. Otherwise the NewClass ASTs cannot be attributed.
                        // Add default constructor if needed.
            if ((c.flags() & INTERFACE) == 0 &&
                !TreeInfo.hasConstructors(tree.defs)) {
                List<Type> argtypes = List.nil();
                List<Type> typarams = List.nil();
                List<Type> thrown = List.nil();
                long ctorFlags = 0;
                boolean based = false;
                if (c.name.len == 0) {
                    JCNewClass nc = (JCNewClass)env.next.tree;
                    if (nc.constructor != null) {
                        Type superConstrType = types.memberType(c.type,
                                                                nc.constructor);
                        argtypes = superConstrType.getParameterTypes();
                        typarams = superConstrType.getTypeArguments();
                        ctorFlags = nc.constructor.flags() & VARARGS;
                        if (nc.encl != null) {
                            argtypes = argtypes.prepend(nc.encl.type);
                            based = true;
                        }
                        thrown = superConstrType.getThrownTypes();
                    }
                }
                JCTree constrDef = DefaultConstructor(make.at(tree.pos), c,
                                                    typarams, argtypes, thrown,
                                                    ctorFlags, based);
                tree.defs = tree.defs.prepend(constrDef);
            }

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

/* ***************************************************************************
 * tree building
 ****************************************************************************/

    /** Generate default constructor for given class. For classes different
     *  from java.lang.Object, this is:
     *
     *    c(argtype_0 x_0, ..., argtype_n x_n) throws thrown {
     *      super(x_0, ..., x_n)
     *    }
     *
     *  or, if based == true:
     *
     *    c(argtype_0 x_0, ..., argtype_n x_n) throws thrown {
     *      x_0.super(x_1, ..., x_n)
     *    }
     *
     *  @param make     The tree factory.
     *  @param c        The class owning the default constructor.
     *  @param argtypes The parameter types of the constructor.
     *  @param thrown   The thrown exceptions of the constructor.
     *  @param based    Is first parameter a this$n?
     */
    JCTree DefaultConstructor(TreeMaker make,
                            ClassSymbol c,
                            List<Type> typarams,
                            List<Type> argtypes,
                            List<Type> thrown,
                            long flags,
                            boolean based) {
        List<JCVariableDecl> params = make.Params(argtypes, syms.noSymbol);
        List<JCStatement> stats = List.nil();
        if (c.type != syms.objectType)
            stats = stats.prepend(SuperCall(make, typarams, params, based));
        if ((c.flags() & ENUM) != 0 &&
            (types.supertype(c.type).tsym == syms.enumSym ||
             target.compilerBootstrap(c))) {
            // constructors of true enums are private
            flags = (flags & ~AccessFlags) | PRIVATE | GENERATEDCONSTR;
        } else
            flags |= (c.flags() & AccessFlags) | GENERATEDCONSTR;
        if (c.name.len == 0) flags |= ANONCONSTR;
        JCTree result = make.MethodDef(
            make.Modifiers(flags),
            names.init,
            null,
            make.TypeParams(typarams),
            params,
            make.Types(thrown),
            make.Block(0, stats),
            null);
        return result;
    }

    /** Generate call to superclass constructor. This is:
     *
     *    super(id_0, ..., id_n)
     *
     * or, if based == true
     *
     *    id_0.super(id_1,...,id_n)
     *
     *  where id_0, ..., id_n are the names of the given parameters.
     *
     *  @param make    The tree factory
     *  @param params  The parameters that need to be passed to super
     *  @param typarams  The type parameters that need to be passed to super
     *  @param based   Is first parameter a this$n?
     */
// JavaFX change
    public
// JavaFX change
    JCExpressionStatement SuperCall(TreeMaker make,
                   List<Type> typarams,
                   List<JCVariableDecl> params,
                   boolean based) {
        JCExpression meth;
        if (based) {
            meth = make.Select(make.Ident(params.head), names._super);
            params = params.tail;
        } else {
            meth = make.Ident(names._super);
        }
        List<JCExpression> typeargs = typarams.nonEmpty() ? make.Types(typarams) : null;
        return make.Exec(make.Apply(typeargs, meth, make.Idents(params)));
    }

    private List<JCVariableDecl> buildParams(List<JCTree> fxparams) {
        List<JCVariableDecl> params = List.nil();
        for (JCTree var : fxparams) {
            if (var.getTag() == JavafxTag.VARDECL) {
                params = params.append(jcVarDeclFromJFXVar((JFXVar)var));
            } else if (var.getTag() == JCTree.VARDEF) {
                params = params.append((JCVariableDecl)var);
            } else {
                throw new AssertionError("Unexpected tree in the JFXFunctionMemberDeclaration parameter list.");
            }
        }
        return params;
    }

    private JavafxJCVarDecl jcVarDeclFromJFXVar(JFXVar tree) {
        JCModifiers mods = tree.getModifiers();
        if (mods == null) {
            mods = make.Modifiers(0);
        }
        return make.JavafxVarDef(mods, tree.name,
                JavafxFlags.VARIABLE, jcType(tree.getJFXType()),
                null, JavafxBindStatus.UNBOUND);
    }

    JCExpression jcType(JFXType jfxType) {
        JCExpression type = null;
        
        if (jfxType != null) {
            if (jfxType instanceof JFXTypeClass) {
                type = ((JFXTypeClass)jfxType).getClassName();
                if (type instanceof JCIdent) {
                    Name className = ((JCIdent)type).getName();
                    if (className == numberTypeName) {
                        type = make.TypeIdent(TypeTags.DOUBLE);
                        type.type = syms.javafx_NumberType;
                    } else if (className == integerTypeName) {
                        type = make.TypeIdent(TypeTags.INT);
                        type.type = syms.javafx_IntegerType;
                    } else if (className == booleanTypeName) {
                        type = make.TypeIdent(TypeTags.BOOLEAN);
                        type.type = syms.javafx_BooleanType;
                    } else if (className == voidTypeName) {
                        type = make.TypeIdent(TypeTags.VOID);
                        type.type = syms.voidType;
                    } 
                }
            } else {
                // TODO: Figure out what this could be???
                throw new Error("Unexpected instanceof for JFXVar.getType()!!!");
            }
        } else {
            type = make.TypeIdent(TypeTags.NONE);
            type.type = syms.javafx_AnyType;
        }
        
        return type;
    }
}