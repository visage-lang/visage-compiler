/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.parsers;

import org.apache.batik.parser.ParseException;

public interface F3PathHandler {

	public void arc(float rx,
                    float ry,
                    float xAxisRotation,
                    boolean largeArcFlag,
                    boolean sweepFlag,
                    float x,
                    float y,
                    boolean absolute)
        throws ParseException;

	public void closePath() throws ParseException;

	public void curvetoCubic(float x1,
                             float y1,
                             float x2,
                             float y2,
                             float x,
                             float y,
                             boolean absolute)
        throws ParseException;

	public void curvetoCubicSmooth(float x2,
                                   float y2,
                                   float x,
                                   float y,
                                   boolean absolute)
        throws ParseException;

	public void curvetoQuadratic(float x1,
                                 float y1,
                                 float x,
                                 float y,
                                 boolean absolute)
        throws ParseException;

	public void curvetoQuadraticSmooth(float x,
                                       float y,
                                       boolean absolute)
        throws ParseException;
	
	public void endPath() throws ParseException;

	public void lineto(float x,
                       float y,
                       boolean absolute)
        throws ParseException;

	public void linetoHorizontal(float x,
                                 boolean absolute)
        throws ParseException;

	public void linetoVertical(float y,
                               boolean absolute)
        throws ParseException;

	public void moveto(float x,
                       float y,
                       boolean absolute)
        throws ParseException;

	public void startPath() throws ParseException;
}
