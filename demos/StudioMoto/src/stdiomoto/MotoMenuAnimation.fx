package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;

public class MotoMenuAnimation extends CompositeNode {
    attribute active: Boolean;
    operation stop();
    operation start();
    attribute t: Number;
    attribute anim: KeyFrameAnimation;
}

attribute MotoMenuAnimation.anim = KeyFrameAnimation {
	        repeatCount: INFINITY
		    keyFrames:
		        after (1s) {
			        trigger {t++;}
		        }
				    
           };

operation MotoMenuAnimation.start() {
    active = true;
}

operation MotoMenuAnimation.stop() {
    active = false;
}

trigger on MotoMenuAnimation.active = value {
	if (value) { anim.start(); } else { anim.stop(); }
}

function MotoMenuAnimation.composeNode() =
Group {
    content: 
    [HBox {
        var n = 10
        operation makeRandom() {return (Math.random()*n).intValue();}
        content:
        foreach (j in [1..16]) 
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
                trigger on (newValue = rand) {     
                    println("rand={rand}" );
                    if (rand % 2 == indexof j % 2) {
                        self.opacity = if rand  <= indexof i then 1 else 0;
                    }
                }
                transform: translate(0, .5)
                var r = (1- (indexof i/n))*.5
                height: 1.5
                width: 4
                fill: new Color(1, .5+r, 0, 1)
            }  
        }
    }]
};
