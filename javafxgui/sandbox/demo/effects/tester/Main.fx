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
import javafx.gui.component.*;
import javafx.gui.effect.*;
import javafx.gui.effect.light.*;
import java.lang.System;

var hSlider = Slider{minimum: 20 maximum: 180 value: 20};
var vSlider = Slider{minimum: 0 maximum: 160 value: 160 orientation: Orientation.VERTICAL};

var image = Image {width: 150, height: 100, url: "{__DIR__}/blossoms.jpg"}

var canvas = Canvas {
    background: Color.color(0.3, 0.3, 0.3)
    content: [
        Circle {
            //var self = this
            effect: Blend {
                topInput: Identity {
                    source: image
                    x: 20//bind self.currentWidth-150
                    y: 20//bind self.currentHeight-100
                }
                mode: BlendMode.ADD
                opacity: bind (vSlider.value / 160.0)
            }
            onMouseExited: function(e:MouseEvent) { e.node.opacity = 1.0 }
            onMouseEntered: function(e:MouseEvent) { e.node.opacity = 0.5 }
            centerX: 150
            centerY: 150
            radius: bind(hSlider.value + vSlider.value) / 2
            fill: bind makeColor2(hSlider.value, vSlider.value)
        },
        Group {
            effect: DropShadow {
                offsetX: bind hSlider.value / 10
                offsetY: bind hSlider.value / 10
            }
            opacity: 0.5
            cursor: Cursor.HAND
            content: [
            Rectangle {
                translateX: bind hSlider.value
                translateY: bind 180 - vSlider.value
                width: bind hSlider.value
                height: bind 180 - vSlider.value
                fill: bind makeColor1(hSlider.value, vSlider.value)
            },
            Rectangle {
                x: bind hSlider.value + 50
                y: bind 180 - vSlider.value + 50
                width: bind hSlider.value
                height: bind 180 - vSlider.value
                fill: bind makeColor1(hSlider.value, vSlider.value)
            },
            Rectangle {
                x: bind hSlider.value + 100
                y: bind 180 - vSlider.value + 100
                width: bind hSlider.value
                height: bind 180 - vSlider.value
                fill: bind makeColor1(hSlider.value, vSlider.value)
            }]
        },
        Circle {
            effect: Lighting {
                surfaceScale: 10
                light: DistantLight {
                    elevation: bind vSlider.value / 4
                    azimuth: bind hSlider.value - 180.0
                }
                bumpInput: Flood {
                    paint: RadialGradient {
                        centerX: 0.5
                        centerY: 0.5
                        radius: 0.13
                        proportional: true
                        cycleMethod: CycleMethod.REFLECT
                        stops: [
                            Stop { offset: 0.0 color: Color{opacity: 0.8} },
                            Stop { offset: 0.1 color: Color{opacity: 0.75} },
                            Stop { offset: 0.9 color: Color{opacity: 0.25} },
                            Stop { offset: 1.0 color: Color{opacity: 0.2} }
                        ]
/*
                        stops: [
                            Stop { offset: 0.0 color: Color{opacity: 1.0} },
                            Stop { offset: 0.2 color: Color{opacity: 0.3} },
                            Stop { offset: 1.0 color: Color{opacity: 0.0} }
                        ]
*/
/*
                        stops: for (n in [1..10]) {
                            Stop { offset: (n-2.5)/7.5 color: Color{opacity: 1.0-java.lang.Math.log10(n)} }
                        }
*/
                    }
                }
            }
            clip: Circle { centerX: 570, centerY: 150, radius: 30 }
            clipAntialiased: true
            centerX: 550
            centerY: 150
            radius: 60
            fill: Color.RED
        },
        Circle {
            centerX: 680
            centerY: 150
            radius: 40
            fill: Color.WHITE
            effect:
            Blend {
                mode: BlendMode.SRC_IN
                topInput:
                Flood {
                    paint: RadialGradient {
                        centerX: 0.5
                        centerY: 0.5
                        radius: 0.5
                        stops: [
                            Stop { offset: 0.0 color: Color.RED },
                            Stop { offset: 1.0 color: Color.BLUE }
                        ]
                    }
                }
            }
        },
        ComponentView {
            effect: GaussianBlur { radius: bind hSlider.value / 10 }
            //effect: Reflection { fraction: 1.0, bottomOpacity: 1.0, topOpacity: 1.0 }
            translateX: bind hSlider.value/3.0 + 50.0
            translateY: bind -vSlider.value/3.0 + 340
            component: Button {
                text: "Button Node"
                action: function():Void { System.out.println(vSlider.value) }
            }
        },
        Rectangle {
            var ix = 350;
            var iy = 70;
            var iw = 150;
            var ih = 100;
            effect: PerspectiveTransform {
                ulx: ix
                uly: iy
                urx: bind ix+iw-(hSlider.value/10)
                ury: iy
                lrx: ix+iw
                lry: bind iy+ih-(vSlider.value/10)
                llx: ix
                lly: iy+ih
            }
            x: ix
            y: iy
            width: iw
            height: ih
            fill: Color.BLUE
        },
        ImageView {
            var ix = 500;
            var iy = 10;
            image: image
            effect: MotionBlur { angle: bind hSlider.value }
            x: ix
            y: iy
        },
        Text {
            effect: Lighting {
                light: DistantLight {
                    elevation: bind vSlider.value / 4
                    azimuth: bind hSlider.value - 180.0
                }
            }
            font: Font{size: 80, style: FontStyle.BOLD}
            x: 150
            y: 280
            content: "Effects for FX!"
            fill: Color.BLUE
        },
        Text {
            effect: Reflection { fraction: bind vSlider.value / 200.0 }
            font: Font{size: 30}
            x: 50
            y: 380
            content: "Play with the sliders..."
            fill: Color.RED
            cache: true
        },
        Rectangle { x: 400, y: 300, width: 280, height: 150, fill: Color.WHITE },
        Text {
            effect: InnerShadow { offsetX: 2, offsetY: 3 }
            font: Font {size: 80, style: FontStyle.BOLD}
            x: 420
            y: 400
            content: "Inner!"
            fill: Color.RED
        }]
};

function makeColor1(hVal: Integer, vVal: Integer): Color {
    return Color{red: hVal/180.0 green: 0.5 blue: (180 - vVal)/180.0};
}

function makeColor2(hVal: Integer, vVal: Integer): Paint {
    return LinearGradient { 
        startX: 0.0 
        startY: 0.0
        endX: 1.0
        endY: 0.0
        stops: [
           Stop { offset: 0.0 color: Color.BLACK },
           Stop { offset: 1.0 color: Color{red: 0.5 green: (180-hVal)/180.0 blue: (180-vVal)/180.0} }]
    };
}

Frame {
    title: "JavaFX Effects Demo"
    width: 750
    height: 500
    content: BorderPanel {
        bottom: hSlider
        right: vSlider
        center: canvas
    }
    visible: true
}
