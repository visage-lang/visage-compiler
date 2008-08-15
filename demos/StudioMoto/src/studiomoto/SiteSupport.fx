package studiomoto;
import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.ext.swing.*;

public class SiteSupport extends MotoPanel {
    function arrow() {
        Polyline {
            transform: Transform.translate(0, 14)
            points: [0, 0, 4, 4, 0, 8] 
            stroke: Color.WHITE
            strokeWidth: 3
        }
    }    
    function label(text:String) {
        ComponentView {
            transform: Transform.translate(10, 0)
            component: Label {
                //TODO cursor: Cursor.HAND
                text: "<html><div style='font-face:Arial;font-size:26pt;'><span style='color:white;'>moto</span><span style='color:yellow;font-weight:bold;font-style:italic;'>{text}</span></div></html>"
            }
        }
    }    
    
     override var title = ComponentView {
            component: Label {
                text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Site</span><span style='color:yellow;'>Support</span></div></html>"
            }
        };
    
     override var content = VBox {
     content:
    [ComponentView {
        component: Label {
            text:
            "<html><div style='width:200;color:white;font-size:10pt;font-face:arial;'>Questions? We all need a little support sometimes. Get answers to questions you have about the STUDIOMOTO site and its features, right here. Select an application below
</div>
</html>"
        }
    },
    VBox {
        

        

        
        content:
        [HBox {
            content: 
            [arrow(), label("remix")]
        },
        HBox {
            
            content:
            [arrow(), label("graph")]
        }]
    }]
};


}

function run( ) {
    Canvas {
        background: Color.BLACK
        content:
        SiteSupport {height: 200, width: 1000}
    }
}

