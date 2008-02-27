package jfx.assortie.system;

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
            throw new Exception("FX file: \"" + name + "\" was not found!");
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
}


