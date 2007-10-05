package com.sun.tools.javafx.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.compilers.CompilerAdapter;
import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * JavaFxAntTask
 *
 * @author Brian Goetz
 */
public class JavaFxAntTask extends Javac {
    private Path compilerClassPath;

    private static final String FAIL_MSG
            = "JavaFX compile failed; see the compiler error output for details.";

    public static final String FX_ENTRY_POINT = "com.sun.tools.javafx.Main";

    public JavaFxAntTask() {
        super();
        super.setCompiler(JavaFxCompilerAdapter.class.getName());
        super.setIncludeantruntime(true);
    }

    @Override
    protected void scanDir(File srcDir, File destDir, String[] files) {
        GlobPatternMapper m = new GlobPatternMapper();
        m.setFrom("*.fx");
        m.setTo("*.class");
        SourceFileScanner sfs = new SourceFileScanner(this);
        File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);

        if (newFiles.length > 0) {
            File[] newCompileList
                    = new File[compileList.length + newFiles.length];
            System.arraycopy(compileList, 0, newCompileList, 0, compileList.length);
            System.arraycopy(newFiles, 0, newCompileList, compileList.length, newFiles.length);
            compileList = newCompileList;
        }
    }

    /**
     * Workaround for classloader bug in CompilerAdapterFactory
     */
    @Override
    protected void compile() {
        String compilerImpl = getCompiler();

        if (compileList.length > 0) {
            log("Compiling " + compileList.length + " source file"
                    + (compileList.length == 1 ? "" : "s")
                    + (getDestdir() != null ? " to " + getDestdir() : ""));

            if (listFiles) {
                for (int i = 0; i < compileList.length; i++) {
                    String filename = compileList[i].getAbsolutePath();
                    log(filename);
                }
            }

            CompilerAdapter adapter = new JavaFxCompilerAdapter();

            // now we need to populate the compiler adapter
            adapter.setJavac(this);

            // finally, lets execute the compiler!!
            if (!adapter.execute()) {
                if (failOnError) {
                    throw new BuildException(FAIL_MSG, getLocation());
                } else {
                    log(FAIL_MSG, Project.MSG_ERR);
                }
            }
        }
    }

    public void setCompilerClassPath(Path p) {
        compilerClassPath = p;
    }

    public void setCompilerClassPathRef(Reference r) {
        setCompilerClassPath((Path) r.getReferencedObject());
    }

    private URL[] pathAsURLs() throws java.net.MalformedURLException {
        Path p = compilerClassPath != null ? compilerClassPath : new Path(getProject());
        java.util.ArrayList<URL> urls = new java.util.ArrayList<URL>();
        for (String s : p.list()) {
            urls.add(new File(s).toURI().toURL());
        }
        return urls.toArray(new URL[0]);
    }

    public static class JavaFxCompilerAdapter extends DefaultCompilerAdapter {

        public boolean execute() throws BuildException {
            Commandline cmd = setupModernJavacCommand();
            try {
                URL[] jars = ((JavaFxAntTask) getJavac()).pathAsURLs();
                URLClassLoader loader = new URLClassLoader(jars) {
                    @Override
                    protected Class loadClass(String n, boolean r) throws ClassNotFoundException {
                        if (n.indexOf("sun.tools") >= 0 || n.startsWith("com.sun.source")) {
                            Class c = findLoadedClass(n);
                            if (c != null) {
                                getJavac().log("found loaded class: " + n);
                                return c;
                            }
                            c = findClass(n);
                            if (c == null) {
                                getJavac().log("didn't find class:  " + n);
                                return super.loadClass(n, r);
                            }
                            if (r)
                                resolveClass(c);
                            return c;
                        }
                        return super.loadClass(n, r);
                    }
                };
                Class c = Class.forName(FX_ENTRY_POINT, true, loader);
                Object compiler = c.newInstance();
                Method compile = c.getMethod("compile", String[].class);
                Object[] args = cmd.getArguments();
                int result = (Integer) compile.invoke(compiler, new Object[]{args});
                return (result == 0);  // zero errors
            } catch (Exception ex) {
                getJavac().log(ex.toString());
                if (ex instanceof ClassNotFoundException ||
                        ex instanceof java.lang.reflect.InvocationTargetException) {
                    throw new BuildException(ex);
                }
                if (ex instanceof BuildException) {
                    throw (BuildException) ex;
                } else {
                    throw new BuildException("Error starting JavaFX compiler",
                            ex, location);
                }
            }
        }
    }
}
