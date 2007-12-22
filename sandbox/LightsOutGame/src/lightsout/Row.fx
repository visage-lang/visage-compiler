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
    attribute lights: Light[];
    attribute model: LightsOutModel;
    public attribute content: Node[] = bind lights;
}