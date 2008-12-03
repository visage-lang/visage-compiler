/*
 * Regression test JFXC-1087 : bound interpolator
 *
 * @test/fail
 * @run
 */

import java.lang.System;
import javafx.animation.*;

var op:Number = -1.0;               
var kf = bind KeyFrame {
            time: 600ms
            values: op => 9.9 tween Interpolator.EASEOUT
         }
var vs = kf.values[0];
System.out.println(vs.target.get());
System.out.println(vs.value());
System.out.println(vs.interpolate == Interpolator.EASEOUT);
