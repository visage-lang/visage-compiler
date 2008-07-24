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
package com.sun.javafx.api.ui.path.ext.awt.geom;
import java.awt.geom.*;
import java.awt.Shape;
import java.awt.Rectangle;


public class ExtendedGeneralPath 
    implements ExtendedShape, Cloneable
{
    class EPI
        implements ExtendedPathIterator
    {

        public int currentSegment(double ad[])
        {
            int i = types[segNum];
            switch(i)
            {
            case 0: // '\0'
            case 1: // '\001'
                ad[0] = values[valsIdx];
                ad[1] = values[valsIdx + 1];
                break;

            case 2: // '\002'
                ad[0] = values[valsIdx];
                ad[1] = values[valsIdx + 1];
                ad[2] = values[valsIdx + 2];
                ad[3] = values[valsIdx + 3];
                break;

            case 3: // '\003'
                ad[0] = values[valsIdx];
                ad[1] = values[valsIdx + 1];
                ad[2] = values[valsIdx + 2];
                ad[3] = values[valsIdx + 3];
                ad[4] = values[valsIdx + 4];
                ad[5] = values[valsIdx + 5];
                break;

            case 4321: 
                ad[0] = values[valsIdx];
                ad[1] = values[valsIdx + 1];
                ad[2] = values[valsIdx + 2];
                ad[3] = values[valsIdx + 3];
                ad[4] = values[valsIdx + 4];
                ad[5] = values[valsIdx + 5];
                ad[6] = values[valsIdx + 6];
                break;
            }
            return i;
        }

        public int currentSegment(float af[])
        {
            int i = types[segNum];
            switch(i)
            {
            case 0: // '\0'
            case 1: // '\001'
                af[0] = (float)values[valsIdx];
                af[1] = (float)values[valsIdx + 1];
                break;

            case 2: // '\002'
                af[0] = (float)values[valsIdx];
                af[1] = (float)values[valsIdx + 1];
                af[2] = (float)values[valsIdx + 2];
                af[3] = (float)values[valsIdx + 3];
                break;

            case 3: // '\003'
                af[0] = (float)values[valsIdx];
                af[1] = (float)values[valsIdx + 1];
                af[2] = (float)values[valsIdx + 2];
                af[3] = (float)values[valsIdx + 3];
                af[4] = (float)values[valsIdx + 4];
                af[5] = (float)values[valsIdx + 5];
                break;

            case 4321: 
                af[0] = (float)values[valsIdx];
                af[1] = (float)values[valsIdx + 1];
                af[2] = (float)values[valsIdx + 2];
                af[3] = (float)values[valsIdx + 3];
                af[4] = (float)values[valsIdx + 4];
                af[5] = (float)values[valsIdx + 5];
                af[6] = (float)values[valsIdx + 6];
                break;
            }
            return i;
        }

        public int getWindingRule()
        {
            return path.getWindingRule();
        }

        public boolean isDone()
        {
            return segNum == numSeg;
        }

        public void next()
        {
            int i = types[segNum++];
            switch(i)
            {
            case 0: // '\0'
            case 1: // '\001'
                valsIdx += 2;
                break;

            case 2: // '\002'
                valsIdx += 4;
                break;

            case 3: // '\003'
                valsIdx += 6;
                break;

            case 4321: 
                valsIdx += 7;
                break;
            }
        }

        int segNum;
        int valsIdx;

        EPI()
        {
            segNum = 0;
            valsIdx = 0;
        }
    }


    public ExtendedGeneralPath()
    {
        numVals = 0;
        numSeg = 0;
        values = null;
        types = null;
        path = new GeneralPath();
    }

    public ExtendedGeneralPath(int i)
    {
        numVals = 0;
        numSeg = 0;
        values = null;
        types = null;
        path = new GeneralPath(i);
    }

    public ExtendedGeneralPath(int i, int j)
    {
        numVals = 0;
        numSeg = 0;
        values = null;
        types = null;
        path = new GeneralPath(i, j);
    }

    public ExtendedGeneralPath(Shape shape)
    {
        this();
        append(shape, false);
    }

    public synchronized void arcTo(double d, double d1, double d2, boolean flag, 
            boolean flag1, double d3, double d4)
    {
        if(d == 0.0D || d1 == 0.0D)
        {
            lineTo((float)d3, (float)d4);
            return;
        }
        Point2D point2d = path.getCurrentPoint();
        double d5 = point2d.getX();
        double d6 = point2d.getY();
        if(d5 == d3 && d6 == d4)
            return;
        Arc2D arc2d = computeArc(d5, d6, d, d1, d2, flag, flag1, d3, d4);
        if(arc2d == null)
        {
            return;
        } else
        {
            AffineTransform affinetransform = AffineTransform.getRotateInstance(Math.toRadians(d2), arc2d.getCenterX(), arc2d.getCenterY());
            Shape shape = affinetransform.createTransformedShape(arc2d);
            path.append(shape, true);
            makeRoom(7);
            types[numSeg++] = 4321;
            values[numVals++] = d;
            values[numVals++] = d1;
            values[numVals++] = d2;
            values[numVals++] = flag ? 1.0D : 0.0D;
            values[numVals++] = flag1 ? 1.0D : 0.0D;
            values[numVals++] = d3;
            values[numVals++] = d4;
            return;
        }
    }

    public static Arc2D computeArc(double d, double d1, double d2, double d3, 
            double d4, boolean flag, boolean flag1, double d5, double d6)
    {
        double d7 = (d - d5) / 2D;
        double d8 = (d1 - d6) / 2D;
        d4 = Math.toRadians(d4 % 360D);
        double d9 = Math.cos(d4);
        double d10 = Math.sin(d4);
        double d11 = d9 * d7 + d10 * d8;
        double d12 = -d10 * d7 + d9 * d8;
        d2 = Math.abs(d2);
        d3 = Math.abs(d3);
        double d13 = d2 * d2;
        double d14 = d3 * d3;
        double d15 = d11 * d11;
        double d16 = d12 * d12;
        double d17 = d15 / d13 + d16 / d14;
        if(d17 > 1.0D)
        {
            d2 = Math.sqrt(d17) * d2;
            d3 = Math.sqrt(d17) * d3;
            d13 = d2 * d2;
            d14 = d3 * d3;
        }
        double d18 = flag != flag1 ? 1.0D : -1D;
        double d19 = (d13 * d14 - d13 * d16 - d14 * d15) / (d13 * d16 + d14 * d15);
        d19 = d19 >= 0.0D ? d19 : 0.0D;
        double d20 = d18 * Math.sqrt(d19);
        double d21 = d20 * ((d2 * d12) / d3);
        double d22 = d20 * -((d3 * d11) / d2);
        double d23 = (d + d5) / 2D;
        double d24 = (d1 + d6) / 2D;
        double d25 = d23 + (d9 * d21 - d10 * d22);
        double d26 = d24 + (d10 * d21 + d9 * d22);
        double d27 = (d11 - d21) / d2;
        double d28 = (d12 - d22) / d3;
        double d29 = (-d11 - d21) / d2;
        double d30 = (-d12 - d22) / d3;
        double d32 = Math.sqrt(d27 * d27 + d28 * d28);
        double d31 = d27;
        d18 = d28 >= 0.0D ? 1.0D : -1D;
        double d33 = Math.toDegrees(d18 * Math.acos(d31 / d32));
        d32 = Math.sqrt((d27 * d27 + d28 * d28) * (d29 * d29 + d30 * d30));
        d31 = d27 * d29 + d28 * d30;
        d18 = d27 * d30 - d28 * d29 >= 0.0D ? 1.0D : -1D;
        double d34 = Math.toDegrees(d18 * Math.acos(d31 / d32));
        if(!flag1 && d34 > 0.0D)
            d34 -= 360D;
        else
        if(flag1 && d34 < 0.0D)
            d34 += 360D;
        d34 %= 360D;
        d33 %= 360D;
        java.awt.geom.Arc2D.Double double1 = new java.awt.geom.Arc2D.Double();
        double1.x = d25 - d2;
        double1.y = d26 - d3;
        double1.width = d2 * 2D;
        double1.height = d3 * 2D;
        double1.start = -d33;
        double1.extent = -d34;
        return double1;
    }

    public synchronized void moveTo(float f, float f1)
    {
        path.moveTo(f, f1);
        makeRoom(2);
        types[numSeg++] = 0;
        values[numVals++] = f;
        values[numVals++] = f1;
    }

    public synchronized void lineTo(float f, float f1)
    {
        path.lineTo(f, f1);
        makeRoom(2);
        types[numSeg++] = 1;
        values[numVals++] = f;
        values[numVals++] = f1;
    }

    public synchronized void quadTo(float f, float f1, float f2, float f3)
    {
        path.quadTo(f, f1, f2, f3);
        makeRoom(4);
        types[numSeg++] = 2;
        values[numVals++] = f;
        values[numVals++] = f1;
        values[numVals++] = f2;
        values[numVals++] = f3;
    }

    public synchronized void curveTo(float f, float f1, float f2, float f3, float f4, float f5)
    {
        path.curveTo(f, f1, f2, f3, f4, f5);
        makeRoom(6);
        types[numSeg++] = 3;
        values[numVals++] = f;
        values[numVals++] = f1;
        values[numVals++] = f2;
        values[numVals++] = f3;
        values[numVals++] = f4;
        values[numVals++] = f5;
    }

    public synchronized void closePath()
    {
        path.closePath();
        makeRoom(0);
        types[numSeg++] = 4;
    }

    public void append(Shape shape, boolean flag)
    {
        append(shape.getPathIterator(new AffineTransform()), flag);
    }

    public void append(PathIterator pathiterator, boolean flag)
    {
        while(!pathiterator.isDone()) 
        {
            double ad[] = new double[6];
            int i = pathiterator.currentSegment(ad);
            pathiterator.next();
            if(flag && numVals != 0)
            {
                if(i == 0)
                {
                    double d = ad[0];
                    double d1 = ad[1];
                    if(d != values[numVals - 2] || d1 != values[numVals - 1])
                    {
                        i = 1;
                    } else
                    {
                        if(pathiterator.isDone())
                            break;
                        i = pathiterator.currentSegment(ad);
                        pathiterator.next();
                    }
                }
                flag = false;
            }
            switch(i)
            {
            case 4: // '\004'
                closePath();
                break;

            case 0: // '\0'
                moveTo((float)ad[0], (float)ad[1]);
                break;

            case 1: // '\001'
                lineTo((float)ad[0], (float)ad[1]);
                break;

            case 2: // '\002'
                quadTo((float)ad[0], (float)ad[1], (float)ad[2], (float)ad[3]);
                break;

            case 3: // '\003'
                curveTo((float)ad[0], (float)ad[1], (float)ad[2], (float)ad[3], (float)ad[4], (float)ad[5]);
                break;
            }
        }
    }

    public void append(ExtendedPathIterator extendedpathiterator, boolean flag)
    {
        while(!extendedpathiterator.isDone()) 
        {
            double ad[] = new double[7];
            int i = extendedpathiterator.currentSegment(ad);
            extendedpathiterator.next();
            if(flag && numVals != 0)
            {
                if(i == 0)
                {
                    double d = ad[0];
                    double d1 = ad[1];
                    if(d != values[numVals - 2] || d1 != values[numVals - 1])
                    {
                        i = 1;
                    } else
                    {
                        if(extendedpathiterator.isDone())
                            break;
                        i = extendedpathiterator.currentSegment(ad);
                        extendedpathiterator.next();
                    }
                }
                flag = false;
            }
            switch(i)
            {
            case 4: // '\004'
                closePath();
                break;

            case 0: // '\0'
                moveTo((float)ad[0], (float)ad[1]);
                break;

            case 1: // '\001'
                lineTo((float)ad[0], (float)ad[1]);
                break;

            case 2: // '\002'
                quadTo((float)ad[0], (float)ad[1], (float)ad[2], (float)ad[3]);
                break;

            case 3: // '\003'
                curveTo((float)ad[0], (float)ad[1], (float)ad[2], (float)ad[3], (float)ad[4], (float)ad[5]);
                break;

            case 4321: 
                arcTo(ad[0], ad[1], ad[2], ad[3] != 0.0D, ad[4] != 0.0D, ad[5], ad[6]);
                break;
            }
        }
    }

    public synchronized int getWindingRule()
    {
        return path.getWindingRule();
    }

    public void setWindingRule(int i)
    {
        path.setWindingRule(i);
    }

    public synchronized Point2D getCurrentPoint()
    {
        return path.getCurrentPoint();
    }

    public synchronized void reset()
    {
        path.reset();
        numSeg = 0;
        numVals = 0;
    }

    public void transform(AffineTransform affinetransform)
    {
        if(affinetransform.getType() != 0)
            throw new IllegalArgumentException("ExtendedGeneralPaths can not be transformed");
        else
            return;
    }

    public synchronized Shape createTransformedShape(AffineTransform affinetransform)
    {
        return path.createTransformedShape(affinetransform);
    }

    public Rectangle getBounds()
    {
        return path.getBounds();
    }

    public synchronized Rectangle2D getBounds2D()
    {
        return path.getBounds2D();
    }

    public boolean contains(double d, double d1)
    {
        return path.contains(d, d1);
    }

    public boolean contains(Point2D point2d)
    {
        return path.contains(point2d);
    }

    public boolean contains(double d, double d1, double d2, double d3)
    {
        return path.contains(d, d1, d2, d3);
    }

    public boolean contains(Rectangle2D rectangle2d)
    {
        return path.contains(rectangle2d);
    }

    public boolean intersects(double d, double d1, double d2, double d3)
    {
        return path.intersects(d, d1, d2, d3);
    }

    public boolean intersects(Rectangle2D rectangle2d)
    {
        return path.intersects(rectangle2d);
    }

    public PathIterator getPathIterator(AffineTransform affinetransform)
    {
        return path.getPathIterator(affinetransform);
    }

    public PathIterator getPathIterator(AffineTransform affinetransform, double d)
    {
        return path.getPathIterator(affinetransform, d);
    }

    public ExtendedPathIterator getExtendedPathIterator()
    {
        return new EPI();
    }

    public Object clone()
    {
        try
        {
            ExtendedGeneralPath extendedgeneralpath = (ExtendedGeneralPath)super.clone();
            extendedgeneralpath.path = (GeneralPath)path.clone();
            extendedgeneralpath.values = new double[values.length];
            System.arraycopy(extendedgeneralpath.values, 0, values, 0, values.length);
            extendedgeneralpath.numVals = numVals;
            extendedgeneralpath.types = new int[types.length];
            System.arraycopy(extendedgeneralpath.types, 0, types, 0, types.length);
            extendedgeneralpath.numSeg = numSeg;
            return extendedgeneralpath;
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            return null;
        }
    }

    private void makeRoom(int i)
    {
        if(values == null)
        {
            values = new double[2 * i];
            types = new int[2];
            numVals = 0;
            numSeg = 0;
            return;
        }
        if(numVals + i > values.length)
        {
            int j = values.length * 2;
            if(j < numVals + i)
                j = numVals + i;
            double ad[] = new double[j];
            System.arraycopy(values, 0, ad, 0, numVals);
            values = ad;
        }
        if(numSeg == types.length)
        {
            int ai[] = new int[types.length * 2];
            System.arraycopy(types, 0, ai, 0, types.length);
            types = ai;
        }
    }

    protected GeneralPath path;
    int numVals;
    int numSeg;
    double values[];
    int types[];

    public GeneralPath getPath() {
        return path;
    }
}
