/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.migrator.main;

import  com.sun.tools.migrator.antlr.*;
import  com.sun.tools.migrator.tree.*;
import  com.sun.tools.migrator.tree.MTTree.*;

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
import java.util.Iterator;

import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.processing.*;

import com.sun.tools.javafx.code.*;

/** This class is the root of the migration tool: proto-JavaFX => released JavaFX.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class MigrationTool {

    /** The log to be used for error reporting.
     */
    public Log log;

    /** The tree factory module.
     */
    protected MTTreeMaker make;

    /** The class reader.
     */
    protected ClassReader reader;

    /** The class writer.
     */
    protected ClassWriter writer;

    /** The language version.
     */
    protected Source source;

    /** The name table.
     */
    protected Name.Table names;

    /** Force a completion failure on this name
     */
    protected final Name completionFailureName;

    /** Type utilities.
     */
    protected Types types;

//    protected MTToJava jfxToJava;

    protected Context context;
    
    static String destDir;

    /** Construct a new compiler using a shared context.
     */
    public MigrationTool(final Context context) {
        this.context = context;
        MTTreeMaker.instance(context);
        MTTreeInfo.preRegister(context);

        names = Name.Table.instance(context);
        log = Log.instance(context);
        reader = ClassReader.instance(context);
        make = (MTTreeMaker)MTTreeMaker.instance(context);
        writer = ClassWriter.instance(context);

        source = Source.instance(context);
        types = Types.instance(context);
    }

    /* Switches:
     */

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
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: mtool dest_dir src1.fx src2.fx ...");
            System.exit(1);
        }
        MigrationTool mig = new MigrationTool(new Context());
        mig.migrate(args);
    }
    
    void migrate(String[] args) {
        destDir = args[0];
        for (int i = 1; i < args.length; ++i) {
            String src = args[1];
            MTCompilationUnit cu = parse(src);
            printMTSource(cu, src);
        }
    }

    /** Parse contents of input stream.
     *  @param filename     The name of the file from which input stream comes.
     *  @param input        The input stream to be parsed.
     */
    protected MTCompilationUnit parse(String src) {
        File srcFile = new File(src);
        BufferedReader inputReader = 
            new BufferedReader(new FileReader(srcFile));
        int length = (int)srcFile.length();
        char[] content = new char[length];
        inputReader.read(content, 0, length);
        MTCompilationUnit tree = make.TopLevel(null, List.<MTTree>nil());
        if (content != null) {
	    int initialErrorCount = log.nerrors;
            {
                AbstractGeneratedParser generatedParser = new interpParser(context, content);
                try {  
                    tree = generatedParser.module();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                parseErrors |= (log.nerrors > initialErrorCount);
            }
        }

        return tree;
    }

    /** Emit pretty=printed fx source corresponding to an input file.
     */
    void printMTSource(MTCompilationUnit cu, String src) {
        BufferedWriter out = null;
        {
            try {
                try {
                    File outFile = new File(destDir, src);
                    FileWriter fw = new FileWriter(outFile);
                    out = new BufferedWriter(fw);
                    new MTPretty(out, true).printUnit(cu, null);
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


    /** Track whether any errors occurred while parsing source text. */
    private boolean parseErrors = false;

    /****
    public List<MTEnv<MTAttrContext>> jfxToJava(List<MTEnv<MTAttrContext>> envs) {
        ListBuffer<MTEnv<MTAttrContext>> results = lb();
        for (List<MTEnv<MTAttrContext>> l = envs; l.nonEmpty(); l = l.tail) {
            jfxToJava(l.head, results);
        }
        return stopIfError(results);
    }

    public List<MTEnv<MTAttrContext>> jfxToJava(MTEnv<MTAttrContext> env) {
        ListBuffer<MTEnv<MTAttrContext>> results = lb();
        jfxToJava(env, results);
        return stopIfError(results);
    }
    
    protected void jfxToJava(MTEnv<MTAttrContext> env, ListBuffer<MTEnv<MTAttrContext>> results) {
        try {
            if (errorCount() > 0)
                return;

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
    }
     * ****/

    /** Close the compiler, flushing the logs
     */
    public void close() {
        close(true);
    }

    private void close(boolean disposeNames) {
        reader = null;
        make = null;
        writer = null;
        source = null;
        types = null;

        log.flush();
        {
            if (names != null && disposeNames)
                names.dispose();
            names = null;
        }
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

    public static void enableLogging() {
        Logger logger = Logger.getLogger(com.sun.tools.javac.Main.class.getPackage().getName());
        logger.setLevel(Level.ALL);
        for (Handler h : logger.getParent().getHandlers()) {
            h.setLevel(Level.ALL);
       }

    }
}
