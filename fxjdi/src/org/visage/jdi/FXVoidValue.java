/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.visage.jdi;

import com.sun.jdi.VoidValue;

/**
 *
 * @author sundar
 */
public class FXVoidValue extends FXValue implements VoidValue {
    public FXVoidValue(FXVirtualMachine fxvm, VoidValue underlying) {
        super(fxvm, underlying);
    }
    
    @Override
    protected VoidValue underlying() {
        return (VoidValue) super.underlying();
    }
}
