/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.parsers;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.TransformListHandler;

public class TransformListHandlerAdapter implements TransformListHandler {
	private F3TransformListHandler handler;

	public F3TransformListHandler getHandler() {
		return handler;
	}

	public void setHandler(F3TransformListHandler handler) {
		this.handler = handler;
	}
	
	public void startTransformList() throws ParseException {
		handler.startTransformList();
	}

	public void matrix(float a,
                       float b,
                       float c,
                       float d,
                       float e,
                       float f)
        throws ParseException {
		handler.matrix(a, b, c, d, e, f);
	}

	public void rotate(float theta) throws ParseException {
		handler.rotate(theta);
	}

	public void rotate(float theta, float cx, float cy) throws ParseException {
		handler.rotateXY(theta, cx, cy);
	}

	public void translate(float tx) throws ParseException {
		handler.translate(tx);
	}

	public void translate(float tx, float ty) throws ParseException {
		handler.translateXY(tx, ty);
	}

	public void scale(float sx) throws ParseException {
		handler.scale(sx);
	}

	public void scale(float sx, float sy) throws ParseException {
		handler.scaleXY(sx, sy);
	}

	public void skewX(float skx) throws ParseException {
		handler.skewX(skx);
	}

	public void skewY(float sky) throws ParseException {
		handler.skewY(sky);
	}

	public void endTransformList() throws ParseException {
		handler.endTransformList();
	}
}
