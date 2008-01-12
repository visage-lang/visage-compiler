/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator;

import org.w3c.dom.Attr;

public interface AttributeHandler {
    public void handleAttribute(Attr attr, Builder builder) throws Exception;
}
