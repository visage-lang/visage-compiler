package casual.theme;

import javafx.ui.Paint;
import javafx.ui.Color;
import javafx.ui.LinearGradient;
import javafx.ui.Stop;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

public class DevTheme extends DefaultTheme
{
}

attribute DevTheme.description = "Development Casual theme";

attribute DevTheme.chatPanelFont = new Font("sansserif", "PLAIN", 12);
attribute DevTheme.chatFrameBackground = new Color(0.72, 0.72, 0.72, 1.0);
attribute DevTheme.chatPanelBackgroundDark = new Color(0.08, 0.08, 0.08, 1.0);
attribute DevTheme.chatPanelBackgroundLight = new Color(0.20, 0.20, 0.20, 1.0);
attribute DevTheme.chatPanelBackground = LinearGradient
{
    x1: 0
    y1: 0
    x2: 0
    y2: 1
    
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
            color: brown
        },
        Stop
        {
            offset: 1
            color: yellow
        }
    ]
};
