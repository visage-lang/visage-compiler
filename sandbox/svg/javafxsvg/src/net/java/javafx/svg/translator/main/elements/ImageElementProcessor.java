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
import net.java.javafx.svg.translator.main.util.Length;
import java.net.URI;
import org.w3c.dom.Element;


public class ImageElementProcessor extends BaseElementProcessor {
    public ImageElementProcessor() {
        setBeforeElement(new NamedBeforeElement("ImageView"));
        
        setOnAttributes(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                if (element.hasAttribute("transform")) {
                    builder.processAttribute(element.getAttributeNode("transform"));
                }
            }
        });

        setOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                String x = Utils.getAttribute(element, "x", "0");
                String y = Utils.getAttribute(element, "y", "0");
		float xl = Length.parseLength(x).getValue();
		float yl = Length.parseLength(y).getValue();
                String width = Utils.getLengthRepresentation(element, "width");
                String height = Utils.getLengthRepresentation(element, "height");
                String url = element.getAttributeNS(Utils.XLINK_URI, Utils.XLINK_HREF);
                builder.increaseIndentation();
                if (!(xl == 0 && yl == 0)) {
		    builder.append("transform: translate(" + x + ", " + y + ")");
		}
		try {
		    URI u = new URI(url);
		    if (u.getScheme() == null) {
			url = "\"{__DIR__}/"+url+"\"";
		    } else {
			url = "'"+url+"'";
		    }
		} catch (Exception e) {
		    url = "\"{__DIR__}/"+url+"\"";
		}
                builder.append("image: { url: " + url + " }");
                builder.append("size: { height: " + height + ", width: " + width + "}");
                builder.decreaseIndentation();
            }
        });
        
        setAfterElement(DefaultAfterElement.getInstance());
    }
}
