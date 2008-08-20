package hello;

public class CheckBox extends AbstractButton {

    var jcheckbox: javax.swing.JCheckBox;

    public var borderPaintedFlat: Boolean on replace {
	if (jcheckbox != null) {
	    jcheckbox.setBorderPaintedFlat(borderPaintedFlat);
	}
    }

    protected function createButton(): javax.swing.AbstractButton {
	jcheckbox = new javax.swing.JCheckBox();
	jcheckbox.setBorderPaintedFlat(borderPaintedFlat);
	return jcheckbox;
    }

}
