/*
 * Row.fx
 *
 * Created on Dec 20, 2007, 4:02:29 PM
 */

package lightsout;

import javafx.ui.canvas.*;

/**
 * @author jclarke
 */

public class Row extends Group {
    var lights: Light[];
    var model: LightsOutModel;
    public var content: Node[] = bind lights;
}
