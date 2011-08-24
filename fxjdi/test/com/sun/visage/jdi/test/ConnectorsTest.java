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

package org.visage.jdi.test;

import org.visage.jdi.*;
import org.visage.jdi.connect.VisageLaunchingConnector;
import org.visage.jdi.connect.VisageProcessAttachingConnector;
import org.visage.jdi.connect.VisageRawLaunchingConnector;
import org.visage.jdi.connect.VisageSharedMemoryAttachingConnector;
import org.visage.jdi.connect.VisageSharedMemoryListeningConnector;
import org.visage.jdi.connect.VisageSocketAttachingConnector;
import org.visage.jdi.connect.VisageSocketListeningConnector;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.ListeningConnector;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Checks for Visage-JDI connector classes and VisageBootstrap.
 *
 * @author sundar
 */
public class ConnectorsTest {
    @Test
    public void testFXConnectors() {
        LaunchingConnector conn = VisageBootstrap.virtualMachineManager().defaultConnector();
        Assert.assertEquals("org.visage.jdi.connect.VisageLaunchingConnector", conn.name());
        
        VisageLaunchingConnector conn1 = new VisageLaunchingConnector();
        Assert.assertEquals("org.visage.jdi.connect.VisageLaunchingConnector", conn1.name());
        Assert.assertEquals(true, conn1 instanceof LaunchingConnector);

        VisageProcessAttachingConnector conn2 = new VisageProcessAttachingConnector();
        Assert.assertEquals("org.visage.jdi.connect.VisageProcessAttachingConnector", conn2.name());
        Assert.assertEquals(true, conn2 instanceof AttachingConnector);

        VisageRawLaunchingConnector conn3 = new VisageRawLaunchingConnector();
        Assert.assertEquals("org.visage.jdi.connect.VisageRawLaunchingConnector", conn3.name());
        Assert.assertEquals(true, conn3 instanceof LaunchingConnector);

        VisageSocketAttachingConnector conn4 = new VisageSocketAttachingConnector();
        Assert.assertEquals("org.visage.jdi.connect.VisageSocketAttachingConnector", conn4.name());
        Assert.assertEquals(true, conn4 instanceof AttachingConnector);

        VisageSocketListeningConnector conn5 = new VisageSocketListeningConnector();
        Assert.assertEquals("org.visage.jdi.connect.VisageSocketListeningConnector", conn5.name());
        Assert.assertEquals(true, conn5 instanceof ListeningConnector);

        // Conditionally adding Visage shared mem connectors - because underlying platform shared
        // memory connectors are not available on all platforms
        if (VisageSharedMemoryAttachingConnector.isAvailable()) {
            VisageSharedMemoryAttachingConnector conn6 = new VisageSharedMemoryAttachingConnector();
            Assert.assertEquals("org.visage.jdi.connect.VisageSharedMemoryAttachingConnector", conn6.name());
            Assert.assertEquals(true, conn6 instanceof AttachingConnector);
        }

        if (VisageSharedMemoryListeningConnector.isAvailable()) {
            VisageSharedMemoryListeningConnector conn7 = new VisageSharedMemoryListeningConnector();
            Assert.assertEquals("org.visage.jdi.connect.VisageSharedMemoryListeningConnector", conn7.name());
            Assert.assertEquals(true, conn7 instanceof ListeningConnector);
        }
    }
}