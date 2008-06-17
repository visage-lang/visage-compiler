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

package demo.effects.tester;

import javafx.gui.*;
import javafx.gui.effect.*;
import javafx.gui.effect.light.*;
import java.lang.Math;
import java.lang.System;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;

function updateMap(off: Integer) : DisplacementMap {
    var w = map.width;
    var h = map.height;
    var w2 = w/2 as Number;
    var h2 = h/2 as Number;

    for (y in [0..<h], x in [0..<w]) {
        var dx = x - w2;
        var dy = y - h2;
        if (dx == 0) dx = 1; // TODO
        var theta = Math.atan(dy/dx);

        dy = off * Math.sin(theta);
        dx = off * Math.cos(theta);

        var xf = (dx / w);
        var yf = (dy / h);

        map.setSamples(x, y, xf, yf);
    }

    DisplacementMap { mapData: map }
}

var image = Image {url: "{__DIR__}/blossoms.jpg"}
var slider = Slider { minimum: 0, maximum: 100, value: 0 }
var map = FloatMap { width: image.width as Integer, height: image.height as Integer }
var displace = bind updateMap(slider.value);

var canvas = Canvas {
    background: Color.color(0.3, 0.3, 0.3)
    content:
    Group {
        content: [
        ImageView {
            image: image
            x: 50
            y: 50
            effect: bind displace
        },
        ComponentView {
            translateX: 20
            translateY: 450
            component: slider
        }
        ]
    }
};

Frame {
    title: "JavaFX Effects Demo"
    content: canvas
    width: 700
    height: 500
    visible: true
}
