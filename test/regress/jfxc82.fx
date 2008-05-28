/**
 * regression test:  unary operators don't destroy the type of a variable.
 * @test
 * @run
 */

public class KeyStroke { 
    attribute description: String; 
    attribute id: Number 
        on replace { 
            KEYBOARD.keyMap.put(id, this); 
            description = javax.swing.KeyStroke.getKeyStroke(id.intValue(), 0).toString(); 
         
    }; 
    attribute keyChar: String; 
    public static attribute KEYBOARD = Keyboard{} 
} 

public class Keyboard { 
     
    protected attribute keyMap:java.util.Map = new java.util.HashMap(); 


    public function getKeyStroke(id:Number): KeyStroke { 
        return keyMap.get(id) as KeyStroke; 
    } 

} 
