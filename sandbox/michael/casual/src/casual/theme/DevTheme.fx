package casual.theme;

import javafx.ui.Paint;
import javafx.ui.Color;
import javafx.ui.LinearGradient;
import javafx.ui.Stop;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

public class DevTheme extends DefaultTheme
{
    attribute description = "Development Casual theme";

    attribute chatPanelFont = new Font("sansserif", "PLAIN", 12);
    attribute chatFrameBackground = new Color(0.72, 0.72, 0.72, 1.0);
    attribute chatPanelBackgroundDark = new Color(0.08, 0.08, 0.08, 1.0);
    attribute chatPanelBackgroundLight = new Color(0.20, 0.20, 0.20, 1.0);
    // TODO JFX531 Replace with proportional LinearGradient
    attribute chatPanelBackground: LinearGradientJFXC531 = LinearGradientJFXC531
    {
        startX: 0
        startY: 0
        endX: 0
        endY: bind chatPanelBackground.height

        stops:
        [
            Stop
            {
                offset: 0
                color: chatPanelBackgroundDark
            },
            Stop
            {
                offset: 0.6
                color: Color.BROWN
            },
            Stop
            {
                offset: 1
                color: Color.YELLOW
            }
        ]
    };
}