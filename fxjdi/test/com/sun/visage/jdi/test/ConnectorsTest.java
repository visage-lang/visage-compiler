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

package com.sun.visage.jdi.test;

import com.sun.visage.jdi.*;
import com.sun.visage.jdi.connect.FXLaunchingConnector;
import com.sun.visage.jdi.connect.FXProcessAttachingConnector;
import com.sun.visage.jdi.connect.FXRawLaunchingConnector;
import com.sun.visage.jdi.connect.FXSharedMemoryAttachingConnector;
import com.sun.visage.jdi.connect.FXSharedMemoryListeningConnector;
import com.sun.visage.jdi.connect.FXSocketAttachingConnector;
import com.sun.visage.jdi.connect.FXSocketListeningConnector;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.ListeningConnector;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Checks for FX-JDI connector classes and FXBootstrap.
 *
 * @author sundar
 */
public class ConnectorsTest {
    @Test
    public void testFXConnectors() {
        LaunchingConnector conn = FXBootstrap.virtualMachineManager().defaultConnector();
        Assert.assertEquals("com.sun.visage.jdi.connect.FXLaunchingConnector", conn.name());
        
        FXLaunchingConnector conn1 = new FXLaunchingConnector();
        Assert.assertEquals("com.sun.visage.jdi.connect.FXLaunchingConnector", conn1.name());
        Assert.assertEquals(true, conn1 instanceof LaunchingConnector);

        FXProcessAttachingConnector conn2 = new FXProcessAttachingConnector();
        Assert.assertEquals("com.sun.visage.jdi.connect.FXProcessAttachingConnector", conn2.name());
        Assert.assertEquals(true, conn2 instanceof AttachingConnector);

        FXRawLaunchingConnector conn3 = new FXRawLaunchingConnector();
        Assert.assertEquals("com.sun.visage.jdi.connect.FXRawLaunchingConnector", conn3.name());
        Assert.assertEquals(true, conn3 instanceof LaunchingConnector);

        FXSocketAttachingConnector conn4 = new FXSocketAttachingConnector();
        Assert.assertEquals("com.sun.visage.jdi.connect.FXSocketAttachingConnector", conn4.name());
        Assert.assertEquals(true, conn4 instanceof AttachingConnector);

        FXSocketListeningConnector conn5 = new FXSocketListeningConnector();
        Assert.assertEquals("com.sun.visage.jdi.connect.FXSocketListeningConnector", conn5.name());
        Assert.assertEquals(true, conn5 instanceof ListeningConnector);

        // Conditionally adding FX shared mem connectors - because underlying platform shared
        // memory connectors are not available on all platforms
        if (FXSharedMemoryAttachingConnector.isAvailable()) {
            FXSharedMemoryAttachingConnector conn6 = new FXSharedMemoryAttachingConnector();
            Assert.assertEquals("com.sun.visage.jdi.connect.FXSharedMemoryAttachingConnector", conn6.name());
            Assert.assertEquals(true, conn6 instanceof AttachingConnector);
        }

        if (FXSharedMemoryListeningConnector.isAvailable()) {
            FXSharedMemoryListeningConnector conn7 = new FXSharedMemoryListeningConnector();
            Assert.assertEquals("com.sun.visage.jdi.connect.FXSharedMemoryListeningConnector", conn7.name());
            Assert.assertEquals(true, conn7 instanceof ListeningConnector);
        }
    }
}