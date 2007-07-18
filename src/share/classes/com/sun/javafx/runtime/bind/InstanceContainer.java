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

package com.sun.javafx.runtime.bind;

/**
 * Container
 *
 * @author Brian Goetz
 */
public class InstanceContainer extends AbstractContainer implements Container {
    protected final Object instance;

    public InstanceContainer(String name, Object instance) {
        super(name);
        this.instance = instance;
    }

    protected void doSetInt(LocationKey key, int value) {
        assert key instanceof FieldLocationKey : "Key must be a FieldLocationKey";
        key.setInt(instance, value);
    }

    protected void doSetDouble(LocationKey key, double value) {
        assert key instanceof FieldLocationKey : "Key must be a FieldLocationKey";
        key.setDouble(instance, value);
    }

    protected void doSetReference(LocationKey key, Object value) {
        assert key instanceof FieldLocationKey : "Key must be a FieldLocationKey";
        key.setReference(instance, value);
    }

    protected int doGetInt(LocationKey key) {
        assert key instanceof FieldLocationKey : "Key must be a FieldLocationKey";
        return key.getInt(instance);
    }

    protected double doGetDouble(LocationKey key) {
        assert key instanceof FieldLocationKey : "Key must be a FieldLocationKey";
        return key.getDouble(instance);
    }

    protected Object doGetReference(LocationKey key) {
        assert key instanceof FieldLocationKey : "Key must be a FieldLocationKey";
        return key.getReference(instance);
    }

    protected void doUpdate(LocationKey key, BindingClosure closure) {
        assert key instanceof FieldLocationKey : "Key must be a FieldLocationKey";
        key.update(instance, closure);
    }
}
