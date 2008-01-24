package bounce;

import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import java.lang.System;
import javafx.ui.animation.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;
import java.lang.Object;
import java.lang.Double; // hack

var pf = PointerFactory {};

Frame {
    title: "Ball";
    onClose: function() { System.exit(0); }
    visible: true
    content:
    Canvas {
        border: EmptyBorder {}
        content:
        Group {
            
            // model variables
            
            var x = 0.0;
            var y = 0.0;
            var sx = 1.0;
            var sy = 1.0;
            var r = 50.0;
            var bpx = bind pf.make(x)
            var px = bpx.unwrap();
            var bpy = bind pf.make(y);
            var py = bpy.unwrap();
            var bpsx = bind pf.make(sx);
            var psx = bpsx.unwrap();
            var bpsy = bind pf.make(sy);
            var psy = bpsy.unwrap();
            var color1 = Color.SILVER
            var color2 = Color.MAROON
            
            content:
            [Rect {height: 460, width: 800, fill: Color.BLACK},
            Group {
                transform: bind [Transform.translate(x, y), 
                                 Transform.translate(50.0, 50.0),
                                 Transform.scale(sx, sy),
                                 Transform.translate(-50.0, -50.0)]
                content: 
                [Rect {
                    stroke: Color.BLACK
                    x: 0
                    y: 0
                    arcHeight: 100, arcWidth: 100
                    width: 100
                    height: 100
                    fill: RadialGradient {
                        var d = 2*r
                        cx: r
                        cy: r
                        focusX: 0.75*d
                        focusY: 0.25*d
                        radius: r
                        
                        stops:
                        [Stop {
                            offset: 0
                            color: color1
                        },
                        Stop {
                            offset: 0.85,
                            color: color2
                        },
                        Stop {
                            offset: 1
                            color: color2
                        }]
                        
                    }
                    
                    var ax = Timeline {
                        // x
                        keyFrames:
                        [KeyFrame {
                                keyTime: 0s
                                keyValues: 
                                NumberValue {
                                    target: px;
                                    value: 0.0
                                }
                        },
                        KeyFrame {
                                keyTime: 10s
                                keyValues: 
                                NumberValue {
                                    target: px;
                                    value: 700.0
                                    interpolate: NumberValue.LINEAR
                                }
                        }]
                        autoReverse: true
                        repeatCount: Double.POSITIVE_INFINITY
                    }
                    
                    var ay = Timeline {
                        // y
                        repeatCount: Double.POSITIVE_INFINITY
                        keyFrames:
                        [KeyFrame {
                                keyTime: 0s
                                keyValues: 
                                NumberValue {
                                    target: py;
                                    value: 0.0
                                }
                        },
                        KeyFrame {
                                keyTime: 2.2s
                                keyValues: 
                                NumberValue {
                                    target: py;
                                    value: 375.0;
                                    interpolate: NumberValue.SPLINE(0, 0, .5, 0)
                                }
                        },
                        KeyFrame {
                                keyTime: 2.25s;
                                keyValues: 
                                NumberValue {
                                    target: py;
                                    value: 375.0;
                                }
                        },
                        KeyFrame {
                                keyTime: 4.5s
                                keyValues: 
                                NumberValue {
                                    target: py;
                                    value: 0.0;
                                    interpolate: NumberValue.SPLINE(0, 0, 0, 0.5)
                                }
                        }]
                    }
                    
                    var sxy =  Timeline {
                        // scale x y
                        repeatCount: Double.POSITIVE_INFINITY
                        keyFrames:
                        [KeyFrame {
                                keyTime: 2s
                                keyValues: 
                                [NumberValue {
                                    target: psx;
                                    value: 1.0
                                },
                                NumberValue {
                                    target: psy;
                                    value: 1.0
                                }]
                        },
                        KeyFrame {
                                keyTime: 2.25s
                                keyValues: 
                                [NumberValue {
                                    target: psx;
                                    value: 1.2;
                                    interpolate: NumberValue.LINEAR;
                                },
                                NumberValue {
                                    target: psy;
                                    value: 0.7;
                                    interpolate: NumberValue.LINEAR;
                                }]
                        },
                        KeyFrame {
                                keyTime: 2.5s
                                keyValues: 
                                [NumberValue {
                                    target: psx;
                                    value: 1.0;
                                    interpolate: NumberValue.LINEAR;
                                },
                                NumberValue {
                                    target: psy;
                                    value: 1.0;
                                    interpolate: NumberValue.LINEAR;
                                }]
                        },
                        KeyFrame {
                                keyTime: 4.5s
                                keyValues: 
                                [NumberValue {
                                    target: psx;
                                    value: 1.0;
                                },
                                NumberValue {
                                    target: psy;
                                    value: 1.0;
                                }]
                        }]
                    }
                    
                    var clip = Timeline {
                        repeatCount: Double.POSITIVE_INFINITY
                        keyFrames: 
                        KeyFrame {
                            keyTime: 0s
                            timelines: [ax, ay, sxy]
                        }
                    }
                    onMouseClicked: function(e) {
                        clip.start();
                    }                    
                }]
            }]
        }
    }
}
