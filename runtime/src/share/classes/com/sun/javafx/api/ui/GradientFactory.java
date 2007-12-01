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

package com.sun.javafx.api.ui;

import com.sun.javafx.runtime.awt.LinearGradientPaint;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.awt.MultipleGradientPaint.ColorSpaceType;
import com.sun.javafx.runtime.awt.MultipleGradientPaint.CycleMethod;
import com.sun.javafx.runtime.awt.RadialGradientPaint;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import com.sun.tools.javafx.ui.SequenceUtil;

/**
 *
 * @author jclarke
 */
public class GradientFactory {
    
    public static LinearGradientPaint createLinearGradientPaint(Point2D start, Point2D end,
            Sequence<? extends java.lang.Double> fractions, 
            Sequence<? extends java.awt.Color> colors,
            CycleMethod cycleMethod, ColorSpaceType colorSpace, AffineTransform transform) {
        
        float[] dFractions = SequenceUtil.sequenceOfDouble2floatArray(fractions);
        Color[] dColors = new Color[colors.size()];
        colors.toArray(dColors, 0);
        
        return new LinearGradientPaint(start, end, dFractions, dColors, 
                cycleMethod, colorSpace, transform);
        
    }
    
    public static RadialGradientPaint createRadialGradientPaint(Point2D center, 
            float radius, Point2D focus,
            Sequence<? extends java.lang.Double> fractions, 
            Sequence<? extends java.awt.Color> colors,
            CycleMethod cycleMethod, ColorSpaceType colorSpace, AffineTransform transform) {
        
        float[] dFractions = SequenceUtil.sequenceOfDouble2floatArray(fractions);
        Color[] dColors = new Color[colors.size()];
        colors.toArray(dColors, 0);
        
        return new RadialGradientPaint(center, radius, focus, dFractions, dColors, 
                cycleMethod, colorSpace, transform);
        
    }

}
