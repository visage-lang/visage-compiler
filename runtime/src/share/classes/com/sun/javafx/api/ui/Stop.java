/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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
import java.awt.Color;

public class Stop {
    
    float mOffset;
    
    Color mColor;
    
    Gradient mGradient;


    public interface Gradient {
        public void update();
    }
    
    Gradient getGradient() {
        return mGradient;
    }
    
    void setGradient(Gradient gradient) {
        mGradient = gradient;
    }
    
    public void setOffset(float offset) {
        mOffset = offset;
        if (mGradient != null) {
            mGradient.update();
        }
    }
    public float getOffset() {
        return mOffset;
    }
    public void setColor(Color color) {
        mColor = color;
        if (mGradient != null) {
            mGradient.update();
        }
    }
    public Color getColor() {
        return mColor;
    }
}
