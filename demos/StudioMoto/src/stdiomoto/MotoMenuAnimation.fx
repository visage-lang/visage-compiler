package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;

public class MotoMenuAnimation extends CompositeNode {
    attribute active: Boolean
    on replace { if (active) anim.start() else anim.stop(); };
    function stop() { active = false }
    function start() { active = true }
    attribute t: Number;
    attribute anim: Timeline = Timeline {
        repeatCount: INFINITY
	keyFrames:
	        after (1s) {
		        trigger {t++;}
	        }
    };

    function composeNode() {
        Group {
            content: 
            [HBox {
                var n = 10
                function makeRandom() {return (Math.random()*n).intValue();}
                content:
                for (j in [1..16]) 
                VBox {
        	    var rand = 0
                    trigger on (newValue = t) {     
                        rand = makeRandom();
                    }
                    transform: translate(1.2, 0)
                    content:
                    foreach (i in [1..n])
                    Rect {
                        var: self
                        attribute xrand = bind rand
                        on replace {
                            println("rand={xrand}" );
                            if (xrand % 2 == indexof j % 2) {
                                self.opacity = if (xrand <= indexof i) 1 else 0;
                            }
                        }
                        transform: translate(0, .5)
                        var r = (1- (indexof i/n))*.5
                        height: 1.5
                        width: 4
                        fill: new Color(1, .5+r, 0, 1)
                }
            }
        }
    ]}}
}
