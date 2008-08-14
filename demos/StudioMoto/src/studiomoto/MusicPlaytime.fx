package studiomoto;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.ext.swing.*;


public class MusicPlaytime extends MotoPanel {
    override var title = ComponentView {
        component: Label {
            text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Music</span><span style='color:yellow;'>Playtime</span></div></html>"
        }
    };

    override var content = ComponentView {
        component: Label {
            text:
            "<html><div style='width:600;color:white;font-size:10pt;font-face:arial;'>Make your own music come to life, on your mobile phone. Create original animations, videos, ringtones and more with these interactive tools.
    <p>
    <a href='' style='color:yellow;'>MOTOREMIX</a><br>
    Become a video director, right from your PC. Pick a track and video clips from one of our featured artists, then mix your own music video<br>
    <a href='' style='color:yellow;'>GO</a>&nbsp;&gt;
    <p>
    <a href='' style='color:yellow;'>MOTOGRAPH</a><br>
    Turn music into an extrac-sensory experience. Design your own animations and sounds for your cell phone.<br>
    <a href='' style='color:yellow;'>GO</a>&nbsp;&gt;
    <p>
    </div>
    </html>"
        }
    };
    
}

function run( ) {
    Canvas {
        background: Color.BLACK
        content:
        About {height: 200, width: 1000}
    }
}
