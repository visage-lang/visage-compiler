/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator;

import org.w3c.dom.Element;

public interface ElementProcessor {
    public boolean isGenerable();
    public void beforeProcessing(Element element, Builder builder) throws Exception;
    public void beforeElement(Element element, Builder builder) throws Exception;
    public void onAttributes(Element element, Builder builder) throws Exception;
    public void onElement(Element element, Builder builder) throws Exception;
    public void afterElement(Element element, Builder builder) throws Exception;
}
