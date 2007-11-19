import java.lang.System;
import javax.swing.*;
import java.beans.*;

public abstract class AbstractWidget  {
    public attribute opaque: Boolean;
    public attribute height: Number;
    public attribute width: Number;
    public attribute x: Number;
    public attribute y: Number;   
}

public abstract class Widget extends AbstractWidget {
    private function updateBounds() {
        if (component <> null) {
            component.setBounds(x.intValue(), y.intValue(), width.intValue(), height.intValue());
        }
    }
    
    attribute height: Number on replace { 
        updateBounds();
    }
    attribute width: Number on replace { updateBounds(); }
    attribute x: Number on replace { updateBounds(); }
    attribute y: Number on replace { updateBounds(); }
    attribute opaque: Boolean on replace { 
        if (component <> null) {
            component.setOpaque(opaque);
        } 
    }
    protected attribute component: JComponent = null;
    protected abstract function createComponent(): JComponent;
    public function getComponent(): JComponent {
        if (component == null) {
            component = createComponent();
            component.addPropertyChangeListener(PropertyChangeListener {
                public function propertyChange(e:PropertyChangeEvent):Void {
                    var name = e.getPropertyName();
                    if (name == "opaque") {
                        opaque = e.getNewValue() as java.lang.Boolean;
                    } 
//else if (name == "bounds") {
  //                      var bounds = e.getNewValue() as java.awt.Rectangle;
                        
    //                }
                }
            });
        }
        return component;
    }
    
    public function getJComponent(): JComponent { return getComponent(); }
}

public abstract class AbstractButton extends AbstractWidget {
    
}

public class Button extends Widget, AbstractButton {
    
    private attribute button: JButton;
    protected function createComponent():JComponent {
        System.out.println("create button");
        button = new JButton(text); 
        button.addPropertyChangeListener(PropertyChangeListener {
            public function propertyChange(e:PropertyChangeEvent):Void {
                var name = e.getPropertyName();
                if (name == "text") {
                    text = e.getNewValue() as String;
                }
            }
        });
        return button; 
    }
    public attribute text: String = "" on replace(oldValue) { 
        System.out.println("text replaced {text}");
        if (button <> null) {
            button.setText(text);
        }
    }
}

var b = Button {text: "Hello", height: 100}
b.text = "blah";
b.text = "baz";
b.getComponent();
