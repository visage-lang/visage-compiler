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

package org.visage.jdi.event;

import org.visage.jdi.VisageLocation;
import org.visage.jdi.VisageObjectReference;
import org.visage.jdi.VisageVirtualMachine;
import org.visage.jdi.VisageWrapper;
import com.sun.jdi.event.ExceptionEvent;

/**
 *
 * @author sundar
 */
public class VisageExceptionEvent extends VisageLocatableEvent implements ExceptionEvent {
    public VisageExceptionEvent(VisageVirtualMachine visagevm, ExceptionEvent underlying) {
        super(visagevm, underlying);
    }

    public VisageLocation catchLocation() {
        return VisageWrapper.wrap(virtualMachine(), underlying().catchLocation());
    }

    public VisageObjectReference exception() {
        return VisageWrapper.wrap(virtualMachine(), underlying().exception());
    }

    @Override
    protected ExceptionEvent underlying() {
        return (ExceptionEvent) super.underlying();
    }
}
