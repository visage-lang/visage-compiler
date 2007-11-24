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

package com.sun.tools.javafx.main;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.DiagnosticListener;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.processing.*;
import com.sun.tools.javafx.comp.*;
import com.sun.tools.javafx.code.*;
import java.util.Iterator;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import static com.sun.tools.javac.util.ListBuffer.lb;
import com.sun.tools.javafx.antlr.*;

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
        return version("javafx.version");
    }

    /** The current full version number as a string.
     */
    public static String fullVersion() {
        return version("javafx.full"); // mm.mm.oo[-milestone]-build
    }

    private static final String versionRBName = "com.sun.tools.javafx.resources.javafxcompiler";
    private static ResourceBundle versionRB;

    private static String version(String key) {
        if (versionRB == null) {
            try {
                versionRB = ResourceBundle.getBundle(versionRBName);
            } catch (MissingResourceException e) {
                return Log.getLocalizedString("version.resource.missing", System.getProperty("java.version"));
            }
        }
        try {
            return versionRB.getString(key);
        }
        catch (MissingResourceException e) {
            return Log.getLocalizedString("version.unknown", System.getProperty("java.version"));
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

    /** The class reader.
     */
    protected ClassReader reader;

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

    /** The attributor.
     */
    protected JavafxAttr attr;

    /** The attributor.
     */
    protected JavafxCheck chk;

    /** The annotation annotator.
     */
    protected JavafxAnnotate annotate;
    
    /** The back-end preper
     */
    protected JavafxPrepForBackEnd prepForBackEnd;    
    
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
    protected TaskListener taskListener;

// Javafx change
    protected JavafxModuleBuilder javafxModuleBuilder;
    protected JavafxVarUsageAnalysis varUsageAnalysis;
    protected JavafxToJava jfxToJava;
    protected JavafxInitializationBuilder initBuilder;

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
        
        // if fileManager not already set, register the JavacFileManager to be used
        if (context.get(JavaFileManager.class) == null)
	    com.sun.tools.javafx.util.JavafxFileManager.preRegister(context);
        com.sun.tools.javafx.tree.JavafxTreeMaker.preRegister(context);
        com.sun.tools.javafx.tree.JavafxTreeInfo.preRegister(context);
        com.sun.tools.javafx.code.JavafxSymtab.preRegister(context);
        com.sun.tools.javafx.comp.JavafxClassReader.preRegister(context);
        
        javafxJavaCompiler = JavafxJavaCompiler.instance(context);
        names = Name.Table.instance(context);
        options = Options.instance(context);
        log = Log.instance(context);
        reader = ClassReader.instance(context);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        writer = ClassWriter.instance(context);
        enter = JavafxEnter.instance(context);
        todo = JavafxTodo.instance(context);

        fileManager = context.get(JavaFileManager.class);

        javafxModuleBuilder = JavafxModuleBuilder.instance(context);
        varUsageAnalysis = JavafxVarUsageAnalysis.instance(context);
        jfxToJava = JavafxToJava.instance(context);
        initBuilder = JavafxInitializationBuilder.instance(context);
        prepForBackEnd = JavafxPrepForBackEnd.instance(context);
        
        // Add the javafx message resource bundle
        Messages.instance(context).add(javafxErrorsKey);
        try {
            // catch completion problems with predefineds
            syms = (JavafxSymtab)JavafxSymtab.instance(context);
        } catch (CompletionFailure ex) {
            // inlined Check.completionError as it is not initialized yet
            log.error("cant.access", ex.sym, ex.errmsg);
            if (ex instanceof ClassReader.BadClassFile)
                throw new Abort();
        }
        source = Source.instance(context);
        attr = JavafxAttr.instance(context);
        chk = JavafxCheck.instance(context);
        annotate = JavafxAnnotate.instance(context);
        types = Types.instance(context);
        taskListener = context.get(TaskListener.class);

        reader.sourceCompleter = this;

        Options options = Options.instance(context);

        verbose       = options.get("-verbose")       != null;
        sourceOutput  = options.get("-printsource")   != null; // used to be -s
        stubOutput    = options.get("-stubs")         != null;
        relax         = options.get("-relax")         != null;
        printFlat     = options.get("-printflat")     != null;
        attrParseOnly = options.get("-attrparseonly") != null;
        encoding      = options.get("-encoding");
        lineDebugInfo = options.get("-g:")            == null ||
                        options.get("-g:lines")       != null;
        genEndPos     = options.get("-Xjcov")         != null ||
                        context.get(DiagnosticListener.class) != null;
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

    /** Switch: should we store the ending positions?
     */
    public boolean genEndPos;

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
    protected CompilePolicy compilePolicy;
    
    /**
     * The policy for what to do with implicitly read source files
     */
    protected ImplicitSourcePolicy implicitSourcePolicy;

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
            log.error("error.reading.file", filename, e.getLocalizedMessage());
            return null;
        }
    }

    /** Parse contents of input stream.
     *  @param filename     The name of the file from which input stream comes.
     *  @param input        The input stream to be parsed.
     */
    protected JCCompilationUnit parse(JavaFileObject filename, CharSequence content) {
        long msec = now();
        JCCompilationUnit tree = make.TopLevel(List.<JCTree.JCAnnotation>nil(),
                                      null, List.<JCTree>nil());
        if (content != null) {
            if (verbose) {
                printVerbose("parsing.started", filename);
            }
            if (taskListener != null) {
                TaskEvent e = new TaskEvent(TaskEvent.Kind.PARSE, filename);
                taskListener.started(e);
            }
	    int initialErrorCount = log.nerrors;
            String parserChoice = options.get("parser");
            if (parserChoice == null) {
                parserChoice = "vn"; // default
            }
            {
                AbstractGeneratedParser generatedParser;
//                if (parserChoice.equals("v1")) {
//                    generatedParser = new v1Parser(context, content);
//                } else {
                    generatedParser = new v2Parser(context, content);
//                }
                try { 
                    JCCompilationUnit unit = generatedParser.module();
                    if (unit != null) // test shouldn't be needed when we have better error recovery
                        tree = unit;
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                parseErrors |= (log.nerrors > initialErrorCount);
                if (lineDebugInfo) {
                    String hunk = content.toString();
                    tree.lineMap = Position.makeLineMap(hunk.toCharArray(), hunk.length(), false);
                }        
            }
            if (verbose) {
                printVerbose("parsing.done", Long.toString(elapsed(msec)));
            }
        }

        tree.sourcefile = filename;
        
        printJavafxSource(tree);

        javafxModuleBuilder.visitTopLevel(tree);

        if (content != null && taskListener != null) {
            TaskEvent e = new TaskEvent(TaskEvent.Kind.PARSE, tree);
            taskListener.finished(e);
        }

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
    public JCTree.JCCompilationUnit parse(JavaFileObject filename) {
        JavaFileObject prev = log.useSource(filename);
        try {
            JCTree.JCCompilationUnit t = parse(filename, readSource(filename));
            if (t.endPositions != null)
                log.setEndPosTable(filename, t.endPositions);
            return t;
        } finally {
            log.useSource(prev);
        }
    }

    /** Emit Java-like source corresponding to an input file.
     */
    void printJavaSource(JavafxEnv<JavafxAttrContext> env) throws IOException {
        String dump = options.get("dumpjava");
        if (dump != null) {
            String fn = env.toplevel.sourcefile.toString().replace(".fx", ".javadump");
            File outFile = new File(dump, (new File(fn)).getName());
            FileWriter fw = new FileWriter(outFile);
            BufferedWriter out = new BufferedWriter(fw);
            try {
                new BlockExprPretty(out, true).printUnit(env.toplevel, null);
            } finally {
                out.close();
            }
        }
    }

    /** Emit pretty=printed fx source corresponding to an input file.
     */
    void printJavafxSource(JCCompilationUnit cu) {
        String dump = options.get("dumpfx");
        BufferedWriter out = null;
        if (dump != null) {
            try {
                try {
                    String fn = cu.sourcefile.toString().replace(".fx", ".fxdump");
                    File outFile = new File(dump, (new File(fn)).getName());
                    FileWriter fw = new FileWriter(outFile);
                    out = new BufferedWriter(fw);
                    new JavafxPretty(out, true).printUnit(cu, null);
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
        JCCompilationUnit tree;
        JavaFileObject filename = c.classfile;
        JavaFileObject prev = log.useSource(filename);

        try {
            tree = parse(filename, filename.getCharContent(false));
        } catch (IOException e) {
            log.error("error.reading.file", filename, e);
            tree = make.TopLevel(List.<JCTree.JCAnnotation>nil(), null, List.<JCTree>nil());
        } finally {
            log.useSource(prev);
        }

        if (taskListener != null) {
            TaskEvent e = new TaskEvent(TaskEvent.Kind.ENTER, tree);
            taskListener.started(e);
        }

        enter.complete(List.of(tree), c);

        if (taskListener != null) {
            TaskEvent e = new TaskEvent(TaskEvent.Kind.ENTER, tree);
            taskListener.finished(e);
        }

        if (enter.getEnv(c) == null) {
            boolean isPkgInfo =
                tree.sourcefile.isNameCompatible("package-info",
                                                 JavaFileObject.Kind.SOURCE);
            if (isPkgInfo) {
                if (enter.getEnv(tree.packge) == null) {
                    String msg
                        = log.getLocalizedString("file.does.not.contain.package",
                                                 c.location());
                    throw new ClassReader.BadClassFile(c, filename, msg);
                }
            } else {
                throw new
                    ClassReader.BadClassFile(c, filename, log.
                                             getLocalizedString("file.doesnt.contain.class",
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
        compile(sourceFileObject, List.<String>nil());
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
                        List<String> classnames)
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
            List<JCCompilationUnit> cus = stopIfError(parseFiles(sourceFileObjects));

//             stopIfError(buildJavafxModule(cus, sourceFileObjects));

            // These method calls must be chained to avoid memory leaks
            enterTrees(cus);
            
            compile2(null);
            close();
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
    
    protected void jfxToJava(JavafxEnv<JavafxAttrContext> env, ListBuffer<JavafxEnv<JavafxAttrContext>> results) {
        try {
            if (errorCount() > 0)
                return;

            if (relax || deferredSugar.contains(env)) {
                results.append(env);
                return;
            }

            if (verboseCompilePolicy)
                log.printLines(log.noticeWriter, "[flow " + env.enclClass.sym + "]");
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
            finally {
                log.useSource(prev);
            }
        }
        finally {
            if (taskListener != null) {
                TaskEvent e = new TaskEvent(TaskEvent.Kind.ANALYZE, env.toplevel, env.enclClass.sym);
                taskListener.finished(e);
            }
        }
    }

    public List<JCCompilationUnit> buildJavafxModule(List<JCCompilationUnit> cus, List<JavaFileObject> sourceFileObjects) {
        Iterator<JavaFileObject> fileObjIterator = sourceFileObjects.iterator();
        for (JCCompilationUnit cu : cus) {
            JavaFileObject jfo = fileObjIterator.next();
            JavaFileObject prev = log.useSource(jfo);
            try {
                javafxModuleBuilder.visitTopLevel(cu);
            }
            finally {
                log.useSource(prev);
            }
        }
        return cus;
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
                backEnd(prepForBackEnd(jfxToJava(varAnalysis(attribute(todo)))), results);
                break;

            case SIMPLE:
                backEnd(prepForBackEnd(jfxToJava(varAnalysis(attribute(todo)))), results);
                break;

            case BY_FILE: {
                ListBuffer<JavafxEnv<JavafxAttrContext>> envbuff = ListBuffer.lb();
                for (List<JavafxEnv<JavafxAttrContext>> list : groupByFile(jfxToJava(varAnalysis(attribute(todo)))).values())
                    envbuff.appendList(prepForBackEnd(list));
                backEnd(envbuff.toList(), results);
                break;
            }
            case BY_TODO: {
                ListBuffer<JavafxEnv<JavafxAttrContext>> envbuff = ListBuffer.lb();
                while (todo.nonEmpty()) {
                    envbuff.append(attribute(todo.next()));
                }
                
                backEnd(prepForBackEnd(jfxToJava(varAnalysis(envbuff.toList()))), results);
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
	    elapsed_msec = elapsed(start_msec);;
            printVerbose("total", Long.toString(elapsed_msec));
	}

        reportDeferredDiagnostics();

        if (!log.hasDiagnosticListener()) {
            printCount("error", errorCount());
            printCount("warn", warningCount());
        }
        
        if (initBuilder != null) {
            initBuilder.clearCaches();
        }
    }
    
    /**
     * Generate any files on the todo list.  Called by JavafxcTaskImpl.
     */
    public void generate(ListBuffer<JavaFileObject> results) throws IOException {
        compile2(results);
    }
    
    private void backEnd(List<JavafxEnv<JavafxAttrContext>> envs, ListBuffer<JavaFileObject> results) throws IOException {
        ListBuffer<JCCompilationUnit> trees = lb();
        for (JavafxEnv<JavafxAttrContext> env : envs) {
            printJavaSource(env);
            trees.append(env.toplevel);
       }
       javafxJavaCompiler.backEnd(trees.toList(), results);
    }

    /**
     * Parses a list of files.
     */
   public List<JCCompilationUnit> parseFiles(List<JavaFileObject> fileObjects) throws IOException {
       if (errorCount() > 0)
       	   return List.nil();

        //parse all files
        ListBuffer<JCCompilationUnit> trees = lb();
        for (JavaFileObject fileObject : fileObjects)
            trees.append(parse(fileObject));
        return trees.toList();
    }

    /**
     * Enter the symbols found in a list of parse trees.
     * As a side-effect, this puts elements on the "todo" list.
     * Also stores a list of all top level classes in rootClasses.
     */
    public List<JCCompilationUnit> enterTrees(List<JCCompilationUnit> roots) {
        //enter symbols for all files
        if (taskListener != null) {
            for (JCCompilationUnit unit: roots) {
                TaskEvent e = new TaskEvent(TaskEvent.Kind.ENTER, unit);
                taskListener.started(e);
            }
        }
        
        enter.main(roots);
        
        if (taskListener != null) {
            for (JCCompilationUnit unit: roots) {
                TaskEvent e = new TaskEvent(TaskEvent.Kind.ENTER, unit);
                taskListener.finished(e);
            }
        }

        //If generating source, remember the classes declared in
        //the original compilation units listed on the command line.
        if (sourceOutput || stubOutput) {
            ListBuffer<JCClassDecl> cdefs = lb();
            for (JCCompilationUnit unit : roots) {
                for (List<JCTree> defs = unit.defs;
                     defs.nonEmpty();
                     defs = defs.tail) {
                    if (defs.head instanceof JCClassDecl)
                        cdefs.append((JCClassDecl)defs.head);
                }
            }
        }
        return roots;
    }

    /**
     * Check for errors -- called by JavafxTaskImpl.
     */
    public void errorCheck() throws IOException {
        backEnd(prepForBackEnd(jfxToJava(varAnalysis(attribute(todo)))), null);
    }
    
    /**
     * Attribute the existing JavafxTodo list.  Called by JavafxTaskImpl.
     */
    public void attribute() {
        attribute(todo);
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
            log.printLines(log.noticeWriter, "[attribute " + env.enclClass.sym + "]");
        if (verbose)
            printVerbose("checking.attribution", env.enclClass.sym);

        if (taskListener != null) {
            TaskEvent e = new TaskEvent(TaskEvent.Kind.ANALYZE, env.toplevel, env.enclClass.sym);
            taskListener.started(e);
        }

        JavaFileObject prev = log.useSource(
                                  env.enclClass.sym.sourcefile != null ?
                                  env.enclClass.sym.sourcefile :
                                  env.toplevel.sourcefile);
        try {
            attr.attribClass(env.tree.pos(), env.tree instanceof JFXClassDeclaration ? (JFXClassDeclaration)env.tree : null,
                env.enclClass.sym);
        }
        finally {
            log.useSource(prev);
        }

        return env;
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
            log.printLines(log.noticeWriter, "[type-morph " + env.enclClass.sym + "]");

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

    public List<JavafxEnv<JavafxAttrContext>> prepForBackEnd(List<JavafxEnv<JavafxAttrContext>> envs) {
        for (List<JavafxEnv<JavafxAttrContext>> l = envs; l.nonEmpty(); l = l.tail) {
            prepForBackEnd(l.head);
        }
        return envs;
    }

    public JavafxEnv<JavafxAttrContext> prepForBackEnd(JavafxEnv<JavafxAttrContext> env) {
        if (verboseCompilePolicy)
            log.printLines(log.noticeWriter, "[prep-for-back-end " + env.enclClass.sym + "]");

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
        Map<JCCompilationUnit, List<JavafxEnv<JavafxAttrContext>>> groupByFile(List<JavafxEnv<JavafxAttrContext>> list) {
            // use a LinkedHashMap to preserve the order of the original list as much as possible
            Map<JCCompilationUnit, List<JavafxEnv<JavafxAttrContext>>> map = new LinkedHashMap<JCCompilationUnit, List<JavafxEnv<JavafxAttrContext>>>();
            Set<JCCompilationUnit> fixupSet = new HashSet<JCTree.JCCompilationUnit>();
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
            for (JCTree.JCCompilationUnit tree: fixupSet)
                map.put(tree, map.get(tree).reverse());
            return map;
        }

        JCClassDecl removeMethodBodies(JCClassDecl cdef) {
            final boolean isInterface = (cdef.mods.flags & Flags.INTERFACE) != 0;
            class MethodBodyRemover extends TreeTranslator { // TODO: Javafx change Do we need JavafxTreeTranslator here?
                public void visitMethodDef(JCMethodDecl tree) {
                    tree.mods.flags &= ~Flags.SYNCHRONIZED;
                    for (JCVariableDecl vd : tree.params)
                        vd.mods.flags &= ~Flags.FINAL;
                    tree.body = null;
                    super.visitMethodDef(tree);
                }
                public void visitVarDef(JCVariableDecl tree) {
                    if (tree.init != null && tree.init.type.constValue() == null)
                        tree.init = null;
                    super.visitVarDef(tree);
                }
                public void visitClassDef(JCClassDecl tree) {
                    ListBuffer<JCTree> newdefs = lb();
                    for (List<JCTree> it = tree.defs; it.tail != null; it = it.tail) {
                        JCTree t = it.head;
                        switch (t.getTag()) {
                        case JCTree.CLASSDEF:
                            if (isInterface ||
                                (((JCClassDecl) t).mods.flags & (Flags.PROTECTED|Flags.PUBLIC)) != 0 ||
                                (((JCClassDecl) t).mods.flags & (Flags.PRIVATE)) == 0 && ((JCClassDecl) t).sym.packge().getQualifiedName() == names.java_lang)
                                newdefs.append(t);
                            break;
                        case JCTree.METHODDEF:
                            if (isInterface ||
                                (((JCMethodDecl) t).mods.flags & (Flags.PROTECTED|Flags.PUBLIC)) != 0 ||
                                ((JCMethodDecl) t).sym.name == names.init ||
                                (((JCMethodDecl) t).mods.flags & (Flags.PRIVATE)) == 0 && ((JCMethodDecl) t).sym.packge().getQualifiedName() == names.java_lang)
                                newdefs.append(t);
                            break;
                        case JCTree.VARDEF:
                            if (isInterface || (((JCVariableDecl) t).mods.flags & (Flags.PROTECTED|Flags.PUBLIC)) != 0 ||
                                (((JCVariableDecl) t).mods.flags & (Flags.PRIVATE)) == 0 && ((JCVariableDecl) t).sym.packge().getQualifiedName() == names.java_lang)
                                newdefs.append(t);
                            break;
                        default:
                            break;
                        }
                    }
                    tree.defs = newdefs.toList();
                    super.visitClassDef(tree);
                }
            }
            MethodBodyRemover r = new MethodBodyRemover();
            return r.translate(cdef);
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
        reader = null;
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
        Log.printLines(log.noticeWriter, log.getLocalizedString("verbose." + key, arg));
    }

    /** Print numbers of errors and warnings.
     */
    protected void printCount(String kind, int count) {
        if (count != 0) {
            String text;
            if (count == 1)
                text = log.getLocalizedString("count." + kind, String.valueOf(count));
            else
                text = log.getLocalizedString("count." + kind + ".plural", String.valueOf(count));
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
        Logger logger = Logger.getLogger(com.sun.tools.javac.Main.class.getPackage().getName());
        logger.setLevel(Level.ALL);
        for (Handler h : logger.getParent().getHandlers()) {
            h.setLevel(Level.ALL);
       }

    }
}
