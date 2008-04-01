/*
 * Regression test: JFXC-79: Java generic type declarations are not currently supported
 *
 * @test/compile-error
 */


package javafx.ui; 


public class Keyboard { 
    protected attribute keyMap:java.util.Map<java.lang.Number, javafx.ui.KeyStroke>; 

    public function getKeyStroke(id:Number): KeyStroke { 
        return keyMap.get(id); 
    } 

} 
public class KeyStroke { 
    attribute description: String; 

    attribute id: Number 
        on replace { 
            KEYBOARD.keyMap.put(id, this); 
            description = javax.swing.KeyStroke.getKeyStroke(id.intValue(), 0).toString(); 
         
    }; 
    attribute keyChar: String; 
    public static attribute KEYBOARD = Keyboard{}; 
} 
