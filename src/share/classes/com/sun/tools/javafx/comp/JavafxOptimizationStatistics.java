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
import java.io.PrintWriter;

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
    
    private void printInstanceVariableData(PrintWriter pw) {
        int instanceVariableLocationCount = instanceVarLocationCount + instanceDefLocationCount;
        int instanceVariableDirectCount = instanceVarDirectCount + instanceDefDirectCount;
        int instanceVariableCount = instanceVariableLocationCount + instanceVariableDirectCount;

        pw.println("Instance variable count: " + instanceVariableCount);
        pw.println("Instance variable Location count: " + instanceVariableLocationCount);
        pw.println("Instance variable direct count: " + instanceVariableDirectCount);
        
        pw.println("Instance 'var' count: " + (instanceVarLocationCount + instanceVarDirectCount));
        pw.println("Instance 'var' Location count: " + instanceVarLocationCount);
        pw.println("Instance 'var' direct count: " + instanceVarDirectCount);
        
        pw.println("Instance 'def' count: " + (instanceDefLocationCount + instanceDefDirectCount));
        pw.println("Instance 'def' Location count: " + instanceDefLocationCount);
        pw.println("Instance 'def' direct count: " + instanceDefDirectCount);
    }
    
    private void printScriptVariableData(PrintWriter pw) {
        int scriptVariableLocationCount = scriptVarLocationCount + scriptDefLocationCount;
        int scriptVariableDirectCount = scriptVarDirectCount + scriptDefDirectCount;
        int scriptVariableCount = scriptVariableLocationCount + scriptVariableDirectCount;

        pw.println("Script variable count: " + scriptVariableCount);
        pw.println("Script variable Location count: " + scriptVariableLocationCount);
        pw.println("Script variable direct count: " + scriptVariableDirectCount);
        
        pw.println("Script 'var' count: " + (scriptVarLocationCount + scriptVarDirectCount));
        pw.println("Script 'var' Location count: " + scriptVarLocationCount);
        pw.println("Script 'var' direct count: " + scriptVarDirectCount);
        
        pw.println("Script 'def' count: " + (scriptDefLocationCount + scriptDefDirectCount));
        pw.println("Script 'def' Location count: " + scriptDefLocationCount);
        pw.println("Script 'def' direct count: " + scriptDefDirectCount);
    }
    
    private void printLocalVariableData(PrintWriter pw) {
        int localBoundVariableLocationCount = localBoundVarLocationCount + localBoundDefLocationCount;
        int localBoundVariableDirectCount = localBoundVarDirectCount + localBoundDefDirectCount;
        int localBoundVariableCount = localBoundVariableLocationCount + localBoundVariableDirectCount;
        int localBoundVarCount = localBoundVarLocationCount + localBoundVarDirectCount;
        int localBoundDefCount = localBoundDefLocationCount + localBoundDefDirectCount;

        pw.println("Local bound variable count: " + localBoundVariableCount);
        pw.println("Local bound variable Location count: " + localBoundVariableLocationCount);
        pw.println("Local bound variable direct count: " + localBoundVariableDirectCount);
        
        pw.println("Local bound 'var' count: " + localBoundVarCount);
        pw.println("Local bound 'var' Location count: " + localBoundVarLocationCount);
        pw.println("Local bound 'var' direct count: " + localBoundVarDirectCount);
        
        pw.println("Local bound 'def' count: " + localBoundDefCount);
        pw.println("Local bound 'def' Location count: " + localBoundDefLocationCount);
        pw.println("Local bound 'def' direct count: " + localBoundDefDirectCount);
        
        int localUnboundVariableLocationCount = localUnboundVarLocationCount + localUnboundDefLocationCount;
        int localUnboundVariableDirectCount = localUnboundVarDirectCount + localUnboundDefDirectCount;
        int localUnboundVariableCount = localUnboundVariableLocationCount + localUnboundVariableDirectCount;
        int localUnboundVarCount = localUnboundVarLocationCount + localUnboundVarDirectCount;
        int localUnboundDefCount = localUnboundDefLocationCount + localUnboundDefDirectCount;

        pw.println("Local unbound variable count: " + localUnboundVariableCount);
        pw.println("Local unbound variable Location count: " + localUnboundVariableLocationCount);
        pw.println("Local unbound variable direct count: " + localUnboundVariableDirectCount);
        
        pw.println("Local unbound 'var' count: " + localUnboundVarCount);
        pw.println("Local unbound 'var' Location count: " + localUnboundVarLocationCount);
        pw.println("Local unbound 'var' direct count: " + localUnboundVarDirectCount);
        
        pw.println("Local unbound 'def' count: " + localUnboundDefCount);
        pw.println("Local unbound 'def' Location count: " + localUnboundDefLocationCount);
        pw.println("Local unbound 'def' direct count: " + localUnboundDefDirectCount);
        
        int localVariableLocationCount = localBoundVariableLocationCount + localUnboundVariableLocationCount;
        int localVariableDirectCount = localBoundVariableDirectCount + localUnboundVariableDirectCount;
        int localVariableCount = localVariableLocationCount + localVariableDirectCount;
        int localVarCount = localUnboundVarCount + localBoundVarCount;
        int localDefCount = localUnboundDefCount + localBoundDefCount;
        int localVarLocationCount = localBoundVarLocationCount + localUnboundVarLocationCount;
        int localVarDirectCount = localBoundVarDirectCount + localUnboundVarDirectCount;
        int localDefLocationCount = localBoundDefLocationCount + localUnboundDefLocationCount;
        int localDefDirectCount = localBoundDefDirectCount + localUnboundDefDirectCount;

        pw.println("Local variable count: " + localVariableCount);
        pw.println("Local variable Location count: " + localVariableLocationCount);
        pw.println("Local variable direct count: " + localVariableDirectCount);
        
        pw.println("Local 'var' count: " + localVarCount);
        pw.println("Local 'var' Location count: " + localVarLocationCount);
        pw.println("Local 'var' direct count: " + localVarDirectCount);
        
        pw.println("Local 'def' count: " + localDefCount);
        pw.println("Local 'def' Location count: " + localDefLocationCount);
        pw.println("Local 'def' direct count: " + localDefDirectCount);
    }
    
    private void printProxyMethodData(PrintWriter pw) {
        pw.println("Proxy method count: " + proxyMethodCount);
    }
    
    private void printConcreteFieldData(PrintWriter pw) {
        pw.println("Concrete field count: " + concreteFieldCount);
    }
    
    public void printData(String which, PrintWriter pw) {
        if (which.contains("i")) {
            printInstanceVariableData(pw);
        }
        if (which.contains("s")) {
            printScriptVariableData(pw);
        }
        if (which.contains("l")) {
            printLocalVariableData(pw);
        }
        if (which.contains("m")) {
            printProxyMethodData(pw);
        }
        if (which.contains("f")) {
            printConcreteFieldData(pw);
        }
    }
 }
