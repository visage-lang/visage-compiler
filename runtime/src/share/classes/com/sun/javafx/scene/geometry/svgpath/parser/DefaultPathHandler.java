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
package com.sun.javafx.scene.geometry.svgpath.parser;

public class DefaultPathHandler
    implements PathHandler
{

    protected DefaultPathHandler()
    {
    }

    public void startPath()
        throws ParseException
    {
    }

    public void endPath()
        throws ParseException
    {
    }

    public void movetoRel(float f, float f1)
        throws ParseException
    {
    }

    public void movetoAbs(float f, float f1)
        throws ParseException
    {
    }

    public void closePath()
        throws ParseException
    {
    }

    public void linetoRel(float f, float f1)
        throws ParseException
    {
    }

    public void linetoAbs(float f, float f1)
        throws ParseException
    {
    }

    public void linetoHorizontalRel(float f)
        throws ParseException
    {
    }

    public void linetoHorizontalAbs(float f)
        throws ParseException
    {
    }

    public void linetoVerticalRel(float f)
        throws ParseException
    {
    }

    public void linetoVerticalAbs(float f)
        throws ParseException
    {
    }

    public void curvetoCubicRel(float f, float f1, float f2, float f3, float f4, float f5)
        throws ParseException
    {
    }

    public void curvetoCubicAbs(float f, float f1, float f2, float f3, float f4, float f5)
        throws ParseException
    {
    }

    public void curvetoCubicSmoothRel(float f, float f1, float f2, float f3)
        throws ParseException
    {
    }

    public void curvetoCubicSmoothAbs(float f, float f1, float f2, float f3)
        throws ParseException
    {
    }

    public void curvetoQuadraticRel(float f, float f1, float f2, float f3)
        throws ParseException
    {
    }

    public void curvetoQuadraticAbs(float f, float f1, float f2, float f3)
        throws ParseException
    {
    }

    public void curvetoQuadraticSmoothRel(float f, float f1)
        throws ParseException
    {
    }

    public void curvetoQuadraticSmoothAbs(float f, float f1)
        throws ParseException
    {
    }

    public void arcRel(float f, float f1, float f2, boolean flag, boolean flag1, float f3, float f4)
        throws ParseException
    {
    }

    public void arcAbs(float f, float f1, float f2, boolean flag, boolean flag1, float f3, float f4)
        throws ParseException
    {
    }

    public static final PathHandler INSTANCE = new DefaultPathHandler();

}
