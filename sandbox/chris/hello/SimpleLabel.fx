package hello;
import javax.swing.*;
import java.lang.System;

public class SimpleLabel extends Widget {

    private attribute jlabel: JLabel;

    private attribute swingIcon: javax.swing.Icon = bind if (icon == null) null else icon.getIcon()
	on replace {
	    if (jlabel <> null) {
		jlabel.setIcon(swingIcon);
	    }
	}

    public attribute icon: Icon = null;

    public attribute text: String = "" on replace {
        if (jlabel <> null) jlabel.setText(text);
    }

    protected function createComponent(): JComponent {
        if (jlabel == null) {
            jlabel = new JLabel(text);
	    jlabel.setIcon(swingIcon);
        }
        return jlabel;
    }
}

