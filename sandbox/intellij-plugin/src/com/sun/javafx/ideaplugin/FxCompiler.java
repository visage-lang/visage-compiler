/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.ideaplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.ProjectJdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FxCompiler
 *
 * @author Brian Goetz
 */
// TODO - use reflection for classes in "javax.tools" package as well to support JDK 1.5
public class FxCompiler implements TranslatingCompiler {

    private static final EnumMap<Diagnostic.Kind, CompilerMessageCategory> diagnosticKindMap = new EnumMap<Diagnostic.Kind, CompilerMessageCategory>(Diagnostic.Kind.class);

    static {
        diagnosticKindMap.put(Diagnostic.Kind.ERROR, CompilerMessageCategory.ERROR);
        diagnosticKindMap.put(Diagnostic.Kind.MANDATORY_WARNING, CompilerMessageCategory.WARNING);
        diagnosticKindMap.put(Diagnostic.Kind.WARNING, CompilerMessageCategory.WARNING);
        diagnosticKindMap.put(Diagnostic.Kind.NOTE, CompilerMessageCategory.INFORMATION);
        diagnosticKindMap.put(Diagnostic.Kind.OTHER, CompilerMessageCategory.INFORMATION);
    }

    public FxCompiler() {
    }

    public boolean isCompilableFile(VirtualFile virtualFile, CompileContext compileContext) {
        return FxPlugin.FX_FILE_TYPE.equals(virtualFile.getFileType());
    }

    public ExitStatus compile(final CompileContext compileContext, VirtualFile[] virtualFiles) {
        Map<Module, Set<VirtualFile>> map = buildModuleToFilesMap(compileContext, virtualFiles);

        final AtomicBoolean compilationFailed = new AtomicBoolean (false);
        final ArrayList<VirtualFile> filesToRecompile = new ArrayList<VirtualFile> ();
        final ArrayList<OutputItem> outputItems = new ArrayList<OutputItem> ();

        for (Map.Entry<Module, Set<VirtualFile>> entry : map.entrySet()) {
            if (compilationFailed.get ())
                continue;
            Module module = entry.getKey();
            Set<VirtualFile> files = entry.getValue();
            ModuleRootManager rootManager = ModuleRootManager.getInstance(module);

            filesToRecompile.addAll (files);
            List<String> args = createCommandLine (rootManager);

            ProjectJdk jdk = rootManager.getJdk (); // TODO - check not-null
            if (jdk == null) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "JDK not set for module: " + module.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            }

            ClassLoader loader;
            try {
                loader = new URLClassLoader (new URL[] { new URL ("file://" + jdk.getHomeDirectory ().getPath () + "/lib/javafxc.jar") }, getClass ().getClassLoader ());
            } catch (MalformedURLException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot locate javafxc.jar library in SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            }

            Object javafxTool;
            Class<?> javafxToolClass;
            Method getStandardFileManagerMethod;
            Class<?> javafxFileManagerClass;
            Method getFileForInputMethod;
            Method getTaskMethod;
            Class<?> javafxTaskClass;
            Method callMethod;
            Method parseMethod;
            Method analyzeMethod;
            Method generateMethod;
            try {
                javafxToolClass = Class.forName ("com.sun.tools.javafx.api.JavafxcTool", true, loader);
                javafxTool = javafxToolClass.getMethod ("create").invoke (null);
                getStandardFileManagerMethod = javafxToolClass.getMethod ("getStandardFileManager", DiagnosticListener.class, Locale.class, Charset.class);
                javafxFileManagerClass = Class.forName ("com.sun.tools.javafx.util.JavafxFileManager", true, loader);
                getFileForInputMethod = javafxFileManagerClass.getMethod ("getFileForInput", String.class);
                getTaskMethod = javafxToolClass.getMethod ("getTask", Writer.class, JavaFileManager.class, DiagnosticListener.class, Iterable.class, Iterable.class);
                javafxTaskClass = Class.forName ("com.sun.javafx.api.JavafxcTask", true, loader);
                callMethod = javafxTaskClass.getMethod ("call");
                parseMethod = javafxTaskClass.getMethod ("parse");
                analyzeMethod = javafxTaskClass.getMethod ("analyze");
                generateMethod = javafxTaskClass.getMethod ("generate");
            } catch (ClassNotFoundException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            } catch (InvocationTargetException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            } catch (NoSuchMethodException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            } catch (IllegalAccessException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            }

            List<JavaFileObject> filePaths = new ArrayList<JavaFileObject>();
            final Map<JavaFileObject, VirtualFile> fileMap = new HashMap<JavaFileObject, VirtualFile> ();
            for (VirtualFile file : files) {
                JavaFileObject javaFileObject;
                try {
                    Object javacFileManager = getStandardFileManagerMethod.invoke (javafxTool, null, null, Charset.defaultCharset ());
                    javaFileObject = (JavaFileObject) getFileForInputMethod.invoke (javacFileManager, file.getPath ());
                } catch (IllegalAccessException e) {
                    compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                    compilationFailed.set (true);
                    break;
                } catch (InvocationTargetException e) {
                    compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                    compilationFailed.set (true);
                    break;
                }
                fileMap.put(javaFileObject, file);
                filePaths.add(javaFileObject);
            }

            final AtomicBoolean errorWhileCompiling = new AtomicBoolean (false);
            Object compilerTask;
            try {
                compilerTask = getTaskMethod.invoke (javafxTool, null, null, new DiagnosticListener() {
                    public void report(Diagnostic diagnostic) {
                        compileContext.addMessage(diagnosticKindMap.get(diagnostic.getKind()), diagnostic.getMessage(Locale.getDefault()),
                                fileMap.get(diagnostic.getSource()).getUrl(), (int) diagnostic.getLineNumber(), (int) diagnostic.getColumnNumber());
                        if (diagnostic.getKind () == Diagnostic.Kind.ERROR) {
                            errorWhileCompiling.set (true);
                            compilationFailed.set (true);
                        }
                    }
                }, args, filePaths);
            } catch (IllegalAccessException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            } catch (InvocationTargetException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            }

            try {
                Object state = callMethod.invoke (compilerTask);
                if (state == Boolean.FALSE  &&  ! errorWhileCompiling.get ()) {
                    compileContext.addMessage (CompilerMessageCategory.ERROR, "Compilation error in module: " + module.getName (), null, 0, 0);
                    errorWhileCompiling.set (true);
                }
/*                Object iterable;
                iterable = parseMethod.invoke (compilerTask);
                System.out.println ("iterable = " + iterable);
                iterable = analyzeMethod.invoke (compilerTask);
                System.out.println ("iterable = " + iterable);
                iterable = generateMethod.invoke (compilerTask); // TODO - store result
                System.out.println ("iterable = " + iterable);*/
            } catch (IllegalAccessException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            } catch (InvocationTargetException e) {
                compileContext.addMessage (CompilerMessageCategory.ERROR, "Cannot link with JavaFX Compiler of SDK: " + jdk.getName (), null, 0, 0);
                compilationFailed.set (true);
                break;
            }

            if (! errorWhileCompiling.get ()) {
                filesToRecompile.removeAll (files);
            }
        }

