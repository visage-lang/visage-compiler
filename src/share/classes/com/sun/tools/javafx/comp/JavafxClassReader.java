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

package com.sun.tools.javafx.comp;

import java.util.IdentityHashMap;
import java.util.Set;
import java.util.HashSet;

import javax.tools.JavaFileObject;
import com.sun.tools.mjavac.jvm.ClassReader;
import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.code.Type.*;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.util.List;

import static com.sun.tools.mjavac.code.Flags.*;
import static com.sun.tools.mjavac.code.Kinds.*;
import static com.sun.tools.mjavac.code.TypeTags.*;
import com.sun.tools.javafx.code.JavafxClassSymbol;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.tree.JavafxTreeMaker;
import com.sun.tools.javafx.util.MsgSym;

import com.sun.tools.javafx.main.JavafxCompiler;

/** Provides operations to read a classfile into an internal
 *  representation. The internal representation is anchored in a
 *  JavafxClassSymbol which contains in its scope symbol representations
 *  for all other definitions in the classfile. Top-level Classes themselves
 *  appear as members of the scopes of PackageSymbols.
 *
 *  We delegate actual classfile-reading to javac's ClassReader, and then
 *  translates the resulting ClassSymbol to JavafxClassSymbol, doing some
 *  renaming etc to make the resulting Symbols and Types match those produced
 *  by the parser.  This munging is incomplete, and there are still places
 *  where the compiler needs to know of a class comes from the parser or a
 *  classfile; those places will hopefully become fewer.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxClassReader extends ClassReader {
    protected static final Context.Key<ClassReader> backendClassReaderKey =
         new Context.Key<ClassReader>();

    private final JavafxDefs defs;
    protected final JavafxTypes fxTypes;
    protected final JavafxTreeMaker fxmake;

    /** The raw class-reader, shared by the back-end. */
    public ClassReader jreader;

    private final Name functionClassPrefixName;
    private Context ctx;
    private Messages messages;
    
    public static void preRegister(final Context context, final ClassReader jreader) {
        context.put(backendClassReaderKey, jreader);
        Object instance = context.get(classReaderKey);
        if (instance instanceof JavafxClassReader)
            ((JavafxClassReader) instance).jreader = jreader;
        else
            preRegister(context);
    }
    public static void preRegister(final Context context) {
        context.put(classReaderKey, new Context.Factory<ClassReader>() {
	       public JavafxClassReader make() {
		   JavafxClassReader reader = new JavafxClassReader(context, true);
                   reader.jreader = context.get(backendClassReaderKey);
                   return reader;
	       }
        });
    }

    public static JavafxClassReader instance(Context context) {
        JavafxClassReader instance = (JavafxClassReader) context.get(classReaderKey);
        if (instance == null)
            instance = new JavafxClassReader(context, true);
        return instance;
    }

    /** Construct a new class reader, optionally treated as the
     *  definitive classreader for this invocation.
     */
    protected JavafxClassReader(Context context, boolean definitive) {
        super(context, definitive);
        defs = JavafxDefs.instance(context);
        fxTypes = JavafxTypes.instance(context);
        fxmake = JavafxTreeMaker.instance(context);
        functionClassPrefixName = names.fromString(JavafxSymtab.functionClassPrefix);
        ctx = context;
        messages = Messages.instance(context);
    }

    public Name.Table getNames() {
        return names;
    }
    
    /** Reassign names of classes that might have been loaded with
      * their flat names. */
    void fixupFullname (JavafxClassSymbol cSym, ClassSymbol jsymbol) {
        if (cSym.fullname != jsymbol.fullname &&
                cSym.owner.kind == PCK && jsymbol.owner.kind == TYP) {
            cSym.owner.members().remove(cSym);
            cSym.name = jsymbol.name;
            ClassSymbol owner = enterClass(((ClassSymbol) jsymbol.owner).flatname);
            cSym.owner = owner;
            cSym.fullname = ClassSymbol.formFullName(cSym.name, owner);
        }
    }

    public JavafxClassSymbol enterClass(ClassSymbol jsymbol) {
        Name className = jsymbol.flatname;
        boolean mixin = className.endsWith(defs.mixinClassSuffixName);
        if (mixin)
            className = className.subName(0, className.len - defs.mixinClassSuffixName.len);
        JavafxClassSymbol cSym = (JavafxClassSymbol) enterClass(className);
        //cSym.flags_field |= jsymbol.flags_field;
        if (mixin)
            cSym.flags_field |= JavafxFlags.MIXIN;
        else {
            fixupFullname(cSym, jsymbol);
            cSym.jsymbol = jsymbol;
        }
        return cSym;
    }

    /** Define a new class given its name and owner.
     */
    @Override
    public ClassSymbol defineClass(Name name, Symbol owner) {
        ClassSymbol c = new JavafxClassSymbol(0, name, owner);
        if (owner.kind == PCK)
            assert classes.get(c.flatname) == null : c;
        c.completer = this;
        return c;
    }

    /* FIXME: The re-written class-reader doesn't translate annotations yet.
 
    protected void attachAnnotations(final Symbol sym) {
        int numAttributes = nextChar();
        if (numAttributes != 0) {
            ListBuffer<CompoundAnnotationProxy> proxies =
                new ListBuffer<CompoundAnnotationProxy>();
            for (int i = 0; i<numAttributes; i++) {
                CompoundAnnotationProxy proxy = readCompoundAnnotation();
                if (proxy.type.tsym == syms.proprietaryType.tsym)
                    sym.flags_field |= PROPRIETARY;
                else {
                        proxies.append(proxy);
                }
            }
            annotate.later(new JavafxAnnotationCompleter(sym, proxies.toList(), this));
        }
    }

    static public class JavafxAnnotationCompleter extends AnnotationCompleter {
        JavafxClassReader classReader;
        public JavafxAnnotationCompleter(Symbol sym, List<CompoundAnnotationProxy> l, ClassReader classReader) {
            super(sym, l, classReader);
            this.classReader = (JavafxClassReader)classReader;
        }
        // implement Annotate.Annotator.enterAnnotation()
        public void enterAnnotation() {
            JavaFileObject previousClassFile = classReader.currentClassFile;
            try {
                classReader.currentClassFile = classFile;
                List<Attribute.Compound> newList = deproxyCompoundList(l);
                JavafxSymtab javafxSyms = (JavafxSymtab)classReader.syms;
                for (Attribute.Compound comp : newList) {
                }

                sym.attributes_field = ((sym.attributes_field == null)
                                        ? newList
                                        : newList.prependList(sym.attributes_field));
            } finally {
                classReader.currentClassFile = previousClassFile;
            }
        }
    }
    */

    /** Map javac Type/Symbol to javafx Type/Symbol. */
    IdentityHashMap<Object,Object> typeMap = new IdentityHashMap<Object,Object>();
    
    /** Translate a List of raw JVM types to Javafx types. */
    List<Type> translateTypes (List<Type> types) {
        if (types == null)
            return null;
        List<Type> ts = (List<Type>) typeMap.get(types);
        if (ts != null)
            return ts;
        ListBuffer<Type> rs = new ListBuffer<Type>();
        for (List<Type> t = types;
                 t.tail != null;
                 t = t.tail)
            rs.append(translateType(t.head));
        ts = rs.toList();
        typeMap.put(types, ts);
        return ts;
    }

    /** Translate raw JVM type to Javafx type. */
    Type translateType (Type type) {
        if (type == null)
            return null;
        Type t = (Type) typeMap.get(type);
        if (t != null)
            return t;
        switch (type.tag) {
            case VOID:
                t = syms.voidType;
                break;
            case BOOLEAN:
                t = syms.booleanType;
                break;
            case CHAR:
                t = syms.charType;
                break;
            case BYTE:
                t = syms.byteType;
                break;
            case SHORT:
                t = syms.shortType;
                break;
            case INT:
                t = syms.intType;
                break;
            case LONG:
                t = syms.longType;
                break;
            case DOUBLE:
                t = syms.doubleType;
                break;
            case FLOAT:
                t = syms.floatType;
                break;
            case TYPEVAR:
                TypeVar tv = (TypeVar) type;
                TypeVar tx = new TypeVar(null, (Type) null, (Type) null);
                typeMap.put(type, tx); // In case of a cycle.
                tx.bound = translateType(tv.bound);
                tx.lower = translateType(tv.lower);
                tx.tsym = new TypeSymbol(0, tv.tsym.name, tx, translateSymbol(tv.tsym.owner));
                return tx;
            case FORALL:
                ForAll tf = (ForAll) type;
                t = new ForAll(translateTypes(tf.tvars), translateType(tf.qtype));
                break;
            case WILDCARD:
                WildcardType wt = (WildcardType) type;
                t = new WildcardType(translateType(wt.type), wt.kind,
                        translateTypeSymbol(wt.tsym));
                break;
            case CLASS:
                TypeSymbol tsym = type.tsym;
                if (tsym instanceof ClassSymbol) {
                    if (tsym.name.endsWith(defs.mixinClassSuffixName)) {
                        t = enterClass((ClassSymbol) tsym).type;
                        break;
                    }
                    final ClassType ctype = (ClassType) type;
                    if (ctype.isCompound()) {
                        t = types.makeCompoundType(translateTypes(ctype.interfaces_field), translateType(ctype.supertype_field));
                        break;
                    }
                    Name flatname = ((ClassSymbol) tsym).flatname;
                    if (flatname.startsWith(functionClassPrefixName)
                        && flatname != functionClassPrefixName) {
                        t = ((JavafxSymtab) syms).makeFunctionType(translateTypes(ctype.typarams_field));
                        break;
                    }
                    TypeSymbol sym = translateTypeSymbol(tsym);
                    ClassType ntype;
                    if (tsym.type == type)
                        ntype = (ClassType) sym.type;
                    else
                        ntype = new ClassType(Type.noType, List.<Type>nil(), sym) {
                            boolean completed = false;
                            @Override
                            public Type getEnclosingType() {
                                if (!completed) {
                                    completed = true;
                                    tsym.complete();
                                    super.setEnclosingType(translateType(ctype.getEnclosingType()));
                                }
                                return super.getEnclosingType();
                            }
                            @Override
                            public void setEnclosingType(Type outer) {
                                throw new UnsupportedOperationException();
                            }
                            @Override
                            public boolean equals(Object t) {
                                return super.equals(t);
                            }

                            @Override
                            public int hashCode() {
                                return super.hashCode();
                            }
                        };
                    typeMap.put(type, ntype); // In case of a cycle.
                    ntype.typarams_field = translateTypes(type.getTypeArguments());
                    return ntype;
                }
                break;
            case ARRAY:
                t = new ArrayType(translateType(((ArrayType) type).elemtype), syms.arrayClass);
                break;
            case METHOD:
                t = new MethodType(translateTypes(type.getParameterTypes()),
                        translateType(type.getReturnType()),
                        translateTypes(type.getThrownTypes()),
                        syms.methodClass);
                break;
            default:
                t = type; // FIXME
        }
        typeMap.put(type, t);
        return t;
    }

    TypeSymbol translateTypeSymbol(TypeSymbol tsym) {
        if (tsym == syms.predefClass)
            return tsym;
        ClassSymbol csym = (ClassSymbol) tsym; // FIXME
        return enterClass(csym);
    }
    
    Symbol translateSymbol(Symbol sym) {
        if (sym == null)
            return null;
        Symbol s = (Symbol) typeMap.get(sym);
        if (s != null)
            return s;
        if (sym instanceof PackageSymbol)
            s = enterPackage(((PackageSymbol) sym).fullname);
        else if (sym instanceof MethodSymbol) {
            Symbol owner = translateSymbol(sym.owner);
            MethodSymbol m = translateMethodSymbol(sym.flags_field, sym, owner);
            ((ClassSymbol) owner).members_field.enter(m);
            s = m;
        }
        else
            s = translateTypeSymbol((TypeSymbol) sym);
        typeMap.put(sym, s);
        return s;
    }
    
    void popMethodTypeArg(MethodType type) {
        List<Type> argtypes = type.argtypes;
        ListBuffer<Type> lb = ListBuffer.<Type>lb();
        
        for (int i = 1; i < argtypes.size(); i++) {
            lb.append(argtypes.get(i));
        }
        
        type.argtypes = lb.toList();
    }
    
    MethodSymbol translateMethodSymbol(long flags, Symbol sym, Symbol owner) {
        Name name = sym.name;
        Type mtype = sym.type;
        String nameString = name.toString();
        
        int boundStringIndex = nameString.indexOf(JavafxDefs.boundFunctionDollarSuffix);
        if (boundStringIndex != -1) {
            // this is a bound function
            // remove the bound suffix, and mark as bound
            nameString = nameString.substring(0, boundStringIndex);
            flags |= JavafxFlags.BOUND;
        }
        JavafxSymtab javafxSyms = (JavafxSymtab) this.syms;
        for (Attribute.Compound ann : sym.getAnnotationMirrors()) {
            if (ann.type.tsym.flatName() == javafxSyms.javafx_signatureAnnotationType.tsym.flatName()) {
                String sig = (String)ann.values.head.snd.getValue();
                signatureBuffer = new byte[sig.length()];
                try {
                    mtype = sigToType(names.fromString(sig));
                }
                catch (Exception e) {
                    throw new AssertionError("Bad Javafx signature");
                }
            }
        }
        Type type = translateType(mtype);
        if (type instanceof MethodType) {
            boolean convertToStatic = false;
            
            if (nameString.endsWith(JavafxDefs.implFunctionSuffix)) {
                nameString = nameString.substring(0, nameString.length() - JavafxDefs.implFunctionSuffix.length());
                convertToStatic = true;
            }
            
            if (convertToStatic) {
                flags &= ~Flags.STATIC;
                popMethodTypeArg((MethodType)type);
            }
        }
    
        name = names.fromString(nameString);
        
        return new MethodSymbol(flags, name, type, owner);
    }

    // JFXC-2849 - Mixins: Change the mixin interface from $Intf to $Mixin.
    private void checkForIntfSymbol(Symbol sym) throws CompletionFailure {        
        if (sym.name.endsWith(defs.deprecatedInterfaceSuffixName)) {
            String fileString = ((ClassSymbol) sym).classfile.getName();
            String message = messages.getLocalizedString(MsgSym.MESSAGEPREFIX_COMPILER_MISC +
                                                            MsgSym.MESSAGE_DEPRECATED_INTERFACE_CLASS,
                                                         fileString);
            log.rawError(Position.NOPOS, message);
            throw new CompletionFailure(sym, message);
        }
    }

    @Override
    public void complete(Symbol sym) throws CompletionFailure {
        checkForIntfSymbol(sym);
        if (jreader.sourceCompleter == null)
           jreader.sourceCompleter = JavafxCompiler.instance(ctx);
        if (sym instanceof PackageSymbol) {
            PackageSymbol psym = (PackageSymbol) sym;
            PackageSymbol jpackage;
            if (psym == syms.unnamedPackage)
                jpackage = jreader.syms.unnamedPackage;
            else
                jpackage = jreader.enterPackage(psym.fullname);
            jpackage.complete();
            if (psym.members_field == null) psym.members_field = new Scope(psym);
            for (Scope.Entry e = jpackage.members_field.elems;
                 e != null;  e = e.sibling) {
                 if (e.sym instanceof ClassSymbol) {
                     ClassSymbol jsym = (ClassSymbol) e.sym;
                     if (jsym.name.endsWith(defs.mixinClassSuffixName))
                         continue;
                     JavafxClassSymbol csym = enterClass(jsym);
                     psym.members_field.enter(csym);
                     csym.classfile = jsym.classfile;
                     csym.jsymbol = jsym;
                 }
            }
            if (jpackage.exists())
                psym.flags_field |= EXISTS;
        } else {
            sym.owner.complete();
            JavafxClassSymbol csym = (JavafxClassSymbol) sym;
            ClassSymbol jsymbol = csym.jsymbol;
            if (jsymbol != null && jsymbol.classfile != null && 
                jsymbol.classfile.getKind() == JavaFileObject.Kind.SOURCE &&
                jsymbol.classfile.getName().endsWith(".fx")) {
                SourceCompleter fxSourceCompleter = JavafxCompiler.instance(ctx);
                fxSourceCompleter.complete(csym);
                return;
            } else { 
                csym.jsymbol = jsymbol = jreader.loadClass(csym.flatname);
            }
            fixupFullname(csym, jsymbol);
            typeMap.put(jsymbol, csym);
            jsymbol.classfile = ((ClassSymbol) sym).classfile;
            
            ClassType ct = (ClassType)csym.type;
            ClassType jt = (ClassType)jsymbol.type;
            csym.members_field = new Scope(csym);

            // flags are derived from flag bits and access modifier annoations
            csym.flags_field = flagsFromAnnotationsAndFlags(jsymbol);
            
            ct.typarams_field = translateTypes(jt.typarams_field);
            ct.setEnclosingType(translateType(jt.getEnclosingType()));
            
            ct.supertype_field = translateType(jt.supertype_field);
            
            // JFXC-2841 - Mixins: Cannot find firePropertyChange method in SwingComboBox.fx
            if (ct.supertype_field != null && 
                ct.supertype_field.tsym != null &&
                ct.supertype_field.tsym.kind == TYP) {
                
            }
            
            ListBuffer<Type> interfaces = new ListBuffer<Type>();
            Type iface = null;
            if (jt.interfaces_field != null) { // true for ErrorType
                for (List<Type> it = jt.interfaces_field;
                     it.tail != null;
                     it = it.tail) {
                    Type itype = it.head;
                    checkForIntfSymbol(itype.tsym);
                    if (((ClassSymbol) itype.tsym).flatname == defs.cFXObjectName) {
                        csym.flags_field |= JavafxFlags.FX_CLASS;
                    } else if (((ClassSymbol) itype.tsym).flatname == defs.cFXMixinName) {
                        csym.flags_field |= JavafxFlags.MIXIN | JavafxFlags.FX_CLASS;
                    } else if ((csym.fullname.len + defs.mixinClassSuffixName.len ==
                             ((ClassSymbol) itype.tsym).fullname.len) &&
                            ((ClassSymbol) itype.tsym).fullname.startsWith(csym.fullname) &&
                            itype.tsym.name.endsWith(defs.mixinClassSuffixName)) {
                        iface = itype;
                        iface.tsym.complete();
                        csym.flags_field |= JavafxFlags.MIXIN | JavafxFlags.FX_CLASS;
                    } else {
                        itype = translateType(itype);
                        interfaces.append(itype);
                    }
                }
            }
           
            if (iface != null) {
                for (List<Type> it = ((ClassType) iface.tsym.type).interfaces_field;
                 it.tail != null;
                 it = it.tail) {
                    Type itype = it.head;
                    checkForIntfSymbol(itype.tsym);
                    if (((ClassSymbol) itype.tsym).flatname == defs.cFXObjectName) {
                        csym.flags_field |= JavafxFlags.FX_CLASS;
                    } else if (((ClassSymbol) itype.tsym).flatname == defs.cFXMixinName) {
                        csym.flags_field |= JavafxFlags.MIXIN | JavafxFlags.FX_CLASS;
                    } else {
                        itype = translateType(itype);
                        interfaces.append(itype);
                    }
                }
            }
            ct.interfaces_field = interfaces.toList();

            // Now translate the members.
            // Do an initial "reverse" pass so we copy the order.
            List<Symbol> symlist = List.nil();
            for (Scope.Entry e = jsymbol.members_field.elems;
                 e != null;  e = e.sibling) {
                if ((e.sym.flags_field & SYNTHETIC) != 0)
                    continue;
                symlist = symlist.prepend(e.sym);
            }
            boolean isFXClass = (csym.flags_field & JavafxFlags.FX_CLASS) != 0;
            boolean isMixinClass = (csym.flags_field & JavafxFlags.MIXIN) != 0;
            
            JavafxVarSymbol scriptAccessSymbol = isFXClass ? fxmake.ScriptAccessSymbol(csym) : null;

            Set<Name> priorNames = new HashSet<Name>();
            handleSyms:
            for (List<Symbol> l = symlist; l.nonEmpty(); l=l.tail) {
                Symbol memsym = l.head;
                Name name = memsym.name;
                long flags = flagsFromAnnotationsAndFlags(memsym);
                if ((flags & PRIVATE) != 0)
                    continue;
                boolean sawSourceNameAnnotation = false;
                JavafxSymtab javafxSyms = (JavafxSymtab) this.syms;
                for (Attribute.Compound a : memsym.getAnnotationMirrors()) {
                    if (a.type.tsym.flatName() == javafxSyms.javafx_staticAnnotationType.tsym.flatName()) {
                        flags |=  Flags.STATIC;
                    } else if (a.type.tsym.flatName() == javafxSyms.javafx_defAnnotationType.tsym.flatName()) {
                        flags |=  JavafxFlags.IS_DEF;
                    } else if (a.type.tsym.flatName() == javafxSyms.javafx_publicInitAnnotationType.tsym.flatName()) {
                        flags |=  JavafxFlags.PUBLIC_INIT;
                    } else if (a.type.tsym.flatName() == javafxSyms.javafx_publicReadAnnotationType.tsym.flatName()) {
                        flags |=  JavafxFlags.PUBLIC_READ;
                    } else if (a.type.tsym.flatName() == javafxSyms.javafx_inheritedAnnotationType.tsym.flatName()) {
                        continue handleSyms;
                    } else if (a.type.tsym.flatName() == javafxSyms.javafx_sourceNameAnnotationType.tsym.flatName()) {
                        Attribute aa = a.member(name.table.value);
                        Object sourceName = aa.getValue();
                        if (sourceName instanceof String) {
                            name = names.fromString((String) sourceName);
                            sawSourceNameAnnotation = true;
                        }
                    }
                }
                if (memsym instanceof MethodSymbol) {
                    MethodSymbol m = translateMethodSymbol(flags, memsym, csym);     
                    csym.members_field.enter(m);
                }
                else if (memsym instanceof VarSymbol) {
                    // Eliminate any duplicate value/location.
                    if (priorNames.contains(name))
                        continue;
                    Type otype = memsym.type;
                    Type type = translateType(otype);
                    JavafxVarSymbol v;
                    if (scriptAccessSymbol != null && name == scriptAccessSymbol.name) {
                        v = scriptAccessSymbol;
                    } else {
                        v = new JavafxVarSymbol(fxTypes, names, flags, name, type, csym);
                        csym.addVar(v, (flags & STATIC) != 0);
                    }
                    csym.members_field.enter(v);
                    priorNames.add(name);
                }
                else {
                    memsym.flags_field = flags;
                    csym.members_field.enter(translateSymbol(memsym));
                }
            }
        }
    }
    
    private long flagsFromAnnotationsAndFlags(Symbol sym) {
        long initialFlags = sym.flags_field;
        long nonAccessFlags = initialFlags & ~JavafxFlags.JavafxAccessFlags;
        long accessFlags = initialFlags & JavafxFlags.JavafxAccessFlags;
        JavafxSymtab javafxSyms = (JavafxSymtab) this.syms;
        for (Attribute.Compound a : sym.getAnnotationMirrors()) {
            if (a.type.tsym.flatName() == javafxSyms.javafx_protectedAnnotationType.tsym.flatName()) {
                accessFlags = Flags.PROTECTED;
            } else if (a.type.tsym.flatName() == javafxSyms.javafx_packageAnnotationType.tsym.flatName()) {
                accessFlags = 0L;
            } else if (a.type.tsym.flatName() == javafxSyms.javafx_publicAnnotationType.tsym.flatName()) {
                accessFlags = Flags.PUBLIC;
            } else if (a.type.tsym.flatName() == javafxSyms.javafx_scriptPrivateAnnotationType.tsym.flatName()) {
                accessFlags = JavafxFlags.SCRIPT_PRIVATE;
            }
        }
        if (accessFlags == 0L) {
            accessFlags = JavafxFlags.PACKAGE_ACCESS;
        }
        return nonAccessFlags | accessFlags;
    }
}
