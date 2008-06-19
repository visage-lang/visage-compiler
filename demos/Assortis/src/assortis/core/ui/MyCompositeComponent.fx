/*
* MyComponent.fx
*
* Created on Apr 18, 2008, 4:55:25 PM
*/

package assortis.core.ui;

/**
 * @author Alexandr Scherbatiy
 */


import javafx.gui.*;
import javafx.gui.swing.*;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JComponent;

import javafx.gui.swing.Layout.*;

public abstract class MyCompositeComponent extends Component{
    
    public abstract function composeComponent(): Component;        
    
    protected function createJComponent(): JComponent {
        var panel = new JPanel(new BorderLayout());
        panel.add (composeComponent().getJComponent(), BorderLayout.CENTER);
        return panel;
    }

}
