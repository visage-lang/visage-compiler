/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.*;

/**
 * SimulatedFXObject
 *
 * @author Brian Goetz
 */
public abstract class FXObjectFactory<T extends FXObject> {
    private final String[] attributes;
    private final Class<T> intf;

    protected FXObjectFactory(Class<T> intf, String[] attributes) {
        this.attributes = attributes;
        this.intf = intf;
        for (String name : attributes) {
            try {
                intf.getMethod("get$" + name);
            }
            catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Cannot find accessor method for attribute " + name);
            }
        }
    }

    public T make() {
        final Map<String, AbstractVariable> locs = new HashMap<String, AbstractVariable>();
        Method[] methods = intf.getMethods();
        for (Method m : methods) {
            if (!m.getName().startsWith("get$"))
                continue;
            String name = m.getName().substring("get$".length());
            String fqReturnType = m.getReturnType().getName();
            String returnType = fqReturnType.substring(fqReturnType.lastIndexOf(".") + 1);
            if (returnType.equals("IntLocation"))
                locs.put(name, IntVariable.make());
            else if (returnType.equals("DoubleLocation"))
                locs.put(name, DoubleVariable.make());
            else if (returnType.equals("BooleanLocation"))
                locs.put(name, BooleanVariable.make());
            else if (returnType.equals("ObjectLocation"))
                locs.put(name, ObjectVariable.make());
            else if (returnType.equals("SequenceLocation"))
                locs.put(name, SequenceVariable.make(TypeInfo.Object));
            else
                throw new IllegalStateException("Unknown location type " + fqReturnType);
        }

        @SuppressWarnings("unchecked")
        T result = (T) Proxy.newProxyInstance(intf.getClassLoader(),
                                              new Class[]{intf},
                                              new InvocationHandler() {
                                                  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                                      if (method.getName().equals("initialize$")) {
                                                          List<AbstractVariable> vars = new ArrayList<AbstractVariable>();
                                                          addTriggers((T) proxy);
                                                          for (String name : attributes) {
                                                              if (locs.get(name).needDefault())
                                                                  applyDefault((T) proxy, name, locs.get(name));
                                                              vars.add((AbstractVariable) locs.get(name));
                                                          }
                                                          init((T) proxy);
                                                          postInit((T) proxy);
                                                          InitHelper.finish(vars.toArray(new AbstractVariable[vars.size()]));
                                                      }
                                                      else if (method.getName().startsWith("get$")) {
                                                          return locs.get(method.getName().substring("get$".length()));
                                                      }
                                                      else if (method.getName().equals("equals")) {
                                                          return proxy == args[0];
                                                      }
                                                      else if (method.getName().equals("hashCode")) {
                                                          return System.identityHashCode(proxy);
                                                      }
                                                      else if (method.getName().equals("toString")) {
                                                          return intf.getName() + "@" + System.identityHashCode(proxy);
                                                      }
                                                      else
                                                          throw new NoSuchMethodException(method.getName());

                                                      return null;
                                                  }
                                              });
        return result;
    }

    public void addTriggers(T receiver) { }

    public void applyDefault(T receiver, String attrName, AbstractVariable attrLocation) { }

    public void init(T receiver) { }

    public void postInit(T receiver) { }
}
