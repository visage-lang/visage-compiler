/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;

import net.java.javafx.svg.translator.BaseElementProcessor;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Element;


public class SVGElementProcessor extends BaseElementProcessor {
	public SVGElementProcessor () {
		setOnElement(new ElementHandler() {
			public void handleElement(Element element, Builder builder) throws Exception {
				Utils.processSubElements(element, builder);
			}
		});
	}
}
