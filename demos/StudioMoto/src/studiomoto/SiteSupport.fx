package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class SiteSupport extends MotoPanel {
    private function arrow() {
        Polyline {
            transform: Transform.translate(0, 14)
            points: [0, 0, 4, 4, 0, 8] 
            stroke: Color.WHITE
            strokeWidth: 3
        }
    }    
    private function label(text:String) {
        View {
            transform: Transform.translate(10, 0)
            content: Label {
                cursor: Cursor.HAND
                text: "<html><div style='font-face:Arial;font-size:26pt;'><span style='color:white;'>moto</span><span style='color:yellow;font-weight:bold;font-style:italic;'>{text}</span></div></html>"
            }
        }
    }    
    
     override attribute title = View {
            content: Label {
                text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Site</span><span style='color:yellow;'>Support</span></div></html>"
            }
        };
    
     override attribute content = VBox {
     content:
    [View {
        content: Label {
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

Canvas {
    background: Color.BLACK
    content:
    SiteSupport {height: 200, width: 1000}
}

