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

package com.sun.tools.javafx.main;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import com.sun.javafx.api.JavafxTaskEvent;
import com.sun.javafx.api.JavafxTaskListener;
import com.sun.source.util.TaskEvent;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.code.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.jvm.*;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javafx.comp.*;
import com.sun.tools.javafx.code.*;
import com.sun.tools.javafx.util.MsgSym;
import static com.sun.tools.mjavac.util.ListBuffer.lb;
import com.sun.tools.javafx.antlr.JavafxSyntacticAnalysis;
import com.sun.tools.javafx.tree.xml.TreeXMLTransformer;
import com.sun.tools.javafx.util.PlatformPlugin;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** This class could be the main entry point for GJC when GJC is used as a
 *  component in a larger software system. It provides operations to
 *  construct a new compiler, and to run a new compiler on a set of source
 *  files.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxCompiler implements ClassReader.SourceCompleter {
    private final static String javafxErrorsKey = "com.sun.tools.javafx.resources.javafxcompiler";
    /** The context key for the compiler. */
    protected static final Context.Key<JavafxCompiler> compilerKey =
        new Context.Key<JavafxCompiler>();

    /** Get the JavaCompiler instance for this context. */
    public static JavafxCompiler instance(Context context) {
        JavafxCompiler instance = context.get(compilerKey);
        if (instance == null)
            instance = new JavafxCompiler(context);
        return instance;
    }

    /** The current version number as a string.
     */
    public static String version() {
        return version("release");
    }

    /** The current full version number as a string.
     */
    public static String fullVersion() {
        return version("full"); // mm.mm.oo[-milestone]-build
    }

    private static final String versionRBName = "com.sun.tools.javafx.resources.version";
    private static ResourceBundle versionRB;

    private static String version(String key) {
        if (versionRB == null) {
            try {
                versionRB = ResourceBundle.getBundle(versionRBName);
            } catch (MissingResourceException e) {
                // HACK: MESSAGE_VERSION_RESOURCE_MISSING is currently defined in javafxcompiler.properties
                return Main.getJavafxLocalizedString(MsgSym.MESSAGEPREFIX_COMPILER_MISC
                        + MsgSym.MESSAGE_VERSION_RESOURCE_MISSING, System.getProperty("java.version"));
//                return Log.getLocalizedString(MsgSym.MESSAGE_VERSION_RESOURCE_MISSING, System.getProperty("java.version"));
            }
        }
        try {
            return versionRB.getString(key);
        }catch (MissingResourceException e) {
            // HACK: MESSAGE_VERSION_UNKNOWN is currently defined in javafxcompiler.properties
            return Main.getJavafxLocalizedString(MsgSym.MESSAGEPREFIX_COMPILER_MISC
                    + MsgSym.MESSAGE_VERSION_UNKNOWN, System.getProperty("java.version"));
//            return Log.getLocalizedString(MsgSym.MESSAGE_VERSION_UNKNOWN, System.getProperty("java.version"));
        }
    }

    private static enum CompilePolicy {
        /*
         * Just attribute the parse trees
         */
        ATTR_ONLY,

        /*
         * Just attribute and do flow analysis on the parse trees.
         * This should catch most user errors.
         */
        CHECK_ONLY,

        /*
         * Attribute everything, then do flow analysis for everything,
         * then desugar everything, and only then generate output.
         * Means nothing is generated if there are any errors in any classes.
         */
        SIMPLE,

        /*
         * After attributing everything and doing flow analysis,
         * group the work by compilation unit.
         * Then, process the work for each compilation unit together.
         * Means nothing is generated for a compilation unit if the are any errors
         * in the compilation unit  (or in any preceding compilation unit.)
         */
        BY_FILE,

        /*
         * Completely process each entry on the todo list in turn.
         * -- this is the same for 1.5.
         * Means output might be generated for some classes in a compilation unit
         * and not others.
         */
        BY_TODO;

        static CompilePolicy decode(String option) {
            if (option == null)
                return DEFAULT_COMPILE_POLICY;
            else if (option.equals("attr"))
                return ATTR_ONLY;
            else if (option.equals("check"))
                return CHECK_ONLY;
            else if (option.equals("simple"))
                return SIMPLE;
            else if (option.equals("byfile"))
                return BY_FILE;
            else if (option.equals("bytodo"))
                return BY_TODO;
            else
                return DEFAULT_COMPILE_POLICY;
        }
    }

    private static CompilePolicy DEFAULT_COMPILE_POLICY = CompilePolicy.BY_TODO;

    private static enum ImplicitSourcePolicy {
        /** Don't generate or process implicitly read source files. */
        NONE,
        /** Generate classes for implicitly read source files. */
        CLASS,
        /** Like CLASS, but generate warnings if annotation processing occurs */
        UNSET;

        static ImplicitSourcePolicy decode(String option) {
            if (option == null)
                return UNSET;
            else if (option.equals("none"))
                return NONE;
            else if (option.equals("class"))
                return CLASS;
            else
                return UNSET;
        }
    }

    /** Command line options
     */
    protected Options options;

    /** The log to be used for error reporting.
     */
    public Log log;

    /** The tree factory module.
     */
    protected JavafxTreeMaker make;

    /** The class writer.
     */
    protected ClassWriter writer;

    /** The module for the symbol table entry phases.
     */
    protected JavafxEnter enter;

    /** The symbol table.
     */
    protected JavafxSymtab syms;

    /** The language version.
     */
    protected Source source;

    /** The name table.
     */
    protected Name.Table names;

    /** The bind analyzer.
     */
    protected JavafxBoundContextAnalysis bindAnalyzer;

    /** Fill in the synthetic definitions needed in a bound function.
     */
    protected JavafxBoundFiller boundFill;

    /** The local var bind converter.
     */
    protected JavafxLocalToClass localToClass;

    /** The type conversion inserter.
     */
    protected JavafxLower convertTypes;

    /** The attributor.
     */
    protected JavafxAttr attr;

    /** The checker.
     */
    protected JavafxCheck chk;

    /** The annotation annotator.
     */
    protected JavafxAnnotate annotate;

    /** The back-end preper
     */
    protected JavafxPrepForBackEnd prepForBackEnd;

    /** Optimization statistics
     */
    protected JavafxOptimizationStatistics optStat;

    /** The Java Compiler instance the processes the flow through gen.
     */
    protected JavafxJavaCompiler javafxJavaCompiler;

    /** Force a completion failure on this name
     */
    protected final Name completionFailureName;

    /** Type utilities.
     */
    protected Types types;

    /** Access to file objects.
     */
    protected JavaFileManager fileManager;

    /** Optional listener for progress events
     */
    protected JavafxTaskListener taskListener;

    protected final JavafxSyntacticAnalysis syntacticAnalysis;
    protected final JavafxDecompose decomposeBindExpressions;
    protected final JavafxVarUsageAnalysis varUsageAnalysis;
    protected final JavafxToJava jfxToJava;

    /**
     * Flag set if any implicit source files read.
     **/
    protected boolean implicitSourceFilesRead;

    protected Context context;

    /** Construct a new compiler using a shared context.
     */
    public JavafxCompiler(final Context context) {
        this.context = context;
        context.put(compilerKey, this);
        registerServices(context);
        JavafxClassReader.instance(context).sourceCompleter = this;

        javafxJavaCompiler = JavafxJavaCompiler.instance(context);
        names = Name.Table.instance(context);
        options = Options.instance(context);
        log = Log.instance(context);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        writer = ClassWriter.instance(context);
        enter = JavafxEnter.instance(context);
        todo = JavafxTodo.instance(context);

        fileManager = context.get(JavaFileManager.class);

        syntacticAnalysis = JavafxSyntacticAnalysis.instance(context);
        decomposeBindExpressions = JavafxDecompose.instance(context);
        varUsageAnalysis = JavafxVarUsageAnalysis.instance(context);
        jfxToJava = JavafxToJava.instance(context);
        prepForBackEnd = JavafxPrepForBackEnd.instance(context);

        // Add the javafx message resource bundle
        Messages.instance(context).add(javafxErrorsKey);
        try {
            // catch completion problems with predefineds
            syms = (JavafxSymtab)JavafxSymtab.instance(context);
        } catch (CompletionFailure ex) {
            // inlined Check.completionError as it is not initialized yet
            log.error(MsgSym.MESSAGE_CANNOT_ACCESS, ex.sym, ex.errmsg);
            if (ex instanceof ClassReader.BadClassFile)
                throw new Abort();
        }
        source = Source.instance(context);
        attr = JavafxAttr.instance(context);
        bindAnalyzer = JavafxBoundContextAnalysis.instance(context);
        boundFill = JavafxBoundFiller.instance(context);
        localToClass = JavafxLocalToClass.instance(context);
        convertTypes = JavafxLower.instance(context);
        chk = JavafxCheck.instance(context);
        annotate = JavafxAnnotate.instance(context);
        optStat = JavafxOptimizationStatistics.instance(context);
        types = Types.instance(context);
        taskListener = context.get(JavafxTaskListener.class);

        verbose       = options.get("-verbose")       != null;
        sourceOutput  = options.get("-printsource")   != null; // used to be -s
        stubOutput    = options.get("-stubs")         != null;
        relax         = options.get("-relax")         != null;
        printFlat     = options.get("-printflat")     != null;
        attrParseOnly = options.get("-attrparseonly") != null;
        encoding      = options.get("-encoding");
        lineDebugInfo = options.get("-g:")            == null ||
                        options.get("-g:lines")       != null;
        devVerbose    = options.get("dev") != null;
        processPcks   = options.get("process.packages") != null;

        verboseCompilePolicy = options.get("verboseCompilePolicy") != null;

        if (attrParseOnly)
            compilePolicy = CompilePolicy.ATTR_ONLY;
        else
            compilePolicy = CompilePolicy.decode(options.get("compilePolicy"));

        implicitSourcePolicy = ImplicitSourcePolicy.decode(options.get("-implicit"));

        completionFailureName =
            (options.get("failcomplete") != null)
            ? names.fromString(options.get("failcomplete"))
            : null;
    }

    /* Switches:
     */

    /** Verbose output.
     */
    public boolean verbose;

    /** Emit plain Java source files rather than class files.
     */
    public boolean sourceOutput;

    /** Emit stub source files rather than class files.
     */
    public boolean stubOutput;

    /** Generate attributed parse tree only.
     */
    public boolean attrParseOnly;

    /** Switch: relax some constraints for producing the jsr14 prototype.
     */
    boolean relax;

    /** Debug switch: Emit Java sources after inner class flattening.
     */
    public boolean printFlat;

    /** The encoding to be used for source input.
     */
    public String encoding;

    /** Generate code with the LineNumberTable attribute for debugging
     */
    public boolean lineDebugInfo;

    /** Switch: should we debug ignored exceptions
     */
    protected boolean devVerbose;

    /** Switch: should we (annotation) process packages as well
     */
    protected boolean processPcks;

    /** Switch: is annotation processing requested explitly via
     * CompilationTask.setProcessors?
     */
    protected boolean explicitAnnotationProcessingRequested = false;

    /**
     * The policy for the order in which to perform the compilation
     */
    CompilePolicy compilePolicy;

    /**
     * The policy for what to do with implicitly read source files
     */
    ImplicitSourcePolicy implicitSourcePolicy;

    /**
     * Report activity related to compilePolicy
     */
    public boolean verboseCompilePolicy;

    /** A queue of all as yet unattributed classes.
     */
    public JavafxTodo todo;

    private Set<JavafxEnv<JavafxAttrContext>> deferredSugar = new HashSet<JavafxEnv<JavafxAttrContext>>();

    /** The set of currently compiled inputfiles, needed to ensure
     *  we don't accidentally overwrite an input file when -s is set.
     *  initialized by `compile'.
     */
    protected Set<JavaFileObject> inputFiles = new HashSet<JavaFileObject>();

    /** The number of errors reported so far.
     */
    public int errorCount() {
        return log.nerrors;
    }

    protected final <T> List<T> stopIfError(ListBuffer<T> listBuffer) {
        if (errorCount() == 0)
            return listBuffer.toList();
        else
            return List.nil();
    }

    protected final <T> List<T> stopIfError(List<T> list) {
        if (errorCount() == 0)
            return list;
        else
            return List.nil();
    }

    /** The number of warnings reported so far.
     */
    public int warningCount() {
        return log.nwarnings;
    }

    /** Try to open input stream with given name.
     *  Report an error if this fails.
     *  @param filename   The file name of the input stream to be opened.
     */
    public CharSequence readSource(JavaFileObject filename) {
        try {
            inputFiles.add(filename);
            return filename.getCharContent(false);
        } catch (IOException e) {
            log.error(MsgSym.MESSAGE_ERROR_READING_FILE, filename, e.getLocalizedMessage());
            return null;
        }
    }

    /** Parse contents of input stream.
     *  @param filename     The name of the file from which input stream comes.
     *  @param input        The input stream to be parsed.
     */
    protected JFXScript parse(JavaFileObject filename, CharSequence content) {
        long msec = now();
        JFXScript tree = null;
        if (content != null) {
            if (verbose) {
                printVerbose(MsgSym.MESSAGE_PARSING_STARTED, filename);
            }
            if (taskListener != null) {
                JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.PARSE, filename);
                taskListener.started(e);
            }
            int initialErrorCount = log.nerrors;

            // Parse the input, returning the AST
            tree = syntacticAnalysis.parse(content, filename.getName());
            parseErrors |= (log.nerrors > initialErrorCount);
            if (tree != null && lineDebugInfo) {
                String hunk = content.toString();
                tree.lineMap = Position.makeLineMap(hunk.toCharArray(), hunk.length(), false);
            }
        }
        if (verbose) {
            printVerbose(MsgSym.MESSAGE_PARSING_DONE, Long.toString(elapsed(msec)));
        }

        // test shouldn't be needed when we have better error recovery
        if (tree == null) {
            // We have nothing, so make an empty module
            tree = make.Script(null, List.<JFXTree>nil());
        }

        tree.sourcefile = filename;

        printJavafxSource("dumpfx", tree, content);

        if (content != null && taskListener != null) {
            JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.PARSE, tree);
            taskListener.finished(e);
        }

        TreeXMLTransformer.afterParse(context, tree);
        return tree;
    }
    // where
        public boolean keepComments = false;
        protected boolean keepComments() {
            return keepComments || sourceOutput || stubOutput;
        }

        /** Parse contents of file.
     *  @param filename     The name of the file to be parsed.
     */
    public JFXScript parse(JavaFileObject filename) {
        JavaFileObject prev = log.useSource(filename);
        try {
            JFXScript t = parse(filename, readSource(filename));
            if (t.endPositions != null)
                log.setEndPosTable(filename, t.endPositions);
            return t;
        } finally {
            log.useSource(prev);
        }
    }

    /** Emit Java-like source corresponding to translated tree.
     */
    void printJavaSource(JavafxEnv<JavafxAttrContext> env) {
        String dump = options.get("dumpjava");
        if (dump != null) {
            try {
                String fn = env.toplevel.sourcefile.toString().replace(".fx", ".javadump");
                File outFile = new File(dump, (new File(fn)).getName());
                FileWriter fw = new FileWriter(outFile);
                BufferedWriter out = new BufferedWriter(fw);
                try {
                    new JavaPretty(out, true, context).printUnit(env.translatedToplevel, null);
                } finally {
                    out.close();
                }
            } catch (IOException ex) {
                System.err.println("Exception thrown in JavaFX pretty printing: " + ex);
            }
        }
    }

    /** Emit Java-like source corresponding to translated tree.
     */
    void printOptimizationStatistics() {
        String which = options.get("optstats");
        if (which != null) {
            optStat.printData(which);
        }
    }

    /** Emit pretty=printed fx source corresponding to an input file.
     */
    void printJavafxSource(String opt, JFXScript cu, CharSequence content) {
        String dump = options.get(opt);
        BufferedWriter out = null;
        if (dump != null) {
            try {
                try {
                    String fn = cu.sourcefile.toString().replace(".fx", ".fxdump");
                    File outFile = new File(dump, (new File(fn)).getName());
                    FileWriter fw = new FileWriter(outFile);
                    out = new BufferedWriter(fw);
                    new JavafxPretty(out, true, content).printUnit(cu);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            } catch (IOException ex) {
                System.err.println("Exception thrown in JavaFX pretty printing: " + ex);
            }
        }
    }

    /** Complete compiling a source file that has been accessed
     *  by the class file reader.
     *  @param c          The class the source file of which needs to be compiled.
     *  @param filename   The name of the source file.
     *  @param f          An input stream that reads the source file.
     */
    public void complete(ClassSymbol c) throws CompletionFailure {
//      System.err.println("completing " + c);//DEBUG
        if (completionFailureName == c.fullname) {
            throw new CompletionFailure(c, "user-selected completion failure by class name");
        }
        JFXScript tree;
        JavaFileObject filename = c.classfile;
        JavaFileObject prev = log.useSource(filename);

        try {
            tree = parse(filename, filename.getCharContent(false));
        } catch (IOException e) {
            log.error(MsgSym.MESSAGE_ERROR_READING_FILE, filename, e);
            tree = make.Script(null, List.<JFXTree>nil());
        } finally {
            log.useSource(prev);
        }

        if (taskListener != null) {
            JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.ENTER, tree);
            taskListener.started(e);
        }

        enter.complete(List.of(tree), c);

        if (taskListener != null) {
            JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.ENTER, tree);
            taskListener.finished(e);
        }

        TreeXMLTransformer.afterEnter(context, tree, c);

        if (enter.getEnv(c) == null) {
            boolean isPkgInfo =
                tree.sourcefile.isNameCompatible("package-info",
                                                 JavaFileObject.Kind.SOURCE);
            if (isPkgInfo) {
                if (enter.getEnv(tree.packge) == null) {
                    String msg
                        = Log.getLocalizedString(MsgSym.MESSAGE_FILE_DOES_NOT_CONTAIN_PACKAGE,
                                                 c.location());
                    throw new ClassReader.BadClassFile(c, filename, msg);
                }
            } else {
                throw new
                    ClassReader.BadClassFile(c, filename, Log.
                                             getLocalizedString(MsgSym.MESSAGE_FILE_DOES_NOT_CONTAIN_CLASS,
                                                                c.fullname));
            }
        }

        implicitSourceFilesRead = true;
    }

    /** Track when the JavaCompiler has been used to compile something. */
    private boolean hasBeenUsed = false;
    private long start_msec = 0;
    public long elapsed_msec = 0;

    /** Track whether any errors occurred while parsing source text. */
    private boolean parseErrors = false;

    public void compile(List<JavaFileObject> sourceFileObject)
        throws Throwable {
        compile(sourceFileObject, List.<String>nil(), null, null, false);
    }

    /**
     * Main method: compile a list of files, return all compiled classes
     *
     * @param sourceFileObjects file objects to be compiled
     * @param classnames class names to process for annotations
     * @param processors user provided annotation processors to bypass
     * discovery, {@code null} means that no processors were provided
     */
    public void compile(List<JavaFileObject> sourceFileObjects,
                        List<String> classnames,
                        Scope namedImportScope,
                        Scope starImportScope,
                        boolean preserveSymbols)
        throws IOException // TODO: temp, from JavacProcessingEnvironment
    {
        // as a JavaCompiler can only be used once, throw an exception if
        // it has been used before.
        if (hasBeenUsed)
            throw new AssertionError("attempt to reuse JavaCompiler");
        hasBeenUsed = true;

        start_msec = now();
        try {
            // Translate JavafxTrees into Javac trees.
            List<JFXScript> cus = stopIfError(parseFiles(sourceFileObjects));

//             stopIfError(buildJavafxModule(cus, sourceFileObjects));

            if (namedImportScope != null)
                cus.head.namedImportScope = namedImportScope;
            if (starImportScope != null)
                cus.head.starImportScope = starImportScope;
            // These method calls must be chained to avoid memory leaks
            enterTrees(cus);

            compile2(null);
            if (attr != null) {
                attr.clearCaches();
            }
            close(! preserveSymbols);
        } catch (Abort ex) {
            if (devVerbose)
                ex.printStackTrace();
        }
    }

    public List<JavafxEnv<JavafxAttrContext>> jfxToJava(List<JavafxEnv<JavafxAttrContext>> envs) {
        ListBuffer<JavafxEnv<JavafxAttrContext>> results = lb();
        for (List<JavafxEnv<JavafxAttrContext>> l = envs; l.nonEmpty(); l = l.tail) {
            jfxToJava(l.head, results);
        }
        return stopIfError(results);
    }

    public List<JavafxEnv<JavafxAttrContext>> jfxToJava(JavafxEnv<JavafxAttrContext> env) {
        ListBuffer<JavafxEnv<JavafxAttrContext>> results = lb();
        jfxToJava(env, results);
        return stopIfError(results);
    }

    protected void jfxToJava(final JavafxEnv<JavafxAttrContext> env, ListBuffer<JavafxEnv<JavafxAttrContext>> results) {
        try {
            if (errorCount() > 0)
                return;

            if (relax || deferredSugar.contains(env)) {
                results.append(env);
                return;
            }
            
            if (verboseCompilePolicy)
                Log.printLines(log.noticeWriter, "[toJava " + env.enclClass.sym + "]");
            JavaFileObject prev = log.useSource(
                                                env.enclClass.sym.sourcefile != null ?
                                                env.enclClass.sym.sourcefile :
                                                env.toplevel.sourcefile);
            try {
                make.at(Position.FIRSTPOS);
                jfxToJava.toJava(env);

                if (errorCount() > 0)
                    return;

                results.append(env);
            }
            catch (RuntimeException ex) {
                if (env.where != null)
                    log.note(env.where, MsgSym.MESSAGE_JAVAFX_INTERNAL_ERROR,
                             JavafxCompiler.fullVersion());
                throw ex;
            }
            finally {
                log.useSource(prev);
            }
        }
        finally {
            if (taskListener != null) {
                JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.ANALYZE, env.translatedToplevel, env.enclClass.sym);
                taskListener.finished(e);
            }
        }
    }

    /**
     * The phases following annotation processing: attribution,
     * desugar, and finally code generation.
     */
    private void compile2(ListBuffer<JavaFileObject> results) throws IOException {
        try {
            switch (compilePolicy) {
            case ATTR_ONLY:
                attribute(todo);
                break;

            case CHECK_ONLY:
                backEnd(prepForBackEnd(jfxToJava(varAnalysis(decomposeBinds(lower(attribute(todo)))))), results);
                break;

            case SIMPLE:
                backEnd(prepForBackEnd(jfxToJava(varAnalysis(decomposeBinds(lower(attribute(todo)))))), results);
                break;

            case BY_FILE: {
                ListBuffer<JavafxEnv<JavafxAttrContext>> envbuff = ListBuffer.lb();
                for (List<JavafxEnv<JavafxAttrContext>> list : groupByFile(jfxToJava(varAnalysis(decomposeBinds(lower(attribute(todo)))))).values())
                    envbuff.appendList(prepForBackEnd(list));
                backEnd(envbuff.toList(), results);
                break;
            }
            case BY_TODO: {
                ListBuffer<JavafxEnv<JavafxAttrContext>> envbuff = ListBuffer.lb();
                while (todo.nonEmpty()) {
                    envbuff.append(attribute(todo.next()));
                }

                backEnd(prepForBackEnd(jfxToJava(varAnalysis(decomposeBinds(lower(stopIfError(envbuff)))))), results);
                break;
            }
            default:
                assert false: "unknown compile policy";
            }
        } catch (Abort ex) {
            if (devVerbose)
                ex.printStackTrace();
        }

        if (verbose) {
            elapsed_msec = elapsed(start_msec);
            printVerbose(MsgSym.MESSAGE_TOTAL, Long.toString(elapsed_msec));
        }

        reportDeferredDiagnostics();

        if (!log.hasDiagnosticListener()) {
            printCount(MsgSym.MESSAGEPREFIX_ERROR, errorCount());
            printCount(MsgSym.MESSAGEPREFIX_WARN, warningCount());
        }

        ((JavafxTypes) types).clearCaches();
    }

    /**
     * Generate any files on the todo list.  Called by JavafxcTaskImpl.
     */
    public void generate(List<JavafxEnv<JavafxAttrContext>> genlist, ListBuffer<JavaFileObject> results) throws IOException {
        todo.appendList(genlist);
        compile2(results);
    }

    private void backEnd(List<JavafxEnv<JavafxAttrContext>> envs, ListBuffer<JavaFileObject> results) throws IOException {
        ListBuffer<JCCompilationUnit> javaTrees = lb();
        for (JavafxEnv<JavafxAttrContext> env : envs) {
            javaTrees.append(env.translatedToplevel);
        }

        PlatformPlugin plugin = PlatformPlugin.instance(context);
        if (plugin != null) {
            plugin.process(javaTrees);
            if (Log.instance(context).nerrors > 0)
                return;
        }

        javafxJavaCompiler.backEnd(javaTrees.toList(), results);
    }

    /**
     * Parses a list of files.
     */
   public List<JFXScript> parseFiles(List<JavaFileObject> fileObjects) throws IOException {
       if (errorCount() > 0)
           return List.nil();

        //parse all files
        ListBuffer<JFXScript> trees = lb();
        for (JavaFileObject fileObject : fileObjects)
            trees.append(parse(fileObject));
        return trees.toList();
    }

    /**
     * Enter the symbols found in a list of parse trees.
     * As a side-effect, this puts elements on the "todo" list.
     * Also stores a list of all top level classes in rootClasses.
     */
    public List<JFXScript> enterTrees(List<JFXScript> roots) {
        //enter symbols for all files
        if (taskListener != null) {
            for (JFXScript unit: roots) {
                JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.ENTER, unit);
                taskListener.started(e);
            }
        }

        enter.main(roots);

        if (taskListener != null) {
            for (JFXScript unit: roots) {
                JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.ENTER, unit);
                taskListener.finished(e);
            }
        }

        //If generating source, remember the classes declared in
        //the original compilation units listed on the command line.
        if (sourceOutput || stubOutput) {
            ListBuffer<JFXClassDeclaration> cdefs = lb();
            for (JFXScript unit : roots) {
                for (List<JFXTree> defs = unit.defs;
                     defs.nonEmpty();
                     defs = defs.tail) {
                    if (defs.head instanceof JFXClassDeclaration)
                        cdefs.append((JFXClassDeclaration)defs.head);
                }
            }
        }
        return roots;
    }

    /**
     * Check for errors -- called by JavafxTaskImpl.
     */
    public void errorCheck() throws IOException {
        backEnd(prepForBackEnd(jfxToJava(varAnalysis(decomposeBinds(lower(attribute(todo)))))), null);
    }

    /**
     * Attribute the existing JavafxTodo list.  Called by JavafxTaskImpl.
     */
    public List<JavafxEnv<JavafxAttrContext>> attribute() {
        return attribute(todo);
    }
    
    /**
     * Attribute a list of parse trees, such as found on the "todo" list.
     * Note that attributing classes may cause additional files to be
     * parsed and entered via the SourceCompleter.
     * Attribution of the entries in the list does not stop if any errors occur.
     * @returns a list of environments for attributd classes.
     */
    public List<JavafxEnv<JavafxAttrContext>> attribute(ListBuffer<JavafxEnv<JavafxAttrContext>> envs) {
        ListBuffer<JavafxEnv<JavafxAttrContext>> results = lb();
        while (envs.nonEmpty())
            results.append(attribute(envs.next()));
        return results.toList();
    }

    /**
     * Attribute a parse tree.
     * @returns the attributed parse tree
     */
    public JavafxEnv<JavafxAttrContext> attribute(JavafxEnv<JavafxAttrContext> env) {
        if (verboseCompilePolicy)
            Log.printLines(log.noticeWriter, "[attribute " + env.enclClass.sym + "]");
        if (verbose)
            printVerbose(MsgSym.MESSAGE_CHECKING_ATTRIBUTION, env.enclClass.sym);

        if (taskListener != null) {
            JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.ANALYZE, env.toplevel, env.enclClass.sym);
            taskListener.started(e);
        }

        JavaFileObject prev = log.useSource(
                                  env.enclClass.sym.sourcefile != null ?
                                  env.enclClass.sym.sourcefile :
                                  env.toplevel.sourcefile);
        try {
            attr.attribClass(env.tree.pos(), env.tree instanceof JFXClassDeclaration ? (JFXClassDeclaration)env.tree : null,
                env.enclClass.sym);
            printJavafxSource("dumpattr", env.toplevel, null);
        }
        finally {
            log.useSource(prev);
        }

        if (taskListener != null) {
            JavafxTaskEvent e = new JavafxTaskEvent(TaskEvent.Kind.ANALYZE, env.toplevel, env.enclClass.sym);
            taskListener.finished(e);
        }

        TreeXMLTransformer.afterAnalyze(context, env.toplevel, env.enclClass.sym);
        return env;
    }

    public List<JavafxEnv<JavafxAttrContext>> decomposeBinds(List<JavafxEnv<JavafxAttrContext>> envs) {
        for (List<JavafxEnv<JavafxAttrContext>> l = envs; l.nonEmpty(); l = l.tail) {
            decomposeBinds(l.head);
        }
        return envs;
    }

    protected void decomposeBinds(JavafxEnv<JavafxAttrContext> env) {
        try {
            // Lower has smashed our analysis
            bindAnalyzer.analyzeBindContexts(env);

            if (verboseCompilePolicy)
                Log.printLines(log.noticeWriter, "[decompose " + env.enclClass.sym + "]");

            boundFill.fill(env);
            printJavafxSource("dumpfill", env.toplevel, null);

            // JavafxLocalToClass needs var analysis info
            varUsageAnalysis.analyzeVarUse(env);

            localToClass.inflateAsNeeded(env);
            printJavafxSource("dumpinflate", env.toplevel, null);
            
            decomposeBindExpressions.decompose(env);
            printJavafxSource("dumpdecompose", env.toplevel, null);

        } catch (RuntimeException ex) {
            if (env.where != null) {
                log.note(env.where, MsgSym.MESSAGE_JAVAFX_INTERNAL_ERROR,
                        JavafxCompiler.fullVersion());
            }
            throw ex;
        }
    }

    /**
     * Analyze variable usage.
     * @returns the list of attributed parse trees
     */
    public List<JavafxEnv<JavafxAttrContext>> varAnalysis(List<JavafxEnv<JavafxAttrContext>> envs) {
        for (List<JavafxEnv<JavafxAttrContext>> l = envs; l.nonEmpty(); l = l.tail) {
            varAnalysis(l.head);
        }
        return envs;
    }

    /**
     * Morph types.
     * @returns the attributed parse tree
     */
    public JavafxEnv<JavafxAttrContext> varAnalysis(JavafxEnv<JavafxAttrContext> env) {
        if (verboseCompilePolicy)
            Log.printLines(log.noticeWriter, "[type-morph " + env.enclClass.sym + "]");

        JavaFileObject prev = log.useSource(
                                  env.enclClass.sym.sourcefile != null ?
                                  env.enclClass.sym.sourcefile :
                                  env.toplevel.sourcefile);
        try {
            varUsageAnalysis.analyzeVarUse(env);
        }
        finally {
            log.useSource(prev);
        }

        return env;
    }

    /**
     * Normalize tree before translation
     * @returns the list of attributed parse trees
     */
    public List<JavafxEnv<JavafxAttrContext>> lower(List<JavafxEnv<JavafxAttrContext>> envs) {
        for (List<JavafxEnv<JavafxAttrContext>> l = envs; l.nonEmpty(); l = l.tail) {
            lower(l.head);
        }
        return envs;
    }

    /**
     * Normalize tree before translation
     * @returns the attributed parse tree
     */
    public JavafxEnv<JavafxAttrContext> lower(JavafxEnv<JavafxAttrContext> env) {
        if (verboseCompilePolicy)
            Log.printLines(log.noticeWriter, "[lower " + env.enclClass.sym + "]");

        JavaFileObject prev = log.useSource(
                                  env.enclClass.sym.sourcefile != null ?
                                  env.enclClass.sym.sourcefile :
                                  env.toplevel.sourcefile);
        try {
            convertTypes.lower(env);
            printJavafxSource("dumplower", env.toplevel, null);
        }
        finally {
            log.useSource(prev);
        }

        return env;
    }

    public List<JavafxEnv<JavafxAttrContext>> prepForBackEnd(List<JavafxEnv<JavafxAttrContext>> envs) {
        printOptimizationStatistics();
        for (List<JavafxEnv<JavafxAttrContext>> l = envs; l.nonEmpty(); l = l.tail) {
            prepForBackEnd(l.head);
        }
        return envs;
    }

    public JavafxEnv<JavafxAttrContext> prepForBackEnd(JavafxEnv<JavafxAttrContext> env) {
        if (verboseCompilePolicy)
            Log.printLines(log.noticeWriter, "[prep-for-back-end " + env.enclClass.sym + "]");
        printJavaSource(env);

        JavaFileObject prev = log.useSource(
                                  env.enclClass.sym.sourcefile != null ?
                                  env.enclClass.sym.sourcefile :
                                  env.toplevel.sourcefile);
        try {
            prepForBackEnd.prep(env);
        }
        finally {
            log.useSource(prev);
        }

        return env;
    }

        // where
        Map<JFXScript, List<JavafxEnv<JavafxAttrContext>>> groupByFile(List<JavafxEnv<JavafxAttrContext>> list) {
            // use a LinkedHashMap to preserve the order of the original list as much as possible
            Map<JFXScript, List<JavafxEnv<JavafxAttrContext>>> map = new LinkedHashMap<JFXScript, List<JavafxEnv<JavafxAttrContext>>>();
            Set<JFXScript> fixupSet = new HashSet<JFXScript>();
            for (List<JavafxEnv<JavafxAttrContext>> l = list; l.nonEmpty(); l = l.tail) {
                JavafxEnv<JavafxAttrContext> env = l.head;
                List<JavafxEnv<JavafxAttrContext>> sublist = map.get(env.toplevel);
                if (sublist == null)
                    sublist = List.of(env);
                else {
                    // this builds the list for the file in reverse order, so make a note
                    // to reverse the list before returning.
                    sublist = sublist.prepend(env);
                    fixupSet.add(env.toplevel);
                }
                map.put(env.toplevel, sublist);
            }
            // fixup any lists that need reversing back to the correct order
            for (JFXScript tree: fixupSet)
                map.put(tree, map.get(tree).reverse());
            return map;
        }

    public void reportDeferredDiagnostics() {
        chk.reportDeferredDiagnostics();
    }

    /** Close the compiler, flushing the logs
     */
    public void close() {
        close(true);
    }

    private void close(boolean disposeNames) {
        make = null;
        writer = null;
        enter = null;
        if (todo != null)
            todo.clear();
        todo = null;
        syms = null;
        source = null;
        attr = null;
        chk = null;
        annotate = null;
        types = null;

        log.flush();
        try {
            fileManager.flush();
        } catch (IOException e) {
            throw new Abort(e);
        } finally {
            if (names != null && disposeNames)
                names.dispose();
            names = null;
        }
    }

    /** Output for "-verbose" option.
     *  @param key The key to look up the correct internationalized string.
     *  @param arg An argument for substitution into the output string.
     */
    protected void printVerbose(String key, Object arg) {
        Log.printLines(log.noticeWriter, Log.getLocalizedString(MsgSym.MESSAGEPREFIX_VERBOSE + key, arg));
    }

    /** Print numbers of errors and warnings.
     */
    protected void printCount(String kind, int count) {
        if (count != 0) {
            String text;
            if (count == 1)
                text = Log.getLocalizedString(MsgSym.MESSAGEPREFIX_COUNT + kind, String.valueOf(count));
            else
                text = Log.getLocalizedString(MsgSym.MESSAGEPREFIX_COUNT + kind + MsgSym.MESSAGESUFFIX_PLURAL, String.valueOf(count));
            Log.printLines(log.errWriter, text);
            log.errWriter.flush();
        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    private static long elapsed(long then) {
        return now() - then;
    }

    public void initRound(JavafxCompiler prev) {
        keepComments = prev.keepComments;
        start_msec = prev.start_msec;
        hasBeenUsed = true;
    }

    public static void enableLogging() {
        Logger logger = Logger.getLogger(com.sun.tools.mjavac.Main.class.getPackage().getName());
        logger.setLevel(Level.ALL);
        for (Handler h : logger.getParent().getHandlers()) {
            h.setLevel(Level.ALL);
       }

    }

    protected void registerServices(final Context context) {
        TreeXMLTransformer.preRegister(context);
        // if fileManager not already set, register the JavacFileManager to be used
        if (context.get(JavaFileManager.class) == null) {
            com.sun.tools.javafx.util.JavafxFileManager.preRegister(context);
        }
        com.sun.tools.javafx.code.JavafxSymtab.preRegister(context);
        com.sun.tools.javafx.code.JavafxTypes.preRegister(context);
    }
}
