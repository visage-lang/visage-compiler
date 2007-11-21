/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.javafx.api.ui;
import javax.swing.*;
import java.util.*;

public class XInputVerifierImpl extends InputVerifier implements XInputVerifier {
    Set mVerifiers = new HashSet();

    @SuppressWarnings("unchecked")
    public void addInputVerifier(XInputVerifier verifier) {
        mVerifiers.add(verifier);
    }

    public void removeInputVerifier(XInputVerifier verifier) {
        mVerifiers.remove(verifier);
    }

    @SuppressWarnings("unchecked")
    public boolean verify(JComponent comp) {
        XInputVerifier[] arr = new XInputVerifier[mVerifiers.size()];
        mVerifiers.toArray(arr);
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].verify(comp)) {
                return false;
            }
        }
        return true;
    }
}