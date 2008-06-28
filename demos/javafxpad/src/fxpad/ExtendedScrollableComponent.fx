/*
 * ExtendedScrollableComponent.fx
 *
 * Created on Apr 19, 2008, 11:22:18 AM
 */

package fxpad;

import fxpad.gui.*;
import javafx.ext.swing.*;

/**
 * Temporary adds functionality to ScrollableComponent that belongs
 * in ScrollableComponent
 * @author jclarke
 */

public abstract class ExtendedScrollableComponent extends ScrollableComponent {
    /**
     * Adds a child that will appear as a column on to the left or right
     * of the main view of the scrollpane depending on the current component orientation.
     */
    public attribute rowHeader:Component on replace  {
        var x = getNonScrollPaneComponent(rowHeader);
        getJScrollPane().setRowHeaderView(x);
    };

    /**
     * Adds a child that will appear as a row at the top or bottom
     * of the main view of the scrollpane depending on the current component orientation.
     */
    public attribute columnHeader:Component on replace  {
        var x = getNonScrollPaneComponent(columnHeader);
        getJScrollPane().setColumnHeaderView(x);
    }   
    
    private function getNonScrollPaneComponent(c:Component): javax.swing.JComponent{
        var comp = c.getJComponent();
        if (comp instanceof javax.swing.JScrollPane) {
            comp= (comp as javax.swing.JScrollPane).getViewport().getView() as
                    javax.swing.JComponent;
        }
        comp;  
    }    

}