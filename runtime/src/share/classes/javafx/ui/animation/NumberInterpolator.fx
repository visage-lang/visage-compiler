package javafx.ui.animation;


public abstract class NumberInterpolator {

    public abstract function interpolate(oldValue:Number,
                                         newValue:Number,
                                         t:Number):Number;

}
