/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.parsers.F3TransformListHandler;
import net.java.javafx.svg.translator.parsers.TransformListHandlerAdapter;

import org.apache.batik.parser.TransformListParser;
import org.w3c.dom.Attr;


public class TransformAttributeHandler implements AttributeHandler {
    private static TransformAttributeHandler instance = new TransformAttributeHandler();
    
    public static TransformAttributeHandler getInstance() {
        return instance;
    }
    
    private TransformAttributeHandler(){}
    
    public void handleAttribute(Attr attr, final Builder builder) throws Exception {
        TransformListParser parser = new TransformListParser();
        TransformListHandlerAdapter adapter = new TransformListHandlerAdapter();
        parser.setTransformListHandler(adapter);
        adapter.setHandler(new F3TransformListHandler() {
            public void startTransformList() {
                builder.append("transform: [");
                builder.increaseIndentation();
            }
                    
            public void matrix(float a,
                               float b,
                               float c,
                               float d,
                               float e,
                               float f)
            {
                builder.append("Transform.matrix(" +
                                    a + ", " +
                                    b + ", " +
                                    c + ", " +
                                    d + ", " +
                                    e + ", " +
                                    f + "),");
            }
                        
            public void rotate(float theta) {
                builder.append("Transform.rotate(" + theta + ", 0, 0),");
            }
                    
            public void rotateXY(float theta, float cx, float cy) {
                builder.append("Transform.rotate(" +
                                    theta + ", " +
                                    cx + ", " +
                                    cy+"),");
            }
                    
            public void translate(float tx) {
                builder.append("Transform.translate(" + tx + ", 0),");
            }
                        
            public void translateXY(float tx, float ty) {
                builder.append("Transform.translate(" + tx + ", " + ty + "),");
            }
                    
            public void scale(float sx) {
                builder.append("Transform.scale(" + sx + ", " + sx + "),");
            }
                    
            public void scaleXY(float sx, float sy) {
                builder.append("Transform.scale(" + sx + ", " + sy + "),");
            }
                    
            public void skewX(float skx) {
                builder.append("Transform.skew(" + skx + ", 0),");
            }
                    
            public void skewY(float sky) {
                builder.append("Transform.skew(0, " + sky + "),");
            }
                    
            public void endTransformList() {
                builder.decreaseIndentation();
                builder.append("]");
            }
        });
        parser.parse(attr.getValue());
    }

}
