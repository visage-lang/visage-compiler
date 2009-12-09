/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;

/**
 *
 * @author ksrini
 */
public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    private static String buildId = null;
    private static final String JAVAFX_HOME = System.getProperty("javafx.home",
            System.getenv("JAVAFX_HOME"));
    private static final String HUDSON_URL = System.getenv("HUDSON_URL");
    private static final String HUDSON_JOB = System.getenv("JOB_NAME");
    private static final String HUDSON_BLD = System.getenv("BUILD_NUMBER");
    
    static String getChangesUrl() {
        return HUDSON_URL + "job/" + HUDSON_JOB + "/changes";
    }
    
    static String getJobUrl() {
       return HUDSON_URL + "job/" + HUDSON_JOB + "/" + HUDSON_BLD;      
    }
    
    static String getArtifactsUrl() {
        return getJobUrl() + "/artifact/performance";
    }
    
    static void toPlotFile(File outputFile, String yvalue) {
        toPlotFile(outputFile, yvalue, null);
    }
    
    static void toPlotFile(File outFile, String yvalue, String uvalue) {
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        PrintStream ps = null;
        try {
            fos = new FileOutputStream(outFile);
            ps = new PrintStream(fos);
            ps.println("YVALUE=" + yvalue);
            if (uvalue == null) {
                ps.println("URL=" + Utils.getChangesUrl());
            } else {
                ps.println("URL=" + uvalue);
            }
        } catch (IOException ioe) {
            logger.severe(ioe.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignore) {
            }
        }
    }
    
    public static String getBuildId() {
        if (buildId != null) return buildId;
        FileReader rdr = null;
        BufferedReader br = null;
        try {
            rdr = new FileReader(new File(JAVAFX_HOME, "timestamp"));
            br = new BufferedReader(rdr);
            String line = br.readLine();
            while (line != null) {
                if (line.startsWith("Build-Number")) {
                    String[] flds = line.split(":");
                    if (flds[1] != null) {
                        buildId = flds[1].trim();
                    }
                }
                line = br.readLine();
            }     
        } catch (IOException ioe) {
            logger.severe(ioe.getMessage());
            throw new RuntimeException(ioe);
        } finally {
            try {
                if (br != null)  br.close();
                if (rdr != null) rdr.close();
            } catch (Exception ignore) {}
        }  
        return buildId;
    }

    static String getMainClassFromJar(String jarfilename) {
        JarFile jf = null;
        String mainclassname = null;
        try {
            jf = new JarFile(jarfilename);
            Manifest mf = jf.getManifest();
            if (mf != null) {
                Attributes attr = mf.getMainAttributes();
                if (attr != null) {
                    mainclassname = attr.getValue("Main-Class");
                }
            }
        } catch (IOException ioe) {
            logger.severe("Processing: " + jarfilename + ":" + ioe.getMessage());
        } finally {
            if (jf != null) {
                try {
                    jf.close();
                } catch (IOException ignore) { /* swallow the exception */ }
            }
        }
        return mainclassname;
    }
}
