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

package com.sun.tools.javafx.ui.animation;

/** Derived from the example in Section 15.3 of CLR. */

import java.util.Comparator;

public class IntervalNode extends RBNode {
  private Interval   interval;
  private Comparator endpointComparator;
  private Object     minEndpoint;
  private Object     maxEndpoint;

  public IntervalNode(Interval interval, Comparator endpointComparator, Object data) {
    super(data);
    this.interval = interval;
    this.endpointComparator = endpointComparator;
  }

  public void copyFrom(RBNode arg) {
    IntervalNode argNode = (IntervalNode) arg;
    this.interval = argNode.interval;
  }

  public Interval getInterval() {
    return interval;
  }

  public Object getMinEndpoint() {
    return minEndpoint;
  }

  public Object getMaxEndpoint() {
    return maxEndpoint;
  }

  public boolean update() {
    Object newMaxEndpoint = computeMaxEndpoint();
    Object newMinEndpoint = computeMinEndpoint();

    if ((maxEndpoint != newMaxEndpoint) || (minEndpoint != newMinEndpoint)) {
      maxEndpoint = newMaxEndpoint;
      minEndpoint = newMinEndpoint;
      return true;
    }

    return false;
  }

  // Computes maximum endpoint without setting it in this node
  public Object computeMinEndpoint() {
    IntervalNode left = (IntervalNode) getLeft();
    if (left != null) {
      return left.getMinEndpoint();
    }
    return interval.getLowEndpoint();
  }

  // Computes maximum endpoint without setting it in this node
  public Object computeMaxEndpoint() {
    Object curMax = interval.getHighEndpoint();
    if (getLeft() != null) {
      IntervalNode left = (IntervalNode) getLeft();
      if (endpointComparator.compare(left.getMaxEndpoint(), curMax) > 0) {
        curMax = left.getMaxEndpoint();
      }
    }

    if (getRight() != null) {
      IntervalNode right = (IntervalNode) getRight();
      if (endpointComparator.compare(right.getMaxEndpoint(), curMax) > 0) {
        curMax = right.getMaxEndpoint();
      }
    }
    return curMax;
  }

  public String toString() {
    String res = interval.toString();
    Object d = getData();
    if (d != null) {
      res += " " + d;
    }
    return res;
  }
}
