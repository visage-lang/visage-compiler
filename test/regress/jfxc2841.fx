/**
 * regression test: JFXC-2841 : Mixins: Cannot find firePropertyChange method in SwingComboBox.fx
 *
 * @test
 *
 */
import javax.swing.JComboBox; 
import javax.swing.ComboBoxModel; 

public class comboImpl extends JComboBox { 

    public override function setModel(aModel: ComboBoxModel): Void { 
        var oldModel = dataModel; 
             
        firePropertyChange("model", oldModel, dataModel); 
    } 
} 
