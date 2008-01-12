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


public class FillAttributeHandler implements AttributeHandler {
    // TODO Unify FillAttributeHandler with StrokeAttributeHandler
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        if (!"none".equals(attr.getValue())) {
            float alpha = 1;
            String fillOpacity =
                attr.getOwnerElement().getAttribute("fill-opacity");
            if (fillOpacity != null && fillOpacity != "")  {
                try {
                    alpha = new DecimalFormat("0").parse(fillOpacity).floatValue();
                } catch (Exception e) {
                    System.err.println("Invalid fill-opacity" + fillOpacity + ": " + e);
                }
            }
            Utils.colorAttributeWithAlpha(attr, builder, alpha);
        }
    }

}

