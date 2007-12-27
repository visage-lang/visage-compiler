package framework;

import java.io.ByteArrayOutputStream;
import java.io.File;

import junit.framework.TestCase;
import org.apache.tools.ant.filters.StringInputStream;

/**
 * CompilationErrorWrapper
 *
 * @author Brian Goetz
 */
public class CompilationErrorWrapper extends TestCase {
    private final File testFile;
    private final ByteArrayOutputStream err;

    public CompilationErrorWrapper(File testFile, ByteArrayOutputStream err) {
        super(testFile.toString());
        this.testFile = testFile;
        this.err = err;
    }

    protected void runTest() throws Throwable {
        TestHelper.dumpFile(new StringInputStream(new String(err.toByteArray())), "Compiler Output", testFile.toString());
        fail("Compilation errors in " + testFile);
    }
}
