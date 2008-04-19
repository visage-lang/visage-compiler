/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.reflect;

/** A run-time representation of a JavaFX function type. */

public class FunctionTypeRef extends TypeRef {
  int minArgs;
  TypeRef[] argTypes;
  boolean varArgs;
  TypeRef returnType;

  FunctionTypeRef() {
  }

  /** The fixed (minimum) number of arguments needed.
   * Does not count varargs, and (possible future) optional args.
   */
  public int minArgs() { return minArgs; }

  /** Was this method declarfed to take a variable number of arguments?
   * Note that varArgs aren't yet supported in JavaFX. */
  public boolean isVarArgs() { return varArgs; }

  public TypeRef getArgumentType(int i) {
    return argTypes[varArgs && i >= minArgs ? minArgs : i]; }

  public TypeRef getReturnType() { return returnType; }

    public boolean equals(FunctionTypeRef ftype) {
        if (minArgs != ftype.minArgs || varArgs != ftype.varArgs
              || ! returnType.equals(ftype.returnType))
            return false;
        for (int i = minArgs; --i >= 0; ) {
            if (! argTypes[i].equals(ftype.argTypes[i]))
                return false;
        }
        return true;
    }
}

