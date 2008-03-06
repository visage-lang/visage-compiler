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
    private final Method setUp;
    private final Method tearDown;

    public FXUnitTestWrapper(String name, File testFile, TestCase object, Method testMethod, Method setUp, Method tearDown) {
        super(name);
        this.testFile = testFile;
        this.object = object;
        this.testMethod = testMethod;
        this.setUp = setUp;
        this.tearDown = tearDown;
    }

    @Override
    protected void setUp() throws Exception {
        if (setUp != null && testMethod != null) {
            System.out.println("SetUp(fxunit): " + testFile + " - " + testMethod.getName());
            setUp.invoke(object);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (tearDown != null && testMethod != null) {
            System.out.println("TearDown(fxunit): " + testFile + " - " + testMethod.getName());
            tearDown.invoke(object);
        }
    }

    @Override
    protected void runTest() throws Throwable {
        if (testMethod != null) {
            System.out.println("Test(fxunit): " + testFile + " - " + testMethod.getName());
            try {
                testMethod.invoke(object);
            }
            catch (InvocationTargetException e) {
                throw e.getCause();
            }
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
            Method setUp = null;
            Method tearDown = null;
            for (Method m : methods) {
                if (m.getName().equals("setUp") && !Modifier.isStatic(m.getModifiers()))
                    setUp = m;
                if (m.getName().equals("tearDown") && !Modifier.isStatic(m.getModifiers()))
                    tearDown = m;
            }
            for (Method m : methods) {
                if (m.getName().startsWith("test") && !Modifier.isStatic(m.getModifiers())) {
                    m.setAccessible(true);
                    suite.addTest(new FXUnitTestWrapper(m.getName(), testFile, instance, m, setUp, tearDown));
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
