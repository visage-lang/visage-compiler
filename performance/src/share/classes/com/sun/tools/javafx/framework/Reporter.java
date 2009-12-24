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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author ksrini
 */
public class Reporter {

    final Map<String, ResultData> result12;
    final Map<String, ResultData> result13;
    final Map<String, ResultData> goals;
    final Map<String, ResultData> result;
    final Map<String, ResultData> last;

    public Reporter() {
        result12 = Utils.readResults12Csv();
        result13 = Utils.readResults13Csv();
        goals    = Utils.readGoalsCsv();
        result   = Utils.readCurrentResultsCsv();
        last     = Utils.readLastBuildCsv();
    }

    void startTable(PrintStream ps, String header) {
        //ps.println("<CENTER>");
        ps.println("  <TABLE BORDER=1 CELLPADDING=4 CELLSPACING=0>");
        ps.println("  <TR VALIGN=\"TOP\">");
        ps.println("     <TH COLSPAN=8><P><B>" + header + "</P></B></TH>");
        ps.println("   </TR>");
        ps.println("   <TR>");
        ps.println("     <TH><P><B>Benchmark</B></P></TH>");
        ps.println("     <TD><P><B>JavaFX 1.2.1</B></P></TD>");
        ps.println("     <TD><P><B>JavaFX 1.3</B></P></TD>");
        ps.println("     <TD><P><B>Goal</B></P></TD>");
        ps.println("     <TD><P><B>Last</B></P></TD>");
        ps.println("     <TD><P><B>Current</B></P></TD>");
        ps.println("     <TD><P><B>%change this build to goal</B></P></TD>");
        ps.println("     <TD><P><B>%change this build to last build</B></P></TD>");
        ps.println("  </TR>");
    }

    void endTable(PrintStream ps) {
        ps.println("</TABLE>");
        ps.println("<P><P>");
    }

    void printValues(PrintStream ps, String... args) {
        ps.println("<TR>");
        for (String x : args) {
            ps.println("<TD><P>" + x + "</TD></P>");
        }
        ps.println("</TR>");     
    }

    void openHtml(PrintStream ps) {
        ps.println("<HTML>");
        ps.println("<BODY LANG=\"en-US\" DIR=\"LTR\">");
    }

    void closeHtml(PrintStream ps) {
        ps.println("</BODY></HTML>");
    }

    private String getPerformanceValue(Map<String, ResultData> in, String key) {
        return getValue(true, in, key);
    }
    private String getHeapsizeValue(Map<String, ResultData> in, String key) {
        return getValue(false, in, key);
    }
    private String getValue(boolean performance, Map<String,
            ResultData> in, String key) {
        if (in == null) {
            return "0.0";
        }
        ResultData rd = in.get(key);
        if (rd == null || ((performance) ? rd.getPerformance() : rd.getHeapsize()) == null) {
            return "0.0";
        } else {
            return (performance) ? rd.getPerformance() : rd.getHeapsize();
        }
    }
    private String getNameAsHref(String name) {
        return "<a href=" + Utils.getBenchmarkSourceLink(name) +
                ">" + name + "</a>";
    }
    private String percentChange(boolean performance,
            Map<String, ResultData> in1, Map<String, ResultData> in2, String key) {
        float f1 = 0;
        float f2 = 0;
        ResultData rd = null;
        if (in1 != null) {
            rd = in1.get(key);
            f1 = (float) ((rd == null)
                    ? 0.0
                    : Float.parseFloat((performance ? rd.getPerformance() : rd.getHeapsize())));
        }
        if (in2 != null) {
            rd = in2.get(key);
            f2 = (float) ((rd == null)
                    ? 0.0
                    : Float.parseFloat((performance ? rd.getPerformance() : rd.getHeapsize())));
        }
        float change = (f1 - f2) / f1 * 100;
        return Float.toString(change);
    }

    private void printTimingReport() {
        FileOutputStream fos = null;
        PrintStream ps = null;
        try {
            fos = new FileOutputStream("timing-result.html");
            ps = new PrintStream(fos);
            openHtml(ps);
            startTable(ps, "Timing in milliseconds");
            Set<String> kset = new TreeSet<String>(result.keySet());
            for (String x : kset) {
                printValues(ps, x,
                        getPerformanceValue(result12, x),
                        getPerformanceValue(result13, x),
                        getPerformanceValue(goals, x),
                        getPerformanceValue(last, x),
                        getPerformanceValue(result, x),
                        percentChange(true, goals, result, x),
                        percentChange(true, last, result, x));
            }
            endTable(ps);
            closeHtml(ps);
        } catch (IOException ioe) {
            Utils.logger.severe(ioe.toString());
        } finally {
            Utils.close(ps);
            Utils.close(fos);
        }
    }
  
    private void printHeapReport() {
        FileOutputStream fos = null;
        PrintStream ps = null;
        try {
            fos = new FileOutputStream("footprint-result.html");
            ps = new PrintStream(fos);
            openHtml(ps);
            startTable(ps, "Footprint in MBytes");
            Set<String> kset = new TreeSet<String>(result.keySet());
            for (String x : kset) {
                printValues(ps, getNameAsHref(x),
                        getHeapsizeValue(result12, x),
                        getHeapsizeValue(result13, x),
                        getHeapsizeValue(goals, x),
                        getHeapsizeValue(last, x),
                        getHeapsizeValue(result, x),
                        percentChange(false, goals, result, x),
                        percentChange(false, last, result, x));
            }
            endTable(ps);
            closeHtml(ps);
        } catch (IOException ioe) {
            Utils.logger.severe(ioe.toString());
        } finally {
            Utils.close(ps);
            Utils.close(fos);
        }
    }

    void printToHtml() {
        printTimingReport();
        printHeapReport();
    }

    public static void main(String... args) {
        Reporter r = new Reporter();
        r.printToHtml();
    }
}
