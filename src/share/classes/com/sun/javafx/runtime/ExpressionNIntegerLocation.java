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

/**
 *
 * @author Robert Field
 */
public class ExpressionNIntegerLocation extends ExpressionIntegerLocation {

    public final Location[] args;
    
    ExpressionNIntegerLocation(int value, Context context, int exprNum,
            Location[] args) {
        super(value, context, exprNum);
        this.args = args;
    }
    
    public static ExpressionNIntegerLocation make(Context context, int exprNum,
            Location[] args) {
        ExpressionNIntegerLocation loc = new ExpressionNIntegerLocation(0, context, exprNum, args);
        for (Location arg : args) {
            arg.addWeakChangeListener(loc, null);
        }
        loc.changed(null);
        return loc;
    }

    public void changed(Object key) {
        context.applyN(this, exprNum, args);
    }
}
