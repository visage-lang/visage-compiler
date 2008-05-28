/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator;

import org.w3c.dom.Element;

public interface ElementHandler {
    public void handleElement(Element element, Builder builder) throws Exception;

}
