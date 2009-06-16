/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime.liveconnect;

import java.util.*;

import com.sun.java.browser.plugin2.liveconnect.v1.*;
import javafx.reflect.*;

public class FXClassDelegate extends FXTypeDelegate {
    public FXClassDelegate(FXClassType clazz, Bridge bridge) {
        this.clazz = clazz;
        this.bridge = bridge;
    }

    public boolean invoke(String methodName,
                          Object receiver,
                          Object[] arguments,
                          boolean isStatic,
                          boolean objectIsApplet,
                          Result[] result) throws Exception {
        if (functionMap == null)
            collectFunctions();

        FunctionBundle bundle = functionMap.get(methodName);
        if (bundle == null) {
            // Try again with the lower-case / case-insensitive version
            bundle = lowerCaseFunctionMap.get(methodName.toLowerCase());
        }
        if (bundle == null) {
            throw new NoSuchMethodException(methodName + " in class: " + clazz.getName());
        }
        result[0] = bundle.invoke(receiver, arguments);
        return true;
    }

    public boolean getField(String fieldName,
                            Object receiver,
                            boolean isStatic,
                            boolean objectIsApplet,
                            Result[] result) throws Exception {
        FXValue val = getField0(fieldName, receiver, isStatic, objectIsApplet);
        if (val != null) {
            // FIXME: figure out skipUnboxing flag
            result[0] = new Result(unbox(val), false);
        }
        return true;
    }

    private FXValue getField0(String fieldName,
                              Object receiver,
                              boolean isStatic,
                              boolean objectIsApplet) throws Exception {
        if (varMap == null) {
            collectVariables();
        }
        FXVarMember var = varMap.get(fieldName);
        if (var == null) {
            // Try again with the lower-case / case-insensitive version
            var = lowerCaseVarMap.get(fieldName);
        }
        if (var == null) {
            throw new NoSuchFieldException(fieldName);
        }
        return var.getValue((FXObjectValue) receiver);
    }


    public boolean setField(String fieldName,
                            Object receiver,
                            Object value,
                            boolean isStatic,
                            boolean objectIsApplet) throws Exception {
        setField0(fieldName, (FXObjectValue) receiver, value, isStatic, objectIsApplet);
        return true;
    }

    private void setField0(String fieldName,
                           FXObjectValue receiver,
                           Object value,
                           boolean isStatic,
                           boolean objectIsApplet) throws Exception {
        if (varMap == null) {
            collectVariables();
        }
        FXVarMember var = varMap.get(fieldName);
        if (var == null) {
            // Try again with the lower-case / case-insensitive version
            var = lowerCaseVarMap.get(fieldName);
        }
        if (var == null) {
            throw new NoSuchFieldException(fieldName);
        }
        var.setValue(receiver, (FXValue) bridge.convert(value, var.getType()));
    }

    public boolean hasField(String fieldName,
                            Object receiver,
                            boolean isStatic,
                            boolean objectIsApplet,
                            boolean[] result) {
        result[0] = hasField0(fieldName, (FXValue) receiver, objectIsApplet);
        return true;
    }

    private boolean hasField0(String fieldName,
                              FXValue receiver,
                              boolean objectIsApplet) {
        if (varMap == null) {
            collectVariables();
        }
        FXVarMember var = varMap.get(fieldName);
        if (var == null) {
            // Try again with the lower-case / case-insensitive version
            var = lowerCaseVarMap.get(fieldName);
        }
        return (var != null);
    }

    public boolean hasMethod(String methodName,
                             Object receiver,
                             boolean isStatic,
                             boolean objectIsApplet,
                             boolean[] result) {
        result[0] = hasMethod0(methodName, (FXObjectValue) receiver, objectIsApplet);
        return true;
    }

    private boolean hasMethod0(String methodName,
                               FXObjectValue receiver,
                               boolean objectIsApplet) {
        if (functionMap == null) {
            collectFunctions();
        }

        FunctionBundle bundle = (FunctionBundle) functionMap.get(methodName);
        if (bundle != null) {
            return true;
        }
        // Try again with the lower-case / case-insensitive version
        bundle = (FunctionBundle) lowerCaseFunctionMap.get(methodName.toLowerCase());
        return (bundle != null);
    }

