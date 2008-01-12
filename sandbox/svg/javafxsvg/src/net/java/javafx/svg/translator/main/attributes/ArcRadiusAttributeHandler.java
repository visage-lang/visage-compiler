/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Attr;


public class ArcRadiusAttributeHandler implements AttributeHandler {
    private String arc;
    private String radius;
    
    public ArcRadiusAttributeHandler(String arc, String radius) {
        this.arc = arc;
        this.radius = radius;
    }
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        if ("rect".equals(attr.getOwnerElement().getLocalName())) {
            LengthAttributeHandler.getInstance().
                handleAttribute(Utils.newAttr(arc, attr), builder);
        } else {
            LengthAttributeHandler.getInstance().
            handleAttribute(Utils.newAttr(radius, attr), builder);
        }
    }
}
