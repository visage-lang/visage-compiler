/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

/**
 * BoundIntSelectExpression
 *
 * @author Robert Field
 */
public abstract class BoundIntSelectExpression<U> extends IndirectIntExpression implements IntLocation {
    private final ObjectLocation<U> selector;

    public BoundIntSelectExpression(ObjectLocation<U> selector) {
        super(false /*lazy*/, selector);
        this.selector = selector;
    }

    protected abstract IntLocation computeSelect(U selectorValue);

    protected IntLocation computeLocation() {
        clearDynamicDependencies();
        U selectorValue = selector.get();
        if (selectorValue == null) {
            return IntConstant.make(DEFAULT);
        } else {
            IntLocation result = computeSelect(selectorValue);
            addDynamicDependency(result);
            return result;
        }
    }

    public int setAsInt(int value) {
        return helper.get().setAsInt(value);
    }

    public void setDefault() {
        helper.get().setDefault();
    }

    public Integer set(Integer value) {
        return helper.get().set(value);
    }
}
