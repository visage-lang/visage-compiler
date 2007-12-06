/*
 * JavaFxBall.fx
 * 
 * License: The code is released under Creative Commons Attribution 2.5 License
 * (http://creativecommons.org/licenses/by/2.5/)
 */

package javafxballs;

import javafx.ui.UIElement;
import javafx.ui.*;
import javafx.ui.canvas.*;

/**
 *
 * @author Alex
 */
class JavaFxBall extends BallBase {
    attribute img: ball on replace {
        if (img <> null) {
            img.transform = [Translate{x:bind _x, y: bind _y}];
            initialize();
        }
    };
    public function move() {
        super.move();
    };
}    
