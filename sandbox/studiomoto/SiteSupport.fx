package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class SiteSupport extends MotoPanel {
}

attribute SiteSupport.title = View {
    content: Label {
        text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Site</span><span style='color:yellow;'>Support</span></div></html>"
    }
};

attribute SiteSupport.content = 
VBox {
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
        
        function label(text:String) = View {
            
            transform: translate(10, 0)
            content: Label {
                cursor: HAND
                text: "<html><div style='font-face:Arial;font-size:26pt;'><span style='color:white;'>moto</span><span style='color:yellow;font-weight:bold;font-style:italic;'>{text}</span></div></html>"
            }
        };
        
        function arrow() = Polyline {
            transform: translate(0, 14)
            points: [0, 0, 4, 4, 0, 8] 
            stroke: white
            strokeWidth: 3
        };
        
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

Canvas {
    background: black
    content:
    SiteSupport {height: 200, width: 1000}
}
