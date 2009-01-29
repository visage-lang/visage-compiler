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

package com.sun.javafx.runtime.location;

/**
 * The base class for the per-script class which combines all binding expressions
 *
 * @author Robert Field
 */
public abstract class ScriptBindingExpressions extends BindingExpression {
    protected final int id;
    protected final Object arg$0;
    protected final Object arg$1;
    protected final Object[] moreArgs;
    protected final int dependents;

    public ScriptBindingExpressions(int id, Object arg0, Object arg1, Object[] moreArgs, int dependents) {
        this.id = id;
        this.arg$0 = arg0;
        this.arg$1 = arg1;
        this.moreArgs = moreArgs;
        this.dependents = dependents;
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        if (arg$0 != null && (dependents & 0x1) != 0)  {
            location.addDependency((Location)arg$0);
        }
        if (arg$1 != null && (dependents & 0x2) != 0)  {
            location.addDependency((Location)arg$1);
        }
        if (moreArgs != null && (dependents & ~0x3) != 0) {
            for (int i = 0; i < moreArgs.length; ++i) {
                if ((dependents & (1 << (i + 2))) != 0) {
                    location.addDependency((Location)moreArgs[i]);
                }
            }
        }
    }
}
