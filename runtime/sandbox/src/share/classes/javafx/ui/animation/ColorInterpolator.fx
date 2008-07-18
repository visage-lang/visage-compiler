package javafx.ui.animation;
import javafx.ui.Color;

public abstract class ColorInterpolator {

    public abstract function interpolate(oldValue:Color,
                                         newValue:Color,
                                         t:Number):Color;

}
