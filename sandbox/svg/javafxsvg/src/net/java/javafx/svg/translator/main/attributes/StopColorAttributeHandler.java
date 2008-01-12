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


public class StopColorAttributeHandler implements AttributeHandler {
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        float alpha = 1;
        String stopOpacity =
            attr.getOwnerElement().getAttribute("stop-opacity");
        if (stopOpacity != null && !"".equals(stopOpacity)) {
            try {                
                alpha = new DecimalFormat("0").parse(stopOpacity).floatValue();
            } catch (Exception e) {
                System.err.println("Invalid stop-opacity: " + stopOpacity);
            }
        }
        Utils.colorAttributeWithAlpha(Utils.newAttr("color", attr), builder, alpha);
    }
}
