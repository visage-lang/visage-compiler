
package com.sun.tools.javafx.preview;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MemoryClassLoader extends ClassLoader {

    Map<String, byte[]> classBytes;

    public MemoryClassLoader() {
        classBytes = new HashMap<String, byte[]>();
    }

    public void loadMap(Map<String, byte[]> classBytes) throws ClassNotFoundException {
        for (String key : classBytes.keySet()) {
            this.classBytes.put(key, classBytes.get(key));
        }
    }

    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {

        if (classBytes.get(name) == null) { return super.loadClass(name, resolve); }
        
        Class result = findClass(name);
        
        if (resolve) { resolveClass(result); }

        return result;
    }

    @Override
    protected Class findClass(String className) throws ClassNotFoundException {
        byte[] buf = classBytes.get(className);
        if (buf != null) {
            classBytes.put(className, null);
            return defineClass(className, buf, 0, buf.length);
        } else {
            return super.findClass(className);
        }
    }
    
    @Override
    public InputStream getResourceAsStream(String resource) {
        //System.out.println("[memory class loader] read resource: " + resource);
        if (resource.endsWith(".fxproperties")) {
            String localeName = Locale.getDefault().getDisplayName();
            byte[] propBytes = classBytes.get(localeName);

            if (propBytes != null) {
                return new ByteArrayInputStream(propBytes);
            }
        }
        return super.getResourceAsStream(resource);
    }
}