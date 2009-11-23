/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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
package com.sun.tools.javafx.framework;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/*
 * @author ksrini
 */
public class Runner {
    static final String RUNNER_NAME = "Runner";
    
    // Command line arguments
    static String mainClass = null;
    static List<String> appArgs = new ArrayList<String>();
    static final int ITERATIONS = Integer.getInteger(RUNNER_NAME + ".iterations", 10);
    static int interval = 5*1000;
    static int duration = 30*1000;
    
    static final String msg[] = { " none "};
    static boolean runExecution = true;
    static boolean runFootPrint = true;

    public static void main(String... args) {
        init(args);
        doRun();
        System.exit(0);
    }
    
    static void init(String[] args) {
        String rtype = System.getProperty(RUNNER_NAME + ".runtype");

        if (rtype != null) {
            runExecution = false;
            runFootPrint = false;
            String values[] = rtype.split("\\|");
            for (String x : values) {
                if (x.equals("EXECUTION")) {
                    runExecution = true;
                }
                if (x.equals("FOOTPRINT")) {
                    runFootPrint = true;
                }
            }
        }


        boolean mustExit = false;
        for (String x : args) {
            appArgs.add(x);
        }
        if (mustExit) {
            System.exit(1);
        }
    }    
    
    static void usage(boolean mustExit) {
        String out = "";
        for (String x: msg) {
            out = out.concat(x + "\n");
        }
        TestProcess.logger.severe(out);
        if (mustExit) {
            System.exit(1);
        }
    }
   
    static void doRun() {
        List<String> cmdsList = new ArrayList<String>();
        cmdsList.add(TestProcess.JAVAFX_EXE);
        cmdsList.add("-D" + TestProcess.JPSMARKER);
        String vmProps = System.getProperty(RUNNER_NAME + ".vmoptions");
        // ant could pass the property itself if undefined, ignore it.
        if (vmProps != null && !vmProps.contains(RUNNER_NAME + ".vmoptions")) {
            String vmOpts[] = vmProps.split(",\\s");
            for (String x : vmOpts) {
                cmdsList.add(x);
            }
        }
        cmdsList.add("-cp");
        cmdsList.add(TestProcess.APP_CLASSPATH);
        cmdsList.add(TestProcess.APP_NAME);
        cmdsList.addAll(appArgs);
        cmdsList.add("-time");
        cmdsList.add("-iter");
        cmdsList.add(Integer.toString(ITERATIONS));
        float timeToSleep = 0.0f;

        if (runExecution) {
            String value = analyzePerformance(cmdsList);
            timeToSleep = Math.round(Float.parseFloat(value)) * ITERATIONS;
        }
        if (runFootPrint) {
            duration += (timeToSleep == 0.0f) ? 2 * 60 * 1000 : timeToSleep;
            cmdsList.add("-pause");
            analyzeFootPrint(cmdsList);
        }
    }
    
    static String analyzePerformance(List<String> cmds) {
       List<String> output = TestProcess.doExec(cmds);
       String value = "0";
       for (String x : output) {
           if (x.matches("average time:.*ms.*")) {
               String f[] = x.split("\\s");
               value = f[2];
               System.out.println(TestProcess.APP_NAME +
                       " Execution time: " + value + " mSecs");
               Utils.toPlotFile(new File(TestProcess.APP_WORKDIR,
                       TestProcess.APP_NAME + "-perf.txt"), value);
               return value;
           }
       }
       return null;
    }

    static public void printClasses(Process testProc) {
        FileOutputStream jmpos = null;
        PrintStream jps = null;
        String[] appIds = TestProcess.getAppPids();
        if (appIds == null) {
            throw new RuntimeException("Error: no appids found for target");
        }
        try {
            jmpos = new FileOutputStream(
                    File.createTempFile(
                    "jmap-" + TestProcess.APP_NAME, ".txt",
                    new File(TestProcess.APP_WORKDIR)));
            jps = new PrintStream(jmpos);
            List<String> jmapOutput = TestProcess.doExec(
                    TestProcess.JMAP_EXE, "-histo:live", appIds[0]);
            for (String x : jmapOutput) {
                jps.println(x);
                if (x.startsWith("Total")) {
                    String[] flds = x.split("\\s");
                    float msize = Float.parseFloat(flds[flds.length - 1]);
                    String yvalue = Float.toString(msize / 1024 / 1024);
                    System.out.println(TestProcess.APP_NAME + " Footprint: " +
                            yvalue + " MBytes");
                    File outFile = new File(TestProcess.APP_WORKDIR,
                            TestProcess.APP_NAME + "-dfp.txt");
                    Utils.toPlotFile(outFile, yvalue);
                }
            }
        } catch (IOException ex) {
            TestProcess.logger.severe(ex.getMessage());
        } finally {
            try {
                if (jps != null) {
                    jps.close();
                }
                if (jmpos != null) {
                    jmpos.close();
                }
            } catch (IOException ex) {
                TestProcess.logger.severe(ex.getMessage());
            }
            TestProcess.killTestApplication();
            if (testProc != null) {
                testProc.destroy();
            }
        }
    }
    
    static void analyzeFootPrint(List<String> cmds) {
        if (TestProcess.debug) {
            System.out.println("----Test-Execution args----");
            for (String x : cmds) {
                System.out.println(x);
            }
        }
        ProcessBuilder pb = new ProcessBuilder(cmds);
        pb = pb.directory(new File(TestProcess.APP_CLASSPATH));
        try {
            pb.redirectErrorStream(true);
            final Process p = pb.start();
            Thread.sleep(duration);
            printClasses(p);
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }
}
