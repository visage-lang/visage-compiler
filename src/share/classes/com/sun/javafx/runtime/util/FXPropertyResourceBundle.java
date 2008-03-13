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

package com.sun.javafx.runtime.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.sun.javafx.runtime.util.backport.ResourceBundle;
import com.sun.javafx.runtime.util.backport.ResourceBundleEnumeration;

class FXPropertyResourceBundle extends ResourceBundle {

    private static final String CHARTAG = "@charset \"";
    private static final List<String> FORMAT_FXPROPERTIES
            = Collections.unmodifiableList(Arrays.asList("javafx.properties"));
    private ConcurrentMap<String, Object> lookup;
    private static final Pattern escPattern = 
                                Pattern.compile("(\\\\u[0-9a-fA-F]{4}|\\\\.)");
    private static Logger logger = null;

    // code point literals
    private static final int CRETURN    = 0x000d;
    private static final int NEWLINE    = 0x000a;
    private static final int FSLASH     = 0x002f;
    private static final int DQUOTE     = 0x0022;
    private static final int SQUOTE     = 0x0027;
    private static final int EQUAL      = 0x003d;
    private static final int BSLASH     = 0x005c;
    private static final int SUBST      = 0xfffd;
    private static final int BOM        = 0xfeff;

    // to be removed if we discard JDK 5 support
    private static final Locale ROOTLOCALE = new Locale("");

    public FXPropertyResourceBundle(InputStream is, String resourceName) 
                                                        throws IOException {
        this(getReader(is), resourceName);
    }

    public FXPropertyResourceBundle(Reader reader, String resourceName)
                                                        throws IOException {
        lookup = new ConcurrentHashMap<String, Object>();
        initialize(reader, resourceName);
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

    private void initialize(Reader reader, String resourceName) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        int c;
        int lineNum = 1;
        StringBuilder sb = new StringBuilder();
        String key = null;
        boolean foundEqual = false;
        boolean firstChar = true;
        int quote = 0;  // quoting character used for a literal

        while ((c = getCodePoint(br)) != -1) {
            switch (c) {
            case CRETURN:
                // normalize '\r' and "\r\n" to '\n'
                br.mark(8);
                if (getCodePoint(br) != NEWLINE) {
                    br.reset();
                }
                // fall through
            case NEWLINE:
                lineNum ++;
                if (quote != 0) {
                    sb.appendCodePoint(NEWLINE);
                }
                break;

            case FSLASH:
                if (quote != 0) {
                    sb.appendCodePoint(c);
                } else {
                    lineNum += skipComments(br, resourceName);
                }
                break;

            case SQUOTE:
            case DQUOTE:
                if (quote == 0) {
                    if ((key == null && foundEqual) ||
                        (key != null && !foundEqual)) {
                        logPropertySyntaxError(c, lineNum, resourceName);
                        break;
                    }
                     
                    // start of a literal
                    quote = c;
                } else if (c != SQUOTE && c != DQUOTE) {
                    // a normal character in a literal
                    sb.appendCodePoint(c);
                } else if (quote != c) {
                    // the other quote character in a literal
                    sb.appendCodePoint(c);
                } else {
                    // closing of a quote
                    quote = 0;
                    if (!foundEqual && key == null) {
                        key = sb.toString();
                        sb.setLength(0);
                    } else if (foundEqual && key != null) {
                        processKeyValue(key, sb.toString());
                        sb.setLength(0);
                        key = null;
                        foundEqual = false;
                    } else {
                        logPropertySyntaxError(c, lineNum, resourceName);
                    }
                }
                break;

            case EQUAL:
                if (quote != 0) {
                    sb.appendCodePoint(c);
                } else {
                    if (foundEqual) {
                        logPropertySyntaxError(c, lineNum, resourceName);
                    } else {
                        if (key == null) {
                            logPropertySyntaxError(c, lineNum, resourceName);
                        } else {
                            foundEqual = true;
                        }
                    }
                }
                break;

            case BSLASH:
                if (quote != 0) {
                    sb.appendCodePoint(c);
                    // append the next character no matter what
                    sb.appendCodePoint(getCodePoint(br));
                } else {
                    logPropertySyntaxError(c, lineNum, resourceName);
                }
                break;

            case BOM:
                if (firstChar) {
                    // ignore BOM at the beginning
                    firstChar = false;
                } else {
                    logPropertySyntaxError(c, lineNum, resourceName);
                }
                break;

            default:
                if (quote != 0) {
                    sb.appendCodePoint(c);
                } else if (Character.isWhitespace(c) || c == SUBST) {
                    break;
                } else {
                    logPropertySyntaxError(c, lineNum, resourceName);
                }
                break;
            }
        }

        br.close();
    }

