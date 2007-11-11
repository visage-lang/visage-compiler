/*
 * Copyright 2005-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.util;

import com.sun.tools.javac.util.BaseFileObject;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

public class JavafxFileManager extends JavacFileManager {
    
    /**
     * The JavaFX Script source file extension.
     * @see javax.tools.JavaFileObject.Kind.SOURCE
     */
    public static final String FX_SOURCE_SUFFIX = ".fx";

    /**
     * Register a Context.Factory to create a JavafxFileManager.
     */
    public static void preRegister(final Context context) {
        context.put(JavaFileManager.class, new Context.Factory<JavaFileManager>() {
            public JavaFileManager make() {
                return new JavafxFileManager(context, true, null);
            }
        });
    }

    public JavafxFileManager(Context context, boolean register, Charset charset) {
        super(context, register, charset);      
    }

    @Override
    protected JavaFileObject.Kind getKind(String extension) {
        if (extension.equals(JavaFileObject.Kind.CLASS.extension))
            return JavaFileObject.Kind.CLASS;
        else if (extension.equals(FX_SOURCE_SUFFIX))
            return JavaFileObject.Kind.SOURCE;
        else if (extension.equals(JavaFileObject.Kind.HTML.extension))
            return JavaFileObject.Kind.HTML;
        else
            return JavaFileObject.Kind.OTHER;
    }

    @Override
    public JavaFileObject getRegularFile(File file) {
        return new DelegateJavaFileObject(super.getRegularFile(file));
    }
    
    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(
        Iterable<? extends File> files)
    {
        Iterable<? extends JavaFileObject> objs = super.getJavaFileObjectsFromFiles(files);
        ArrayList<DelegateJavaFileObject> result = new ArrayList<DelegateJavaFileObject>();
        for (JavaFileObject jfo : objs)
            result.add(new DelegateJavaFileObject(jfo));
        return result;
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location,
                                              String className,
                                              JavaFileObject.Kind kind)
        throws IOException
    {
        nullCheck(location);
        // validateClassName(className);
        nullCheck(className);
        nullCheck(kind);
        if (!sourceOrClass.contains(kind))
            throw new IllegalArgumentException("Invalid kind " + kind);
        return (JavaFileObject)getFileForInput(location, "", externalizeFileName(className, kind));
    }

    private static <T> T nullCheck(T o) {
        o.getClass(); // null check
        return o;
    }

    private static String externalizeFileName(CharSequence name, JavaFileObject.Kind kind) {
        String basename = name.toString().replace('.', File.separatorChar);
        String suffix = kind == JavaFileObject.Kind.SOURCE ? 
            FX_SOURCE_SUFFIX : kind.extension;
        return basename + suffix;
    }

    private final Set<JavaFileObject.Kind> sourceOrClass =
        EnumSet.of(JavaFileObject.Kind.SOURCE, JavaFileObject.Kind.CLASS);
    
    private static class DelegateJavaFileObject extends BaseFileObject {
        JavaFileObject delegate;
        boolean isFXSourceFile;
        
        DelegateJavaFileObject(JavaFileObject jfo) {
            delegate = jfo;
            isFXSourceFile = jfo.toString().endsWith(FX_SOURCE_SUFFIX);
        }

        @Override
        public Kind getKind() {
            return isFXSourceFile ? JavaFileObject.Kind.SOURCE : delegate.getKind();
        }

        @Override
        public boolean isNameCompatible(String simpleName, Kind kind) {
            return delegate.isNameCompatible(simpleName, kind);
        }

        @Override
        public NestingKind getNestingKind() {
            return delegate.getNestingKind();
        }

        @Override
        public Modifier getAccessLevel() {
            return delegate.getAccessLevel();
        }

        public URI toUri() {
            return delegate.toUri();
        }

        public String getName() {
            return delegate.getName();
        }

        public InputStream openInputStream() throws IOException {
            return delegate.openInputStream();
        }

        public OutputStream openOutputStream() throws IOException {
            return delegate.openOutputStream();
        }

        @Override
        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            return delegate.openReader(ignoreEncodingErrors);
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return delegate.getCharContent(ignoreEncodingErrors);
        }

        public Writer openWriter() throws IOException {
            return delegate.openWriter();
        }

        public long getLastModified() {
            return delegate.getLastModified();
        }

        public boolean delete() {
            return delegate.delete();
        }
        
        @Override
        public String toString() {
            return delegate.toString();
        }
    }
}
