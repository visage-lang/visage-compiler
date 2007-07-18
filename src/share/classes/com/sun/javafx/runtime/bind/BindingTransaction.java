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

import java.util.List;
import java.util.ArrayList;

/**
 * BindingTransaction
 *
 * @author Brian Goetz
 */
public class BindingTransaction {
    private static BindingTransaction currentTransaction;

    /**
     * List of variables to be recalculated at the end of the transaction
     */
    private List<Affected> toRecalculate = new ArrayList<Affected>();

    private void add(Container container, LocationKey key) {
        for (Affected a : toRecalculate)
            if (a.container == container && a.key == key)
                return;
        toRecalculate.add(new Affected(container, key));
    }

    public static void begin() {
        if (currentTransaction != null)
            throw new IllegalStateException("Cannot nest transactions");
        currentTransaction = new BindingTransaction();
    }

    public static void end() {
        if (currentTransaction == null)
            throw new IllegalStateException("No running transaction");
        for (Affected a : currentTransaction.toRecalculate)
            a.container.recalculate(a.key);
        currentTransaction = null;
    }

    public static void addAffected(Container c, LocationKey key) {
        if (currentTransaction == null)
            throw new IllegalStateException("No running transaction");
        currentTransaction.add(c, key);
    }

    private static class Affected {
        Container container;
        LocationKey key;

        Affected(Container container, LocationKey key) {
            this.container = container;
            this.key = key;
        }
    }
}