    private void processKeyValue(String key, String value) {
        key = processEscape(key);
        value = processEscape(value);
        lookup.put(key, value);
    }

    private int getCodePoint(BufferedReader br) throws IOException {
        int c = br.read();
        if (Character.isHighSurrogate((char)c)) {
            return Character.toCodePoint((char)c, (char)br.read());
        } else {
            return c;
        }
    }

    private int skipComments(BufferedReader br, String resourceName) throws IOException {
        int newlines = 0;

        switch ((char)getCodePoint(br)) {
        case '*':
            // skip till we find a corresponding "*/"
            while (true) {
                int i = getCodePoint(br);
                if ((char)i == '\n') {
                    newlines ++;
                } else if ((char)i == '*') {
                    if ((char)getCodePoint(br) == '/') {
                        break;
                    }
                } else if (i == -1) {
                    // non-closing comment causes an error
                    log(Level.WARNING,
                        "non-closing comment at the end of "+resourceName);
                    break;
                }
            }
            break;

        case '/':
            // skip till we find a new line or end of the file
            while (true) {
                int i = getCodePoint(br);
                if ((char)i == '\n') {
                    newlines ++;
                    break;
                } else if (i == -1) {
                    break;
                }
            }
            break;
        }

        return newlines;
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
                log(Level.WARNING, "Malformed \\uxxxx encoding.");
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
        Charset charset = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(256);
        byte[] ba = new byte[CHARTAG.length()];
        if (bis.read(ba, 0, CHARTAG.length()) == ba.length) {
            String possibleCharsetTag = new String(ba, "UTF-8");
            if (possibleCharsetTag.equals(CHARTAG)) {
                StringBuilder sb = new StringBuilder();
                byte b;
                boolean found = false;
                while (true) {
                    b = (byte)bis.read();
                    
                    if (b == '\r' || b == '\n') {
                        if (!found) {
                            log(Level.WARNING, 
                                "Incorrect format in @charset tag");
                        }
                        break;
                    }

                    if (b != '"') {
                        sb.append((char)b);
                    } else {
                        found = true;
                        if ((char)bis.read() == ';') {
                            // conforms to the CSS encoding declaration
                            try {
                                charset = Charset.forName(sb.toString());
                            } catch (Exception e) {
                                log(Level.WARNING, 
                                    "charset '" + sb.toString() + "' was not available");
                            }
                        } else {
                            log(Level.WARNING, 
                                "Incorrect format in @charset tag");
                        }
                    }
                }
            } else {
                bis.reset();
            }
        }

        if (charset == null) {
            charset = Charset.forName("UTF-8");
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

    static class FXPropertiesControl extends ResourceBundle.Control {
	static final FXPropertiesControl INSTANCE = new FXPropertiesControl();

	private FXPropertiesControl() {
	}

        @Override
        public List<String> getFormats(String baseName) {
            if (baseName == null) {
                throw new NullPointerException();
            }
 
            return FXPropertyResourceBundle.FORMAT_FXPROPERTIES;
        }

        @Override
        public Locale getFallbackLocale(String baseName, Locale locale) {
            if (baseName == null || locale == null) {
                throw new NullPointerException();
            }
            return null;
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
	    final String resourceName = toResourceName(bundleName, "fxproperties");
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
		    bundle = new FXPropertyResourceBundle(stream, resourceName);
	        } finally {
		    stream.close();
	        }
	    }

            return bundle;
        }
    }

    private static void logPropertySyntaxError(int c, int lineNum, String resourceName) {
        log(Level.WARNING,
            "'" + (char)c + "'(0x" + Integer.toHexString(c) +
            ") is incorrectly placed in line " + lineNum + " of " + resourceName);
    }

    private static void log(Level l, String msg) {
        if (logger == null) {
            logger = Logger.getLogger("com.sun.javafx.runtime.util.FXPropertyResourceBundle");
        }


        logger.log(l, msg);
    }
}
