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

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Paint;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jclarke
 */
public class GradientFactory {
    private static boolean isJDK15 = System.getProperty("java.version").startsWith("1.5");
    
    private static final String JDK15_COLORSPACETYPE_CLASSNAME = "com.sun.javafx.runtime.awt.MultipleGradientPaint$ColorSpaceType";
    private static final String JDK15_CYCLEMETHOD_CLASSNAME = "com.sun.javafx.runtime.awt.MultipleGradientPaint$CycleMethod";
    private static final String JDK15_LINEARGRADIENTPAINT_CLASSNAME = "com.sun.javafx.runtime.awt.LinearGradientPaint";
    private static final String JDK15_RADIALGRADIENTPAINT_CLASSNAME = "com.sun.javafx.runtime.awt.RadialGradientPaint";

    private static final String JDK16_COLORSPACETYPE_CLASSNAME = "java.awt.MultipleGradientPaint$ColorSpaceType";
    private static final String JDK16_CYCLEMETHOD_CLASSNAME = "java.awt.MultipleGradientPaint$CycleMethod";
    private static final String JDK16_LINEARGRADIENTPAINT_CLASSNAME = "java.awt.LinearGradientPaint";
    private static final String JDK16_RADIALGRADIENTPAINT_CLASSNAME = "java.awt.RadialGradientPaint";
    
    private static Object[] ColorSpaceObjects;
    private static Object[] CycleMethodObjects;
    private static Class colorSpaceClass;
    private static Class cycleMethodClass;
    private static Class linearGradientClass;
    private static Class radialGradientClass;
    private static Constructor linearGradientCtor;
    private static Constructor radialGradientCtor;
    
    static {
        try {
            if (isJDK15) {
                colorSpaceClass = Class.forName(JDK15_COLORSPACETYPE_CLASSNAME);
                ColorSpaceObjects = colorSpaceClass.getEnumConstants();
                cycleMethodClass = Class.forName(JDK15_CYCLEMETHOD_CLASSNAME);
                CycleMethodObjects = cycleMethodClass.getEnumConstants();
                linearGradientClass = Class.forName(JDK15_LINEARGRADIENTPAINT_CLASSNAME);
                radialGradientClass = Class.forName(JDK15_RADIALGRADIENTPAINT_CLASSNAME);
                
            } else {
                colorSpaceClass = Class.forName(JDK16_COLORSPACETYPE_CLASSNAME);
                ColorSpaceObjects = colorSpaceClass.getEnumConstants();
                cycleMethodClass = Class.forName(JDK16_CYCLEMETHOD_CLASSNAME);
                CycleMethodObjects = cycleMethodClass.getEnumConstants();
                linearGradientClass = Class.forName(JDK16_LINEARGRADIENTPAINT_CLASSNAME);
                radialGradientClass = Class.forName(JDK16_RADIALGRADIENTPAINT_CLASSNAME);
            }
            linearGradientCtor = linearGradientClass.getConstructor(
                    java.awt.geom.Point2D.class,
                    java.awt.geom.Point2D.class,
                    float[].class,
                    Color[].class,
                    cycleMethodClass,
                    colorSpaceClass,
                    java.awt.geom.AffineTransform.class);
            radialGradientCtor = radialGradientClass.getConstructor(
                    java.awt.geom.Point2D.class,
                    Float.TYPE,
                    java.awt.geom.Point2D.class,
                    float[].class,
                    Color[].class,
                    cycleMethodClass,
                    colorSpaceClass,
                    java.awt.geom.AffineTransform.class);

        }catch(Exception ex) {
            Logger.getLogger(GradientFactory.class.getName()).log(
                        Level.WARNING, "Error instantiating GradientFactory", ex);
        }
    }
    
    public static Paint createLinearGradientPaint(Point2D start, Point2D end,
            Sequence<? extends java.lang.Number> fractions, 
            Sequence<? extends java.awt.Color> colors,
            int cycleMethod, int colorSpace, AffineTransform transform) 
        throws InstantiationException, IllegalAccessException, 
            IllegalArgumentException, InvocationTargetException {
        float[] dFractions = Sequences.toFloatArray(fractions);
        Color[] dColors = new Color[colors.size()];
        colors.toArray(dColors, 0);
        Object cycleMethodObject = CycleMethodObjects[cycleMethod];
        Object colorSpaceObject = ColorSpaceObjects[colorSpace];
        return (Paint)linearGradientCtor.newInstance(start, end,
                dFractions, dColors, cycleMethodObject, colorSpaceObject, transform);
    }
    
    public static Paint createRadialGradientPaint(Point2D center, 
            float radius, Point2D focus,
            Sequence<? extends java.lang.Number> fractions, 
            Sequence<? extends java.awt.Color> colors,
            int cycleMethod, int colorSpace, AffineTransform transform) 
        throws InstantiationException, 
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        float[] dFractions = Sequences.toFloatArray(fractions);
        Color[] dColors = new Color[colors.size()];
        colors.toArray(dColors, 0);
        Object cycleMethodObject = CycleMethodObjects[cycleMethod];
        Object colorSpaceObject = ColorSpaceObjects[colorSpace]; 
        return (Paint)radialGradientCtor.newInstance(center, radius, focus, 
                dFractions, dColors, cycleMethodObject, colorSpaceObject, transform);
                
        
    }
    

    

}
