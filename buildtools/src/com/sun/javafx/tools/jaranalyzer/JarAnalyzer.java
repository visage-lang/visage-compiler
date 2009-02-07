package com.sun.javafx.tools.jaranalyzer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * This tool is used to compute from a jar file the size in kbytes used by each
 * package found, as well as the total size.
 * The result files have the right format for being read by the Plot plugin of
 * Hudson.
 * Typical use is to call that tool from an Ant target.
 * Parameters are:
 * <ul>
 * <li>the path to the jar file
 * <li>the path to a place top write result files
 * <li>a URL value that points to one of the result file, the HTML array that
 * lists size per package and total size.
 * </ul>
 *
 * There's alse a legacy way of call it manually in order to compare two
 * successive jar files.
 * 
 * @author ksrini
 */
public class JarAnalyzer {

    static String getPackageName(String name) {
        String out = name.substring(0, name.lastIndexOf("/"));
        return out.replace("/", ".");
    }

    static Hashtable<String, Long> readJarFile(URL url) {
        FileInputStream fis = null;
        try {
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();
            return readJarFile(conn.getInputStream());
        } catch (IOException ioe) {
            Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE,
                    null, ioe);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
        return null;
    }

    static Hashtable<String, Long> readJarFile(File inFile) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inFile);
            return readJarFile(fis);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE,
                    null, ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
        return null;
    }

    static Hashtable<String, Long> readJarFile(InputStream in) {
        Hashtable<String, Long> tbl = new Hashtable<String, Long>();
        try {
            ZipInputStream zis = new ZipInputStream(in);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String zname = ze.getName();
                if (zname.endsWith(".class")) {
                    String pkgname = getPackageName(zname);
                    if (!tbl.containsKey(pkgname)) {
                        tbl.put(pkgname, new Long(ze.getSize()));
                    } else {
                        Long value = tbl.get(pkgname);
                        value += ze.getSize();
                        tbl.put(pkgname, value);
                    }
                }
                ze = zis.getNextEntry();
            }
        } catch (ZipException ex) {
            Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return tbl;
    }

    static void dumpToFile(String name1, String name2, OutputStream ostream,
            Hashtable<String, Long> tbl1, Hashtable<String, Long> tbl2) {

        List<String> keyList = new ArrayList<String>();
        for (String x : Collections.list(tbl1.keys())) {
            keyList.add(x);
        }
        Collections.sort(keyList);
        PrintWriter pw = null;
        pw = new PrintWriter(new OutputStreamWriter(ostream));
        pw.printf("\t%s\t%s\n", name1, name2);
        long sum1 = 0L;
        long sum2 = 0L;
        for (String x : keyList) {
            pw.printf("%s\t%s\t%s\n", x, tbl1.get(x) / 1024, tbl2.get(x) / 1024);
            sum1 += tbl1.get(x);
            sum2 += tbl2.get(x);
        }
        pw.printf("Total\t%s\t%s\n", sum1 / 1024, sum2 / 1024);
        pw.flush();
    }

    static void dumpToFile(String name, OutputStream ostream,
            Hashtable<String, Long> tbl) {

        List<String> keyList = new ArrayList<String>();
        for (String x : Collections.list(tbl.keys())) {
            keyList.add(x);
        }
        Collections.sort(keyList);
        PrintWriter pw = null;
        pw = new PrintWriter(new OutputStreamWriter(ostream));
        pw.println(name);
        long sum = 0L;
        for (String x : keyList) {
            pw.printf("%s\t%s\n", x, tbl.get(x) / 1024);
            sum += tbl.get(x);
        }
        pw.printf("Total\t%s\n", sum / 1024);
        pw.flush();
    }

    static void dumpToFileAllPackages(
            Hashtable<String, Long> tbl, String outputRootDir, String urlDir)
            throws IOException {

        // report.properties file
        // This is the input file for the plotter plugin
        File file1 = new File(outputRootDir + "/staticsizes");
        file1.createNewFile();
        OutputStream ostream1 = new FileOutputStream(file1);

        // report.properties.html file
        // This is the URL to access detailed packages informations
        File file2 = new File(outputRootDir + "/staticsizes.html");
        file2.createNewFile();
        OutputStream ostream2 = new FileOutputStream(file2);

        List<String> keyList = new ArrayList<String>();
        for (String x : Collections.list(tbl.keys())) {
            keyList.add(x);
        }
        Collections.sort(keyList);
        PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(ostream1));
        long sum = 0L;
        for (String x : keyList) {
            sum += tbl.get(x);
        }
        pw1.printf("YVALUE=%s\n", sum / 1024);
        pw1.printf("URL=%s\n", urlDir + "/staticsizes.html");
        pw1.flush();
        pw1.close();

        // Build the html table detailed packages informations
        PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(ostream2));
        pw2.printf("<HTML>");
        pw2.printf("<BODY LANG=\"en-US\" DIR=\"LTR\">");
        pw2.printf("<CENTER>");
        pw2.printf("\t<TABLE WIDTH=420 BORDER=1 CELLPADDING=4 CELLSPACING=0>");
        pw2.printf("\t<TR VALIGN=TOP>");
        pw2.printf("\t<TH WIDTH=308>");
        pw2.printf("\t<P>Package</P>");
        pw2.printf("\t</TH>");
        pw2.printf("\t<TD WIDTH=95>");
        pw2.printf("\t<P><B>Size in kbytes</B></P>");
        pw2.printf("\t</TD>");
        pw2.printf("\t</TR>");
        for (String x : keyList) {
            pw2.printf("\t<TR VALIGN=TOP>");
            pw2.printf("\t<TD WIDTH=308>");
            pw2.printf("\t<P>" + x + "</P>");
            pw2.printf("\t</TD>");
            pw2.printf("\t<TD WIDTH=95>");
            pw2.printf("\t<P>" + tbl.get(x) / 1024 + "</P>");
            pw2.printf("\t</TD>");
            pw2.printf("\t</TR>");
        }
        pw2.printf("\t<TR VALIGN=TOP>");
        pw2.printf("\t<TD WIDTH=308>");
        pw2.printf("\t<P><B>Total</B></P>");
        pw2.printf("\t</TD>");
        pw2.printf("\t<TD WIDTH=95>");
        pw2.printf("\t<P>" + sum / 1024 + "</P>");
        pw2.printf("\t</TD>");
        pw2.printf("\t</TR>");

        pw2.printf("\t</TABLE>");
        pw2.printf("\t</CENTER>");
        pw2.printf("\t</BODY>");
        pw2.printf("\t</HTML>");

        pw2.flush();
        pw2.close();
    }
    private static final String BLDTAG = "BLDTAG";
    private static final String OPENJFX =
            "http://openjfx.java.sun.com/hudson/job/openjfx-compiler/" +
            BLDTAG + "/artifact/openjfx-compiler/dist/lib/shared/javafxrt.jar";

    private static JarStat getTotals(File file) {
        ZipFile zf = null;
        JarStat js = null;
        try {
            js = new JarStat(file);
            zf = new ZipFile(file);
            for (ZipEntry ze : Collections.list(zf.entries())) {
                js.addSizes(zf, ze);
            }
        } catch (ZipException ex) {
            Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException ex) {
                }
            }
        }
        return js;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null) {
            System.err.println("Usage: input_jar_file output_root_dir url_dir");
            System.err.println("Usage: --compare bld# bld#");
            System.exit(1);
        } else if (args[0].endsWith("compare")) {
            try {
                URL url1 = new URL(OPENJFX.replace(BLDTAG, args[1]));
                URL url2 = new URL(OPENJFX.replace(BLDTAG, args[2]));
                Hashtable<String, Long> tbl1 = readJarFile(url1);
                Hashtable<String, Long> tbl2 = readJarFile(url2);
                dumpToFile(args[1], args[2], System.out, tbl1, tbl2);
            } catch (MalformedURLException ex) {
                Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        } else {
            try {
                String inputJarFile = args[0];
                String outputRootDir = args[1];
                String urlDir = args[2];
                Hashtable<String, Long> tbl = readJarFile(new File(inputJarFile));

                // Plot information for all packages
                dumpToFileAllPackages(tbl, outputRootDir, urlDir);
                JarStat js = getTotals(new File(inputJarFile));
                File outputFile = new File(outputRootDir + "/staticsizes." +
                        "jar-size-compressed");
                js.printSize(outputFile, true);
                outputFile = new File(outputRootDir + "/staticsizes." +
                        "jar-size-uncompressed");
                js.printSize(outputFile, false);
                // Plot information for single package
                for (String key : tbl.keySet()) {
                    outputFile = new File(outputRootDir + "/staticsizes." + key);
                    outputFile.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(outputFile);
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(ostream));
                    pw.printf("YVALUE=%s\n", tbl.get(key) / 1024);
                    pw.flush();
                    pw.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(JarAnalyzer.class.getName()).log(Level.SEVERE,
                        null, ex);
                System.exit(1);
            }
        }
        System.exit(0);
    }
}

