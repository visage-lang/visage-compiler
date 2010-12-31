/*
 * Copyright (c) 2010-2011, Visage Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name Visage nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package javafx.lang;

import java.lang.Comparable;
import java.lang.IllegalStateException;
import java.lang.StringBuilder;
import java.lang.ThreadLocal;
import java.text.DecimalFormat;
import java.util.Formattable;
import java.util.Formatter;
import java.util.FormattableFlags;
import javafx.util.Bits;
import java.lang.Appendable;

/**
 * Used to specify a length of zero magnitude.
 *
 * @profile common
 */
public def ZERO = Length {}

/**
 * Constant for disallowing certain unit conversions in Length member methods.
 *
 * @profile common
 */
public def UNKNOWN = Double.NaN;

// internal static contants
def DPS_PER_IN:Double = 96;
def DPS_PER_CM:Double = DPS_PER_IN / 2.54;
def DPS_PER_MM:Double = DPS_PER_CM / 10;
def DPS_PER_PT:Double = DPS_PER_IN / 72;
def DPS_PER_PC:Double = DPS_PER_IN / 6;
def DF = ThreadLocal {
    override function initialValue():Object {
        return new DecimalFormat("#.######");
    }
}

// script-level "static" functions below

/**
 * Factory method that returns a Length instance for a specified
 * value and unit type.
 *
 * @param value the magnitude of the length
 * @param unit the type of the length
 * @return a Length instance of the specified value and unit
 * @profile common
 */
public function valueOf(value:Double, unit:LengthUnit):Length {
    if (value == 0) {
        ZERO
    } else if (unit == LengthUnit.PIXEL) Length {
        pixels: value
    } else if (unit == LengthUnit.DENSITY_INDEPENDENT_PIXEL) Length {
        dps: value
    } else if (unit == LengthUnit.SCALE_INDEPENDENT_PIXEL) Length {
        sps: value
    } else if (unit == LengthUnit.EM) Length {
        ems: value
    } else if (unit == LengthUnit.PERCENTAGE) Length {
        percentage: value
    } else if (unit == LengthUnit.INCH) Length {
        dps: value * DPS_PER_IN;
    } else if (unit == LengthUnit.CENTIMETER) Length {
        dps: value * DPS_PER_CM;
    } else if (unit == LengthUnit.MILLIMETER) Length {
        dps: value * DPS_PER_MM;
    } else if (unit == LengthUnit.POINT) Length {
        dps: value * DPS_PER_PT;
    } else if (unit == LengthUnit.PICA) Length {
        dps: value * DPS_PER_PC;
    } else {
        throw new IllegalStateException("Unknown length unit: {unit}");
    }

}
public function valueOf(value:Float, unit:LengthUnit):Length {
    valueOf(value as Double, unit)
}

public def TYPE_INFO = com.sun.javafx.runtime.TypeInfo.makeAndRegisterTypeInfo(ZERO);

/**
 * A class that defines a length value.  Lengths are immutable, but can be
 * created in several different ways:
 * <blockquote><pre>
 * Length l = 2cm + 5mm;
 * Length l = 2.5cm;
 * Length l = 25mm;
 * </pre></blockquote>
 * <p>
 * Length instances can also contain several different types of units:
 * <ul>
 * <li>pixel - Exact screen pixels.  This should not be used for layouts that
 * need to be resizable or device portable.
 * <li>dp - Density-independent pixels - Reference pixel for the target device.
 * Will scale to a whole or even fractional pixel value based on the device
 * viewing distance and density.  This is approximately one pixel on a device
 * with a density of 96dpi at arm's length.  For example:
 * <ul>
 * <li>Desktop - 28 inches away, .26mm pixel (96 dpi), 1dp = 1px
 * <li>Medium Density Mobile - 17 inches away, .16mm pixel (160 dpi), 1dp = 1px
 * <li>High Density Mobile - 17 inches away, .16mm pixel (240 dpi), 1dp = 1.5px
 * <li>55" HD TV - 10 feet away, .73mm pixel (35 dpi), 1dp = 1.5px
 * </ul>
 * <li>sp - Scale-independent pixels - Similar to dp, but also relative to
 * the user font scale.  For the default font scale will be 1 to 1 with dp.
 * <li>em - Typographic measure relative to the enclosing element's font height.
 * <li>percentage - Percentage of the enclosing element's length or size.
 * </ul>
 * <p>
 * Inches, centimeters, millimeters, points, and picas are losslessly converted
 * into density-independent pixels at a scale of 96dpi (preserving compatibility
 * with CSS).
 * <p>
 * Upon creation (either as a literal or via the {@link #valueOf(Double, LengthUnit)}
 * function, only one of the unit types may be in use.  However, subsequent operations
 * (addition/subtraction) may result in complex lengths with multiple unit types.
 * <p>
 * Complex lengths or lengths with multiple types may be converted to compatible
 * types by using one of the to*Length() methods, in which case they can be directly
 * compared.  Comparison of lengths with complex or incompatible units may
 * throw a {@link ComplexLengthException}.
 *
 * @author Stephen Chin <steveonjava@gmail.com>
 * @profile common
 */