    public boolean hasFieldOrMethod(String name,
                                    Object receiver,
                                    boolean isStatic,
                                    boolean objectIsApplet,
                                    boolean[] result) {
        boolean res = (hasField0(name, (FXObjectValue) receiver, objectIsApplet) ||
                       hasMethod0(name, (FXObjectValue) receiver, objectIsApplet));
        result[0] = res;
        return true;
    }

    public Object findClass(String name) {
        // FIXME
        return null;
    }

    public Object newInstance(Object clazz,
                              Object[] arguments) throws Exception {
        // FIXME
        return null;
    }

    //----------------------------------------------------------------------
    // Internals only below this point
    //

    private FXClassType clazz;
    private Bridge bridge;

    // Map of the names of visible variables to the variables themselves
    private Map<String,FXVarMember> varMap;
    // Map of the lower-case names of visible variables to the variables themselves
    private Map<String,FXVarMember> lowerCaseVarMap;
    // Map of the names of visible functions to the FunctionBundles they correspond to
    private Map<String,FunctionBundle> functionMap;
    // Lower-case version of the map above
    private Map<String,FunctionBundle> lowerCaseFunctionMap;

    private void collectVariables() {
        List<FXVarMember> vars = clazz.getVariables(true);
        Map<String,FXVarMember> varMap = new HashMap<String,FXVarMember>();
        Map<String,FXVarMember> lowerCaseVarMap = new HashMap<String,FXVarMember>();
        for (FXVarMember var : vars) {
            varMap.put(var.getName(), var);
            // Lower-case / case-insensitive version as well
            lowerCaseVarMap.put(var.getName().toLowerCase(), var);
        }
        this.varMap = varMap;
        this.lowerCaseVarMap = lowerCaseVarMap;
    }

    private class FunctionInfo {
        private FXFunctionMember function;
        private FXType[] argumentTypes;
        private boolean returnsVoid;

        public FunctionInfo(FXFunctionMember function) {
            this.function = function;
            // Query the argument types
            FXFunctionType type = function.getType();
            argumentTypes = new FXType[type.minArgs()];
            for (int i = 0; i < argumentTypes.length; i++) {
                argumentTypes[i] = type.getArgumentType(i);
            }
            FXType retType = getReturnType();
            if (retType.equals(voidType)) {
                returnsVoid = true;
            }
        }

        // We override equals() to filter out duplicate methods that
        // come to us through different points of the inheritance
        // hierarchy (i.e., an abstract base class as well as an
        // interface)
        public boolean equals(Object o) {
            if (o == null || (o.getClass() != getClass())) {
                return false;
            }
            FunctionInfo self = this;
            FunctionInfo other = (FunctionInfo) o;
            // We consider ourselves equal if the name, return type
            // and parameters match, ignoring the declaring class
            return (self.getName().equals(other.getName()) &&
                    self.getReturnType().equals(other.getReturnType()) &&
                    arraysEqual(self.getArgumentTypes(),
                                other.getArgumentTypes()));
        }

        private boolean arraysEqual(FXType[] params1,
                                    FXType[] params2) {
            if ((params1 == null) != (params2 == null)) {
                return false;
            }
            if (params1 == null) {
                return true;
            }
            if (params1.length != params2.length) {
                return false;
            }
            for (int i = 0; i < params1.length; i++) {
                if (!params1[i].equals(params2[i])) {
                    return false;
                }
            }
            return true;
        }

        public FXFunctionMember getFunction() { return function; }

        public    String     getName()          { return getFunction().getName(); }
        public    FXType[]   getArgumentTypes() { return argumentTypes;           }

        // This might do either an invoke() or a newInstance() operation;
        // in the case of newInstance(), the target is ignored and may be null
        public Object invoke(Object target, Object[] args) throws Exception {
            Object res = function.invoke((FXObjectValue) target, (FXValue[]) args);
            // Return Void.TYPE for methods returning void to
            // disambiguate null and void return values to the caller
            if (res == null && returnsVoid)
                return Void.TYPE;
            return res;
        }

        // This returns the return type for a Method or the declaring
        // class for a Constructor
        public FXType getReturnType() {
            return function.getType().getReturnType();
        }

        public String toString() {
            return function.toString();
        }
    }

    // Represents a set of overloaded functions
    private class FunctionBundle {
        protected List<FunctionInfo> functions = new ArrayList<FunctionInfo>();

