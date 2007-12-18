/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.Location;
import com.sun.javafx.runtime.location.MutableLocation;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for initializing JavaFX instances from object literals.
 *
 * @author Brian Goetz
 */
public class InitHelper {
    private Location[] initOrder;
    private int initIndex;
    private Map<String, Location> initMap = new java.util.concurrent.ConcurrentHashMap<String, Location>();
    private Set<String> literalInitSet = new java.util.HashSet<String>();
    private boolean literalsDone = false;

    public InitHelper(int numFields) {
        this.initOrder = new Location[numFields];
    }

    public void add(Location loc, String name) { 
        if (!literalsDone) {
            literalInitSet.add(name);
        }
        
        if (initMap.get(name) != null) {
            return;
        }
        initMap.put(name, loc);
        initOrder[initIndex++] = loc; 
    }

    public void initialize() {
        for (Location loc : initOrder) {
            if (loc != null) {
                if (loc instanceof MutableLocation)
                    loc.valueChanged();
                else if (!loc.isLazy())
                    loc.update();
            }
        }
        /// Release memory. We don't need this anymore.
        initOrder = null;
        initMap = null;
        literalInitSet = null;
    }

    public void literalsDone$() {
        literalsDone = true;
    }

    public boolean isNotLiteralSet$(String name) {
        return !literalInitSet.contains(name);
    }
}
