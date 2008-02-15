package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class About extends MotoPanel {
    attribute title: Node = View {
        content: Label {
            text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>About</span><span style='color:yellow;'>StudioMoto</span></div></html>"
        }
    };
    attribute content: Node = View {
        content: Label {
            text:
            "<html><div style='width:600;color:white;font-size:10pt;font-face:arial;'>STUDIOMOTO connects you to some of today and tommorrow's biggest music artists - through their music, Motorola technology and your own imagination. Read that back again. Sounds like a big promise, but here's how it delivers.
    <p>
    Explore STUDIOMOTO and you'll find exclusive tracks to <a href='' style='color:yellow;'>download</a> and playback on your PC or mobile phone, free ringtones, and <a href='' style='color:yellow;'>and the latest news</a> on music tours, release dates, and even chances to win tickets to see your favorite bands.
    <p>
    Plus, with tools like <a href='' style='color:yellow;'>MOTOREMIX</a> and <a href='' style='color:yellow;'>MOTOGRAPH</a>, you can edit your own music videos or create custom multimedia animations for your mobile phone.
    <p>
    So the question is: with so much to do, why are you reading an \"about\" page?
    </div>
    </html>"
        }
    };   
}


Canvas {
    background: Color.BLACK
    content:
    About {height: 200, width: 1000}
}
