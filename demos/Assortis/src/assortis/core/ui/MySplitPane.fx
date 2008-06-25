package assortis.core.ui;


import javafx.ext.swing.*;
import javafx.scene.*;
import javax.swing.*;

import java.awt.BorderLayout;

import java.lang.System;

public class MySplitPane extends Component{
    
    protected attribute splitPane: JSplitPane = new JSplitPane();
    
    public attribute orientation: Orientation;
    public attribute weight:Number on replace{
        splitPane.setResizeWeight(weight);
    };
    
    public attribute one: Component on replace{
        var panel = new JPanel(new BorderLayout());
        
        //var scrollPane = new JScrollPane(one.content.getJComponent());

        panel.add(one.getJComponent(), BorderLayout.CENTER);
        //panel.add(scrollPane, BorderLayout.CENTER);

        //var scrollPane = new JScrollPane(panel);
        
        if (orientation == Orientation.HORIZONTAL){
            splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setLeftComponent(panel);
            //splitPane.setLeftComponent(scrollPane);
            } else{
            splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane.setTopComponent(panel);
            //splitPane.setTopComponent(scrollPane);
        }
    };

    public attribute two: Component on replace{
        var panel = new JPanel(new BorderLayout());
        panel.add(two.getJComponent(),BorderLayout.CENTER);
        //var scrollPane = new JScrollPane(panel);
        
        if (orientation == Orientation.HORIZONTAL){
            splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setRightComponent(panel);
            //splitPane.setRightComponent(scrollPane);
            }else{
            splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane.setBottomComponent(panel);
            //splitPane.setBottomComponent(scrollPane);
        }
    };



    protected function createJComponent(): JComponent {
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7);

        return splitPane;

    }

}
