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

package com.sun.javafx.jdi.connect;

import com.sun.javafx.jdi.FXVirtualMachine;
import com.sun.javafx.jdi.FXWrapper;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author sundar
 */
public class FXLaunchingConnector extends FXConnector implements LaunchingConnector {
    public FXLaunchingConnector() {
        this(makePlatformConnector());
    }
    
    public FXLaunchingConnector(LaunchingConnector underlying) {
        super(underlying);
    }

    public FXVirtualMachine launch(Map<String, ? extends Argument> args)
            throws IOException, IllegalConnectorArgumentsException, VMStartException {
        return FXWrapper.wrap(underlying().launch(args));
    }

    @Override
    protected LaunchingConnector underlying() {
        return (LaunchingConnector) super.underlying();
    }

    private static LaunchingConnector makePlatformConnector() {
        Class connectorClass = null;
        try {
            connectorClass = Class.forName("com.sun.tools.jdi.SunCommandLineLauncher");
        } catch (ClassNotFoundException cnfe) {
        }
        if (connectorClass == null) {
            throw new RuntimeException("can not load platform connector class");
        }
        try {
            return (LaunchingConnector) connectorClass.newInstance();
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
}
