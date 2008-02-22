package studiomoto;
import java.lang.System;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;
import javafx.ui.animation.*;

public class MotoMenuAnimation extends CompositeNode {
    attribute active: Boolean
            on replace { if (active) anim.start() else anim.stop(); };
    function stop() { active = false }
    function start() { active = true }
    attribute t: Number on replace {     
        rand = makeRandom();
    };
    attribute anim: Timeline = Timeline {
        repeatCount: java.lang.Integer.MAX_VALUE
	keyFrames: KeyFrame {
            keyTime: 1s
            action: function() {
                t++;
            }
        }
    };
    private attribute n = 10;
    private function makeRandom():Integer {return (Math.random()*n).intValue();}
    private attribute rand:Integer = 0;
    function composeNode():Node {
        Group {
            content: 
            [HBox {
                content:
                for (j in [1..16]) 
                    VBox {
                        transform: Transform.translate(1.2, 0)
                        content:
                        for (i in [1..n]) {
                            Rect {
                                var self = this
                                var ndxi = indexof i
                                var ndxj = indexof j
                                var xrand = bind rand
                                    on replace {
                                        System.out.println("rand={xrand}" );
                                        if (xrand % 2 == ndxj % 2) {
                                            self.opacity = if (xrand <= ndxi) 1 else 0;
                                        }
                                    }
                                transform: Transform.translate(0, .5)
                                var r = (1- (ndxi/n))*.5
                                height: 1.5
                                width: 4
                                fill: Color.rgba(1, .5+r, 0, 1)
                            };
                        }
                    }
                }
        ]}
    }
}

