package com.sun.javafx.ideaplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.sun.javafx.api.JavafxCompiler;
import com.sun.javafx.api.JavafxcTask;
import com.sun.tools.javafx.api.JavafxcTool;
import org.jetbrains.annotations.NotNull;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FxCompiler
 *
 * @author Brian Goetz
 */
public class FxCompiler implements TranslatingCompiler {
    private static final String PATH_SEPARATOR = File.pathSeparator;
    private static final Logger LOG = Logger.getInstance("com.sun.javafx.ideaplugin");
    private static final EnumMap<Diagnostic.Kind, CompilerMessageCategory> diagnosticKindMap
            = new EnumMap<Diagnostic.Kind, CompilerMessageCategory>(Diagnostic.Kind.class);
    static {
        diagnosticKindMap.put(Diagnostic.Kind.ERROR, CompilerMessageCategory.ERROR);
        diagnosticKindMap.put(Diagnostic.Kind.MANDATORY_WARNING, CompilerMessageCategory.WARNING);
        diagnosticKindMap.put(Diagnostic.Kind.WARNING, CompilerMessageCategory.WARNING);
        diagnosticKindMap.put(Diagnostic.Kind.NOTE, CompilerMessageCategory.INFORMATION);
        diagnosticKindMap.put(Diagnostic.Kind.OTHER, CompilerMessageCategory.INFORMATION);
    }

    private final Project project;
    private final JavafxCompiler compiler = compilerLocator();
    private final JavafxcTool compilerTool = JavafxcTool.create();

    public FxCompiler(Project project) {
        this.project = project;
    }

    public boolean isCompilableFile(VirtualFile virtualFile, CompileContext compileContext) {
        return FxPlugin.FX_FILE_TYPE.equals(virtualFile.getFileType());
    }

    public ExitStatus compile(final CompileContext compileContext, VirtualFile[] virtualFiles) {
        final AtomicBoolean failed = new AtomicBoolean(false);

        Map<Module, Set<VirtualFile>> map = buildModuleToFilesMap(compileContext, virtualFiles);
        final Set<OutputItem> compiledItems = new HashSet<OutputItem>();
        final Set<VirtualFile> allCompiling = new HashSet<VirtualFile>();
        final Map<JavaFileObject, VirtualFile> fileMap = new HashMap<JavaFileObject, VirtualFile>();
        for (Map.Entry<Module, Set<VirtualFile>> entry : map.entrySet()) {
            if (failed.get())
                continue;
            Module module = entry.getKey();
            Set<VirtualFile> files = entry.getValue();
            allCompiling.addAll(files);
            ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
            String moduleOutputUrl = rootManager.getCompilerOutputPathUrl();

            OrderEntry[] entries = rootManager.getOrderEntries();
            List<VirtualFile> cpVFiles = new ArrayList<VirtualFile>();
            for (OrderEntry orderEntry : entries)
                cpVFiles.addAll(Arrays.asList(orderEntry.getFiles(OrderRootType.COMPILATION_CLASSES)));
            VirtualFile[] cpFilesArray = cpVFiles.toArray(new VirtualFile[cpVFiles.size()]);

            List<String> args = new ArrayList<String>();
            List<JavaFileObject> filePaths = new ArrayList<JavaFileObject>();
            args.add("-target");
            args.add("1.5");
            args.add("-d");
            args.add(VirtualFileManager.extractPath(moduleOutputUrl));

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < cpFilesArray.length; i++) {
                VirtualFile file = cpFilesArray[i];
                String path = file.getPath();
                int jarSeparatorIndex = path.indexOf(JarFileSystem.JAR_SEPARATOR);
                if (jarSeparatorIndex > 0)
                    path = path.substring(0, jarSeparatorIndex);
                sb.append(path);
                if (i < cpFilesArray.length - 1) {
                    sb.append(PATH_SEPARATOR);
                }
            }
            args.add("-cp");
            args.add(sb.toString());

            for (VirtualFile f : files) {
                JavaFileObject inputFO = compilerTool.getStandardFileManager(null, null, Charset.defaultCharset()).getFileForInput(f.getPath());
                fileMap.put(inputFO, f);
                filePaths.add(inputFO);
            }

            LOG.debug(Arrays.asList(args).toString());
            JavafxcTask compileTask = compiler.getTask(null, null, new DiagnosticListener<JavaFileObject>() {
                public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                    compileContext.addMessage(diagnosticKindMap.get(diagnostic.getKind()),
                            diagnostic.getMessage(Locale.getDefault()),
                            fileMap.get(diagnostic.getSource()).getUrl(),
                            (int) diagnostic.getLineNumber(), (int) diagnostic.getColumnNumber());
                }
            }, args, filePaths);
            if (!compileTask.call())
                failed.set(true);
        }

        return new ExitStatus() {
            public OutputItem[] getSuccessfullyCompiled() {
                return (!failed.get()) ? compiledItems.toArray(new OutputItem[compiledItems.size()]) : new OutputItem[0];
            }

            public VirtualFile[] getFilesToRecompile() {
                return (!failed.get()) ? new VirtualFile[0] : allCompiling.toArray(new VirtualFile[allCompiling.size()]);
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


    protected static JavafxCompiler compilerLocator() {
        Iterator<?> iterator;
        Class<?> loaderClass;
        String loadMethodName;
        boolean usingServiceLoader;

        try {
            loaderClass = Class.forName("java.util.ServiceLoader");
            loadMethodName = "load";
            usingServiceLoader = true;
        } catch (ClassNotFoundException cnfe) {
            try {
                loaderClass = Class.forName("sun.misc.Service");
                loadMethodName = "providers";
                usingServiceLoader = false;
            } catch (ClassNotFoundException cnfe2) {
                throw new AssertionError("Failed discovering ServiceLoader");
            }
        }

        try {
            // java.util.ServiceLoader.load or sun.misc.Service.providers
            Method loadMethod = loaderClass.getMethod(loadMethodName,
                    Class.class,
                    ClassLoader.class);
            ClassLoader cl = FxCompiler.class.getClassLoader();
            Object result = loadMethod.invoke(null, JavafxCompiler.class, cl);

            // For java.util.ServiceLoader, we have to call another
            // method to get the iterator.
            if (usingServiceLoader) {
                Method m = loaderClass.getMethod("iterator");
                result = m.invoke(result); // serviceLoader.iterator();
            }

            iterator = (Iterator<?>) result;
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IllegalStateException("Failed accessing ServiceLoader: " + t);
        }

        if (!iterator.hasNext())
            throw new IllegalStateException("No JavaFX Script compiler found");

        return (JavafxCompiler) iterator.next();
    }


}
