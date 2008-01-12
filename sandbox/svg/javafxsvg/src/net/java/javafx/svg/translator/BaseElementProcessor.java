/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator;

import org.w3c.dom.Element;

public class BaseElementProcessor implements ElementProcessor {
    private boolean generable = true;
    private ElementHandler beforeProcessing;
    private ElementHandler beforeElement;
    private ElementHandler onAttributes;
    private ElementHandler onElement;
    private ElementHandler afterElement;
    
    public boolean isGenerable() {
        return generable;
    }
    public void setGenerable(boolean generable) {
        this.generable = generable;
    }
    
    public void beforeProcessing(Element element, Builder builder) throws Exception {
        if (beforeProcessing != null) {
            beforeProcessing.handleElement(element, builder);
        }
    }

    public void beforeElement(Element element, Builder builder) throws Exception {
        if (beforeElement != null) {
            beforeElement.handleElement(element, builder);
        }
    }
    
    public void onAttributes(Element element, Builder builder) throws Exception {
        if (onAttributes != null) {
            onAttributes.handleElement(element, builder);
        }
    }

    public void onElement(Element element, Builder builder) throws Exception {
        if (onElement != null) {
            onElement.handleElement(element, builder);
        }
    }

    public void afterElement(Element element, Builder builder) throws Exception {
        if (afterElement!= null) {
            afterElement.handleElement(element, builder);
        }
    }


    public ElementHandler getAfterElement() {
        return afterElement;
    }

    public void setAfterElement(ElementHandler afterElement) {
        this.afterElement = afterElement;
    }

    public ElementHandler getBeforeElement() {
        return beforeElement;
    }

    public void setBeforeElement(ElementHandler beforeElement) {
        this.beforeElement = beforeElement;
    }

    public ElementHandler getBeforeProcessing() {
        return beforeProcessing;
    }

    public void setBeforeProcessing(ElementHandler beforeProcessing) {
        this.beforeProcessing = beforeProcessing;
    }

    public ElementHandler getOnAttributes() {
        return onAttributes;
    }

    public void setOnAttributes(ElementHandler onAttributes) {
        this.onAttributes = onAttributes;
    }

    public ElementHandler getOnElement() {
        return onElement;
    }

    public void setOnElement(ElementHandler onElement) {
        this.onElement = onElement;
    }
}
