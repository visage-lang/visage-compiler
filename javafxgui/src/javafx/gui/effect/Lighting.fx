/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import javafx.gui.Color;
import javafx.gui.effect.light.Light;
import javafx.gui.effect.light.DistantLight;
import com.sun.scenario.effect.PhongLighting;
import com.sun.javafx.gui.AccessHelper;

public class Lighting extends Effect {
    private attribute phong = create();

    private function create() : PhongLighting {
        var l = DistantLight {};
        return new PhongLighting(AccessHelper.getLightImpl(l));
    }

    public function getImpl():com.sun.scenario.effect.Effect {
        phong
    };

    /**
     * The light source for this {@code Lighting} effect.
     */
    public attribute light: Light = DistantLight {}
        on replace { phong.setLight(AccessHelper.getLightImpl(light)); }

    /**
     * The optional bump map input.  If not specified, a bump map will
     * be automatically generated from the source content.
     */
    public attribute bumpInput: Effect = Shadow { radius: 10 }
        on replace { phong.setBumpInput(bumpInput.getImpl()); }

    /**
     * The content input for this {@code Effect}.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute contentInput: Effect = Source { }
        on replace { phong.setContentInput(contentInput.getImpl()); }

    /**
     * The diffuse constant.
     * <pre>
     *       Min: 0.0
     *       Max: 2.0
     *   Default: 1.0
     *  Identity: n/a
     * </pre>
     */
    public attribute diffuseConstant: Number = 1.0
        on replace { phong.setDiffuseConstant(diffuseConstant.floatValue()); }

    /**
     * The specular constant.
     * <pre>
     *       Min: 0.0
     *       Max: 2.0
     *   Default: 0.3
     *  Identity: n/a
     * </pre>
     */
    public attribute specularConstant: Number = 0.3
        on replace { phong.setSpecularConstant(specularConstant.floatValue()); }

    /**
     * The specular exponent.
     * <pre>
     *       Min:  0.0
     *       Max: 40.0
     *   Default: 20.0
     *  Identity:  n/a
     * </pre>
     */
    public attribute specularExponent: Number = 20.0
        on replace { phong.setSpecularExponent(specularExponent.floatValue()); }

    /**
     * The surface scale factor.
     * <pre>
     *       Min:  0.0
     *       Max: 10.0
     *   Default:  1.5
     *  Identity:  n/a
     * </pre>
     */
    public attribute surfaceScale: Number = 1.5
        on replace { phong.setSurfaceScale(surfaceScale.floatValue()); }
}
