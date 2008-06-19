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

package demo.animation.tester;

import javafx.gui.*;
import javafx.gui.swing.*;
import javafx.animation.*;
import javafx.lang.Duration;

import java.lang.Object;
import java.lang.System;

var interpolate = Interpolator.LINEAR;

var x = 0;
var y = 0.0;
var w = 50.0;
var h = 50.0;
var color = Color.LIME;
var opacity = 1.0;

var durSlider =
    Slider {
        minimum: 1400
        maximum: 3000
        value: 2000
    }
var dur = Duration { millis: bind durSlider.value }
var durLabel = Label { text: bind "Dur {dur.millis as Integer}ms:" }

var repSlider =
    Slider {
        minimum: 0
        maximum: 4
        value: 2
    }
var rep = bind repSlider.value;
var repLabel = Label { text: bind "Repeat {rep}:" }

var autoRevCheckBox =
    CheckBox {
        text: "Auto Reverse"
        selected: true
    }

var toggleCheckBox =
    CheckBox {
        text: "Toggle"
        selected: false
    }

var t = Timeline {
    repeatCount: bind rep
    autoReverse: bind autoRevCheckBox.selected
    toggle: bind toggleCheckBox.selected
    keyFrames: [
    KeyFrame {
        time: 0s
        values: x => 0
    },
    KeyFrame {
        time: 700ms
        values: y => 0.0
        action: function() { System.err.println("Hello 1"); }
    },
    KeyFrame {
        time: 800ms
        values: color => Color.LIME
    },
    KeyFrame {
        time: 1s
        action: function() { System.err.println("Hello 2"); }
    },
    KeyFrame {
        time: 1.8s
        values: color => Color.RED tween Interpolator.EASEBOTH
    },
    KeyFrame {
        time: 1.1s
        timelines: [
        Timeline {
            keyFrames: [
            KeyFrame {
                time: 0s
                values: [
                    w => 50.0,
                    h => 50.0
                ]
            },
            KeyFrame {
                time: 0.3s
                values: [
                    w => 100.0 tween interpolate,
                    h => 80.0 tween interpolate
                ]
            }
            ]
        }
        ]
    },
    KeyFrame {
        time: bind dur
        values: [
            x => 400 tween interpolate,
            y => 200.0 tween interpolate
        ]
        action: function() { System.err.println("Hello 3"); }
    }
    ]
};

var fader = Timeline {
    toggle: true
    keyFrames: [
    KeyFrame {
        time: 0ms
        values: opacity => 1.0
    },
    KeyFrame {
        time: 250ms
        values: opacity => 0.4 tween Interpolator.LINEAR
    }
    ]
}

var canvas =
    Canvas {
        background: Color.WHITE
        content:
        Rectangle {
            x: bind x
            y: bind y
            width: bind w
            height: bind h
            fill: bind color
            opacity: bind opacity
            onMouseEntered: function(e) { fader.start(); }
            onMouseExited: function(e) { fader.start(); }
        }
    };

var buttons =
    FlowPanel {
        content: [
            Button {
                text: "Start"
                action: function():Void { t.start(); }
            },
            Button {
                text: "Stop"
                action: function():Void { t.stop(); }
                enabled: bind t.running
            },
            Button {
                text: "Pause"
                action: function():Void { t.pause(); }
                enabled: bind t.running and not t.paused
            },
            Button {
                text: "Resume"
                action: function():Void { t.resume(); }
                enabled: bind t.running and t.paused
            },
            autoRevCheckBox,
            toggleCheckBox
        ]
    };

var radios =
    FlowPanel {
        var group = ToggleGroup {}
        content: [
            RadioButton {
                selected: true
                toggleGroup: group
                text: "Linear"
                action: function() { interpolate = Interpolator.LINEAR; }
            },
            RadioButton {
                toggleGroup: group
                text: "EaseBoth"
                action: function() { interpolate = Interpolator.EASEBOTH; }
            },
            RadioButton {
                toggleGroup: group
                text: "EaseIn"
                action: function() { interpolate = Interpolator.EASEIN; }
            }
        ]
    };

var sliders = FlowPanel { content: [durLabel, durSlider, repLabel, repSlider] };

Frame {
    closeAction: function() {System.exit(0);}
    width: 600
    height: 500
    visible: true
    content: BorderPanel {
        center: canvas
        bottom: BorderPanel {
            top: buttons
            center: radios
            bottom: sliders
        }
    }
}