public class Length extends Comparable, Formattable {
    var pixels:Double;
    var dps:Double;
    var sps:Double;
    var ems:Double;
    var percentage:Double;

    /**
     * Returns the number of inches for this length, converting between like
     * measures only (centimeters, millimeters, points, picas, and density-independent pixels).
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @return the number of inches for this length.
     * @profile common
     */
    public function toInches():Double {
        toInches(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of inches for this length, possibly doing pixel
     * conversion with the density factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the number of inches for this length.
     * @profile common
     */
    public function toInches(density:Double):Double {
        toInches(density, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of inches for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of inches for this length.
     * @profile common
     */
    public function toInches(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        toDensityIndependentPixels(density, scale, fontHeight, containerLength) / DPS_PER_IN;
    }
    
    /**
     * Returns the number of centimeters for this length, converting between like
     * measures only (inches, millimeters, points, picas, and density-independent pixels).
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @return the number of centimeters for this length.
     * @profile common
     */
    public function toCentimeters():Double {
        toCentimeters(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of centimeters for this length, possibly doing pixel
     * conversion with the density factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the number of centimeters for this length.
     * @profile common
     */
    public function toCentimeters(density:Double):Double {
        toCentimeters(density, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of centimeters for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of centimeters for this length.
     * @profile common
     */
    public function toCentimeters(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        toDensityIndependentPixels(density, scale, fontHeight, containerLength) / DPS_PER_CM;
    }

    /**
     * Returns the number of millimeters for this length, converting between like
     * measures only (inches, centimeters, points, picas, and density-independent pixels).
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @return the number of millimeters for this length.
     * @profile common
     */
    public function toMillimeters():Double {
        toMillimeters(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of millimeters for this length, possibly doing pixel
     * conversion with the density factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the number of millimeters for this length.
     * @profile common
     */
    public function toMillimeters(density:Double):Double {
        toMillimeters(density, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of millimeters for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of millimeters for this length.
     * @profile common
     */
    public function toMillimeters(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        toDensityIndependentPixels(density, scale, fontHeight, containerLength) / DPS_PER_MM;
    }

    /**
     * Returns the number of points for this length, converting between like
     * measures only (inches, centimeters, millimeters, picas, and density-independent pixels).
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @return the number of points for this length.
     * @profile common
     */
    public function toPoints():Double {
        toPoints(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of points for this length, possibly doing pixel
     * conversion with the density factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the number of points for this length.
     * @profile common
     */
    public function toPoints(density:Double):Double {
        toPoints(density, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of points for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of points for this length.
     * @profile common
     */
    public function toPoints(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        toDensityIndependentPixels(density, scale, fontHeight, containerLength) / DPS_PER_PT;
    }

    /**
     * Returns the number of picas for this length, converting between like
     * measures only (inches, centimeters, millimeters, points, and density-independent pixels).
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @return the number of picas for this length.
     * @profile common
     */
    public function toPicas():Double {
        toPicas(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of picas for this length, possibly doing pixel
     * conversion with the density factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the number of picas for this length.
     * @profile common
     */
    public function toPicas(density:Double):Double {
        toPicas(density, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of picas for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of picas for this length.
     * @profile common
     */
    public function toPicas(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        toDensityIndependentPixels(density, scale, fontHeight, containerLength) / DPS_PER_PC;
    }

    /**
     * Returns the number of ems for this length.
     * <p>
     * If this length is not stored in scale-independent pixels,
     * an {@link UnconvertableLengthException} will be thrown.
     *
     * @return the number of ems for this length.
     * @profile common
     */
    public function toEms():Double {
        toEms(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of ems for this length, possibly doing pixel
     * conversion with the fontWeight factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the number of ems for this length.
     * @profile common
     */
    public function toEms(fontHeight:Double):Double {
        toEms(UNKNOWN, UNKNOWN, fontHeight, UNKNOWN);
    }

    /**
     * Returns the number of ems for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of ems for this length.
     * @profile common
     */
    public function toEms(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        if (pixels == 0 and dps == 0 and sps == 0 and percentage == 0) {
            return ems;
        } else {
            if (fontHeight == UNKNOWN) throw UnconvertableLengthException {
                allowed: LengthUnit.EM
                length: this
            }
            return toPixels(density, scale, fontHeight, containerLength) / fontHeight;
        }
    }

    /**
     * Returns a new Length object represented as ems, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of ems for this length.
     * @profile common
     */
    public function toEmsLength(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Length {
        if (this == ZERO) return this;
        Length {
            ems: toEms(density, scale, fontHeight, containerLength)
        }
    }

    /**
     * Returns the number of pixels for this length.
     * <p>
     * If this length is not stored in pixels,
     * an {@link UnconvertableLengthException} will be thrown.
     *
     * @return the number of pixels for this length.
     * @profile common
     */
    public function toPixels():Double {
        if (dps != 0 or sps != 0 or ems != 0 or percentage != 0) {
            throw UnconvertableLengthException {
                allowed: LengthUnit.PIXEL
                length: this
            }
        }
        return pixels;
    }

    /**
     * Returns the number of pixels for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of pixels for this length.
     * @profile common
     */
    public function toPixels(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        var p = pixels;
        var disallowed:LengthUnit[];
        if (dps != 0) {
            if (density == UNKNOWN) {
                insert [LengthUnit.DENSITY_INDEPENDENT_PIXEL, LengthUnit.INCH, LengthUnit.CENTIMETER, LengthUnit.MILLIMETER, LengthUnit.POINT, LengthUnit.PICA] into disallowed;
            } else {
                p += dps * density;
            }
        }
        if (sps != 0) {
            if (density == UNKNOWN or scale == UNKNOWN) {
                if (density == UNKNOWN and sizeof disallowed == 0) {
                    insert LengthUnit.DENSITY_INDEPENDENT_PIXEL into disallowed;
                }
                if (scale == UNKNOWN) {
                    insert LengthUnit.SCALE_INDEPENDENT_PIXEL into disallowed;
                }
            } else {
                p += sps * scale * density;
            }
        }
        if (ems != 0) {
            if (fontHeight == UNKNOWN) {
                insert LengthUnit.EM into disallowed;
            } else {
                p += ems * fontHeight;
            }
        }
        if (percentage != 0) {
            if (containerLength == UNKNOWN) {
                insert LengthUnit.PERCENTAGE into disallowed;
            } else {
                p += percentage / 100 * containerLength;
            }
        }
        if (sizeof disallowed > 0) throw UnconvertableLengthException {
            disallowed: disallowed
            length: this
        }
        return p;
    }

    /**
     * Returns a new Length object represented as pixels, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of pixels for this length.
     * @profile common
     */
    public function toPixelLength(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Length {
        if (this == ZERO) return this;
        Length {
            pixels: toPixels(density, scale, fontHeight, containerLength)
        }
    }

    /**
     * Returns the number of density-independent pixels for this length, converting between like
     * measures only (inches, centimeters, millimeters, points, and picas).
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @return the number of density-independent pixels for this length.
     * @profile common
     */
    public function toDensityIndependentPixels():Double {
        toDensityIndependentPixels(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of density-independent pixels for this length, possibly doing pixel
     * conversion with the density factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the number of density-independent pixels for this length.
     * @profile common
     */
    public function toDensityIndependentPixels(density:Double):Double {
        toDensityIndependentPixels(density, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of density-independent pixels for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of density-independent pixels for this length.
     * @profile common
     */
    public function toDensityIndependentPixels(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        if (pixels == 0 and sps == 0 and ems == 0 and percentage == 0) {
            return dps;
        } else {
            if (density == UNKNOWN) throw UnconvertableLengthException {
                allowed: LengthUnit.DENSITY_INDEPENDENT_PIXEL
                length: this
            }
            return toPixels(density, scale, fontHeight, containerLength) / density;
        }
    }

    /**
     * Returns a new Length object represented as density-independent pixels, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of density-independent pixels for this length.
     * @profile common
     */
    public function toDensityIndependentLength(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Length {
        if (this == ZERO) return this;
        Length {
            dps: toDensityIndependentPixels(density, scale, fontHeight, containerLength)
        }
    }

    /**
     * Returns the number of scale-independent pixels for this length.
     * <p>
     * If this length is not stored in scale-independent pixels,
     * an {@link UnconvertableLengthException} will be thrown.
     *
     * @return the number of scale-independent pixels for this length.
     * @profile common
     */
    public function toScaleIndependentPixels():Double {
        toScaleIndependentPixels(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of scale-independent pixels for this length, possibly doing
     * unit conversion with the scale factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the number of scale-independent pixels for this length.
     * @profile common
     */
    public function toScaleIndependentPixels(scale:Double):Double {
        toScaleIndependentPixels(UNKNOWN, scale, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the number of scale-independent pixels for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of scale-independent pixels for this length.
     * @profile common
     */
    public function toScaleIndependentPixels(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        if (pixels == 0 and dps == 0 and ems == 0 and percentage == 0) {
            return sps;
        } else {
            if (scale == UNKNOWN) throw UnconvertableLengthException {
                allowed: LengthUnit.SCALE_INDEPENDENT_PIXEL
                length: this
            }
            if (pixels == 0 and ems == 0 and percentage == 0) {
                return sps + dps / scale;
            } else {
                if (density == UNKNOWN) throw UnconvertableLengthException {
                    allowed: [LengthUnit.SCALE_INDEPENDENT_PIXEL, LengthUnit.DENSITY_INDEPENDENT_PIXEL]
                    length: this
                }
                return toPixels(density, scale, fontHeight, containerLength) / density / scale;
            }
        }
    }

    /**
     * Returns a new Length object represented as scale-independent pixels, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the number of scale-independent pixels for this length.
     * @profile common
     */
    public function toScaleIndependentLength(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Length {
        if (this == ZERO) return this;
        Length {
            sps: toScaleIndependentPixels(density, scale, fontHeight, containerLength)
        }
    }

    /**
     * Returns the percentage for this length.
     * <p>
     * If this length is not stored as a percentage,
     * an {@link UnconvertableLengthException} will be thrown.
     *
     * @return the percentage for this length.
     * @profile common
     */
    public function toPercentage():Double {
        toPercentage(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    /**
     * Returns the percentage for this length, possibly doing pixel
     * conversion with the containerLength factor.
     * <p>
     * If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @return the percentage for this length.
     * @profile common
     */
    public function toPercentage(containerLength:Double):Double {
        toPercentage(UNKNOWN, UNKNOWN, UNKNOWN, containerLength);
    }

    /**
     * Returns the percentage for this length, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the percentage for this length.
     * @profile common
     */
    public function toPercentage(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Double {
        if (pixels == 0 and dps == 0 and sps == 0 and ems == 0) {
            return percentage;
        } else {
            if (containerLength == UNKNOWN) throw UnconvertableLengthException {
                allowed: LengthUnit.PERCENTAGE
                length: this
            }
            return toPixels(density, scale, fontHeight, containerLength) * 100 / containerLength;
        }
    }

    /**
     * Returns a new Length object represented as a simple percentage, converting between
     * different measures as necessary.
     * <p>
     * A value of {@link #UNKNOWN} can be passed in for any of the conversion
     * factors.  If this length cannot be converted, an {@link UnconvertableLengthException}
     * will be thrown.
     *
     * @param density Ratio of pixels to density-independent pixels (px/dp)
     * @param scale Font scaling factor in effect for the viewport of this length
     * @param fontHeight Font height (or size) of the nearest enclosing container
     * @param conainerLength Length (in pixels) of the enclosing container
     * @return the percentage for this length.
     * @profile common
     */
    public function toPercentageLength(density:Double, scale:Double, fontHeight:Double, containerLength:Double):Length {
        if (this == ZERO) return this;
        Length {
            percentage: toPercentage(density, scale, fontHeight, containerLength)
        }
    }
    
    /**
     * Add this instance and another Length instance to return a new Length instance.
     * This function does not change the value of the called Length instance.
     *
     * @profile common
     */
    public function add(other:Length):Length {
        return Length {
            pixels: pixels + other.pixels
            dps: dps + other.dps
            sps: sps + other.sps
            ems: ems + other.ems
            percentage: percentage + other.percentage
        }
    }

    /**
     * Subtract other Length instance from this instance to return a new Length instance.
     * This function does not change the value of the called Length instance.
     *
     * @profile common
     */
    public function sub(other:Length):Length {
        return Length {
            pixels: pixels - other.pixels
            dps: dps - other.dps
            sps: sps - other.sps
            ems: ems - other.ems
            percentage: percentage - other.percentage
        }
    }

    /**
     * Multiply this instance with a number to return a new Length instance.
     * This function does not change the value of the called Length instance.
     *
     * @profile common
     */ 
    public function mul(n:Number):Length {
        return Length {
            pixels: pixels * n
            dps: dps * n
            sps: sps * n
            ems: ems * n
            percentage: percentage * n
        }
    }


    /**
     * Divide this instance by a number to return a new Length instance.
     * This function does not change the value of the called Length instance.
     *
     * @profile common
     */     
    public function div(n:Number):Length {
        if (n == 0) {
            throw new java.lang.ArithmeticException("/ by zero");
        }
        
        return Length {
            pixels: pixels / n
            dps: dps / n
            sps: sps / n
            ems: ems / n
            percentage: percentage / n
        }
    }

    /** 
     * Divide this instance by another Length to return the ratio.
     * <p>
     * If the second length is 0, an "/ by zero" exception will be thrown.
     * <p>
     * If the two lengths are not of comparable types, a
     * {@link ComplexLengthException} will be thrown.
     *
     * @profile common
     */     
    public function div(other:Length):Double {
        var numberOfDenominators = 0;
        var denominator:Double;
        if (other.pixels != 0) {
            denominator = other.pixels;
            numberOfDenominators++;
        }
        if (other.dps != 0) {
            denominator = other.dps;
            numberOfDenominators++;
        }
        if (other.sps != 0) {
            denominator = other.sps;
            numberOfDenominators++;
        }
        if (other.ems != 0) {
            denominator = other.ems;
            numberOfDenominators++;
        }
        if (other.percentage != 0) {
            denominator = other.percentage;
            numberOfDenominators++;
        }
        if (numberOfDenominators == 0) {
            throw new java.lang.ArithmeticException("/ by zero");
        }
        var numberOfNumerators = 0;
        var numerator:Double;
        if (pixels != 0) {
            numerator = pixels;
            numberOfNumerators++;
        }
        if (dps != 0) {
            numerator = dps;
            numberOfNumerators++;
        }
        if (sps != 0) {
            numerator = sps;
            numberOfNumerators++;
        }
        if (ems != 0) {
            numerator = ems;
            numberOfNumerators++;
        }
        if (percentage != 0) {
            numerator = percentage;
            numberOfNumerators++;
        }
        if (numberOfNumerators > 1 or numberOfDenominators > 1) {
            throw ComplexLengthException {
                lengths: [this, other]
                operation: "divide"
            }
        }
        return numerator / denominator;
    }

    /** 
     * Return a new Length instance which has a negative magnitude
     * from this instance.  For example, <code>(50mm).negate()</code> returns
     * a Length of -50 millimeters.
     * This function does not change the value of the called Length instance.
     *
     * @profile common
     */
    public function negate():Length {
        return Length {
            pixels: -pixels
            dps: -dps
            sps: -sps
            ems: -ems
            percentage: -percentage
        }
    }

    /**
     * Prints out a string representation of the length with a precision of
     * up to 6 digits (internally lengths are represented as doubles with much
     * higher precision).
     * <p>
     * Incompatible measures will be represented as an arithmetic expression.
     * To display in single units, use one of the to*Length() functions to
     * convert in advance.
     *
     * @profile common
     */        
    override function toString():String {
        def sb = StringBuilder {};
        formatLength(sb, false, false, false, -1, -1);
        sb.toString();
    }

    /**
     * Formatting function that allows lengths to be properly formatted by
     * printf, String.format, or the JavaFX format primitive.
     * <p>
     * Width sets the minimum width of this field, following the justification
     * rules set in flags.
     * <p>
     * Precision can be used to specify exactly how many digits should appear
     * to the right of the decimal, including zero padding.
     * <p>
     * The alternate form (specified in flags) is to choose the shortest
     * length string in the case where ultiple measures can be used (such
     * as choosing an even number of centimeters rather than a fractional inch).
     * The default is to always emit in the base unit.
     * <p>
     * Finally, the uppercase flag is also supported, but only applies to the
     * length suffix.
     */
    override function formatTo(formatter:Formatter, flags:Integer, width:Integer, precision:Integer):Void {
        def shortestForm = Bits.bitAnd(flags, FormattableFlags.ALTERNATE) != 0;
        def uppercase = Bits.bitAnd(flags, FormattableFlags.UPPERCASE) != 0;
        def leftJustify = Bits.bitAnd(flags, FormattableFlags.LEFT_JUSTIFY) != 0;
        formatLength(formatter.out(), shortestForm, uppercase, leftJustify, width, precision);
    }

    function formatLength(a:Appendable, shortestForm:Boolean, upper:Boolean, leftJustify:Boolean, width:Integer, precision:Integer) {
        def sb = StringBuilder {};
        def df = DF.get() as DecimalFormat;
        df.setMinimumFractionDigits(precision);
        var firstTerm = true;
        if (pixels != 0) {
            firstTerm = false;
            sb.append(df.format(pixels));
            sb.append(if (upper) "PX" else "px");
        }
        if (dps != 0) {
            if (not firstTerm) sb.append(" + ") else firstTerm = false;
            var shortest = "{df.format(dps)}{if (upper) "DP" else "dp"}";
            if (shortestForm) {
                def shortDot = shortest.indexOf('.');
                var shortDec = shortest.length() - (if (shortDot == -1) 1000 else shortDot);
                def inStr = "{df.format(dps / DPS_PER_IN)}{if (upper) "IN" else "in"}";
                def inDot = inStr.indexOf('.');
                def inDec = inStr.length() - (if (inDot == -1) 1000 else inDot);
                if (inDec < shortDec) {shortest = inStr; shortDec = inDec}
                def cmStr = "{df.format(dps / DPS_PER_CM)}{if (upper) "CM" else "cm"}";
                def cmDot = cmStr.indexOf('.');
                def cmDec = cmStr.length() - (if (cmDot == -1) 1000 else cmDot);
                if (cmDec < shortDec) {shortest = cmStr; shortDec = cmDec}
                def mmStr = "{df.format(dps / DPS_PER_MM)}{if (upper) "MM" else "mm"}";
                def mmDot = mmStr.indexOf('.');
                def mmDec = mmStr.length() - (if (mmDot == -1) 1000 else mmDot);
                if (mmDec < shortDec) {shortest = mmStr; shortDec = mmDec}
                def pcStr = "{df.format(dps / DPS_PER_PC)}{if (upper) "PC" else "pc"}";
                def pcDot = pcStr.indexOf('.');
                def pcDec = pcStr.length() - (if (pcDot == -1) 1000 else pcDot);
                if (pcDec < shortDec) {shortest = pcStr; shortDec = pcDec}
                def ptStr = "{df.format(dps / DPS_PER_PT)}{if (upper) "PT" else "pt"}";
                def ptDot = ptStr.indexOf('.');
                def ptDec = ptStr.length() - (if (ptDot == -1) 1000 else ptDot);
                if (ptDec < shortDec) {shortest = ptStr}
            }
            sb.append(shortest);
        }
        if (sps != 0) {
            if (not firstTerm) sb.append(" + ") else firstTerm = false;
            sb.append(df.format(sps));
            sb.append(if (upper) "SP" else "sp");
        }
        if (ems != 0) {
            if (not firstTerm) sb.append(" + ") else firstTerm = false;
            sb.append(df.format(ems));
            sb.append(if (upper) "EM" else "em");
        }
        if (percentage != 0) {
            if (not firstTerm) sb.append(" + ") else firstTerm = false;
            sb.append(df.format(percentage));
            sb.append("%");
        }
        if (firstTerm) {
            sb.append(if (upper) "0PX" else "0px");
        }
        def len = sb.length();
        if (len < width) {
            if (leftJustify) {
                for (i in [1..width-len]) sb.append(' ');
            } else {
                for (i in [1..width-len]) sb.<<insert>>(0, ' ');
            }
        }
        a.append(sb);
    }

    /** 
     * Returns true if the specified length is less than (<) this instance.
     * <p>
     * If the two lengths are not of comparable types, a
     * {@link ComplexLengthException} will be thrown.
     *
     * @profile common
     */
    public function lt(other:Length):Boolean {
        try {
            return compareTo(other) < 0;
        } catch (e:ComplexLengthException) {
            e.operation = "lt";
            throw e;
        }
    }

    /** 
     * Returns true if the specified length is less than or equal to (<=) this instance.
     * <p>
     * If the two lengths are not of comparable types, a
     * {@link ComplexLengthException} will be thrown.
     *
     * @profile common
     */
    public function le(other:Length):Boolean {
        try {
            return compareTo(other) <= 0;
        } catch (e:ComplexLengthException) {
            e.operation = "le";
            throw e;
        }
    }

    /** 
     * Returns true if the specified length is greater than (>) this instance.
     * <p>
     * If the two lengths are not of comparable types, a
     * {@link ComplexLengthException} will be thrown.
     *
     * @profile common
     */
    public function gt(other:Length):Boolean {
        try {
            return compareTo(other) > 0;
        } catch (e:ComplexLengthException) {
            e.operation = "gt";
            throw e;
        }
    }

    /**
     * Returns true if the specified length is greater than or equal to (>=) this instance.
     * <p>
     * If the two lengths are not of comparable types, a
     * {@link ComplexLengthException} will be thrown.
     *
     * @profile common
     */
    public function ge(other:Length):Boolean {
        try {
            return compareTo(other) >= 0;
        } catch (e:ComplexLengthException) {
            e.operation = "ge";
            throw e;
        }
    }

    /**
     * Compares the passed in length to this length, returning 0 if they are
     * equal, -1 if this is shorter and 1 if this is longer.
     * <p>
     * If the two lengths are not of comparable types, a
     * {@link ComplexLengthException} will be thrown.
     *
     * @profile common
     */
    override function compareTo(obj:Object):Integer {
        def l = obj as Length;
        if (pixels == l.pixels and dps == l.dps and sps == l.sps and ems == l.ems and percentage == l.percentage) {
            return 0
        }
        if (pixels <= l.pixels and dps <= l.dps and sps <= l.sps and ems <= l.ems and percentage <= l.percentage) {
            return -1
        }
        if (pixels >= l.pixels and dps >= l.dps and sps >= l.sps and ems >= l.ems and percentage >= l.percentage) {
            return 1
        }
        throw ComplexLengthException {
            lengths: [this, l]
            operation: "compare"
        }
    }

    override function equals(obj:Object):Boolean {
        if (not (obj instanceof Length)) return false;
        def l = obj as Length;
        if (isSameObject(l, this)) {
            return true;
        }

        return pixels == l.pixels and dps == l.dps and sps == l.sps and ems == l.ems and percentage == l.percentage;
    }

    override function hashCode():Integer {
        var value = pixels as Long;
        value = Bits.bitXor(value, dps as Long);
        value = Bits.bitXor(value, sps as Long);
        value = Bits.bitXor(value, ems as Long);
        value = Bits.bitXor(value, percentage as Long);
        return Bits.bitXor(value, Bits.unsignedShiftRight(value, 32)) as Integer;
    }
}

