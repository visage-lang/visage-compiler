/*
 * SequenceUtil.java
 * 
 * Created on Dec 1, 2007, 4:02:41 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.ui;

import com.sun.javafx.runtime.sequence.Sequence;
import java.util.Iterator;

/**
 *
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
}
