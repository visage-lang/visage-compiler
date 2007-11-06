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
import java.util.HashMap;
import javax.lang.model.element.NestingKind;
import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import org.apache.tools.ant.util.TeeOutputStream;

/**
 * JavaFileManager that keeps compiled .class bytes in memory.
 *
 * @author A. Sundararajan
 */
public final class MemoryFileManager extends ForwardingJavaFileManager {                 

    /** JavaFX Script source file extension. */
    private final static String EXT = ".fx";

    private Map<String, byte[]> classBytes;
    
    public MemoryFileManager(JavaFileManager fileManager) {
        super(fileManager);
        classBytes = new HashMap<String, byte[]>();
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
     * A file object used to represent Java source coming from a string.
     */
    private static class StringInputBuffer extends SimpleJavaFileObject {
        final String code;
        final boolean isFXSourceFile;
        
        StringInputBuffer(String name, String code) {
            super(toURI(name), Kind.SOURCE);
            this.code = code;
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
            return isFXSourceFile ? JavaFileObject.Kind.SOURCE : super.getKind();
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
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location,
                                    String className,
                                    Kind kind,
                                    FileObject sibling) throws IOException {
        if (kind == Kind.CLASS) {
            return new ClassOutputBuffer(className);
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

    JavaFileObject makeStringSource(String name, String code) {
        return new StringInputBuffer(name, code);
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
