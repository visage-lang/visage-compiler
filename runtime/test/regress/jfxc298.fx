/* JFXC-298:  JavafxClassReader not converting SequenceLocation<RealType> to Sequence<? extends RealTye>
 * @test
 */
import javafx.ui.*; 
import javafx.ui.canvas.*; 

var g = Canvas { 
    content: [ 
        Circle { 
        }, 
        Ellipse { 
        } 
    ] 
}; 
