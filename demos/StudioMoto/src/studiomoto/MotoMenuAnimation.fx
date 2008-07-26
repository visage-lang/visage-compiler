package studiomoto;
import java.lang.System;
import java.lang.Math;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.animation.*;

// workarounds for lack of working local variable trigger
class Helper1 {
    attribute n: Number;
    attribute rand: Integer;
    attribute t: Number on replace {
        rand = (Math.random()*n + 0.5).intValue();
    }
}

class Helper2 {
    attribute i: Integer;
    attribute j: Integer;
    attribute rand: Integer on replace {
        if (j mod 2 == rand mod 2) {
            if (rand <= i) {
                alpha = 1;
            } else {
                alpha = 0;
            }
        }
        //System.out.println("j={j} i={i} rand={rand} alpha={alpha}");
    }
    attribute alpha: Number;
}

public class MotoMenuAnimation extends CustomNode {
    attribute active: Boolean
            on replace { if (active) anim.start() else anim.stop(); };
    function stop() { active = false }
    function start() { active = true }
    attribute t: Number;
    attribute anim: Timeline = Timeline {
        repeatCount: java.lang.Integer.MAX_VALUE
	keyFrames: KeyFrame {
            time: 1s/16
            action: function() {
                t++;
            }
        }
    };

    private attribute n:Number = 10;

    function create():Node {
        Group {
            content: 
            [HBox {
                content:
                for (j in [1..16]) 
                    VBox {
                        var helper1 = Helper1 {n: n, t: bind t}
                        transform: Transform.translate(1.2, 0)
                        content:
                        for (i in [1..n]) {
                            var m:Number = (indexof i).doubleValue();
                            var r = (1.0 - (m/n))*.5;
                            var green = .5 + r;
                            Rectangle {
                                transform: Transform.translate(0, .5)
                                height: 1.5
                                width: 4;
                                var helper2 = Helper2 {
                                    i: indexof i
                                    j: indexof j
                                    rand: bind helper1.rand
                                }
                                // NOTE!! Scenario has repaint and severe performance bugs with opacity (AlphaComposite)
                                //opacity: bind helper2.alpha
                                //
                                // working around it here with the color alpha
                                fill: bind Color.color(1, green, 0, helper2.alpha)
                            };
                        }
                    }
                }
        ]}
    }
}

