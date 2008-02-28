package casual.theme;

import javafx.ui.Paint;
import javafx.ui.Color;
import javafx.ui.LinearGradient;
import javafx.ui.Stop;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

// TODO: JFXC531 Remove this class as soon as LinearGradient supports 
//               proportional values
public class LinearGradientJFXC531 extends LinearGradient {
    attribute width: Number;
    attribute height: Number;
}

public class DefaultTheme extends Theme {
    attribute description = "Default Casual theme (by ant)";

    attribute defaultFont = new Font("sansserif", "PLAIN", 10);
    attribute defaultForeground = Color.BLACK;
    attribute defaultBackground = Color.WHITE;

    attribute uiForeground = new Color(0.12, 0.12, 0.12, 1.0);
    attribute uiBackground = new Color(0.72, 0.72, 0.72, 1.0);
    attribute uiBorderColor = new Color(0.12, 0.12, 0.12, 1.0);
    attribute uiStrokeWidth = 2;

    attribute windowFont = new Font("sansserif", "PLAIN", 10);
    attribute windowInactive = new Color(0.0, 0.0, 0.0, 0.05);
    //attribute windowInactive = new Color(1, 0.5, 0.5, 0.05);
    attribute windowBackground = new Color(0.72, 0.72, 0.72, 1.0);
    attribute windowBorder = EmptyBorder
    {
        top: 2
        left: 2
        bottom: 2
        right: 2
    };
    attribute windowInputAreaBorder = EmptyBorder
    {
        top: 4
        left: 6
        bottom: 4
        right: 6
    };

    attribute titleBarFont = new Font("sansserif", "BOLD", 10);
    attribute titleBarForeground = Color.WHITE;
    attribute titleBarBackground = new Color(0.08, 0.08, 0.08, 1.0);
    attribute titleBarCloseIconColor = new Color(0.50, 0.20, 0.20, 1.0);
    attribute titleBarMinimizeIconColor = new Color(0.72, 0.72, 0.72, 1.0);
    attribute titleBorder = EmptyBorder
    {
        top: 8
        left: 6
        bottom: 6
        right: 6
    };

    attribute chatPanelFont = new Font("sansserif", "PLAIN", 12);
    attribute chatFrameBackground = new Color(0.72, 0.72, 0.72, 1.0);
    attribute chatPanelBackgroundDark = new Color(0.08, 0.08, 0.08, 1.0);
    attribute chatPanelBackgroundLight = new Color(0.20, 0.20, 0.20, 1.0);
    
    // TODO JFX531 Replace with proportional LinearGradient
    attribute chatPanelBackground: LinearGradientJFXC531 = LinearGradientJFXC531
    {
        startX: 0
        startY: 0
        endX: bind 0.075 * chatPanelBackground.width
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
                color: chatPanelBackgroundDark
            },
            Stop
            {
                offset: 1
                color: chatPanelBackgroundLight
            }
        ]
    };

    attribute messageFont = new Font("sansserif", "PLAIN", 12);
    attribute commentFont = new Font("sansserif", "PLAIN", 10);
    attribute messageInBackground = new Color(0.90, 0.90, 0.90, 0.5);
    attribute messageInForeground = Color.BLACK;
    attribute messageOutBackground = new Color(0.30, 0.30, 0.30, 0.5);
    attribute messageOutForeground = Color.WHITE;
    attribute commentForeground = Color.BLACK;
    attribute commentBackground = new Color(0.70, 0.70, 0.70, 0.50);
    attribute messageBorderColor = new Color(0.15, 0.15, 0.15, 1.0);
    attribute messageBorder = EmptyBorder
    {
        top: 6
        left: 8
        bottom: 6
        right: 8
    };
    attribute commentBorder = EmptyBorder
    {
        top: 3
        left: 8
        bottom: 1
        right: 8
    };

    attribute messageInputForeground = Color.WHITE;
    attribute messageInputBackground = new Color(0.50, 0.50, 0.50, 1.0);
    attribute messageInputBorderColor = new Color(0.25, 0.25, 0.25, 1.0);
    attribute messageInputBorder = EmptyBorder
    {
        top: 2
        left: 0
        bottom: 1
        right: 1
    };
    attribute messageInputAreaBorder = EmptyBorder
    {
        top: 6
        left: 8
        bottom: 6
        right: 8
    };

    attribute fieldForeground = new Color(0.0, 0.0, 0.0, 1.0);
    attribute fieldBackground = new Color(0.72, 0.72, 0.72, 1.0);
    attribute fieldFocusColor = new Color(0.90, 0.90, 0.90, 1.0);

    attribute errorForeground = new Color(1.0, 1.0, 1.0, 1.0);
    // TODO JFX531 Replace with proportional LinearGradient
    attribute errorBackgroundInside: LinearGradientJFXC531 = LinearGradientJFXC531
    {
        startX: 0
        startY: 0
        endX: bind 0.5 * errorBackgroundInside.width
        endY: bind errorBackgroundInside.height

        stops:
        [
            Stop
            {
                offset: 0
                color: Color
                {
                    red: 0.70 
                    blue: 0.40 
                    green: 0.40
                    opacity: 0.95
                }
            },
            Stop
            {
                offset: 1
                color: Color
                {
                    red: 0.30 
                    blue: 0.05 
                    green: 0.05
                    opacity: 0.95
                }
            }
        ]
    };
    attribute errorBackgroundOutside = new Color(0.0, 0.0, 0.0, 0.75);
    attribute errorBorderColor = chatFrameBackground;

    attribute warningForeground = new Color(1.0, 1.0, 1.0, 1.0);
    // TODO JFX531 Replace with proportional LinearGradient
    attribute warningBackgroundInside: LinearGradientJFXC531 = LinearGradientJFXC531
    {
        startX: 0
        startY: 0
        endX: bind 0.5 * warningBackgroundInside.width
        endY: bind warningBackgroundInside.height

        stops:
        [
            Stop
            {
                offset: 0
                color: Color
                {
                    red: 0.30 
                    blue: 0.50 
                    green: 0.30
                    opacity: 0.95
                }
            },
            Stop
            {
                offset: 1
                color: Color
                {
                    red: 0.05
                    blue: 0.20 
                    green: 0.05
                    opacity: 0.95
                }
            }
        ]
    };
    attribute warningBackgroundOutside = errorBackgroundOutside;
    attribute warningBorderColor = errorBorderColor;

    attribute infoForeground = new Color(1.0, 1.0, 1.0, 1.0);
    // TODO JFX531 Replace with proportional LinearGradient
    attribute infoBackgroundInside: LinearGradientJFXC531 = LinearGradientJFXC531
    {
        startX: 0
        startY: 0
        endX: bind 0.5 * infoBackgroundInside.width
        endY: bind infoBackgroundInside.height

        stops:
        [
            Stop
            {
                offset: 0
                color: Color
                {
                    red: 0.30 
                    blue: 0.30 
                    green: 0.50
                    opacity: 0.95
                }
            },
            Stop
            {
                offset: 1
                color: Color
                {
                    red: 0.05
                    blue: 0.05 
                    green: 0.20
                    opacity: 0.95
                }
            }
        ]
    };
    attribute infoBackgroundOutside = errorBackgroundOutside;
    attribute infoBorderColor = errorBorderColor;
};

