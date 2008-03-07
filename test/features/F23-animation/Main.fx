/*
 * Feature test #23 - animation
 *
 * @subtest  // remove the "sub" when ready to run in harness
 */

import java.lang.System;
import javafx.ui.Rect;

var x = 2;
x => 100 tween LINEAR;

var rect = Rect {};
rect => {
    height: 400 tween EASEBOTH, 
    width: 500,
    fill: blue tween LINEAR
};
