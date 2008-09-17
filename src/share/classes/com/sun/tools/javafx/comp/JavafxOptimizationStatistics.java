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
import com.sun.tools.javac.util.Log;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.util.MsgSym;

/**
 * Collect and print statistics on optimization.
 * 
 * @author Robert Field
 */
public class JavafxOptimizationStatistics {
    
    private final Log log;

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
    
    private int proxyMethodCount;
    private int concreteFieldCount;
    
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

        log = Log.instance(context);

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
        
        proxyMethodCount = 0;
        concreteFieldCount = 0;

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

    public void recordProxyMethod() {
        ++proxyMethodCount;
    }

    public void recordConcreteField() {
        ++concreteFieldCount;
    }
    
    private void show(String label, int value) {
        log.note(MsgSym.MESSAGE_JAVAFX_OPTIMIZATION_STATISTIC, label, value);
    }
    
    private void printInstanceVariableData() {
        int instanceVariableLocationCount = instanceVarLocationCount + instanceDefLocationCount;
        int instanceVariableDirectCount = instanceVarDirectCount + instanceDefDirectCount;
        int instanceVariableCount = instanceVariableLocationCount + instanceVariableDirectCount;

        show("Instance variable count", instanceVariableCount);
        show("Instance variable Location count", instanceVariableLocationCount);
        show("Instance variable direct count", instanceVariableDirectCount);
        
        show("Instance 'var' count", (instanceVarLocationCount + instanceVarDirectCount));
        show("Instance 'var' Location count", instanceVarLocationCount);
        show("Instance 'var' direct count", instanceVarDirectCount);
        
        show("Instance 'def' count", (instanceDefLocationCount + instanceDefDirectCount));
        show("Instance 'def' Location count", instanceDefLocationCount);
        show("Instance 'def' direct count", instanceDefDirectCount);
    }
    
    private void printScriptVariableData() {
        int scriptVariableLocationCount = scriptVarLocationCount + scriptDefLocationCount;
        int scriptVariableDirectCount = scriptVarDirectCount + scriptDefDirectCount;
        int scriptVariableCount = scriptVariableLocationCount + scriptVariableDirectCount;

        show("Script variable count", scriptVariableCount);
        show("Script variable Location count", scriptVariableLocationCount);
        show("Script variable direct count", scriptVariableDirectCount);
        
        show("Script 'var' count", (scriptVarLocationCount + scriptVarDirectCount));
        show("Script 'var' Location count", scriptVarLocationCount);
        show("Script 'var' direct count", scriptVarDirectCount);
        
        show("Script 'def' count", (scriptDefLocationCount + scriptDefDirectCount));
        show("Script 'def' Location count", scriptDefLocationCount);
        show("Script 'def' direct count", scriptDefDirectCount);
    }
    
    private void printLocalVariableData() {
        int localBoundVariableLocationCount = localBoundVarLocationCount + localBoundDefLocationCount;
        int localBoundVariableDirectCount = localBoundVarDirectCount + localBoundDefDirectCount;
        int localBoundVariableCount = localBoundVariableLocationCount + localBoundVariableDirectCount;
        int localBoundVarCount = localBoundVarLocationCount + localBoundVarDirectCount;
        int localBoundDefCount = localBoundDefLocationCount + localBoundDefDirectCount;

        show("Local bound variable count", localBoundVariableCount);
        show("Local bound variable Location count", localBoundVariableLocationCount);
        show("Local bound variable direct count", localBoundVariableDirectCount);
        
        show("Local bound 'var' count", localBoundVarCount);
        show("Local bound 'var' Location count", localBoundVarLocationCount);
        show("Local bound 'var' direct count", localBoundVarDirectCount);
        
        show("Local bound 'def' count", localBoundDefCount);
        show("Local bound 'def' Location count", localBoundDefLocationCount);
        show("Local bound 'def' direct count", localBoundDefDirectCount);
        
        int localUnboundVariableLocationCount = localUnboundVarLocationCount + localUnboundDefLocationCount;
        int localUnboundVariableDirectCount = localUnboundVarDirectCount + localUnboundDefDirectCount;
        int localUnboundVariableCount = localUnboundVariableLocationCount + localUnboundVariableDirectCount;
        int localUnboundVarCount = localUnboundVarLocationCount + localUnboundVarDirectCount;
        int localUnboundDefCount = localUnboundDefLocationCount + localUnboundDefDirectCount;

        show("Local unbound variable count", localUnboundVariableCount);
        show("Local unbound variable Location count", localUnboundVariableLocationCount);
        show("Local unbound variable direct count", localUnboundVariableDirectCount);
        
        show("Local unbound 'var' count", localUnboundVarCount);
        show("Local unbound 'var' Location count", localUnboundVarLocationCount);
        show("Local unbound 'var' direct count", localUnboundVarDirectCount);
        
        show("Local unbound 'def' count", localUnboundDefCount);
        show("Local unbound 'def' Location count", localUnboundDefLocationCount);
        show("Local unbound 'def' direct count", localUnboundDefDirectCount);
        
        int localVariableLocationCount = localBoundVariableLocationCount + localUnboundVariableLocationCount;
        int localVariableDirectCount = localBoundVariableDirectCount + localUnboundVariableDirectCount;
        int localVariableCount = localVariableLocationCount + localVariableDirectCount;
        int localVarCount = localUnboundVarCount + localBoundVarCount;
        int localDefCount = localUnboundDefCount + localBoundDefCount;
        int localVarLocationCount = localBoundVarLocationCount + localUnboundVarLocationCount;
        int localVarDirectCount = localBoundVarDirectCount + localUnboundVarDirectCount;
        int localDefLocationCount = localBoundDefLocationCount + localUnboundDefLocationCount;
        int localDefDirectCount = localBoundDefDirectCount + localUnboundDefDirectCount;

        show("Local variable count", localVariableCount);
        show("Local variable Location count", localVariableLocationCount);
        show("Local variable direct count", localVariableDirectCount);
        
        show("Local 'var' count", localVarCount);
        show("Local 'var' Location count", localVarLocationCount);
        show("Local 'var' direct count", localVarDirectCount);
        
        show("Local 'def' count", localDefCount);
        show("Local 'def' Location count", localDefLocationCount);
        show("Local 'def' direct count", localDefDirectCount);
    }
    
    private void printProxyMethodData() {
        show("Proxy method count", proxyMethodCount);
    }
    
    private void printConcreteFieldData() {
        show("Concrete field count", concreteFieldCount);
    }
    
    public void printData(String which) {
        if (which.contains("i")) {
            printInstanceVariableData();
        }
        if (which.contains("s")) {
            printScriptVariableData();
        }
        if (which.contains("l")) {
            printLocalVariableData();
        }
        if (which.contains("m")) {
            printProxyMethodData();
        }
        if (which.contains("f")) {
            printConcreteFieldData();
        }
    }
 }
