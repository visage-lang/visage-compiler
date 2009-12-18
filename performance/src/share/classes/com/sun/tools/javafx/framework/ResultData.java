/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.framework;

/**
 *
 * @author ksrini
 */
public class ResultData {
    String name = null;
    String performance = null;
    String heapsize = null;

    public ResultData(String name, String performance, String heapsize) {
        this.name = name;
        this.performance = performance;
        this.heapsize = heapsize;
    }

    public ResultData(String... flds) {
        this.name = flds[0];
        this.performance = flds[1];
        this.heapsize = flds[2];
    }
    
    public String getName() {
        return name;
    }

    public String getPerformance() {
        return performance;
    }
    
    public String getHeapsize() {
        return heapsize;
    }

    @Override
    public String toString() {
        return String.format(Utils.CSV_FORMAT_STRING, name, performance, heapsize);
    }
}
