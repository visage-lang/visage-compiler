/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.javafx.runtime.animation;

/**
 *
 * @author jclarke
 */
public interface FXTimingTarget {
    public void timingEvent(double fraction);
    public void begin();
    public void end();
}
