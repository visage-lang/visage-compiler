/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime.i18n;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.sun.javafx.runtime.i18n.backport.ResourceBundle;
import com.sun.javafx.runtime.i18n.backport.ResourceBundleEnumeration;

class JavaFXPropertyResourceBundle extends ResourceBundle {

    private static final String CHARTAG = "@charset \"";
    private static final List<String> FORMAT_FXPROPERTIES
            = Collections.unmodifiableList(Arrays.asList("javafx.properties"));
    private static final String LITERAL = "(?:\"(.+?)\"|'(.+?)')";
    private static final String SPACES = "(?:[\\s\\n]*)";
    private static final String COMMENTS = SPACES +
                                   "(?://[^\\n]*[\\n]?"+SPACES+"|/\\*.*?\\*/"+SPACES+")*";
    private static final Pattern propPattern = Pattern.compile( "^" + 
                                                        COMMENTS +
                                                        LITERAL + 
                                                        COMMENTS +
                                                        "=" +
                                                        COMMENTS +
                                                        LITERAL + 
                                                        COMMENTS +
                                                        "$", 
                                                        Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern escPattern = Pattern.compile("(\\\\u[0-9a-fA-F]{4}|\\\\.)");

    private ConcurrentMap<String, Object> lookup;

    // to be removed if we discard JDK 5 support
    private static final Locale ROOTLOCALE = new Locale("");

    public JavaFXPropertyResourceBundle(InputStream is) throws IOException {
        this(getReader(is));
    }

    public JavaFXPropertyResourceBundle(Reader reader) throws IOException {
        lookup = new ConcurrentHashMap<String, Object>();
        initialize(reader);
    }

    @Override
    public boolean containsKey(String key) {
	if (key == null) {
	    throw new NullPointerException();
	}
	return true;
    }

    @Override
    protected Object handleGetObject(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return lookup.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        ResourceBundle parent = this.parent;
        return new ResourceBundleEnumeration(lookup.keySet(),
                (parent != null) ? parent.getKeys() : null);
    }

    @Override
    protected Set<String> handleKeySet() {
        return lookup.keySet();
    }

    private void initialize(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        String line;
        String logicalLine = null;
        while ((line = br.readLine()) != null) {
            if (logicalLine == null) {
                logicalLine = line;
            } else {
                logicalLine += "\n" + line;
            }

            Matcher m = propPattern.matcher(logicalLine);
            if (m.matches()) {
                String key = (m.group(1) != null ? m.group(1) : m.group(2));
                String value = (m.group(3) != null  ? m.group(3) : m.group(4));

                key = processEscape(key);
                value = processEscape(value);
                lookup.put(key, value);
                logicalLine = null;
            }
        }
        br.close();
    }

    private static String processEscape(String src) {
        Matcher m = escPattern.matcher(src);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String found = m.group(0);
            if (found.length() == 6) { // must be Unicode escape
                m.appendReplacement(sb, convertUnicodeEscape(found));
            } else {
                m.appendReplacement(sb, convertSingleEscape(found));
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String convertUnicodeEscape(String esc) {
        int value = 0;
        for (int index = 2; index < esc.length(); index ++) {
            char c = esc.charAt(index);
            switch (c) {
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                value = (value << 4) + c - '0';
                break;
            case 'a': case 'b': case 'c':
            case 'd': case 'e': case 'f':
                value = (value << 4) + c + 10 - 'a';
                break;
            case 'A': case 'B': case 'C':
            case 'D': case 'E': case 'F':
                value = (value << 4) + c + 10 - 'A';
                break;
            default:
                throw new IllegalArgumentException(
                             "Malformed \\uxxxx encoding.");
            }
        }
        return String.valueOf((char)value);
    }

    private static String convertSingleEscape(String esc) {
        char c = esc.charAt(1);
        switch (c) {
        case 'n':
            c = '\n';
            break;
        case 't':
            c = '\t';
            break;
        case 'r':
            c = '\r';
            break;
        case 'f':
            c = '\f';
            break;
        case '\\':
            // need to retain double backslash for Matcher.appendReplacement()
            return "\\\\";
        }
        return String.valueOf(c);
    }

    private static Reader getReader(InputStream is) throws IOException {
        String charset = "UTF-8";
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(256);
        byte[] ba = new byte[CHARTAG.length()];
        bis.read(ba, 0, CHARTAG.length());
        String possibleCharsetTag = new String(ba, charset);
        if (possibleCharsetTag.equals(CHARTAG)) {
            StringBuilder sb = new StringBuilder();
            byte b;
            while ((b = (byte)bis.read()) != '\n') {
                sb.append((char)b);
            }
            charset = sb.toString().trim().replace("\";", "");
        } else {
            bis.reset();
        }
        return new InputStreamReader(bis, charset);
    }

    private static class FxEchoBackResourceBundle extends ResourceBundle {
	private static final Set<String> keyset = new HashSet<String>();
	static final FxEchoBackResourceBundle INSTANCE = new FxEchoBackResourceBundle();

	private FxEchoBackResourceBundle() {
	}

	@Override
	public boolean containsKey(String key) {
	    return true;
	}

	@Override
	protected Object handleGetObject(String key) {
	    if (key == null) {
		throw new NullPointerException();
	    }
	    return key;
	}

	@Override
	public Enumeration<String> getKeys() {
	    return new ResourceBundleEnumeration(keyset, null);
	}

	@Override protected Set<String> handleKeySet() {
	    return keyset;
	}
    }

    static class JavaFXPropertiesControl extends ResourceBundle.Control {
	static final JavaFXPropertiesControl INSTANCE = new JavaFXPropertiesControl();

	private JavaFXPropertiesControl() {
	}

        @Override
        public List<String> getFormats(String baseName) {
            if (baseName == null) {
                throw new NullPointerException();
            }
 
            return JavaFXPropertyResourceBundle.FORMAT_FXPROPERTIES;
        }
 
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format,
            			ClassLoader classLoader, boolean reloadFlag)
                throws IllegalAccessException, InstantiationException, IOException {
            if (locale.equals(ROOTLOCALE)) {
                return FxEchoBackResourceBundle.INSTANCE;
            }

            String bundleName = toBundleName(baseName, locale);
            ResourceBundle bundle = null;
	    final String resourceName = bundleName+".fxproperties";
	    final ClassLoader loader = classLoader;
	    final boolean reload = reloadFlag;
	    InputStream stream = null;
	    try {
		    stream = AccessController.doPrivileged(
			new PrivilegedExceptionAction<InputStream>() {
			    public InputStream run() throws IOException {
				InputStream is = null;
				if (reload) {
				    URL url = loader.getResource(resourceName);
				    if (url != null) {
					URLConnection connection = url.openConnection();
					if (connection != null) {
					    // Disable caches to get fresh data for
					    // reloading.
					    connection.setUseCaches(false);
					    is = connection.getInputStream();
					}
				    }
				} else {
				    is = loader.getResourceAsStream(resourceName);
				}
				return is;
			    }
			});
	    } catch (PrivilegedActionException e) {
	        throw (IOException) e.getException();
	    }

	    if (stream != null) {
	        try {
		    bundle = new JavaFXPropertyResourceBundle(stream);
	        } finally {
		    stream.close();
	        }
	    }

            return bundle;
        }
    }
}
