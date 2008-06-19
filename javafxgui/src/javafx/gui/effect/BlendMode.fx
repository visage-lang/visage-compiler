/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package javafx.gui.effect;

import com.sun.scenario.effect.Blend.Mode;

/**
 * A blending mode that defines the manner in which the inputs
 * are composited together.
 * Each {@code Mode} describes a mathematical equation that
 * combines premultiplied inputs to produce some premultiplied result.
 */
public class BlendMode {
    attribute toolkitValue: Mode = Mode.SRC_OVER;
    
    public function toString() : String {
        toolkitValue.toString()
    }

    /**
     * The top input is blended over the bottom input.
     * (Equivalent to the Porter-Duff "source over destination" rule.)
     * <p>
     * Thus:
     * <pre>
     *      <em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     *      <em>C<sub>r</sub></em> = <em>C<sub>top</sub></em> + <em>C<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * </pre>
     */
    public static attribute SRC_OVER = BlendMode {
    }

    /**
     * The part of the top input lying inside of the bottom input
     * is kept in the resulting image.
     * (Equivalent to the Porter-Duff "source in destination" rule.)
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em>*<em>A<sub>bot</sub></em>
     * 	<em>C<sub>r</sub></em> = <em>C<sub>top</sub></em>*<em>A<sub>bot</sub></em>
     * </pre>
     */
    public static attribute SRC_IN = BlendMode {
        toolkitValue: Mode.SRC_IN
    }
    
    /**
     * The part of the top input lying outside of the bottom input
     * is kept in the resulting image.
     * (Equivalent to the Porter-Duff "source held out by destination"
     * rule.)
     * <p>
     * Thus:
     * <pre>
     *      <em>A<sub>r</sub></em> = <em>A<sub>top</sub></em>*(1-<em>A<sub>bot</sub></em>)
     *      <em>C<sub>r</sub></em> = <em>C<sub>top</sub></em>*(1-<em>A<sub>bot</sub></em>)
     * </pre>
     */
    public static attribute SRC_OUT = BlendMode {
        toolkitValue: Mode.SRC_OUT
    }

    /**
     * The part of the top input lying inside of the bottom input
     * is blended with the bottom input.
     * (Equivalent to the Porter-Duff "source atop destination" rule.)
     * <p>
     * Thus:
     * <pre>
     *      <em>A<sub>r</sub></em> = <em>A<sub>top</sub></em>*<em>A<sub>bot</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>) = <em>A<sub>bot</sub></em>
     *      <em>C<sub>r</sub></em> = <em>C<sub>top</sub></em>*<em>A<sub>bot</sub></em> + <em>C<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * </pre>
     */
    public static attribute SRC_ATOP = BlendMode {
        toolkitValue: Mode.SRC_ATOP
    }

    /**
     * The color and alpha components from the top input are
     * added to those from the bottom input.
     * The result is clamped to 1.0 if it exceeds the logical
     * maximum of 1.0.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = min(1, <em>A<sub>top</sub></em>+<em>A<sub>bot</sub></em>)
     * 	<em>C<sub>r</sub></em> = min(1, <em>C<sub>top</sub></em>+<em>C<sub>bot</sub></em>)
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is commutative (ordering of inputs
     * does not matter).
     * <li>This mode is sometimes referred to as "linear dodge" in
     * imaging software packages.
     * </ul>
     */
    public static attribute ADD = BlendMode {
        toolkitValue: Mode.ADD
    }

    /**
     * The color components from the first input are multiplied with those
     * from the second input.
     * The alpha components are blended according to
     * the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>C<sub>r</sub></em> = <em>C<sub>top</sub></em> * <em>C<sub>bot</sub></em>
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is commutative (ordering of inputs
     * does not matter).
     * <li>This mode is the mathematical opposite of
     * the {@link #SCREEN} mode.
     * <li>The resulting color is always at least as dark as either
     * of the input colors.
     * <li>Rendering with a completely black top input produces black;
     * rendering with a completely white top input produces a result
     * equivalent to the bottom input.
     * </ul>
     */
    public static attribute MULTIPLY = BlendMode {
        toolkitValue: Mode.MULTIPLY
    }

    /**
     * The color components from both of the inputs are
     * inverted, multiplied with each other, and that result
     * is again inverted to produce the resulting color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>C<sub>r</sub></em> = 1 - ((1-<em>C<sub>top</sub></em>) * (1-<em>C<sub>bot</sub></em>))
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is commutative (ordering of inputs
     * does not matter).
     * <li>This mode is the mathematical opposite of
     * the {@link #MULTIPLY} mode.
     * <li>The resulting color is always at least as light as either
     * of the input colors.
     * <li>Rendering with a completely white top input produces white;
     * rendering with a completely black top input produces a result
     * equivalent to the bottom input.
     * </ul>
     */
    public static attribute SCREEN = BlendMode {
        toolkitValue: Mode.SCREEN
    }

    /**
     * The input color components are either multiplied or screened,
     * depending on the bottom input color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     *      REMIND: not sure how to express this succinctly yet...
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is a combination of {@link #SCREEN} and
     * {@link #MULTIPLY}, depending on the bottom input color.
     * <li>This mode is the mathematical opposite of
     * the {@link #HARD_LIGHT} mode.
     * <li>In this mode, the top input colors "overlay" the bottom input
     * while preserving highlights and shadows of the latter.
     * </ul>
     */
    public static attribute OVERLAY = BlendMode {
        toolkitValue: Mode.OVERLAY
    }

