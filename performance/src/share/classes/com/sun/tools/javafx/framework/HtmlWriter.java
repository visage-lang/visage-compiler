/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.framework;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author ksrini
 */
public class HtmlWriter implements Closeable {

    private final FileOutputStream fos;
    final PrintStream ps;

    public HtmlWriter(String filename, String tableHeader) throws FileNotFoundException {
        fos = new FileOutputStream(filename);
        ps = new PrintStream(fos);
        openHtml();
        ps.println(tableHeader);
    }

    public void close() throws IOException {
        endTable();
        closeHtml();
        Utils.close(ps);
        Utils.close(fos);
    }

    private void endTable() {
        ps.println("</TABLE>");
        ps.println("<P><P>");
    }

    public void writeToHtml(String... args) {
        ps.println("<TR>");
        for (String x : args) {
            ps.println("<TD><P>" + x + "</TD></P>");
        }
        ps.println("</TR>");
    }

    private void openHtml() {
        ps.println("<HTML>");
        ps.println("<BODY LANG=\"en-US\" DIR=\"LTR\">");
    }

    private void closeHtml() {
        ps.println("</BODY></HTML>");
    }
}
