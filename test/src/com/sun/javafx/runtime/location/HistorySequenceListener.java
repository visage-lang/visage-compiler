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

import junit.framework.Assert;

import java.util.List;
import java.util.ArrayList;

/**
 * SequenceHistoryListener
 *
 * @author Brian Goetz
 */
public class HistorySequenceListener<T> implements SequenceChangeListener<T> {
    private List<String> elements = new ArrayList<String>();

    public void onInsert(int position, T element) {
        elements.add(String.format("i-%d-%s", position, element.toString()));
    }

    public void onDelete(int position, T element) {
        elements.add(String.format("d-%d-%s", position, element.toString()));
    }

    public void onReplace(int position, T oldValue, T newValue) {
        elements.add(String.format("r-%d-%s-%s", position, oldValue.toString(), newValue.toString()));
    }

    public List<String> get() { return elements; }

    public void clear() { elements.clear(); }

    public boolean onChange() {
        elements.clear();
        return true;
    }
}
