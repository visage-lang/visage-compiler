/*
 * Copyright 2010 Sun Microsystems, Inc.  All Rights Reserved.
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

package org.visage.jdi;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;

/**
 *
 * @author sundar
 */
public class VisageLocation extends VisageMirror implements Location {

    public VisageLocation(VisageVirtualMachine fxvm, Location underlying) {
        super(fxvm, underlying);
    }

    public long codeIndex() {
        return underlying().codeIndex();
    }

    public VisageReferenceType declaringType() {
        return VisageWrapper.wrap(virtualMachine(), underlying().declaringType());
    }

    public int lineNumber() {
        return underlying().lineNumber();
    }

    public int lineNumber(String stratum) {
        return underlying().lineNumber(stratum);
    }

    public VisageMethod method() {
        return VisageWrapper.wrap(virtualMachine(), underlying().method());
    }

    public String sourceName() throws AbsentInformationException {
        return underlying().sourceName();
    }

    public String sourceName(String stratum) throws AbsentInformationException {
        return underlying().sourceName(stratum);
    }

    public String sourcePath() throws AbsentInformationException {
        return underlying().sourcePath();
    }

    public String sourcePath(String stratum) throws AbsentInformationException {
        return underlying().sourcePath(stratum);
    }

    public int compareTo(Location o) {
        return underlying().compareTo(VisageWrapper.unwrap(o));
    }

    @Override
    protected Location underlying() {
        return (Location) super.underlying();
    }
}