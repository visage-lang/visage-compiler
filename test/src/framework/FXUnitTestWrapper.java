package framework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * FXUnitTestCase
 *
 * @author Brian Goetz
 */
public class FXUnitTestWrapper extends TestCase {
    private final TestCase object;
    private final File testFile;
    private final Method testMethod;

    public FXUnitTestWrapper(String name, File testFile, TestCase object, Method testMethod) {
        super(name);
        this.testFile = testFile;
        this.object = object;
        this.testMethod = testMethod;
    }

    protected void setUp() throws Exception {
        // @@@ TBD
    }

    protected void tearDown() throws Exception {
        // @@@ TBD
    }

    protected void runTest() throws Throwable {
        System.out.println("Test(fxunit): " + testFile);
        try {
            testMethod.invoke(object);
        }
        catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public static TestSuite makeSuite(File testFile, String name) throws Exception {
        TestSuite suite = new TestSuite(name);
        File buildDir = TestHelper.makeBuildDir(testFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        List<String> files = new ArrayList<String>();
        files.add(testFile.getPath());
        int errors = TestHelper.doCompile(buildDir.getPath(), TestHelper.getClassPath(buildDir), files, out, err);
        if (errors == 0) {
            ClassLoader cl = new URLClassLoader(new URL[] { buildDir.toURL() }, FXUnitTestWrapper.class.getClassLoader());
            String className = testFile.getName();
            className = className.substring(0, className.length() - ".fx".length());
            Class<? extends TestCase> clazz = (Class<? extends TestCase>) cl.loadClass(className);
            List<TestCase> tests = new ArrayList<TestCase>();
            Constructor<? extends TestCase> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            TestCase instance = ctor.newInstance();
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                if (m.getName().startsWith("test") && !Modifier.isStatic(m.getModifiers())) {
                    m.setAccessible(true);
                    suite.addTest(new FXUnitTestWrapper(m.getName(), testFile, instance, m));
                }
            }
        }
        else {
            // capture the errors in a test case that will fail
            suite.addTest(new CompilationErrorWrapper(testFile, err));
        }
        return suite;
    }
}
