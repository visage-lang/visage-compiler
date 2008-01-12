/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;

import org.w3c.dom.Attr;


public class GradientUnitsAttributeHandler implements AttributeHandler {
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        String value = attr.getValue();
        if ("objectBoundingBox".equals(value)) {
            value = "OBJECT_BOUNDING_BOX";
        } else if ("userSpaceOnUse".equals(value)) {
            value = "USER_SPACE_ON_USE";
        } else {
            return;
	}
        builder.append("gradientUnits: GradientUnits." + value);
    }
}
