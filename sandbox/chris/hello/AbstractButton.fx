package hello;

public abstract class AbstractButton extends Widget {

    var buttonComponent: javax.swing.AbstractButton;

    var swingIcon: javax.swing.Icon = bind if (icon == null) null else icon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setIcon(swingIcon);
	    }
	}

    var swingDisabledIcon: javax.swing.Icon = bind if (disabledIcon == null) null else disabledIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setDisabledIcon(swingDisabledIcon);
	    }
	}

    var swingSelectedIcon: javax.swing.Icon = bind if (selectedIcon == null) null else selectedIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setSelectedIcon(swingSelectedIcon);
	    }
	}

    var swingDisabledSelectedIcon: javax.swing.Icon = bind if (disabledSelectedIcon == null) null else disabledSelectedIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setDisabledSelectedIcon(swingDisabledSelectedIcon);
	    }
	}

    var swingRolloverIcon: javax.swing.Icon = bind if (rolloverIcon == null) null else rolloverIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setRolloverIcon(swingRolloverIcon);
	    }
	}

    var swingRolloverSelectedIcon: javax.swing.Icon = bind if (rolloverSelectedIcon == null) null else rolloverSelectedIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setRolloverSelectedIcon(swingRolloverSelectedIcon);
	    }
	}

    var swingPressedIcon: javax.swing.Icon = bind if (pressedIcon == null) null else pressedIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setPressedIcon(swingPressedIcon);
	    }
	}

    public var action: function() = null;
    public var selected: Boolean;
    public var text: String = "" on replace {
        if (buttonComponent != null) buttonComponent.setText(text);
    }

    public var icon: Icon = null;
    public var disabledIcon: Icon = null;
    public var selectedIcon: Icon = null;
    public var disabledSelectedIcon: Icon = null;
    public var rolloverIcon: Icon = null;
    public var rolloverSelectedIcon: Icon = null;
    public var pressedIcon: Icon = null;
    public var iconTextGap: Integer;

    /*
    public var buttonGroup: ButtonGroup on replace(old {
	if (old != null) {
	    delete this from old.content;
	}
	if (buttonGroup != null) {
	    insert this into buttonGroup.content;
	}
    }
    */

    protected abstract function createButton(): javax.swing.AbstractButton;

    public function getButton(): javax.swing.AbstractButton {
	return getComponent() as javax.swing.AbstractButton;
    }

    protected function createComponent(): javax.swing.JComponent {
	buttonComponent = createButton();
	buttonComponent.setSelected(selected);
	buttonComponent.setText(text);
	buttonComponent.setIconTextGap(iconTextGap);
	buttonComponent.setIcon(swingIcon);
	buttonComponent.setDisabledIcon(swingDisabledIcon);
	buttonComponent.setSelectedIcon(swingIcon);
	buttonComponent.setDisabledSelectedIcon(swingDisabledSelectedIcon);
	buttonComponent.setRolloverIcon(swingRolloverIcon);
	buttonComponent.setRolloverSelectedIcon(swingRolloverSelectedIcon);
	buttonComponent.setPressedIcon(swingPressedIcon);
	buttonComponent.addActionListener(java.awt.event.ActionListener {
				     public function actionPerformed(e:java.awt.event.ActionEvent): Void {
					 if (action != null) {
					     action();
					 }
				     }
				 });
	return buttonComponent;
    }


}

