/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.script;

import com.sun.tools.javafx.util.JavafxFileManager;
import java.io.*;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.Map;
import java.util.*;
import java.util.HashMap;
import java.util.Set;
import javax.lang.model.element.NestingKind;
import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.net.URL;
import java.net.URI;

/**
 * JavaFileManager that keeps compiled .class bytes in memory.
 *
 * @author A. Sundararajan
 */
public final class MemoryFileManager extends ForwardingJavaFileManager {                 
    private ClassLoader parentClassLoader;
    List<SimpleJavaFileObject> buffers = new ArrayList<SimpleJavaFileObject>();

    /** JavaFX Script source file extension. */
    private final static String EXT = ".fx";

    private Map<String, byte[]> classBytes;
    
    public MemoryFileManager(JavaFileManager fileManager, ClassLoader cl) {
        super(fileManager);
        classBytes = new HashMap<String, byte[]>();
	parentClassLoader = cl;
    }

    public Map<String, byte[]> getClassBytes() {
        return classBytes;
    }
   
    @Override
    public void close() throws IOException {
        classBytes = new HashMap<String, byte[]>();
    }

    @Override
    public void flush() throws IOException {
    }

    /**
     * A file object used to represent a Java class coming from the parent class loader
     */
    private static class ClassResource extends SimpleJavaFileObject {
	URL url;
	static URI toURI(URL u) {
	    try {
		return u.toURI();
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}
	public ClassResource(URL u) {
	    super(toURI(u), Kind.CLASS);
	    this.url = u;
	}

        @Override
        public InputStream openInputStream() throws IOException {
            return url.openStream();
        }
    }

    /**
     * A file object used to represent Java source coming from a string.
     */
    private static class StringInputBuffer extends SimpleJavaFileObject {
        final String code;
        final boolean isFXSourceFile;
	String binaryName;
        
	public String getBinaryName() {
	    return binaryName.equals("__FX_SCRIPT__.fx") ? "__FX_SCRIPT__" : binaryName;
	}

        StringInputBuffer(String name, String code) {
            super(toURI(name), Kind.SOURCE);
            this.code = code;
	    binaryName = name;
            isFXSourceFile = name.endsWith(JavafxFileManager.FX_SOURCE_SUFFIX);
        }
        
        @Override
        public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
            return CharBuffer.wrap(code);
        }

        public Reader openReader() {
            return new StringReader(code);
        }

        @Override
        public Kind getKind() {
            //return isFXSourceFile ? JavaFileObject.Kind.SOURCE : super.getKind();
	    return JavaFileObject.Kind.SOURCE;
        }

        @Override
        public String getName() {
            return super.getName();
        }

        @Override
        public NestingKind getNestingKind() {
            return super.getNestingKind();
        }

        @Override
        public boolean isNameCompatible(String simpleName, Kind kind) {
            return super.isNameCompatible(simpleName, kind);
        }

        @Override
        public InputStream openInputStream() throws IOException {
            return super.openInputStream();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return super.openOutputStream();
        }

        @Override
        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            return super.openReader(ignoreEncodingErrors);
        }

        @Override
        public Writer openWriter() throws IOException {
            return super.openWriter();
        }
    }

    /**
     * A file object that stores Java bytecode into the classBytes map.
     */
    private class ClassOutputBuffer extends SimpleJavaFileObject {
        private String name;

        ClassOutputBuffer(String name) { 
            super(toURI(name), Kind.CLASS);
            this.name = name;
        }

	public String getBinaryName() {
	    return name;
	}

        @Override
        public OutputStream openOutputStream() {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
                @Override
                public void close() throws IOException {
                    out.close();
                    ByteArrayOutputStream bos = (ByteArrayOutputStream)out;
                    classBytes.put(name, bos.toByteArray());
                }
            };
        }
    }
    @Override
    public FileObject getFileForInput(Location location,
				      String packageName,
				      String relativeName) throws IOException {
	
	return super.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(Location location,
				       String packageName,
				       String relativeName,
				       FileObject sibling) throws IOException {
	
	return super.getFileForOutput(location, packageName, relativeName, sibling);
    }
    
    @Override
    public JavaFileObject getJavaFileForInput(JavaFileManager.Location location,
					      String className,
					      Kind kind) throws IOException {
	if (kind == Kind.CLASS) {
	    URL res = 
		parentClassLoader.getResource(className.replace('.', '/') + ".class");
	    if (res != null) {
		return new ClassResource(res);
	    }
	}
	return super.getJavaFileForInput(location, className, kind);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location,
                                    String className,
                                    Kind kind,
                                    FileObject sibling) throws IOException {
        if (kind == Kind.CLASS) {
            ClassOutputBuffer buf = new ClassOutputBuffer(className);
	    buffers.add(buf);
	    return buf;
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }
    @Override
    public Iterable list(JavaFileManager.Location location,
			 String packageName,
			 Set kinds,
			 boolean recurse)
        throws IOException
    {
	Iterable result = super.list(location, packageName, kinds, recurse);
	List results = new LinkedList();
	for (Object o : result) {
	    results.add(o);
	}
	String prefix = packageName.equals("") ? "" : packageName + ".";
	for (SimpleJavaFileObject b : buffers) {
	    String name = b.getName().replace("/", ".");
	    name = name.substring(1, name.length() - (name.endsWith(EXT) ? EXT.length() : 0));
	    if (prefix.length() == 0) {
		if (!name.contains(".")) {
		    results.add(b);
		}
	    } else {
		if (name.startsWith(prefix)) {
		    name = name.substring(prefix.length());
		    if (!name.contains(".")) {
			results.add(b);
		    }
		}
	    }
	}
	return results;
    }
    
    JavaFileObject makeStringSource(String name, String code) {
	StringInputBuffer buffer = new StringInputBuffer(name, code);
	buffers.add(buffer);
        return buffer;
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
	if (file instanceof StringInputBuffer) {
	    return ((StringInputBuffer)file).getBinaryName();
	} else if (file instanceof ClassOutputBuffer) {
	    return ((ClassOutputBuffer)file).getBinaryName();
	}
	return super.inferBinaryName(location, file);
    }

    static URI toURI(String name) {
        File file = new File(name);
        if (file.exists()) {
            return file.toURI();
        } else {
            try {
                final StringBuilder newUri = new StringBuilder();
                newUri.append("mfm:///");
                newUri.append(name.replace('.', '/'));
                if(name.endsWith(EXT)) newUri.replace(newUri.length() - EXT.length(), newUri.length(), EXT);
                return URI.create(newUri.toString());
            } catch (Exception exp) {
                return URI.create("mfm:///com/sun/tools/javafx/script/javafx_source");
            }
        }
    }
}
