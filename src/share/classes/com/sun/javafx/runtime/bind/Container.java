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
public interface Container {
    /**
     * Fetch the current value of the specified variable, recomputing if necessary (as for lazily bound variables)
     */
    public int getIntValue(LocationKey key);
    public double getDoubleValue(LocationKey key);
    public Object getReferenceValue(LocationKey key);

    /**
     * Modify the current value of the specified variable, invalidating or recomputing dependent variables if
     * necessary
     */
    public void setIntValue(LocationKey key, int value);
    public void setDoubleValue(LocationKey key, double value);
    public void setReferenceValue(LocationKey key, Object value);

    /**
     * Bind a variable to a closure.
     *
     * @param key     The key of the variable being bound
     * @param lazy    Whether or not the variable should be lazily recomputed
     * @param closure The closure to invoke when the variable needs to be evaluated
     */
    public void setBinding(LocationKey key, boolean lazy, BindingClosure closure);

    /**
     * Query whether the specified variable currently has a value (lazily bound variables that have been invalidated
     * may not have a value.)
     */
    public boolean isValid(LocationKey key);

    /**
     * Invalidate the specified variable, invalidating any variables dependent on it as well
     */
    public void invalidate(LocationKey key);

    /**
     * Recalculate the specified (bound) variable
     */
    public void recalculate(LocationKey key);

    /**
     * Are there any other variables dependent on the specified variable?
     */
    public boolean hasDependencies(LocationKey key);

    /**
     * Express a dependency on one of this container's variables.  When the local variable is modified,
     * the remote container is notified to invalidate and potentially recalculate the remote variable.
     *
     * @param localKey        The key of the variable in this container on which a dependency exists
     * @param remoteContainer The container of the variable to invalidate when the local variable is changed
     * @param remoteKey       The key of the remote variable to invalidate when the local variable is changed
     */
    public void addDependency(LocationKey localKey, Container remoteContainer, LocationKey remoteKey);

    void setBidirectionalBinding(LocationKey key, Container remoteContainer, LocationKey remoteKey);
}
