/*
* MyComponent.fx
*
* Created on Apr 18, 2008, 4:55:25 PM
*/

package assortis.core.ui;

/**
 * @author Alexandr Scherbatiy
 */


import javafx.ext.swing.*;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JComponent;

import javafx.ext.swing.Layout.*;

public abstract class MyCompositeComponent extends Component{
    
    public abstract function composeComponent(): Component;        
    
    protected function createJComponent(): JComponent {
        var panel = new JPanel(new BorderLayout());
        panel.add (composeComponent().getJComponent(), BorderLayout.CENTER);
        return panel;
    }

}
