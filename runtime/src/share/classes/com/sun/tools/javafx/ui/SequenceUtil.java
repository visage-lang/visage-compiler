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

package com.sun.tools.javafx.ui;

import com.sun.javafx.runtime.sequence.Sequence;
import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * Utilities to convert squenced to Java Arrays for calling Java methods.
 * This is supposed to be a temporary work around.
 * @author jclarke
 */
public class SequenceUtil {
    public static float[] sequenceOfDouble2floatArray(Sequence<? extends java.lang.Double> sequence) {
        float[] fArray = new float[sequence.size()];
        Iterator<? extends java.lang.Double> iter = sequence.iterator();
        for(int i = 0; iter.hasNext();i++) {
            fArray[i] = iter.next().floatValue();
        }
        return fArray;
    }
    
    public static double[] sequenceOfDouble2doubleArray(Sequence<? extends java.lang.Double> sequence) {
        double[] dArray = new double[sequence.size()];
        Iterator<? extends java.lang.Double> iter = sequence.iterator();
        for(int i = 0; iter.hasNext();i++) {
            dArray[i] = iter.next().doubleValue();
        }
        return dArray;
    }
    public static int[] sequenceOfInteger2intArray(Sequence<? extends java.lang.Integer> sequence) {
        int[] iArray = new int[sequence.size()];
        Iterator<? extends java.lang.Integer> iter = sequence.iterator();
        for(int i = 0; iter.hasNext();i++) {
            iArray[i] = iter.next().intValue();
        }
        return iArray;
    } 
    
    public static String[] sequenceOfString2StringArray(Sequence<? extends String> sequence) {
        String[] iArray = new String[sequence.size()];
        sequence.toArray(iArray, 0);
        return iArray;        
    }

    public static <T>T[] sequenceToArray(Sequence<? extends T> sequence) {
        Class cl = sequence.getElementType();
        T[] c = (T[])Array.newInstance(cl, sequence.size());
        sequence.toArray(c, 0);
        return c;
    }
}
