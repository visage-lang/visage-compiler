/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.parsers;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathHandler;

public class PathHandlerAdapter implements PathHandler {
	private F3PathHandler handler;
	
	public F3PathHandler getHandler() {
		return handler;
	}

	public void setHandler(F3PathHandler handler) {
		this.handler = handler;
	}

	public void arcAbs(float rx,
                       float ry,
                       float xAxisRotation,
                       boolean largeArcFlag,
                       boolean sweepFlag,
                       float x,
                       float y)
        throws ParseException
    {
        handler.arc(rx,
                ry,
                xAxisRotation,
                largeArcFlag,
                sweepFlag,
                x,
                y,
                true);
	}

	public void arcRel(float rx,
                       float ry,
                       float xAxisRotation,
                       boolean largeArcFlag,
                       boolean sweepFlag,
                       float x,
                       float y)
        throws ParseException
    {
        handler.arc(rx,
                    ry,
                    xAxisRotation,
                    largeArcFlag,
                    sweepFlag,
                    x,
                    y,
                    false);
	}

	public void closePath() throws ParseException {
        handler.closePath();
	}

	public void curvetoCubicAbs(float x1,
                                float y1,
                                float x2,
                                float y2,
                                float x,
                                float y)
        throws ParseException
    {
        handler.curvetoCubic(x1, y1, x2, y2, x, y, true);
	}

	public void curvetoCubicRel(float x1,
                                float y1,
                                float x2,
                                float y2,
                                float x,
                                float y)
        throws ParseException
    {
        handler.curvetoCubic(x1, y1, x2, y2, x, y, false);
	}

	public void curvetoCubicSmoothAbs(float x2,
                                      float y2,
                                      float x,
                                      float y)
        throws ParseException
    {
        handler.curvetoCubicSmooth(x2, y2, x, y, true);
	}

	public void curvetoCubicSmoothRel(float x2,
                                      float y2,
                                      float x,
                                      float y)
        throws ParseException
    {
        handler.curvetoCubicSmooth(x2, y2, x, y, false);
	}

	public void curvetoQuadraticAbs(float x1,
                                    float y1,
                                    float x,
                                    float y)
        throws ParseException
    {
        handler.curvetoQuadratic(x1, y1, x, y, true);
	}

	public void curvetoQuadraticRel(float x1,
                                    float y1,
                                    float x,
                                    float y)
        throws ParseException
    {
        handler.curvetoQuadratic(x1, y1, x, y, false);
	}

	public void curvetoQuadraticSmoothAbs(float x, float y) throws ParseException {
        handler.curvetoQuadraticSmooth(x, y, true);
	}

	public void curvetoQuadraticSmoothRel(float x, float y) throws ParseException {
        handler.curvetoQuadraticSmooth(x, y, false);
	}

	public void endPath() throws ParseException {
        handler.endPath();
	}

	public void linetoAbs(float x, float y) throws ParseException {
        handler.lineto(x, y, true);
	}

	public void linetoRel(float x, float y) throws ParseException {
        handler.lineto(x, y, false);
	}

	public void linetoHorizontalAbs(float x) throws ParseException {
        handler.linetoHorizontal(x, true);
	}

	public void linetoHorizontalRel(float x) throws ParseException {
        handler.linetoHorizontal(x, false);
	}

	public void linetoVerticalAbs(float y) throws ParseException {
        handler.linetoVertical(y, true);
	}

	public void linetoVerticalRel(float y) throws ParseException {
        handler.linetoVertical(y, false);
	}

	public void movetoAbs(float x, float y) throws ParseException {
        handler.moveto(x, y, true);
	}

	public void movetoRel(float x, float y) throws ParseException {
        handler.moveto(x, y, false);
	}

	public void startPath() throws ParseException {
        handler.startPath();
	}
}
