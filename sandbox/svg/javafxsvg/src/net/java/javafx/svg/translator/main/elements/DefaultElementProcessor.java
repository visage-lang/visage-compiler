/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;

import net.java.javafx.svg.translator.BaseElementProcessor;

public class DefaultElementProcessor extends BaseElementProcessor {
    private static DefaultElementProcessor instance = new DefaultElementProcessor();
    
    public static DefaultElementProcessor getInstance() {
        return instance;
    }
    
    protected DefaultElementProcessor() {
        setBeforeElement(DefaultBeforeElement.getInstance());
        setOnAttributes(DefaultOnAttributes.getInstance());
        setOnElement(DefaultOnElement.getInstance());
        setAfterElement(DefaultAfterElement.getInstance());
    }
}
