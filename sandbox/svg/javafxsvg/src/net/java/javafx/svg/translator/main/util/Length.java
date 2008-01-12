/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.util;

import org.apache.batik.parser.LengthHandler;
import org.apache.batik.parser.LengthParser;

public class Length {
    private float value;
    private String unit;
    
    public Length() {}
    
    public Length(float value, String unit) {
        setValue(value);
        setUnit(unit);
    }

    public static String methodName(String unit) {
        if ("mm".equals(unit)) { return "UIElement.mmToPixel"; }
        if ("cm".equals(unit)) { return "UIElement.cmToPixel"; }
        if ("in".equals(unit)) { return "UIElement.inchToPixel"; }
        if ("pt".equals(unit)) { return "UIElement.pointToPixel"; }

        // default: px, %
        return null;
    }

    public static Length parseLength(String value) {
        final Length length = new Length();
        
        LengthParser parser = new LengthParser();
        parser.setLengthHandler(new LengthHandler() {
            public void cm() { length.setUnit("cm"); }
            public void em() { length.setUnit("em"); }
            public void ex() { length.setUnit("ex"); }
            public void in() { length.setUnit("in"); }
            public void mm() { length.setUnit("mm"); }
            public void pc() { length.setUnit("pc"); }
            public void pt() { length.setUnit("pt"); }
            public void px() { length.setUnit("px"); }
            public void percentage() {
            	length.setUnit("%");
            	length.setValue(length.getValue() / 100);
            }
            public void lengthValue(float value) { length.setValue(value); }
            public void startLength() {}
            public void endLength() {}
        });
        
        parser.parse(value);
        return length;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
