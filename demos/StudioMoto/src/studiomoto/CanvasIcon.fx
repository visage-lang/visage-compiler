/*
 * CanvasIcon.fx
 *
 * Created on Jun 29, 2008, 6:52:47 PM
 */

package studiomoto;

import javafx.ext.swing.*;
import com.sun.scenario.scenegraph.JSGPanel;

/**
 * @author jclarke
 */

public class CanvasIcon extends Canvas, Icon {
    public function getToolkitIcon():javax.swing.Icon { 
        return getJSGPanel().toIcon(); 
    }
    
    private function getJSGPanel() : JSGPanel {
        getJComponent() as JSGPanel;
    }
}