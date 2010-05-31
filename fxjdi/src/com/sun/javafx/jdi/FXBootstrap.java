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

package com.sun.javafx.jdi;

import com.sun.javafx.jdi.connect.FXConnector;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.JDIPermission;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.ListeningConnector;
import com.sun.jdi.connect.spi.Connection;
import com.sun.jdi.connect.spi.TransportService;
import com.sun.tools.jdi.GenericAttachingConnector;
import com.sun.tools.jdi.GenericListeningConnector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * A simple factory to create FX-JDI conns (similar to platform provided
 * com.sun.jdi.Bootstrap - except that the default is changed here).
 * 
 * @author sundar
 */
public class FXBootstrap {

    public static VirtualMachineManager virtualMachineManager() {
        return FXVirtualMachineManager.virtualMachineManager();
    }

    private static class FXVirtualMachineManager implements VirtualMachineManager {
        private List<Connector> connectors = new ArrayList<Connector>();
        private LaunchingConnector defaultConnector = null;
        private static final int majorVersion = 1;
        private static final int minorVersion = 6;
        private static final Object lock = new Object();
        private static FXVirtualMachineManager vmm;

        public static VirtualMachineManager virtualMachineManager() {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                JDIPermission vmmPermission =
                        new JDIPermission("virtualMachineManager");
                sm.checkPermission(vmmPermission);
            }
            synchronized (lock) {
                if (vmm == null) {
                    vmm = new FXVirtualMachineManager();
                }
            }
            return vmm;
        }

        protected FXVirtualMachineManager() {
            /*
             * Load the connectors
             */
            ServiceLoader<Connector> connectorLoader =
                    ServiceLoader.load(Connector.class, FXConnector.class.getClassLoader());

            Iterator<Connector> conns = connectorLoader.iterator();

            while (conns.hasNext()) {
                Connector connector;

                try {
                    connector = conns.next();
                } catch (ThreadDeath ex) {
                    throw ex;
                } catch (Exception ex) {
                    System.err.println(ex);
                    continue;
                } catch (Error ex) {
                    System.err.println(ex);
                    continue;
                }

                addConnector(connector);
            }

            /*
             * Load any transport services and encapsulate them with
             * an attaching and listening connector.
             */
            ServiceLoader<TransportService> transportLoader =
                    ServiceLoader.load(TransportService.class,
                    TransportService.class.getClassLoader());

            Iterator<TransportService> transportServices =
                    transportLoader.iterator();

            while (transportServices.hasNext()) {
                TransportService transportService;

                try {
                    transportService = transportServices.next();
                } catch (ThreadDeath ex) {
                    throw ex;
                } catch (Exception ex) {
                    System.err.println(ex);
                    continue;
                } catch (Error ex) {
                    System.err.println(ex);
                    continue;
                }

                addConnector(GenericAttachingConnector.create(transportService));
                addConnector(GenericListeningConnector.create(transportService));
            }

            // no connectors found
            if (allConnectors().size() == 0) {
                throw new Error("no Connectors loaded");
            }

            // default is FX-JDI launching connector
            boolean found = false;
            List<LaunchingConnector> launchers = launchingConnectors();
            for (LaunchingConnector lc : launchers) {
                if (lc.name().equals("com.sun.javafx.jdi.connect.FXLaunchingConnector")) {
                    setDefaultConnector(lc);
                    found = true;
                    break;
                }
            }
            if (!found && launchers.size() > 0) {
                setDefaultConnector(launchers.get(0));
            }

        }

        public LaunchingConnector defaultConnector() {
            if (defaultConnector == null) {
                throw new Error("no default LaunchingConnector");
            }
            return defaultConnector;
        }

        public void setDefaultConnector(LaunchingConnector connector) {
            defaultConnector = connector;
        }

        public List<LaunchingConnector> launchingConnectors() {
            List<LaunchingConnector> launchingConnectors = new ArrayList<LaunchingConnector>(connectors.size());
            for (Connector connector : connectors) {
                if (connector instanceof LaunchingConnector) {
                    launchingConnectors.add((LaunchingConnector) connector);
                }
            }
            return Collections.unmodifiableList(launchingConnectors);
        }

        public List<AttachingConnector> attachingConnectors() {
            List<AttachingConnector> attachingConnectors = new ArrayList<AttachingConnector>(connectors.size());
            for (Connector connector : connectors) {
                if (connector instanceof AttachingConnector) {
                    attachingConnectors.add((AttachingConnector) connector);
                }
            }
            return Collections.unmodifiableList(attachingConnectors);
        }

        public List<ListeningConnector> listeningConnectors() {
            List<ListeningConnector> listeningConnectors = new ArrayList<ListeningConnector>(connectors.size());
            for (Connector connector : connectors) {
                if (connector instanceof ListeningConnector) {
                    listeningConnectors.add((ListeningConnector) connector);
                }
            }
            return Collections.unmodifiableList(listeningConnectors);
        }

        public List<Connector> allConnectors() {
            return Collections.unmodifiableList(connectors);
        }

        public List<VirtualMachine> connectedVirtualMachines() {
            VirtualMachineManager pvmm = Bootstrap.virtualMachineManager();
            return FXWrapper.wrapVirtualMachines(pvmm.connectedVirtualMachines());
        }

        public void addConnector(Connector connector) {
            connectors.add(connector);
        }

        public void removeConnector(Connector connector) {
            connectors.remove(connector);
        }

        public VirtualMachine createVirtualMachine(Connection connection, 
                Process process) throws IOException {
            VirtualMachineManager pvmm = Bootstrap.virtualMachineManager();
            return FXWrapper.wrap(pvmm.createVirtualMachine(connection, process));
        }

        public VirtualMachine createVirtualMachine(Connection connection) 
                throws IOException {
            VirtualMachineManager pvmm = Bootstrap.virtualMachineManager();
            return FXWrapper.wrap(pvmm.createVirtualMachine(connection));
        }

        public int majorInterfaceVersion() {
            return majorVersion;
        }

        public int minorInterfaceVersion() {
            return minorVersion;
        }
    }
}