        return new ExitStatus() {
            public OutputItem[] getSuccessfullyCompiled() {
                return compilationFailed.get () ? new OutputItem[0] : outputItems.toArray (new OutputItem[outputItems.size ()]);
             }

            public VirtualFile[] getFilesToRecompile() {
                return compilationFailed.get() ? filesToRecompile.toArray(new VirtualFile[filesToRecompile.size()]) : new VirtualFile[0];
            }
        };
    }

    @NotNull
    public String getDescription() {
        return FxPlugin.FX_LANGUAGE_NAME;
    }

    public boolean validateConfiguration(CompileScope compileScope) {
        return true;
    }

    private static Map<Module, Set<VirtualFile>> buildModuleToFilesMap(final CompileContext context, final VirtualFile[] files) {
        final Map<Module, Set<VirtualFile>> map = new HashMap<Module, Set<VirtualFile>>();
        ApplicationManager.getApplication().runReadAction(new Runnable() {
            public void run() {
                for (VirtualFile file : files) {
                    final Module module = context.getModuleByFile(file);
                    if (module == null)
                        continue;
                    Set<VirtualFile> moduleFiles = map.get(module);
                    if (moduleFiles == null) {
                        moduleFiles = new HashSet<VirtualFile>();
                        map.put(module, moduleFiles);
                    }
                    moduleFiles.add(file);
                }
            }
        });
        return map;
    }

    private static List<String> createCommandLine (ModuleRootManager moduleRootManager) {
        OrderEntry[] entries = moduleRootManager.getOrderEntries();
        List<String> args = new ArrayList<String>();

        args.add("-target");
        args.add("1.5");

        String moduleOutputUrl = moduleRootManager.getCompilerOutputPathUrl(); // TODO - check and fail when null output path
        if (moduleOutputUrl != null) {
            args.add("-d");
            args.add(VirtualFileManager.extractPath(moduleOutputUrl));
        }

        List<VirtualFile> sourcepath = new ArrayList<VirtualFile> ();
        for (OrderEntry orderEntry : entries)
            sourcepath.addAll(Arrays.asList(orderEntry.getFiles(OrderRootType.SOURCES)));
        args.add ("-sourcepath");
        args.add (path2string (sourcepath));

        List<VirtualFile> classpath = new ArrayList<VirtualFile> ();
        for (OrderEntry orderEntry : entries)
            classpath.addAll(Arrays.asList(orderEntry.getFiles(OrderRootType.COMPILATION_CLASSES)));
        args.add("-cp");
        args.add(path2string (classpath));

        return args;
    }

    private static String path2string (List<VirtualFile> pathEntries) {
        StringBuffer buffer = new StringBuffer();
        for (VirtualFile entry : pathEntries) {
            if (buffer.length () != 0)
                buffer.append(File.pathSeparator);
            String path = entry.getPath();
            int jarSeparatorIndex = path.indexOf(JarFileSystem.JAR_SEPARATOR);
            if (jarSeparatorIndex > 0)
                path = path.substring(0, jarSeparatorIndex);
            buffer.append(path);
        }
        return buffer.toString ();
    }

}
