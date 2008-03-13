/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class ProjectManager {

    public static Object runFXFile(String name) throws Exception {
        return runFXFile(name, Thread.currentThread().getContextClassLoader());
    }

    public static Object runFXFile(String name, ClassLoader classLoader) throws Exception {
        try {
            Class cls = classLoader.loadClass(name);
            Method run = cls.getDeclaredMethod(JavafxDefs.runMethodString, new Class[0]);
            return run.invoke(null);
        } catch (Throwable e) {
            throw new Exception("FX file: \"" + name + "\" was not compiled!!", e);
        }
    }

    public static Object runFXCode(String className, String code) throws Exception {
        return CodeManager.execute(className, code);
    }

    public static String readResource(String className, String resource) {
        try {
            Class cls = Class.forName(className);


            StringBuffer contents = new StringBuffer();
            BufferedReader input = new BufferedReader(new InputStreamReader(cls.getResourceAsStream(resource)));
            String line = null;
            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
            
            //System.out.println("resource:");
            //System.out.println(contents);
            
            return contents.toString();

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
    
    public static String getDefaultLookAndFeel(){
        return  javax.swing.UIManager.getSystemLookAndFeelClassName(); 
    }
    
    public static final String DEFAULT_LOOK_AND_FEEL = javax.swing.UIManager.getLookAndFeel().getClass().getName();
}


