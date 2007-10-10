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

package com.sun.javafx.runtime.location;

import java.util.Collection;

/**
 * Wrapper class that creates an ObjectLocation<Integer> view of an IntLocation
 *
 * @author Brian Goetz
 */
class IntObjectLocation implements ObjectLocation<Integer>, ViewLocation {
    private final IntLocation location;

    public IntObjectLocation(IntLocation location) {
        this.location = location;
    }

    public Integer get() {
        return location.get();
    }

    public Integer set(Integer value) {
        return location.set(value);
    }

    public boolean isValid() {
        return location.isValid();
    }

    public boolean isLazy() {
        return location.isLazy();
    }

    public void invalidate() {
        location.invalidate();
    }

    public void update() {
        location.update();
    }

    public void addChangeListener(ChangeListener listener) {
        location.addChangeListener(listener);
    }

    public void addWeakListener(ChangeListener listener) {
        location.addWeakListener(listener);
    }

    public ChangeListener getWeakChangeListener() {
        return location.getWeakChangeListener();
    }

    public Collection<ChangeListener> getListeners() {
        return location.getListeners();
    }

    public Location getUnderlyingLocation() {
        return location;
    }
    
    public void valueChanged() {
        location.valueChanged();
    }
}

class IntObjectMutableLocation extends IntObjectLocation implements MutableLocation {
    public IntObjectMutableLocation(IntLocation location) {
        super(location);
    }
}
