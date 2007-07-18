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

public abstract class Sequence // perhaps: implements java.util.List
{
  /** Static helper to implement the 'sizeof' operation.
   */
  public static int size (Object val)
  {
    if (val instanceof Sequence)
      return ((Sequence) val).size();
    else
      return val == null ? 0 : 1;
  }

  /** Static helper to implement simple indexing.
   */
  public static Object get (Object val, int index)
  {
    if (val instanceof Sequence)
      return ((Sequence) val).get(index);
    else
      return index != 0 ? null : val;
  }

  public abstract int size();
  public abstract Object get(int index);

  /** Static helper to implement the 'insert' statement.
   * Specifically {@code insert vals2 before vals1[index]} can be compiled
   * to {@code vals1 = insert(vals1, index, vals2)}.
   * The {@code insert vals2 as [first|last] into vals1} statement can be
   * compiled to {@code vals1 = insert(vals1, [0|Integer.MAX_VALUE], vals2)}.
   */
  public static Object insert (Object vals1, int index, Object vals2)
  {
    GapSequence seq1;
    int size2 = size(vals2);
    int gapSize;
    if (vals1 instanceof GapSequence
        && ! (seq1 = (GapSequence) vals1).shared
        && size2 <= (gapSize = seq1.gapEnd - seq1.gapStart))
      {
        Object[] buffer = seq1.buffer;
        if (index < seq1.gapStart)
          {
            System.arraycopy(buffer, index, buffer, gapSize+index,
                             seq1.gapStart-index);
          }
        else if (index > seq1.gapStart)
          {
            System.arraycopy(buffer, seq1.gapEnd,
                             buffer, seq1.gapStart,
                             index-seq1.gapStart);
          }
        seq1.gapStart = index;
        seq1.gapEnd = index + gapSize;
        // Should be optimized:
        for (int i = 0;  i < size2;  i++)
          buffer[seq1.gapStart++] = Sequence.get(vals2, i);
        return seq1;

      }
    else if (vals1 == null)
      return vals2;
    else
      {
        int size1 = size(vals1);
        int newSize = size1 + size2;
        int oldSize;
        // Guard againts quadratic behavior in some cases:
        if (vals1 instanceof GapSequence
            && newSize < 2 * (oldSize = ((GapSequence) vals1).buffer.length))
          newSize = 2 * oldSize;
        seq1 = new GapSequence(newSize);
        insert(seq1, 0, vals1);
        insert(seq1, size1, vals2);
        return seq1;
      }
  }
}
