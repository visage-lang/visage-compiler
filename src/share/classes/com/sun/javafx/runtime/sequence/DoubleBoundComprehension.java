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

package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.*;

/**
 *
 * @author Robert Field
 */
public abstract class DoubleBoundComprehension<V> extends AbstractBoundComprehension<Double, V> {

    public DoubleBoundComprehension(Class<V> clazz,
                              SequenceLocation<Double> sequenceLocation,
                              boolean useIndex) {
        super(clazz, sequenceLocation, useIndex);
    }

    protected abstract SequenceLocation<V> getMappedElement$(DoubleLocation elementLocation, IntLocation indexLocation);

    protected State<Double, V> makeState(int index, Double value) {
        DoubleLocation elementLocation = DoubleVariable.make(value.doubleValue());
        SimpleIntVariable indexLocation = useIndex ? new SimpleIntVariable(index) : null;
        SequenceLocation<V> mapped = getMappedElement$(elementLocation, indexLocation);
        return new State<Double, V>(elementLocation, indexLocation, mapped);
    }

}
