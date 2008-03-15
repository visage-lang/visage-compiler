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
import com.sun.tools.javafx.code.JavafxClassSymbol;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxVarSymbol;

import javax.tools.JavaFileObject;
import java.util.Set;
import java.util.HashSet;


/**
 * Add local declaratuions to current environment.
 * The main entry point is {@code memberEnter}, which is called from
 * {@link JavafxAttr} when {@code visit}-ing a tree that contains local
 * declarations.
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

            @Override
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

            @Override
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
        JCTree imp = tree.qualid;
        Name name = TreeInfo.name(imp);
        TypeSymbol p;

        if (!tree.isStatic()) {
            if (tree.qualid.getTag() == SELECT) {
                if (name == names.fromString("Integer")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.integer.primitive.type");
                }
                else if (name == names.fromString("Number")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.number.primitive.type");
                }
                else if (name == names.fromString("Boolean")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.boolean.primitive.type");
                }
                else if (name == names.fromString("String")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.string.primitive.type");
                }
            }
        }
        
        // Create a local environment pointing to this tree to disable
        // effects of other imports in Resolve.findGlobalType
        JavafxEnv<JavafxAttrContext> localEnv = env.dup(tree);

        // Attribute qualifying package or class.
        // The code structure here is rather ugly, but we'll
        // fix it when we do implicit-static-import.  FIXME.
        if (imp instanceof JCFieldAccess) {
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
                return;
            } else {
                // Named type import.
                if (tree.staticImport) {
                    importNamedStatic(tree.pos(), p, name, localEnv);
                    chk.checkCanonical(s.selected);
                    return;
                }
            }
        }
        TypeSymbol c = attribImportType(imp, localEnv).tsym;
        chk.checkCanonical(imp);
        importNamed(tree.pos(), c, env);
    }

    /** Create a fresh environment for method bodies.
     *  @param tree     The method definition.
     *  @param env      The environment current outside of the method definition.
     */
    JavafxEnv<JavafxAttrContext> methodEnv(JFXFunctionDefinition tree, JavafxEnv<JavafxAttrContext> env) {
        Scope localScope = new Scope(tree.sym);
        localScope.next = env.info.scope;
        JavafxEnv<JavafxAttrContext> localEnv =
            env.dup(tree, env.info.dup(localScope));
        localEnv.outer = env;
        localEnv.enclMethod = tree;
        if ((tree.mods.flags & STATIC) != 0) localEnv.info.staticLevel++;
        return localEnv;
    }

    @Override
    public void scan(JCTree tree) {
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

        Scope enclScope = JavafxEnter.enterScope(env);
        JavafxVarSymbol v = new JavafxVarSymbol(0, tree.name, null, enclScope.owner);
        attr.varSymToTree.put(v, tree);
        tree.sym = v;
        SymbolCompleter completer = new SymbolCompleter();
            completer.env = env;
            completer.tree = tree;
            completer.attr = attr;
            v.completer = completer;

        v.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, v, tree);
        if (tree.init != null) {
            v.flags_field |= HASINIT;
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
        assert false;
    }
    
    static class SymbolCompleter implements Completer {
        JavafxEnv<JavafxAttrContext> env;
        JCTree tree;
        JavafxAttr attr;

        public void complete(Symbol m) throws CompletionFailure {
            if (tree instanceof JFXVar)
                attr.finishVar((JFXVar) tree, env);
            else if (attr.pt != Type.noType) {
                // finishOperationDefinition makes use of the expected type pt.
                // This is useful when coming from visitFunctionValue - i.e.
                // attributing an anonymous function.  However, using the
                // expected type from a random call-site (which can happen if
                // we're called via complete) is a bit too flakey.
                // (ML can do it, because they unify across all the call-sites.)
                // This is a trick to run finishOperationDefinition, but in a
                // context where we're cleared the expected type attr.pt.
                m.completer = this;
                attr.attribExpr(tree, env);
            }
            else
                attr.finishOperationDefinition((JFXFunctionDefinition) tree, env);  
        }
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
            Scope enclScope = JavafxEnter.enterScope(env);
            MethodSymbol m = new MethodSymbol(0, tree.name, null, enclScope.owner);
            m.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, m, tree);
            tree.sym = m;
            enclScope.enter(m);
            SymbolCompleter completer = new SymbolCompleter();
            completer.env = env;
            completer.tree = tree;
            completer.attr = attr;
            m.completer = completer;
    }

    @Override
    public void visitReturn(JCReturn tree) {
        super.visitReturn(tree);
        if (localEnv != null) {
            attr.attribStat(tree, localEnv);
        }
    }

