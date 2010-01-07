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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

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

    static final String TIMING_RESULT_FILE_NAME = "timing-result.html";
    static final String FOOTPRINT_RESULT_FILE_NAME = "footprint-result.html";

    public Reporter() {
        result12 = Utils.readResults12Csv();
        result13 = Utils.readResults13Csv();
        goals    = Utils.readGoalsCsv();
        result   = Utils.readCurrentResultsCsv();
        last     = Utils.readLastBuildCsv();
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
            return ResultData.DEFAULT_VALUE;
        }
        ResultData rd = in.get(key);
        if (rd == null || ((performance) ? rd.getPerformance() : rd.getHeapsize()) == null) {
            return ResultData.DEFAULT_VALUE;
        } else {
            return (performance) ? rd.getPerformance() : rd.getHeapsize();
        }
    }
    private String nameAsHref(String name) {
        return "<a href=" + Utils.getBenchmarkSourceLink(name) +
                ">" + name + "</a>";
    }

    private Float percentChange(boolean performance,
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
        return Float.parseFloat(String.format("%10.2f", change));
    }

    private String generateImage(String refName, String name, Number changeFactor) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(changeFactor, 0, 0);
        JFreeChart chart = ChartFactory.createBarChart("", "", "%change", dataset, PlotOrientation.HORIZONTAL, false, false, false);
        try {
            Color bgcolor = null;
            double value = changeFactor.doubleValue();
            if (value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY) {
                bgcolor = Color.YELLOW;
            } else if ( value > 5) {
                bgcolor = Color.GREEN;
            } else if ( value >= 0 && value <= 5) {
                bgcolor = Color.BLUE;
            } else {
                bgcolor = Color.RED;
            }
            chart.setBackgroundPaint(bgcolor);
            String fname = name + "-plot.png";
            File dirFile = new File("images");
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File ofile = new File(dirFile, fname);
            ChartUtilities.saveChartAsPNG(ofile, chart, 300, 100);
            StringBuilder sb = new StringBuilder();
            sb.append("<a href=\"" + refName + "\" target=\"_blank\">");
            sb.append("<img src=\"" + dirFile.getName() + "/" + fname +
                    "\" width=\"150\" height=\"50\" border=\"0\" alt=\"\"/>");
            sb.append("</a>");
            return sb.toString();
        } catch (IOException ioe) {
            Utils.logger.severe(ioe.getMessage());
        }
        return null;
    }

    String detailsTable(String header) {
        StringBuilder sb = new StringBuilder();
        //ps.println("<CENTER>");
        sb.append("  <TABLE BORDER=1 CELLPADDING=4 CELLSPACING=0>");
        sb.append("  <TR VALIGN=\"TOP\">");
        sb.append("     <TH COLSPAN=8><P><B>" + header + "</P></B></TH>");
        sb.append("   </TR>");
        sb.append("   <TR>");
        sb.append("     <TH><P><B>Benchmark</B></P></TH>");
        sb.append("     <TD><P><B>JavaFX 1.2.1</B></P></TD>");
        sb.append("     <TD><P><B>JavaFX 1.3</B></P></TD>");
        sb.append("     <TD><P><B>Goal</B></P></TD>");
        sb.append("     <TD><P><B>Last</B></P></TD>");
        sb.append("     <TD><P><B>Current</B></P></TD>");
        sb.append("     <TD><P><B>%change this build to goal</B></P></TD>");
        sb.append("     <TD><P><B>%change this build to last build</B></P></TD>");
        sb.append("  </TR>");
        return sb.toString();
    }

    String dashboardTable(String header) {
        StringBuilder sb = new StringBuilder();
         //ps.println("<CENTER>");
        sb.append("  <TABLE BORDER=1 CELLPADDING=4 CELLSPACING=0>");
        sb.append("  <TR VALIGN=\"TOP\">");
        sb.append("     <TH COLSPAN=3><P><B>" + header + "</P></B></TH>");
        sb.append("   </TR>");
        sb.append("   <TR>");
        sb.append("     <TH><P><B>Benchmark</B></P></TH>");
        sb.append("     <TD><P><B>Current build vs. Goal</B></P></TD>");
        sb.append("     <TD><P><B>Current build vs. Last</B></P></TD>");
        sb.append("  </TR>");
        return sb.toString();
    }

    private void printTimingReport() {
        HtmlWriter timingReport = null;
        HtmlWriter dashboardTimingReport = null;
        try {
            timingReport = new HtmlWriter(TIMING_RESULT_FILE_NAME, 
                    detailsTable("Timing in milliseconds"));
            dashboardTimingReport = new HtmlWriter("timing-dashboard.html",
                    dashboardTable("Timing"));
            Set<String> kset = new TreeSet<String>(result.keySet());
            for (String x : kset) {
                Float currentToGoal = percentChange(true, goals, result, x);
                Float currentToLast = percentChange(true, last, result, x);
                // print to details table
                timingReport.writeToHtml(nameAsHref(x),
                        getPerformanceValue(result12, x),
                        getPerformanceValue(result13, x),
                        getPerformanceValue(goals, x),
                        getPerformanceValue(last, x),
                        getPerformanceValue(result, x),
                        currentToGoal.toString(),
                        currentToLast.toString());

                // generate images and print to dashboard
                dashboardTimingReport.writeToHtml(nameAsHref(x),
                        generateImage(TIMING_RESULT_FILE_NAME,
                            x + "-timing-goal", currentToGoal),
                        generateImage(TIMING_RESULT_FILE_NAME,
                            x + "-timing-last", currentToLast));
            }
        } catch (IOException ioe) {
            Utils.logger.severe(ioe.toString());
        } finally {
            Utils.close(dashboardTimingReport);
            Utils.close(timingReport);
        }
    }

    private void printHeapReport() {
        HtmlWriter footprintReport = null;
        HtmlWriter dashboardFootprintReport = null;
        try {
            footprintReport = new HtmlWriter(FOOTPRINT_RESULT_FILE_NAME,
                    detailsTable("Footprint in MBytes"));
            dashboardFootprintReport = new HtmlWriter("footprint-dashboard.html",
                    dashboardTable("Footprint"));

            Set<String> kset = new TreeSet<String>(result.keySet());
            for (String x : kset) {
                Float currentToGoal = percentChange(false, goals, result, x);
                Float currentToLast = percentChange(false, last, result, x);
                footprintReport.writeToHtml(nameAsHref(x),
                        getHeapsizeValue(result12, x),
                        getHeapsizeValue(result13, x),
                        getHeapsizeValue(goals, x),
                        getHeapsizeValue(last, x),
                        getHeapsizeValue(result, x),
                        currentToGoal.toString(),
                        currentToLast.toString());

                // generate images and print to dashboard
                dashboardFootprintReport.writeToHtml(nameAsHref(x),
                        generateImage(FOOTPRINT_RESULT_FILE_NAME,
                        x + "-footprint-goal", currentToGoal),
                        generateImage(FOOTPRINT_RESULT_FILE_NAME,
                        x + "-footprint-last", currentToLast));
            }
        } catch (IOException ioe) {
            Utils.logger.severe(ioe.toString());
        } finally {
            Utils.close(footprintReport);
            Utils.close(dashboardFootprintReport);
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
