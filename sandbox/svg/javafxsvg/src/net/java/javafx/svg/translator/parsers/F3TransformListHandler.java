/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.parsers;

import org.apache.batik.parser.ParseException;

public interface F3TransformListHandler {
	
	public void startTransformList() throws ParseException;

	public void matrix(float a,
                       float b,
                       float c,
                       float d,
                       float e,
                       float f)
        throws ParseException;
	
	public void rotate(float theta) throws ParseException;

	public void rotateXY(float theta, float cx, float cy) throws ParseException;

	public void translate(float tx) throws ParseException;
	
	public void translateXY(float tx, float ty) throws ParseException;

	public void scale(float sx) throws ParseException;

	public void scaleXY(float sx, float sy) throws ParseException;

	public void skewX(float skx) throws ParseException;

	public void skewY(float sky) throws ParseException;

	public void endTransformList() throws ParseException;
}
