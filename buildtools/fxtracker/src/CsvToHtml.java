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
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ksrini
 */
public class CsvToHtml {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        csvToHtml(args[0], args[1]);
    }
  
    private static final String CSEP = ":";
    
    static void csvToHtml(String csvfilename, String htmlfilename) {
        FileOutputStream fos = null;
        FileReader  rdr = null;
        PrintStream ps = null;
        try {
            // output file
            fos = new FileOutputStream(htmlfilename);
            ps = new PrintStream(fos);
            
            // input file
            rdr = new FileReader(csvfilename);
            BufferedReader br = new BufferedReader(rdr);
            List<String> inList = new ArrayList<String>();
            String in = br.readLine();
            while (in != null) {
                inList.add(in);
                in = br.readLine();
            }
            br.close();
            
            // Markers we need
            int statStart   = 0;
            int statEnd     = 0;
            int locStart    = 0;
            int locEnd      = 0;
            int fxbaseStart = 0;
            int fxbaseEnd   = inList.size();
            
            for (int idx = 0 ; idx < inList.size() ; idx++) {
                String x = inList.get(idx);
                if (x.startsWith("INTERVAL")) {
                    ++idx;
                    statStart = idx;
                }
                if (x.startsWith("Location classes")) {
                    statEnd = idx;
                    ++idx;
                    locStart = idx;
                }
                if (x.startsWith("FXBase classes")) {
                    locEnd = idx;
                    ++idx;
                    fxbaseStart = idx;
                }
            }
            
            // print statistics
            ps.println("<HTML>");
            ps.println("<BODY LANG=\"en-US\" DIR=\"LTR\">");
            //ps.println("<CENTER>");
            ps.println("  <TABLE BORDER=1 CELLPADDING=4 CELLSPACING=0>");
            ps.println("  <TR VALIGN=\"TOP\">");
            ps.println("     <TH><P><B>INTERVAL</B></P></TH>");
            ps.println("     <TD><P><B>BINDING_EXPRESSION</B></P></TD>");
            ps.println("     <TD><P><B>CHANGE_LISTENER</B></P></TD>");
            ps.println("     <TD><P><B>TRIGGER</B></P></TD>");
            ps.println("     <TD><P><B>VIEW_LOCATION</B></P></TD>");
            ps.println("     <TD><P><B>WEAK_LOCATION</B></P></TD>");
            ps.println("     <TD><P><B>Total Children</B></P></TD>");
            ps.println("     <TD><P><B>Location Map Size</B></P></TD>");
            ps.println("     <TD><P><B>Location Map Change</B></P></TD>");
            ps.println("     <TD><P><B>Iterator Count</B></P></TD>");
            ps.println("     <TD><P><B>Listener Count</B></P></TD>");
            ps.println("     <TD><P><B>FXBase Map size</B></P></TD>");
            ps.println("     <TD><P><B>FXBase Change</B></P></TD>");
            ps.println("     <TD><P><B>SDL Map Size</B></P></TD>");
            ps.println("     <TD><P><B>SDL Map Change</B></P></TD>");
            ps.println("     <TD><P><B>SDL Null Referent Count</B></P></TD>");
            ps.println("  </TR>");
            for  (String line : inList.subList(statStart, statEnd)) {
                String flds[] = line.split(CSEP);
                ps.println("  <TR VALIGN=\"TOP\">");
                for (String x : flds) {
                     ps.println("      <TD><P>" + x + "</P></TD>");
                }               
            }
            ps.println("</TR></TABLE>");
            ps.println("<P><P>");

            // print the location classes
            ps.println("  <TABLE BORDER=1 CELLPADDING=4 CELLSPACING=0>");
            ps.println("  <TR VALIGN=\"TOP\">");
            ps.println("     <TH COLSPAN=2><P><B>Location classes Avg.</P></B></TH>");
            ps.println("   </TR>");
            ps.println("   <TR>");
            ps.println("     <TD><P><B>Classes</P></B></TD>");
            ps.println("     <TD><P><B>Average count</P></B></TD>");
            ps.println("  </TR>");
            
            for (String line : inList.subList(locStart, locEnd) ) {
                String rstr[] = line.split(CSEP);
                ps.println("<TR>");
                ps.println("<TD><P>" + rstr[0] + "</TD></P>");
                ps.println("<TD><P>" + rstr[1] + "</TD></P>");
                ps.println("</TR>");
            }
            
            ps.println("</TABLE>");
            ps.println("<P><P>");

            // print the fxobject classes
            ps.println("  <TABLE BORDER=1 CELLPADDING=4 CELLSPACING=0>");
            ps.println("  <TR VALIGN=\"TOP\">");
            ps.println("     <TH COLSPAN=2><P><B>FXBase classes Avg.</P></B></TH>");
            ps.println("   </TR>");
            ps.println("   <TR>");
            ps.println("     <TD><P><B>Classes</P></B></TD>");
            ps.println("     <TD><P><B>Average count</P></B></TD>");
            ps.println("  </TR>");
            for (String line : inList.subList(fxbaseStart, fxbaseEnd)) {
                String rstr[] = line.split(CSEP);
                ps.println("<TR>");
                ps.println("<TD><P>" + rstr[0] + "</TD></P>");
                ps.println("<TD><P>" + rstr[1] + "</TD></P>");
                ps.println("</TR>");
            }
            ps.println("</TABLE>");
            
            ps.println("</BODY></HTML>");
            ps.close();
            fos.close();
        } catch (IOException ioe) {
            FxTrackerRunner.logger.severe(ioe.getMessage());
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (rdr != null)
                    rdr.close();
                if (ps != null)
                    ps.close();
            } catch (IOException ignore) {}
        }
    }
}
