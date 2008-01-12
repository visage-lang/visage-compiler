/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.parsers.F3PathHandler;
import net.java.javafx.svg.translator.parsers.PathHandlerAdapter;

import org.apache.batik.parser.PathParser;
import org.w3c.dom.Attr;


public class DAttributeHandler implements AttributeHandler {

    public void handleAttribute(Attr attr, final Builder builder) {

        
        PathParser parser = new PathParser();
        PathHandlerAdapter adapter = new PathHandlerAdapter();
        parser.setPathHandler(adapter);
        adapter.setHandler(new F3PathHandler() {
            public void arc(float rx,
                            float ry,
                            float xAxisRotation,
                            boolean largeArcFlag,
                            boolean sweepFlag,
                            float x,
                            float y,
                            boolean absolute)
            {
                builder.append("Path.arcTo(");
                builder.append(rx+", ");
                builder.append(ry+", ");
                builder.append(xAxisRotation+", ");
                builder.append(largeArcFlag+", ");
                builder.append(sweepFlag+", ");
                builder.append(x+", ");
                builder.append(y+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }
            
            public void curvetoCubic(float x1,
                                    float y1,
                                    float x2,
                                    float y2,
                                    float x,
                                    float y,
                                    boolean absolute)
            {
                builder.append("Path.curveTo(");
                builder.increaseIndentation();
                builder.append(x1+", ");
                builder.append(y1+", ");
                builder.append(x2+", ");
                builder.append(y2+", ");
                builder.append(x+", ");
                builder.append(y+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }
            
            public void curvetoCubicSmooth(float x2,
                                           float y2,
                                           float x,
                                           float y,
                                           boolean absolute)
            {
                builder.append("Path.smoothCurveTo(");
                builder.increaseIndentation();
                builder.append(x2+", ");
                builder.append(y2+", ");
                builder.append(x+", ");
                builder.append(y+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }
 
            public void curvetoQuadratic(float x1,
                                         float y1,
                                         float x,
                                         float y,
                                         boolean absolute)
            {
                builder.append("Path.quadTo(");
                builder.increaseIndentation();
                builder.append(x1+", ");
                builder.append(y1+", ");
                builder.append(x+", ");
                builder.append(y+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }

            public void curvetoQuadraticSmooth(float x2,
                                               float y2,
                                               boolean absolute)
           {
                builder.append("Path.smoothQuadTo(");
                builder.increaseIndentation();
                builder.append(x2+", ");
                builder.append(y2+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }


            public void closePath() {
                builder.append("Path.closePath(),");
            }

            public void lineto(float x, float y, boolean absolute) {
                builder.append("Path.lineTo(");
                builder.increaseIndentation();
                builder.append(x+", ");
                builder.append(y+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }

            public void linetoHorizontal(float x, boolean absolute) {
                builder.append("Path.hlineTo(");
                builder.increaseIndentation();
                builder.append(x+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }

            public void linetoVertical(float y, boolean absolute) {
                builder.append("Path.vlineTo(");
                builder.increaseIndentation();
                builder.append(y+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }

            public void moveto(float x, float y, boolean absolute) {
                builder.append("Path.moveTo(");
                builder.increaseIndentation();
                builder.append(x+", ");
                builder.append(y+", ");
                builder.append(absolute+"");
                builder.decreaseIndentation();
                builder.append("),");
            }


            public void startPath() {
                builder.increaseIndentation();
            }
            
            public void endPath() {
                builder.decreaseIndentation();
            }
        });

        //parser.parse(attr.getValue());
        
        builder.append("content: \""+attr.getValue()+"\"");
    }

}
