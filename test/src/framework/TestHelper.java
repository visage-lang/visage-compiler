package framework;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.javafx.api.JavafxCompiler;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

/**
 * FXWrapperTestCase
 *
 * @author Brian Goetz
 */
public abstract class TestHelper {
    private static final JavafxCompiler compiler = compilerLocator();

    public static final String TEST_ROOT = "test";
    public static final String BUILD_ROOT = "build/test";
    public static final String TEST_PREFIX = TEST_ROOT + File.separator;

    public TestHelper(String name, File testFile) {
    }

    protected static File makeBuildDir(File testFile) {
        if (!testFile.getPath().startsWith(TEST_PREFIX))
            throw new IllegalArgumentException("test file path not a relative pathname");
        File buildDir = new File(BUILD_ROOT + File.separator + testFile.getParent().substring(TEST_PREFIX.length()));
        if (!new File(BUILD_ROOT).exists())
            throw new IllegalArgumentException("no " + BUILD_ROOT + " directory in " + new File(".").getAbsolutePath());
        buildDir.mkdirs();
        return buildDir;
    }

    protected static int doCompile(String dir, String classpath, List<String> files, OutputStream out, OutputStream err) {
        List<String> args = new ArrayList<String>();
        args.add("-target");
        args.add("1.5");
        args.add("-d");
        args.add(dir);
        args.add("-cp");
        args.add(classpath);
        for (String f : files)
            args.add(f);
        return compiler.run(null, out, err, args.toArray(new String[args.size()]));
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
            ClassLoader cl = TestHelper.class.getClassLoader();
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

    protected static void dumpFile(InputStream file, String header, String testName) throws IOException {
        dumpFile(System.out, file, header, testName);
    }

    protected static void dumpFile(PrintStream output, InputStream file, String header, String testName) throws IOException {
        System.out.println("--" + header + " for " + testName + "--");
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                output.println(line);
            }
        }
        finally {
            reader.close();
        }
    }

    protected static String getClassPath(File buildDir) {
        Path path = new CommandlineJava().createClasspath(new Project());
        path.createPathElement().setPath(System.getProperty("java.class.path"));
        path.createPathElement().setPath(buildDir.getPath());
        return path.toString();
    }

    
}
