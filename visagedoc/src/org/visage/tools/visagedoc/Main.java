/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package org.visage.tools.visagedoc;

import java.io.PrintWriter;

/**
 * Provides external entry points (tool and programmatic)
 * for the javadoc program.
 *
 * @since 1.4
 */
public class Main {

    /**
     * Constructor should never be called.
     */
    private Main() {
    }

    /**
     * Command line interface.
     * @param args   The command line parameters.
     */
    public static void main(String[] args) {
        System.exit(execute(args));
    }

    /**
     * Programmatic interface.
     * @param args   The command line parameters.
     * @return The return code.
     */
    public static int execute(String[] args) {
        Start jdoc = new Start();
        return jdoc.begin(args);
    }

    /**
     * Programmatic interface.
     * @param programName  Name of the program (for error messages).
     * @param args   The command line parameters.
     * @return The return code.
     */
    public static int execute(String programName, String[] args) {
        Start jdoc = new Start(programName);
        return jdoc.begin(args);
    }

    /**
     * Programmatic interface.
     * @param programName  Name of the program (for error messages).
     * @param defaultDocletClassName  Fully qualified class name.
     * @param args   The command line parameters.
     * @return The return code.
     */
    public static int execute(String programName,
                              String defaultDocletClassName,
                              String[] args) {
        Start jdoc = new Start(programName, defaultDocletClassName);
        return jdoc.begin(args);
    }

    /**
     * Programmatic interface.
     * @param programName  Name of the program (for error messages).
     * @param errWriter    PrintWriter to receive error messages.
     * @param warnWriter    PrintWriter to receive error messages.
     * @param noticeWriter    PrintWriter to receive error messages.
     * @param defaultDocletClassName  Fully qualified class name.
     * @param args   The command line parameters.
     * @return The return code.
     */
    public static int execute(String programName,
                              PrintWriter errWriter,
                              PrintWriter warnWriter,
                              PrintWriter noticeWriter,
                              String defaultDocletClassName,
                              String[] args) {
        Start jdoc = new Start(programName,
                               errWriter, warnWriter, noticeWriter,
                               defaultDocletClassName);
        return jdoc.begin(args);
    }
}
