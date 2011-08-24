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

package org.visage.runtime.liveconnect;

import java.util.*;

import com.sun.java.browser.plugin2.liveconnect.v1.*;
import visage.reflect.*;

public class VisageDelegate implements InvocationDelegate {
    public VisageDelegate(Bridge bridge, String scriptClassName) {
        this.bridge = bridge;
        this.scriptClassName = scriptClassName;
    }

    public boolean invoke(String methodName,
                          Object receiver,
                          Object[] arguments,
                          boolean isStatic,
                          boolean objectIsApplet,
                          Result[] result) throws Exception {
        Object[] box = new Object[] { receiver };
        InvocationDelegate delegate = getDelegate(box, isStatic, objectIsApplet);
        if (delegate == null)
            return false;
        return delegate.invoke(methodName, (isStatic ? null : box[0]), arguments, isStatic, objectIsApplet, result);
    }

    public boolean getField(String fieldName,
                            Object receiver,
                            boolean isStatic,
                            boolean objectIsApplet,
                            Result[] result) throws Exception {
        if (objectIsApplet) {
            if (fieldName.equalsIgnoreCase("script")) {
                result[0] = new Result(new JavaNameSpace(scriptClassName), false);
                return true;
            }
        }

        Object[] box = new Object[] { receiver };
        InvocationDelegate delegate = getDelegate(box, isStatic, objectIsApplet);
        if (delegate == null)
            return false;
        return delegate.getField(fieldName, (isStatic ? null : box[0]), isStatic, objectIsApplet, result);
    }

    public boolean setField(String fieldName,
                            Object receiver,
                            Object value,
                            boolean isStatic,
                            boolean objectIsApplet) throws Exception {
        Object[] box = new Object[] { receiver };
        InvocationDelegate delegate = getDelegate(box, isStatic, objectIsApplet);
        if (delegate == null)
            return false;
        return delegate.setField(fieldName, (isStatic ? null : box[0]), value, isStatic, objectIsApplet);
    }

    public boolean hasField(String fieldName,
                            Object receiver,
                            boolean isStatic,
                            boolean objectIsApplet,
                            boolean[] result) {
        if (objectIsApplet) {
            if (fieldName.equalsIgnoreCase("script")) {
                result[0] = true;
                return true;
            }
        }        

        Object[] box = new Object[] { receiver };
        InvocationDelegate delegate = getDelegate(box, isStatic, objectIsApplet);
        if (delegate == null)
            return false;
        return delegate.hasField(fieldName, (isStatic ? null : box[0]), isStatic, objectIsApplet, result);
    }

    public boolean hasMethod(String methodName,
                             Object receiver,
                             boolean isStatic,
                             boolean objectIsApplet,
                             boolean[] result) {
        Object[] box = new Object[] { receiver };
        InvocationDelegate delegate = getDelegate(box, isStatic, objectIsApplet);
        if (delegate == null) {
            return false;
        }
        return delegate.hasMethod(methodName, (isStatic ? null : box[0]), isStatic, objectIsApplet, result);
    }

    public boolean hasFieldOrMethod(String name,
                                    Object receiver,
                                    boolean isStatic,
                                    boolean objectIsApplet,
                                    boolean[] result) {
        if (objectIsApplet) {
            if (name.equalsIgnoreCase("script")) {
                result[0] = true;
                return true;
            }
        }
        Object[] box = new Object[] { receiver };
        InvocationDelegate delegate = getDelegate(box, isStatic, objectIsApplet);
        if (delegate == null)
            return false;
        return delegate.hasFieldOrMethod(name, (isStatic ? null : box[0]), isStatic, objectIsApplet, result);
    }

    public Object findClass(String name) {
        if (notFXClasses.contains(name)) {
            return null;
        }

        try {
            VisageClassType clazz = context.findClass(name);
            if (clazz != null && clazz.isJfxType()) {
                return clazz;
            }
        } catch (Throwable t) {
        }
        synchronized(this) {
            notFXClasses.add(name);
        }
        return null;
    }

    public Object newInstance(Object clazz,
                              Object[] arguments) throws Exception {
        // FIXME
        throw new UnsupportedOperationException("Instantiation of Visage classes not yet supported");
    }

    //----------------------------------------------------------------------
    // Internals only below this point
    //

    private Bridge bridge;
    private String scriptClassName;
    private VisageLocal.Context context = VisageLocal.getContext();
    private Map<VisageClassType, VisageClassDelegate> classDelegates =
        new HashMap<VisageClassType, VisageClassDelegate>();
    private Set<String> notFXClasses = new HashSet<String>();
    // We only need a singleton sequence delegate
    private VisageSequenceDelegate sequenceDelegate;

    private synchronized InvocationDelegate getDelegate(Object[] box, boolean isStatic, boolean objectIsApplet) {
        Object obj = box[0];
        if ((obj instanceof VisageClassType) ||
            (obj instanceof VisageObjectValue)) {
            if (isStatic) {
                return getClassDelegate((VisageClassType) obj);
            } else {
                VisageObjectValue fxObj = (VisageObjectValue) obj;
                return getClassDelegate(fxObj.getClassType());
            }
        } else if (obj instanceof VisageSequenceValue) {
            if (sequenceDelegate == null) {
                sequenceDelegate = new VisageSequenceDelegate(bridge);
            }
            return sequenceDelegate;
        } else if (objectIsApplet) {
            // The incoming applet object comes in as a non-VisageObjectValue
            // but needs to be identified as such; we could do this check
            // for other values as well but we prefer not to due to the cost
            VisageObjectValue fxObj = context.mirrorOf(obj);
            VisageClassType fxClass = fxObj.getClassType();
            if (fxClass.isJfxType()) {
                // Upgrade the receiver to an VisageObjectValue
                box[0] = fxObj;
                return getClassDelegate(fxClass);
            }
        }

        return null;
    }

    private InvocationDelegate getClassDelegate(VisageClassType fxClass) {
        VisageClassDelegate delegate = classDelegates.get(fxClass);
        if (delegate == null) {
            delegate = new VisageClassDelegate(fxClass, bridge);
            classDelegates.put(fxClass, delegate);
        }
        return delegate;
    }

    private VisageClassType scriptClass;
    private VisageClassType getScriptClass(String className) {
        if (scriptClass == null) {
            scriptClass = context.findClass(className);
        }
        return scriptClass;
    }
}