    /**
     * The darker of the color components from the two inputs are
     * selected to produce the resulting color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>C<sub>r</sub></em> = min(<em>C<sub>top</sub></em>, <em>C<sub>bot</sub></em>)
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is commutative (ordering of inputs
     * does not matter).
     * <li>This mode is the mathematical opposite of
     * the {@link #LIGHTEN} mode.
     * </ul>
     */
    public static attribute DARKEN = BlendMode {
        toolkitValue: Mode.DARKEN
    }

    /**
     * The lighter of the color components from the two inputs are
     * selected to produce the resulting color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>C<sub>r</sub></em> = max(<em>C<sub>top</sub></em>, <em>C<sub>bot</sub></em>)
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is commutative (ordering of inputs
     * does not matter).
     * <li>This mode is the mathematical opposite of
     * the {@link #DARKEN} mode.
     * </ul>
     */
    public static attribute LIGHTEN = BlendMode {
        toolkitValue: Mode.LIGHTEN
    }

    /**
     * The bottom input color components are divided by the inverse
     * of the top input color components to produce the resulting color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>C<sub>r</sub></em> = <em>C<sub>bot</sub></em> / (1-<em>C<sub>top</sub></em>)
     * </pre>
     */
    public static attribute COLOR_DODGE = BlendMode {
        toolkitValue: Mode.COLOR_DODGE
    }

    /**
     * The inverse of the bottom input color components are divided by
     * the top input color components, all of which is then inverted
     * to produce the resulting color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>C<sub>r</sub></em> = 1-((1-<em>C<sub>bot</sub></em>) / <em>C<sub>top</sub></em>)
     * </pre>
     */
    public static attribute COLOR_BURN = BlendMode {
        toolkitValue: Mode.COLOR_BURN
    }

    /**
     * The input color components are either multiplied or screened,
     * depending on the top input color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     *      TODO: not sure how to express this succinctly yet...
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is a combination of {@link #SCREEN} and
     * {@link #MULTIPLY}, depending on the top input color.
     * <li>This mode is the mathematical opposite of
     * the {@link #OVERLAY} mode.
     * </ul>
     */
    public static attribute HARD_LIGHT = BlendMode {
        toolkitValue: Mode.HARD_LIGHT
    }

    /**
     * TODO: this is a complicated formula, TBD...
     */
    public static attribute SOFT_LIGHT = BlendMode {
        toolkitValue: Mode.SOFT_LIGHT
    }

    /**
     * The darker of the color components from the two inputs are
     * subtracted from the lighter ones to produce the resulting color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>C<sub>r</sub></em> = abs(<em>C<sub>top</sub></em>-<em>C<sub>bot</sub></em>)
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is commutative (ordering of inputs
     * does not matter).
     * <li>This mode can be used to invert parts of the bottom input
     * image, or to quickly compare two images (equal pixels will result
     * in black).
     * <li>Rendering with a completely white top input inverts the
     * bottom input; rendering with a completely black top input produces
     * a result equivalent to the bottom input.
     * </ul>
     */
    public static attribute DIFFERENCE = BlendMode {
        toolkitValue: Mode.DIFFERENCE
    }

    /**
     * The color components from the two inputs are multiplied and
     * doubled, and then subtracted from the sum of the bottom input
     * color components, to produce the resulting color.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>C<sub>r</sub></em> = <em>C<sub>top</sub></em> + <em>C<sub>bot</sub></em> - (2*<em>C<sub>top</sub></em>*<em>C<sub>bot</sub></em>)
     * </pre>
     * <p>
     * Notes:
     * <ul>
     * <li>This mode is commutative (ordering of inputs
     * does not matter).
     * <li>This mode can be used to invert parts of the bottom input.
     * <li>This mode produces results that are similar to those of
     * {@link #DIFFERENCE}, except with lower contrast.
     * <li>Rendering with a completely white top input inverts the
     * bottom input; rendering with a completely black top input produces
     * a result equivalent to the bottom input.
     * </ul>
     */
    public static attribute EXCLUSION = BlendMode {
        toolkitValue: Mode.EXCLUSION
    }

    /**
     * The red component of the bottom input is replaced with the
     * red component of the top input; the other color components
     * are unaffected.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>R<sub>r</sub></em> = <em>R<sub>top</sub></em>
     * 	<em>G<sub>r</sub></em> = <em>G<sub>bot</sub></em>
     * 	<em>B<sub>r</sub></em> = <em>B<sub>bot</sub></em>
     * </pre>
     */
    public static attribute RED = BlendMode {
        toolkitValue: Mode.RED
    }

    /**
     * The green component of the bottom input is replaced with the
     * green component of the top input; the other color components
     * are unaffected.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>R<sub>r</sub></em> = <em>R<sub>bot</sub></em>
     * 	<em>G<sub>r</sub></em> = <em>G<sub>top</sub></em>
     * 	<em>B<sub>r</sub></em> = <em>B<sub>bot</sub></em>
     * </pre>
     */
    public static attribute GREEN = BlendMode {
        toolkitValue: Mode.GREEN
    }

    /**
     * The blue component of the bottom input is replaced with the
     * blue component of the top input; the other color components
     * are unaffected.
     * The alpha components are blended according
     * to the {@link #SRC_OVER} equation.
     * <p>
     * Thus:
     * <pre>
     * 	<em>A<sub>r</sub></em> = <em>A<sub>top</sub></em> + <em>A<sub>bot</sub></em>*(1-<em>A<sub>top</sub></em>)
     * 	<em>R<sub>r</sub></em> = <em>R<sub>bot</sub></em>
     * 	<em>G<sub>r</sub></em> = <em>G<sub>bot</sub></em>
     * 	<em>B<sub>r</sub></em> = <em>B<sub>top</sub></em>
     * </pre>
     */
    public static attribute BLUE = BlendMode {
        toolkitValue: Mode.BLUE
    }
}