// Javafx modification
    // Begin JavaFX trees
    @Override
    public void visitClassDeclaration(JFXClassDeclaration that) {
        for (JCExpression superClass : that.getSupertypes()) {
            Type superType = attr.attribType(superClass, env);
            if (that.sym != null && that.sym instanceof JavafxClassSymbol) {
                if (superType != null && superType != Type.noType) {
                    ((JavafxClassSymbol)that.sym).addSuperType(superType);
                }
            }
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
                @Override
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
               @Override
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
        JFXClassDeclaration tree = (JFXClassDeclaration)env.tree;
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
            //TODO: solesupertype implies a bug
            Type supertype = null, solesupertype = null;  
            ListBuffer<Type> interfaces = new ListBuffer<Type>();
            Set<Type> interfaceSet = new HashSet<Type>();
            {
                ListBuffer<JCExpression> extending = ListBuffer.<JCExpression>lb();
                ListBuffer<JCExpression> implementing = ListBuffer.<JCExpression>lb();
                boolean compound = (tree.getModifiers().flags & Flags.FINAL) == 0;
                for (JCExpression stype : tree.getSupertypes()) {
                    Type st = attr.attribType(stype, env);
                    
                    if (st.isInterface()) {
                        implementing.append(stype);
                    } else {
                        solesupertype = extending.isEmpty() ? st : null;
                        extending.append(stype); 
                        if ((st.tsym.flags_field & JavafxFlags.COMPOUND_CLASS) == 0) {
                            compound = false;
                            supertype = st;
                        }
                        else {
                            interfaces.append(st);
                            chk.checkNotRepeated(stype.pos(), types.erasure(st), interfaceSet);
                        }
                    }
                }
                if (compound)
                    c.flags_field |= JavafxFlags.COMPOUND_CLASS;
                        
                tree.setDifferentiatedExtendingImplementing(extending.toList(), implementing.toList());
            }
            
            if (supertype == null)
                supertype =
                ((tree.mods.flags & Flags.ENUM) != 0 && !target.compilerBootstrap(c))
                ? attr.attribBase(enumBase(tree.pos, c), baseEnv,
                                  true, false, false)
                : (c.fullname == names.java_lang_Object)
                ? Type.noType
                : syms.objectType;
            ct.supertype_field = supertype;

            // Determine interfaces.
            List<JCExpression> interfaceTrees = tree.getImplementing();
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
                if (tree.getExtending().head != null) {
                    chk.checkNonCyclic(tree.getExtending().head.pos(),
                                       supertype);
                    ct.supertype_field = Type.noType;
                }
                else if (tree.getImplementing().nonEmpty()) {
                    chk.checkNonCyclic(tree.getImplementing().head.pos(),
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

            attr.attribTypeVariables(tree.getEmptyTypeParameters(), baseEnv);

            chk.checkNonCyclic(tree.pos(), c.type);

            // If this is a class, enter symbols for this and super into
            // current scope.
            if ((c.flags_field & INTERFACE) == 0) {
                VarSymbol thisSym =
                    new VarSymbol(FINAL | HASINIT, names._this, c.type, c);
                thisSym.pos = Position.FIRSTPOS;
                env.info.scope.enter(thisSym);
                if (ct.supertype_field.tag == CLASS && solesupertype != null) {
                    VarSymbol superSym =
                        new VarSymbol(FINAL | HASINIT, names._super,
                                      solesupertype, c);
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

        private JavafxEnv<JavafxAttrContext> baseEnv(JFXClassDeclaration tree, JavafxEnv<JavafxAttrContext> env) {
        Scope typaramScope = new Scope(tree.sym);
        if (tree.getEmptyTypeParameters() != null)
            for (List<JCTypeParameter> typarams = tree.getEmptyTypeParameters();
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
            JFXClassDeclaration tree = (JFXClassDeclaration)env.tree;
            memberEnter(tree.getMembers(), env);
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
