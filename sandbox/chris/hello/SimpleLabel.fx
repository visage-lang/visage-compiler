package hello;
import javax.swing.*;
import java.lang.System;

public class SimpleLabel extends Widget {

    var jlabel: JLabel;

    var swingIcon: javax.swing.Icon = bind if (icon == null) null else icon.getIcon()
	on replace {
	    if (jlabel != null) {
		jlabel.setIcon(swingIcon);
	    }
	}

    public var icon: Icon = null;

    public var text: String = "" on replace {
        if (jlabel != null) jlabel.setText(text);
    }

    protected function createComponent(): JComponent {
        if (jlabel == null) {
            jlabel = new JLabel(text);
	    jlabel.setIcon(swingIcon);
        }
        return jlabel;
    }
}

