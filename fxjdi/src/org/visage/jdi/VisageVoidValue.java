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
public class VisageVoidValue extends VisageValue implements VoidValue {
    public VisageVoidValue(VisageVirtualMachine fxvm, VoidValue underlying) {
        super(fxvm, underlying);
    }
    
    @Override
    protected VoidValue underlying() {
        return (VoidValue) super.underlying();
    }
}
