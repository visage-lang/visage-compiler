package hello;
import javax.swing.*;
import java.lang.System;

public class Panel extends Widget {

    private attribute jpanel: JPanel;

    public attribute content: Widget[]
	on replace[i](w) {
	    if (jpanel != null) {
		System.out.println("REPLACE: {i} {w}");
		jpanel.remove(i);
		jpanel.add(content[i].getComponent(), i);
	    }
        }
	on insert[i](w) {
	    if (jpanel != null) {
		System.out.println("INSERT: {i} {w}");
		jpanel.add(content[i].getComponent(), i);
	    }
	}
	on delete[i](w) {
	    if (jpanel != null) {
		System.out.println("DELETE: {i} {w}");
		jpanel.remove(i);
	    }
	}

    public function createComponent(): JComponent {
	jpanel = createPanel();
	return jpanel;
    }
    
    protected function createLayout(): java.awt.LayoutManager {
	return null;
    }

    protected function createPanel(): javax.swing.JPanel {
	var panel = new JPanel();
	panel.setOpaque(false);
	for (i in content) {
	    var comp = i.getComponent();
	    var x = i.x;
	    var y = i.y;
	    var w = i.width;
	    var h = i.height;
	    panel.add(comp);
	    comp.setBounds(x.intValue(), y.intValue(), w.intValue(), h.intValue());
	}
	panel.setLayout(createLayout());
	return panel;
    }

}

