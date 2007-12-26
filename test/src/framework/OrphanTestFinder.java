package framework;

import java.util.List;

import junit.framework.TestCase;

/**
 * Test case used by FXCompilerTest to complain if test files are marked as neither test nor subtest
 *
 * @author Brian Goetz
 */
public class OrphanTestFinder extends TestCase {
    private final List<String> orphanFiles;

    public OrphanTestFinder(List<String> orphanFiles) {
        super("test");
        this.orphanFiles = orphanFiles;
    }

    public void test() {
        if (orphanFiles == null || orphanFiles.size() == 0)
            return;

        StringBuffer sb = new StringBuffer();
        String NL = System.getProperty("line.separator");
        sb.append("Test files found with neither @test nor @subtest: ");
        sb.append(NL);
        for (String s : orphanFiles) {
            sb.append("  ");
            sb.append(s);
            sb.append(NL);
        }
        fail(sb.toString());
    }
}