class JarStat {

    private File jarFile;
    private long size;  // uncompressed sizes
    private long csize; // compressed sizes

    JarStat(File jarFile) {
        this.jarFile = jarFile;
        size = 0L;
        csize = 0L;
    }

    void addSizes(ZipFile zf, ZipEntry ze) {
        long sz = ze.getSize();
        if (sz == 0) { // don't bother with 0 file size
            return;
        }
        this.size += sz;

        GZIPOutputStream gzos = null;
        InputStream zis = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            gzos = new GZIPOutputStream(baos);
            zis = zf.getInputStream(ze);
            byte buf[] = new byte[8192];
            int n = zis.read(buf);
            while (n > 0) {
                gzos.write(buf);
                n = zis.read(buf);
            }
            gzos.finish();
            gzos.flush();
            this.csize += baos.size();
        //System.out.println("entry: " + ze.getName() + " size:" + this.size + " csize:" + this.csize);
        } catch (IOException ex) {
            Logger.getLogger(JarStat.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                zis.close();
                gzos.close();
            } catch (IOException ex) {
                Logger.getLogger(JarStat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void printSize(File outFile, boolean reportCompressed) {
        try {
            outFile.createNewFile();
            OutputStream ostream = new FileOutputStream(outFile);
            PrintWriter pw = new PrintWriter(ostream);
            pw.println("YVALUE=" + ((reportCompressed) ? this.csize : this.size) / 1024);
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(JarStat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
