/**
 * regression test: If statement with void then and else part are attributed correctly.
 * @test
 * @run
 */
import java.awt.font.TextLayout; 
import java.lang.Object;

public class Font { 
    public attribute size: Integer; 
    public attribute face: Object; 
    public attribute faceName: String; 

    function bar() : Void {
        if(face <> null ) { 
        } else if (face <> null) { 
        } else if (faceName <> null) { 
        } else { 
        } 
    }
} 
