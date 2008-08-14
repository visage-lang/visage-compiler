package hello;

public abstract class AbstractButton extends Widget {

    attribute buttonComponent: javax.swing.AbstractButton;

    attribute swingIcon: javax.swing.Icon = bind if (icon == null) null else icon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setIcon(swingIcon);
	    }
	}

    attribute swingDisabledIcon: javax.swing.Icon = bind if (disabledIcon == null) null else disabledIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setDisabledIcon(swingDisabledIcon);
	    }
	}

    attribute swingSelectedIcon: javax.swing.Icon = bind if (selectedIcon == null) null else selectedIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setSelectedIcon(swingSelectedIcon);
	    }
	}

    attribute swingDisabledSelectedIcon: javax.swing.Icon = bind if (disabledSelectedIcon == null) null else disabledSelectedIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setDisabledSelectedIcon(swingDisabledSelectedIcon);
	    }
	}

    attribute swingRolloverIcon: javax.swing.Icon = bind if (rolloverIcon == null) null else rolloverIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setRolloverIcon(swingRolloverIcon);
	    }
	}

    attribute swingRolloverSelectedIcon: javax.swing.Icon = bind if (rolloverSelectedIcon == null) null else rolloverSelectedIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setRolloverSelectedIcon(swingRolloverSelectedIcon);
	    }
	}

    attribute swingPressedIcon: javax.swing.Icon = bind if (pressedIcon == null) null else pressedIcon.getIcon()
	on replace {
	    if (buttonComponent != null) {
		buttonComponent.setPressedIcon(swingPressedIcon);
	    }
	}

    public attribute action: function() = null;
    public attribute selected: Boolean;
    public attribute text: String = "" on replace {
        if (buttonComponent != null) buttonComponent.setText(text);
    }

    public attribute icon: Icon = null;
    public attribute disabledIcon: Icon = null;
    public attribute selectedIcon: Icon = null;
    public attribute disabledSelectedIcon: Icon = null;
    public attribute rolloverIcon: Icon = null;
    public attribute rolloverSelectedIcon: Icon = null;
    public attribute pressedIcon: Icon = null;
    public attribute iconTextGap: Integer;

    /*
    public attribute buttonGroup: ButtonGroup on replace(old {
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

