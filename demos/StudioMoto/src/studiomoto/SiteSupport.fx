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
                text: "<html><div style='font-face:Arial;font-size:26pt;'><span style='color:Color.WHITE;'>moto</span><span style='color:Color.YELLOW;font-weight:bold;font-style:italic;'>{text}</span></div></html>"
            }
        }
    }    
    attribute title: Node = View {
            content: Label {
                text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:Color.WHITE;'>Site</span><span style='color:Color.YELLOW;'>Support</span></div></html>"
            }
        };
    
    attribute content: Node = VBox {
    content:
    [View {
        content: Label {
            text:
            "<html><div style='width:200;color:Color.WHITE;font-size:10pt;font-face:arial;'>Questions? We all need a little support sometimes. Get answers to questions you have about the STUDIOMOTO site and its features, right here. Select an application below
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

