/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package jfx.assortis.system;

/**
 *
 * @author Alexandr Scherbatiy
 */

import com.sun.javafx.runtime.*;
import com.sun.tools.javafx.comp.JavafxDefs;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProjectManager {

    private static final String[] commandLineArgs = new String[]{};

    public static Object runFXFile(String name) throws Exception {
        return runFXFile(name, Thread.currentThread().getContextClassLoader());
    }

    public static Object runFXFile(String name, ClassLoader classLoader) throws Exception {
        try {
            Class cls = classLoader.loadClass(name);
            //Method run = cls.getDeclaredMethod(JavafxDefs.runMethodString, new Class[0]);
            Method run = cls.getDeclaredMethod(JavafxDefs.runMethodString, Sequence.class);
            Object args = Sequences.make(String.class, commandLineArgs);
            return run.invoke(null,args);
        } catch (Throwable e) {
            throw new Exception("FX file: \"" + name + "\" was not compiled!!", e);
        }
    }

    public static Object runFXCode(String className, String code, String propName, String props) throws Exception {
        return CodeManager.execute(className, code, propName, props);
    }

    public static String readResource(String className, String resource) {
        try {
            Class cls = Class.forName(className);


            StringBuffer contents = new StringBuffer();
            InputStream is = cls.getResourceAsStream(resource);
            if (is != null) {
                BufferedReader input = new BufferedReader(new InputStreamReader(is));
                String line = null;
                    while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            
                //System.out.println("resource:");
                //System.out.println(contents);
            
                return contents.toString();
	    }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return "";
    }
    
    public static String getFileName(String className){
        return className.substring(className.lastIndexOf('.') + 1) + ".fx";
    }

    public static String getFilePath(String className){
        return className.replace('.','/') + ".fx";
    }
    
    public static String getFXPropertiesName(String className, Locale l){
        return className.substring(className.lastIndexOf('.') + 1) + 
            "_" + l.toString() + ".fxproperties";
    }
    
    public static List<Locale> getFXPropertiesLocales(URI dirURI, final String baseName){
	File dir = new File(dirURI); 
	String[] propFiles = dir.list(new FilenameFilter() {
	    public boolean accept(File d, String f) {
		return (f.startsWith(baseName + "_") &&
		        f.endsWith(".fxproperties"));
	    }
	});
	List<Locale> locs = new ArrayList<Locale>();
	for (String propf : propFiles) {
	    String locStr = propf.substring(baseName.length()+1, propf.lastIndexOf('.'));
//System.out.printf("locStr: %s\n", locStr);
	    String[] args = locStr.split("_", 3);
	    locs.add(new Locale(args[0], 
                                (args.length > 1 ? args[1] : ""),
                                (args.length > 2 ? args[2] : "")));
//System.out.println(locs);
	}
	Locale def = Locale.getDefault();
	while (def.getLanguage() != "") {
	    int index = locs.indexOf(def);
	    if (index != -1) {
		locs.remove(index);
		locs.add(0, def);
		break;
	    }
	    String[] args = def.toString().split("_", 0);
	    switch (args.length) {
	    default:
	    case 3:
		def = new Locale(args[0], args[1]);
		break;
	    case 2:
		def = new Locale(args[0]);
		break;
	    case 1:
		locs.add(0, def);
		break;
	    }
	}
	return locs;
    }
    
    public static String getDefaultLookAndFeel(){
        //return  javax.swing.UIManager.getSystemLookAndFeelClassName(); 
        return "org.jvnet.substance.skin.SubstanceNebulaLookAndFeel";
    }
    
    public static final String DEFAULT_LOOK_AND_FEEL = javax.swing.UIManager.getLookAndFeel().getClass().getName();
}


