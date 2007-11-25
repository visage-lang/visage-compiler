/*
 * ball.fx -- automatically generated from ball.svg using JavaFX SVG Browser (http://blogs.sun.com/chrisoliver/)
 * 
 * License: The code is released under Creative Commons Attribution 2.5 License
 * (http://creativecommons.org/licenses/by/2.5/)
 */

package javafxballs;

import javafx.ui.UIElement;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class ball extends CompositeNode {
   public function composeNode(): Node {
      return Group { content:[
         Group {
            transform: [Translate{x: -80.0, y: -48.0}]
            content: [
                Group {
                    content: [
                        Circle {
                            cx: 106.0
                            cy: 74.0
                            fill: rgba(0xcc, 0xff, 0x00, 0xff)
                            opacity: 0.9
                            radius: 25.0
                            stroke: rgba(0xa7, 0xd1, 0x00, 0xff)
                            strokeWidth: 2.0
                        },
                        Path {
                            d: [
                                MoveTo {
                                    x: 123.143
                                    y: 61.088
                                    absolute: true
                                },
                                CurveTo {
                                    x1: 130.602
                                    y1: 70.889
                                    x2: 129.01
                                    y2: 84.643
                                    x3: 119.59
                                    y3: 91.813
                                    smooth: false
                                    absolute: true
                                },
                                CurveTo {
                                    x1: 110.171
                                    y1: 98.981
                                    x2: 96.489
                                    y2: 96.843
                                    x3: 89.032
                                    y3: 87.043
                                    smooth: false
                                    absolute: true
                                },
                                CurveTo {
                                    x1: 81.573
                                    y1: 77.24
                                    x2: 83.165
                                    y2: 63.486
                                    x3: 92.584
                                    y3: 56.316
                                    smooth: false
                                    absolute: true
                                },
                                CurveTo {
                                    x1: 102.004
                                    y1: 49.149
                                    x2: 115.686
                                    y2: 51.285
                                    x3: 123.143
                                    y3: 61.088
                                    smooth: false
                                    absolute: true
                                },
                                ClosePath {}
                            ]
                            fill: XMLID_6_()
                            opacity: 0.73999999
                        },
                        Ellipse {
                            cx: 96.5
                            cy: 62.5
                            fill: rgba(0xff, 0xff, 0xff, 0xff)
                            radiusX: 8.294
                            radiusY: 4.906
                            transform: [
                                matrix(0.7958, -0.6055, 0.6055, 0.7958, -18.1424, 71.1966)
                            ]
                        }
                    ]
                }
            ]
        }
    ]};
   }

    function XMLID_6_() {
      return [
        RadialGradient {
            cx: 156.7178
            cy: 129.2988
            transform: [
                matrix(1.19, 0.165, 0.165, 1.2813, -82.7876, -94.3076)
            ]
            gradientUnits: USER_SPACE_ON_USE
            radius: 53.625
            stops: [
                Stop {
                    offset: 0.0
                    color: rgba(0xFF, 0xFF, 0xFF, 0xff)
                },
                Stop {
                    offset: 0.2033
                    color: rgba(0xFE, 0xFF, 0xFD, 0xff)
                },
                Stop {
                    offset: 0.2765
                    color: rgba(0xFD, 0xFD, 0xF6, 0xff)
                },
                Stop {
                    offset: 0.3286
                    color: rgba(0xF9, 0xFB, 0xEB, 0xff)
                },
                Stop {
                    offset: 0.3708
                    color: rgba(0xF4, 0xF7, 0xDA, 0xff)
                },
                Stop {
                    offset: 0.4065
                    color: rgba(0xEE, 0xF2, 0xC4, 0xff)
                },
                Stop {
                    offset: 0.4157
                    color: rgba(0xEC, 0xF1, 0xBD, 0xff)
                },
                Stop {
                    offset: 1.0
                    color: rgba(0xCC, 0xFF, 0x00, 0xff)
                }
            ]
        }
    ];}
}
//<<ball>> {}