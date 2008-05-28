package casual.theme;

import javafx.ui.Paint;
import javafx.ui.Color;
import javafx.ui.LinearGradient;
import javafx.ui.Stop;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

public class DevTheme extends DefaultTheme
{
    override attribute description = "Development Casual theme";

    override attribute chatPanelFont = Font.Font("sansserif", ["PLAIN"], 12);
    override attribute chatFrameBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override attribute chatPanelBackgroundDark = Color.color(0.08, 0.08, 0.08, 1.0);
    override attribute chatPanelBackgroundLight = Color.color(0.20, 0.20, 0.20, 1.0);
    override attribute chatPanelBackground = LinearGradient
    {
        startX: 0
        startY: 0
        endX: 0
        endY: 1

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
    } as Color;

    // TODO JFX531 Remove this function
    public function getChatPanelBackground(w: Number, h: Number): Color {
        LinearGradientJFXC531 {
            startX: 0
            startY: 0
            endX: 0.075 * w
            endY: h

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
                    color: chatPanelBackgroundDark
                },
                Stop
                {
                    offset: 1
                    color: chatPanelBackgroundLight
                }
            ]
        } as Color
    }
    
}
