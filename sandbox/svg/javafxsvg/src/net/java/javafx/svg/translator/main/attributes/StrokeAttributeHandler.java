/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import java.text.DecimalFormat;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Attr;


public class StrokeAttributeHandler implements AttributeHandler {
    // TODO Unify StrokeAttributeHandler with FillAttributeHandler 
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        if (!"none".equals(attr.getValue())) {
            float alpha = 1;
            String strokeOpacity =
                attr.getOwnerElement().getAttribute("stroke-opacity");
            if (strokeOpacity != null && !"".equals(strokeOpacity)) {
                try {
                   alpha = new DecimalFormat("0").parse(strokeOpacity).floatValue();
                } catch (Exception e) {
                     System.err.println("Invalid stroke-opacity {strokeOpacity}");
                }
            }
            Utils.colorAttributeWithAlpha(attr, builder, alpha);
        }
    }
}
