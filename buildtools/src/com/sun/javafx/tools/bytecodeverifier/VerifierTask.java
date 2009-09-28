/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.tools.bytecodeverifier;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * VerifierTask - this ANT task can be used to verify
 * bytecodes contained in one or more .class files.
 *
 * @author A. Sundararajan
 */
public class VerifierTask extends Task {
    private Path path;
    private File outFile;

    public VerifierTask() {
        super();
    }

    public void setPath(Path p) {
        path = p;
    }

    public void setPathRef(Reference r) {
        setPath((Path) r.getReferencedObject());
    }

    public void setOut(String out) {
        outFile = new File(out);
    }
    
    protected void checkParameters() throws BuildException {
        if (path == null) {
            throw new BuildException("bytecode-verifier: classpath must be set", getLocation());
        }
    }

    @Override    
    public void execute() throws BuildException {
        checkParameters();
        PrintWriter err = null;
        try {
            if (outFile != null) {
                if (! outFile.isFile()) {
                    throw new BuildException("Output file " + outFile.getAbsolutePath() + " does not exist!"); 
                }
                err = new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
            }
            Verifier.verifyPath(path.toString(), err == null? new PrintWriter(System.err) : err);
        } catch (IOException exp) {
            throw new BuildException(exp);
        } finally {
            if (err != null) err.close();
        }
    }
}