        public void add(FXFunctionMember function) {
            FunctionInfo info = new FunctionInfo(function);
            // Filter out duplicate methods early
            if (!functions.contains(info)) {
                functions.add(info);
            }
        }

        public Result invoke(Object target, Object[] arguments) throws Exception {
            FunctionInfo chosenInfo = null;
            FunctionInfo ambiguousInfo = null;
            FXType[] chosenParameterTypes = null;
            int minNumConversions = 0;
            boolean ambiguous = false;

            for (Iterator iter = functions.iterator(); iter.hasNext(); ) {
                FunctionInfo info = (FunctionInfo) iter.next();
                FXType[] parameterTypes = info.getArgumentTypes();
                if (arguments == null) {
                    if (parameterTypes.length != 0)
                        continue;
                } else if (parameterTypes.length != arguments.length)
                    continue;

                // If this contains a negative number after analysis,
                // the argument lists aren't compatible
                int numConversions = 0;
                for (int i = 0; i < parameterTypes.length; i++) {
                    Object arg = arguments[i];
                    FXType expectedType = parameterTypes[i];
                    int cost = bridge.conversionCost(arg, expectedType);
                    if (cost < 0) {
                        numConversions = -1;
                        break;
                    }
                    numConversions += cost;
                }

                if (numConversions >= 0) {
                    if (chosenInfo == null ||
                        (numConversions < minNumConversions)) {
                        chosenInfo = info;
                        chosenParameterTypes = info.getArgumentTypes();
                        minNumConversions = numConversions;
                        ambiguous = false;
                    } else if (numConversions == minNumConversions) {
                        ambiguous = true;
                        ambiguousInfo = info;
                    }
                }
            }

            if (chosenInfo == null) {
                throw new IllegalArgumentException("No method found matching name " +
                                                   ((FunctionInfo) functions.get(0)).getName() +
                                                   " and arguments " + argsToString(arguments));
            }

            if (ambiguous) {
                throw new IllegalArgumentException("More than one method matching name " +
                                                   ((FunctionInfo) functions.get(0)).getName() +
                                                   " and arguments " + argsToString(arguments) +
                                                   "\n  Method 1: " + chosenInfo.getFunction().toString() +
                                                   "\n  Method 2: " + ambiguousInfo.getFunction().toString());
            }
            
            // Convert all arguments
            FXValue[] newArgs = null;
            if (arguments != null) {
                newArgs = new FXValue[arguments.length];
                for (int i = 0; i < arguments.length; i++) {
                    newArgs[i] = (FXValue) bridge.convert(arguments[i], chosenParameterTypes[i]);
                }
            } else {
                // FX reflection mechanism doesn't like null argument array
                newArgs = new FXValue[0];
            }
            Object ret = chosenInfo.invoke(target, newArgs);
            // Convert certain FXValues back to Java values (primitives, Strings)
            return new Result(unbox(ret), false);
        }
    }

    private void collectFunctions() {
        List<FXFunctionMember> funcs = clazz.getFunctions(true);
        Map<String,FunctionBundle> funcMap = new HashMap<String,FunctionBundle>();
        Map<String,FunctionBundle> lowerCaseFuncMap = new HashMap<String,FunctionBundle>();
        for (FXFunctionMember func : funcs) {
            FunctionBundle bundle = funcMap.get(func.getName());
            if (bundle == null) {
                bundle = new FunctionBundle();
                funcMap.put(func.getName(), bundle);
            }
            bundle.add(func);

            // Lower-case / case-insensitive version as well (note
            // that the MemberBundle might contain different or more
            // entries than the case-sensitive one)
            String lowerCaseName = func.getName().toLowerCase();
            bundle = lowerCaseFuncMap.get(lowerCaseName);
            if (bundle == null) {
                bundle = new FunctionBundle();
                lowerCaseFuncMap.put(lowerCaseName, bundle);
            }
            bundle.add(func);
        }
        this.functionMap = funcMap;
        this.lowerCaseFunctionMap = lowerCaseFuncMap;
    }

    private static String argsToString(Object[] arguments) {
        StringBuffer buf = new StringBuffer("[");
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                if (i > 0)
                    buf.append(", ");
                Object arg = arguments[i];
                String className = null;
                if (arg != null)
                    className = arg.getClass().getName();
                buf.append(className);
            }
        }
        buf.append("]");
        return buf.toString();
    }
}
