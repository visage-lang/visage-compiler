/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.reflect;

/** A handle/proxy for an {@code Object} reference.
 */

public abstract class ObjectRef extends ValueRef {
    protected ObjectRef() {
    }

    public abstract ClassRef getType();

    public ReflectionContext getReflectionContect() {
      return getType().getReflectionContect();
    }

  /** Initialize an attribute of an object to a given value.
   * Should only be called between {@code cls.allocate()} and {@code obj.initialize()}.
   */
  public void initAttribute(String name, ValueRef value) {
    AttributeRef attr = getType().getAttribute(name);
    initAttribute(attr, value);
  }
  /** Initialize an attribute of an object to a given value.
   * Should only be called between {@code cls.allocate()} and {@code obj.initialize()}.
   */
  public void initAttribute(AttributeRef attr, ValueRef value) {
      throw new UnsupportedOperationException("unimplemented: initAttribute");
  }
  /** Bind an attribute of an object to a given location.
   * Should only be called between {@code cls.allocate()} and {@code obj.initialize()}.
   */
  public void initBinding(String name, LocationRef location) {
    AttributeRef attr = getType().getAttribute(name);
    initBinding(attr, location);
  }
  /** Bind an attribute of an object to a given location.
   * Should only be called between {@code cls.allocate()} and {@code obj.initialize()}.
   */
  public void initBinding(AttributeRef attr, LocationRef location) {
      throw new UnsupportedOperationException("unimplemented: initBinding");
  }

  /** Finish constructing an object.
   * Run init hooks, triggers etc.
   * @return the constructed object - normally the same as this.
   */
  public ObjectRef initialize() {
      throw new UnsupportedOperationException("unimplemented: initialize");
  }

  /** Convenience method to invoke a member function. */
  public ValueRef invoke(String name, ValueRef... args) {
    TypeRef[] types = new TypeRef[args.length];
    for (int i = args.length;  --i >= 0; ) types[i] = args[i].getType();
    return getType().getMethod(name, types).invoke(this, args);
  }
  /** Convenience method to invoke a member function. */
  public ValueRef invoke(MethodRef method, ValueRef... args) {
    return method.invoke(this, args);
  }
}
