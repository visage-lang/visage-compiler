package hello;
import javax.swing.JButton;

public class Button extends AbstractButton {

    protected function createButton(): javax.swing.AbstractButton {
	return new JButton();
    }

}
