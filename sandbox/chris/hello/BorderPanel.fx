package hello;

public class BorderPanel {

    var jpanel: javax.swing.JPanel;

    public var top: Widget 
			  /*
	on replace(old) {
	if (jpanel != null) {
	    if (old != null) {
		jpanel.remove(old.getComponent());
	    }
	    if (top  != null) {
		jpanel.add(top.getComponent(), java.awt.BorderLayout.NORTH);
	    }
	    jpanel.revalidate();
	    jpanel.repaint();
	}
    }
			  */;

    public var left: Widget /* on replace(old) {
	if (jpanel != null) {
	    if (old != null) {
		jpanel.remove(old.getComponent());
	    }
	    if (left  != null) {
		jpanel.add(left.getComponent(), java.awt.BorderLayout.WEST);
	    }
	    jpanel.revalidate();
	    jpanel.repaint();
	}
	}*/;
    public var bottom: Widget /* on replace(old) {
	if (jpanel != null) {
	    if (old != null) {
		jpanel.remove(old.getComponent());
	    }
	    if (bottom  != null) {
		jpanel.add(bottom.getComponent(), java.awt.BorderLayout.SOUTH);
	    }
	    jpanel.revalidate();
	    jpanel.repaint();
	}
	}*/;

    public var right: Widget /* on replace(old) {
	if (jpanel != null) {
	    if (old != null) {
		jpanel.remove(old.getComponent());
	    }
	    if (right  != null) {
		jpanel.add(right.getComponent(), java.awt.BorderLayout.EAST);
	    }
	    jpanel.revalidate();
	    jpanel.repaint();
	}
	} */;
    public var center: Widget /* on replace(old) {
	if (jpanel != null) {
	    if (old != null) {
		jpanel.remove(old.getComponent());
	    }
	    if (center  != null) {
		jpanel.add(center.getComponent(), java.awt.BorderLayout.CENTER);
	    }
	    jpanel.revalidate();
	    jpanel.repaint();
	}
	}*/;

    protected function createComponent(): javax.swing.JComponent {
	jpanel = new javax.swing.JPanel();
	if (top != null) {
	    jpanel.add(top.getComponent(), java.awt.BorderLayout.NORTH);
	}
	if (left != null) {
	    jpanel.add(left.getComponent(), java.awt.BorderLayout.WEST);
	}
	if (center != null) {
	    jpanel.add(center.getComponent(), java.awt.BorderLayout.CENTER);
	}
	if (bottom != null) {
	    jpanel.add(bottom.getComponent(), java.awt.BorderLayout.SOUTH);
	}
	if (right != null) {
	    jpanel.add(right.getComponent(), java.awt.BorderLayout.EAST);
	}
	return jpanel;
    }
}
