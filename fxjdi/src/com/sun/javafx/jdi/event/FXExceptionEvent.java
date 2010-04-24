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

package com.sun.javafx.jdi.event;

import com.sun.javafx.jdi.FXLocation;
import com.sun.javafx.jdi.FXObjectReference;
import com.sun.javafx.jdi.FXVirtualMachine;
import com.sun.javafx.jdi.FXWrapper;
import com.sun.jdi.event.ExceptionEvent;

/**
 *
 * @author sundar
 */
public class FXExceptionEvent extends FXLocatableEvent implements ExceptionEvent {
    public FXExceptionEvent(FXVirtualMachine fxvm, ExceptionEvent underlying) {
        super(fxvm, underlying);
    }

    public FXLocation catchLocation() {
        return FXWrapper.wrap(virtualMachine(), underlying().catchLocation());
    }

    public FXObjectReference exception() {
        return FXWrapper.wrap(virtualMachine(), underlying().exception());
    }

    @Override
    protected ExceptionEvent underlying() {
        return (ExceptionEvent) super.underlying();
    }
}
