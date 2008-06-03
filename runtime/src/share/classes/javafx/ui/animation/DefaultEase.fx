package javafx.ui.animation;
import java.lang.Math;
import java.lang.System;

public abstract class DefaultEase extends NumberInterpolator {

    attribute beginPole: Number;
    attribute endPole: Number;
    attribute beginPoleDelta: Number;
    attribute endPoleDelta: Number;
    attribute primary_K: Number;

    function calcControlValues() {
        beginPole = 0.0 - beginPoleDelta; 
        endPole = 1000 + endPoleDelta;
        primary_K = 1.0;
        var kN = (beginPole - 1000) * (0 - endPole);
        var kD = (beginPole - 0) * (1000 - endPole);
        if (kD > 0.0001) {primary_K = Math.abs(kN/kD);}
    }

    init {
        calcControlValues();
    }

    public function interpolate(oldValue: Number,
                                newValue: Number,
                                t:Number):Number {
        var K = Math.exp(t * Math.log(primary_K));
        var nextValue = t;
        if (K <> 1.0) {
            var aNumerator = beginPole * endPole * (1-K);
            var aDenominator = endPole - K*beginPole;
            if (aDenominator <> 0) {
                nextValue = aNumerator / aDenominator;
            }
        }
        return oldValue + (nextValue/1000.0)*(newValue-oldValue);
    }

}
