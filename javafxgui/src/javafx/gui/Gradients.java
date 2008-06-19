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

package javafx.gui;

import java.awt.Paint;
import java.awt.Color;
import java.awt.geom.Point2D;


class Gradients {
    private static Factory factory;

    static {
        try {
            factory = new Factory6();
        }
        catch (NoClassDefFoundError ignored) {
            factory = new Factory5();
        }
    }

    static Paint createLinearGradientPaint(Point2D start, Point2D end, float[] fractions, Color[] colors, int cycleMethodInt) {
        return factory.createLinearGradientPaint(start, end, fractions, colors, cycleMethodInt);
    }

    static Paint createRadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, int cycleMethodInt) {
        return factory.createRadialGradientPaint(center, radius, focus, fractions, colors, cycleMethodInt);
    }

    private static interface Factory {
        Paint createLinearGradientPaint(Point2D start, Point2D end, float[] fractions, Color[] colors, int cycleMethodInt);
        Paint createRadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, int cycleMethodInt);
    }

    private static class Factory6 implements Factory {
        Factory6() {
            // Force linkage error in java 1.5; just instantiating Factory6 won't do that.
            Class c = java.awt.MultipleGradientPaint.class;
        }

        private java.awt.MultipleGradientPaint.CycleMethod cm(int cycleMethodInt) {
            if (cycleMethodInt == 1) {
                return java.awt.MultipleGradientPaint.CycleMethod.REFLECT;
            }
            else if (cycleMethodInt == 2) {
                return java.awt.MultipleGradientPaint.CycleMethod.REPEAT;
            }
            else {
                return java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE;
            }
        }

        public Paint createLinearGradientPaint(Point2D start, Point2D end, float[] fractions, Color[] colors, int cycleMethodInt) {
            return new java.awt.LinearGradientPaint(start, end, fractions, colors, cm(cycleMethodInt));
        }

        public Paint createRadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, int cycleMethodInt) {
            return new java.awt.RadialGradientPaint(center, radius, focus, fractions, colors, cm(cycleMethodInt));
        }
    }

    private static class Factory5 implements Factory {
        private com.sun.javafx.gui.gradients.MultipleGradientPaint.CycleMethod cm(int cycleMethodInt) {
            if (cycleMethodInt == 1) {
                return com.sun.javafx.gui.gradients.MultipleGradientPaint.CycleMethod.REFLECT;
            }
            else if (cycleMethodInt == 2) {
                return com.sun.javafx.gui.gradients.MultipleGradientPaint.CycleMethod.REPEAT;
            }
            else {
                return com.sun.javafx.gui.gradients.MultipleGradientPaint.CycleMethod.NO_CYCLE;
            }
        }

        public Paint createLinearGradientPaint(Point2D start, Point2D end, float[] fractions, Color[] colors, int cycleMethodInt) {
            return new com.sun.javafx.gui.gradients.LinearGradientPaint(start, end, fractions, colors, cm(cycleMethodInt));
        }

        public Paint createRadialGradientPaint(Point2D center, float radius, Point2D focus, float[] fractions, Color[] colors, int cycleMethodInt) {
            return new com.sun.javafx.gui.gradients.RadialGradientPaint(center, radius, focus, fractions, colors, cm(cycleMethodInt));
        }
    }

}
