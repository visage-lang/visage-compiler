/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main;

import static net.java.javafx.svg.translator.main.util.Utils.SVG_URI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.File;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ContentProvider;
import net.java.javafx.svg.translator.provider.FixedStringContentProvider;

import org.xml.sax.InputSource;


public class Main {
    // private static String lineSeparator = System.getProperty("line.separator");

    public static void main(String[] args) throws Exception {
        int argc = 0;

        InputStream beforePreprocessing = Main.class.getResourceAsStream("beforePreprocessing.txt");
        InputStream beforeProcessing = Main.class.getResourceAsStream("beforeProcessing.txt");
        InputStream afterProcessing = Main.class.getResourceAsStream("afterProcessing.txt");
        if (argc < args.length) {
        	if ("--section-files".equals(args[argc])) {
            	if (args.length < 4) {
            		usage();
            	}
        		beforePreprocessing = new FileInputStream(args[++argc]);
        		beforeProcessing = new FileInputStream(args[++argc]);
        		afterProcessing = new FileInputStream(args[++argc]);
        		argc++;
        	}
        }
	String fileName = "SVG";
        InputStream is = System.in;
        InputSource inputSource = null;
        if (argc < args.length) {
        	if (!"-".equals(args[argc])) {
        		inputSource = new InputSource(args[argc]);
        	} else {
        		inputSource = new InputSource(is);
        	}
        	argc++;
        }
        
        OutputStream os = System.out;
        if (argc < args.length) {
        	if (!"-".equals(args[argc])) {
		    fileName = new File(args[argc]).getName();
		    int dot = fileName.lastIndexOf(".");
		    if (dot > 0) {
			fileName = fileName.substring(0, dot);
		    }
		    os = new FileOutputStream(args[argc]);
        	}
        	argc++;
        }
        Writer writer = new OutputStreamWriter(os);
        
        String packageDirective = "";
        if (argc < args.length) {
        	packageDirective = "package " + Builder.printId(args[argc], true)+ ";\n";
        	argc++;
        }
        
        SVGBuilder svgBuilder = new SVGBuilder(1, "    ", SVG_URI);
        Builder builder = svgBuilder.getBuilder();
	builder.getStore().put("FILE_NAME", fileName);
        
        String importDirectives =
        	"import javafx.ui.*;\n" +
                "import javafx.ui.canvas.*;\n";
        
        ContentProvider provider = new FixedStringContentProvider(
				packageDirective +
				importDirectives +
				readInputStream(beforePreprocessing),
				readInputStream(beforeProcessing),
				readInputStream(afterProcessing));
        
        builder.build(inputSource, writer, provider);
        writer.flush(); writer.close();
    }
    
    private static void usage() {
        System.err.println("Usage: Main [--section-files beforePreprocessing beforeProcessing afterProcessing] [inputSource | - ] [resultFile | - ] [packageName]");
        System.exit(1);
    }

    private static String readInputStream(InputStream is) throws IOException {
	    if (is == null) {
	    	return "";
	    }
	    String line;
	    StringBuilder sb = new StringBuilder();
	    BufferedReader in = new BufferedReader(new InputStreamReader(is));
	    while ((line = in.readLine()) != null) {
	    	sb.append(line);
	    	sb.append('\n');
	    }
	    
	    return sb.toString();
    }
}
