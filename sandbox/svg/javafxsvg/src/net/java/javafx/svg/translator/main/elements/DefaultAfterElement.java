/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;

import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;

import org.w3c.dom.Element;


public class DefaultAfterElement implements ElementHandler {
    
    private static DefaultAfterElement instance = new DefaultAfterElement();
    
    public static DefaultAfterElement getInstance() {
        return instance;
    }
    private DefaultAfterElement() {}

    public void handleElement(Element element, Builder builder) throws Exception {
        builder.append("},");
    }
}
