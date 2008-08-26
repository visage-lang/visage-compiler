/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime.location;

/**
 * Indicates that a Location can be bound.  Locations can be bound at most once; rebinding is not permitted.
 *
 * @param T_BINDING the type of the XxxBindingExpression that computes the new value.
 *
 * @author Brian Goetz
 */
public interface BindableLocation<T_VALUE, T_BINDING extends AbstractBindingExpression, T_LISTENER>
        extends Location {

    public void bind(boolean lazy, T_BINDING binding, Location... dependencies);

    void bindFromLiteral(boolean lazy, T_BINDING binding, Location... dependencies);

    public void bijectiveBind(ObjectLocation<T_VALUE> other);

    void bijectiveBindFromLiteral(ObjectLocation<T_VALUE> other);

    public boolean isBound();

    boolean isLazy();

    /** Has the variable ever been initialized? */
    boolean isInitialized();

    void addChangeListener(T_LISTENER listener);

    void removeChangeListener(T_LISTENER listener);
}
