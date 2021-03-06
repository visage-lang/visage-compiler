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

package org.visage.jdi.connect;

import org.visage.jdi.VisageWrapper;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author sundar
 */
public class VisageProcessAttachingConnector extends VisageConnector implements AttachingConnector {
    public VisageProcessAttachingConnector() {
        this(makePlatformConnector());
    }

    public VisageProcessAttachingConnector(AttachingConnector underlying) {
        super(underlying);
    }

    public VirtualMachine attach(Map<String, ? extends Argument> args)
            throws IOException, IllegalConnectorArgumentsException {
        return VisageWrapper.wrap(underlying().attach(args));
    }

    @Override
    protected AttachingConnector underlying() {
        return (AttachingConnector) super.underlying();
    }

    private static final String PROCESS_ATTACHING_CONN = "com.sun.tools.jdi.ProcessAttachingConnector";
    private static AttachingConnector makePlatformConnector() {
        Class connectorClass = null;
        try {
            connectorClass = Class.forName(PROCESS_ATTACHING_CONN);
        } catch (ClassNotFoundException cnfe) {
        }
        if (connectorClass == null) {
            throw new RuntimeException("can not load class: " + PROCESS_ATTACHING_CONN);
        }
        try {
            return (AttachingConnector) connectorClass.newInstance();
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
}
