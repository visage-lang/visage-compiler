package com.sun.tools.javafx.ant;

import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * JavaFxAntTask
 *
 * @author Brian Goetz
 */
public class JavaFxAntTask extends Javac {

    public static final String FX_ENTRY_POINT = "com.sun.tools.javafx.Main";

    public JavaFxAntTask() {
        super();
        super.setCompiler(JavaFxCompilerAdapter.class.getName());
        super.setIncludeantruntime(true);
    }

    protected void scanDir(File srcDir, File destDir, String[] files) {
        GlobPatternMapper m = new GlobPatternMapper();
        m.setFrom("*.fx");
        m.setTo("*.java");
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


    public static class JavaFxCompilerAdapter extends DefaultCompilerAdapter {

        public boolean execute() throws BuildException {
            Commandline cmd = setupModernJavacCommand();
            CommandlineJava commandLine = new CommandlineJava();
            commandLine.setClassname(FX_ENTRY_POINT);
            Path p = commandLine.createClasspath(attributes.getProject());
            p.add(attributes.getClasspath());
            for (String arg : cmd.getArguments())
                commandLine.createArgument().setValue(arg);
            Execute exe = new Execute();
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            String[] strings = commandLine.getCommandline();
            exe.setCommandline(strings);

            try {
                return exe.execute() == 0;
            } catch (IOException e) {
                throw new BuildException(e, attributes.getLocation());
            }
        }

    }
}
