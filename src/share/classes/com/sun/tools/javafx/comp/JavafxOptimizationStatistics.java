/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.comp;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javafx.code.JavafxFlags;

/**
 * Collect and print statistics on optimization.
 * 
 * @author Robert Field
 */
public class JavafxOptimizationStatistics {
    
    private int instanceVarLocationCount;
    private int instanceVarDirectCount;
    private int instanceDefLocationCount;
    private int instanceDefDirectCount;

    private int scriptVarLocationCount;
    private int scriptVarDirectCount;
    private int scriptDefLocationCount;
    private int scriptDefDirectCount;

    private int localBoundVarLocationCount;
    private int localBoundVarDirectCount;
    private int localBoundDefLocationCount;
    private int localBoundDefDirectCount;

    private int localUnboundVarLocationCount;
    private int localUnboundVarDirectCount;
    private int localUnboundDefLocationCount;
    private int localUnboundDefDirectCount;
    
    /**
     * Context set-up
     */
    protected static final Context.Key<JavafxOptimizationStatistics> jfxOptStatKey = new Context.Key<JavafxOptimizationStatistics>();

    public static JavafxOptimizationStatistics instance(Context context) {
        JavafxOptimizationStatistics instance = context.get(jfxOptStatKey);
        if (instance == null) {
            instance = new JavafxOptimizationStatistics(context);
        }
        return instance;
    }

    protected JavafxOptimizationStatistics(Context context) {
        context.put(jfxOptStatKey, this);

        instanceVarLocationCount = 0;
        instanceVarDirectCount = 0;
        instanceDefLocationCount = 0;
        instanceDefDirectCount = 0;

        scriptVarLocationCount = 0;
        scriptVarDirectCount = 0;
        scriptDefLocationCount = 0;
        scriptDefDirectCount = 0;

        localBoundVarLocationCount = 0;
        localBoundVarDirectCount = 0;
        localBoundDefLocationCount = 0;
        localBoundDefDirectCount = 0;

        localUnboundVarLocationCount = 0;
        localUnboundVarDirectCount = 0;
        localUnboundDefLocationCount = 0;
        localUnboundDefDirectCount = 0;

    }

    public void recordClassVar(VarSymbol vsym, boolean isLocation) {
        long flags = vsym.flags();
        boolean isDef = (flags & JavafxFlags.IS_DEF) != 0;
        boolean isScript = (flags & Flags.STATIC) != 0;
        if (isLocation) {
            if (isScript) {
                if (isDef) {
                    ++scriptDefLocationCount;
                } else {
                    ++scriptVarLocationCount;
                }
            } else {
                if (isDef) {
                    ++instanceDefLocationCount;
                } else {
                    ++instanceVarLocationCount;
                }
            }
        } else {
            if (isScript) {
                if (isDef) {
                    ++scriptDefDirectCount;
                } else {
                    ++scriptVarDirectCount;
                }
            } else {
                if (isDef) {
                    ++instanceDefDirectCount;
                } else {
                    ++instanceVarDirectCount;
                }
            }
        }
    }

    public void recordLocalVar(VarSymbol vsym, boolean isBound, boolean isLocation) {
        long flags = vsym.flags();
        boolean isDef = (flags & JavafxFlags.IS_DEF) != 0;
        if (isBound) {
            if (isLocation) {
                if (isDef) {
                    ++localBoundDefLocationCount;
                } else {
                    ++localBoundVarLocationCount;
                }
            } else {
                if (isDef) {
                    ++localBoundDefDirectCount;
                } else {
                    ++localBoundVarDirectCount;
                }
            }
        } else {
            if (isLocation) {
                if (isDef) {
                    ++localUnboundDefLocationCount;
                } else {
                    ++localUnboundVarLocationCount;
                }
            } else {
                if (isDef) {
                    ++localUnboundDefDirectCount;
                } else {
                    ++localUnboundVarDirectCount;
                }
            }
        }
    }
}
