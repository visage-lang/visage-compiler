/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package com.sun.javafx.api.ui.path.parser;

import java.awt.geom.*;
import java.awt.Shape;
import java.io.IOException;
import java.io.Reader;
import com.sun.javafx.api.ui.path.ext.awt.geom.ExtendedGeneralPath;


public class AWTPathProducer
    implements PathHandler, ShapeProducer
{

    public AWTPathProducer()
    {
    }

    public static Shape createShape(Reader reader, int i)
        throws IOException, ParseException
    {
        PathParser pathparser = new PathParser();
        AWTPathProducer awtpathproducer = new AWTPathProducer();
        awtpathproducer.setWindingRule(i);
        pathparser.setPathHandler(awtpathproducer);
        pathparser.parse(reader);
        return awtpathproducer.getShape();
    }


    public static GeneralPath createPath(Reader reader, int i)
        throws IOException, ParseException
    {
        PathParser pathparser = new PathParser();
        AWTPathProducer awtpathproducer = new AWTPathProducer();
        awtpathproducer.setWindingRule(i);
        pathparser.setPathHandler(awtpathproducer);
        pathparser.parse(reader);
        return awtpathproducer.getPath();
    }

    public void setWindingRule(int i)
    {
        windingRule = i;
    }

    public int getWindingRule()
    {
        return windingRule;
    }

    public Shape getShape()
    {
        return path;
    }

    public GeneralPath getPath() {
        return path.getPath();
    }

    public void startPath()
        throws ParseException
    {
        currentX = 0.0F;
        currentY = 0.0F;
        xCenter = 0.0F;
        yCenter = 0.0F;
        path = new ExtendedGeneralPath(windingRule);
    }

    public void endPath()
        throws ParseException
    {
    }

    public void movetoRel(float f, float f1)
        throws ParseException
    {
        path.moveTo(xCenter = currentX += f, yCenter = currentY += f1);
    }

    public void movetoAbs(float f, float f1)
        throws ParseException
    {
        path.moveTo(xCenter = currentX = f, yCenter = currentY = f1);
    }

    public void closePath()
        throws ParseException
    {
        path.closePath();
        Point2D point2d = path.getCurrentPoint();
        currentX = (float)point2d.getX();
        currentY = (float)point2d.getY();
    }

    public void linetoRel(float f, float f1)
        throws ParseException
    {
        path.lineTo(xCenter = currentX += f, yCenter = currentY += f1);
    }

    public void linetoAbs(float f, float f1)
        throws ParseException
    {
        path.lineTo(xCenter = currentX = f, yCenter = currentY = f1);
    }

    public void linetoHorizontalRel(float f)
        throws ParseException
    {
        path.lineTo(xCenter = currentX += f, yCenter = currentY);
    }

    public void linetoHorizontalAbs(float f)
        throws ParseException
    {
        path.lineTo(xCenter = currentX = f, yCenter = currentY);
    }

    public void linetoVerticalRel(float f)
        throws ParseException
    {
        path.lineTo(xCenter = currentX, yCenter = currentY += f);
    }

    public void linetoVerticalAbs(float f)
        throws ParseException
    {
        path.lineTo(xCenter = currentX, yCenter = currentY = f);
    }

    public void curvetoCubicRel(float f, float f1, float f2, float f3, float f4, float f5)
        throws ParseException
    {
        path.curveTo(currentX + f, currentY + f1, xCenter = currentX + f2, yCenter = currentY + f3, currentX += f4, currentY += f5);
    }

    public void curvetoCubicAbs(float f, float f1, float f2, float f3, float f4, float f5)
        throws ParseException
    {
        path.curveTo(f, f1, xCenter = f2, yCenter = f3, currentX = f4, currentY = f5);
    }

    public void curvetoCubicSmoothRel(float f, float f1, float f2, float f3)
        throws ParseException
    {
        path.curveTo(currentX * 2.0F - xCenter, currentY * 2.0F - yCenter, xCenter = currentX + f, yCenter = currentY + f1, currentX += f2, currentY += f3);
    }

    public void curvetoCubicSmoothAbs(float f, float f1, float f2, float f3)
        throws ParseException
    {
        path.curveTo(currentX * 2.0F - xCenter, currentY * 2.0F - yCenter, xCenter = f, yCenter = f1, currentX = f2, currentY = f3);
    }

    public void curvetoQuadraticRel(float f, float f1, float f2, float f3)
        throws ParseException
    {
        path.quadTo(xCenter = currentX + f, yCenter = currentY + f1, currentX += f2, currentY += f3);
    }

    public void curvetoQuadraticAbs(float f, float f1, float f2, float f3)
        throws ParseException
    {
        path.quadTo(xCenter = f, yCenter = f1, currentX = f2, currentY = f3);
    }

    public void curvetoQuadraticSmoothRel(float f, float f1)
        throws ParseException
    {
        path.quadTo(xCenter = currentX * 2.0F - xCenter, yCenter = currentY * 2.0F - yCenter, currentX += f, currentY += f1);
    }

    public void curvetoQuadraticSmoothAbs(float f, float f1)
        throws ParseException
    {
        path.quadTo(xCenter = currentX * 2.0F - xCenter, yCenter = currentY * 2.0F - yCenter, currentX = f, currentY = f1);
    }

    public void arcRel(float f, float f1, float f2, boolean flag, boolean flag1, float f3, float f4)
        throws ParseException
    {
        path.arcTo(f, f1, f2, flag, flag1, xCenter = currentX += f3, yCenter = currentY += f4);
    }

    public void arcAbs(float f, float f1, float f2, boolean flag, boolean flag1, float f3, float f4)
        throws ParseException
    {
        path.arcTo(f, f1, f2, flag, flag1, xCenter = currentX = f3, yCenter = currentY = f4);
    }

    protected ExtendedGeneralPath path;
    protected float currentX;
    protected float currentY;
    protected float xCenter;
    protected float yCenter;
    protected int windingRule;
}
