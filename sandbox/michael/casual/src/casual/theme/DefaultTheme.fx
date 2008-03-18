package casual.theme;

import javafx.ui.Paint;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.LinearGradient;
import javafx.ui.Stop;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

public class DefaultTheme extends Theme {
    override attribute description = "Default Casual theme (by ant)";

    override attribute defaultFont = Font.Font("sansserif", ["PLAIN"], 10);
    override attribute defaultForeground = Color.BLACK;
    override attribute defaultBackground = Color.WHITE;

    override attribute uiForeground = Color.color(0.12, 0.12, 0.12, 1.0);
    override attribute uiBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override attribute uiBorderColor = Color.color(0.12, 0.12, 0.12, 1.0);
    override attribute uiStrokeWidth = 2;

    override attribute windowFont = Font.Font("sansserif", ["PLAIN"], 10);
    override attribute windowInactive = Color.color(0.0, 0.0, 0.0, 0.05);
    //attribute windowInactive = new Color(1, 0.5, 0.5, 0.05);
    override attribute windowBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override attribute windowBorder = EmptyBorder
    {
        top: 2
        left: 2
        bottom: 2
        right: 2
    };
    override attribute windowInputAreaBorder = EmptyBorder
    {
        top: 4
        left: 6
        bottom: 4
        right: 6
    };

    override attribute titleBarFont = Font.Font("sansserif", ["BOLD"], 10);
    override attribute titleBarForeground = Color.WHITE;
    override attribute titleBarBackground = Color.color(0.08, 0.08, 0.08, 1.0);
    override attribute titleBarCloseIconColor = Color.color(0.50, 0.20, 0.20, 1.0);
    override attribute titleBarMinimizeIconColor = Color.color(0.72, 0.72, 0.72, 1.0);
    override attribute titleBorder = EmptyBorder
    {
        top: 8
        left: 6
        bottom: 6
        right: 6
    };

    override attribute chatPanelFont = Font.Font("sansserif", ["PLAIN"], 12);
    override attribute chatFrameBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override attribute chatPanelBackgroundDark = Color.color(0.08, 0.08, 0.08, 1.0);
    override attribute chatPanelBackgroundLight = Color.color(0.20, 0.20, 0.20, 1.0);
    
    override attribute chatPanelBackground = LinearGradient
    {
        startX: 0
        startY: 0
        endX: 0.075
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
                color: chatPanelBackgroundDark
            },
            Stop
            {
                offset: 1
                color: chatPanelBackgroundLight
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

    override attribute messageFont = Font.Font("sansserif", ["PLAIN"], 12);
    override attribute commentFont = Font.Font("sansserif", ["PLAIN"], 10);
    override attribute messageInBackground = Color.color(0.90, 0.90, 0.90, 0.5);
    override attribute messageInForeground = Color.BLACK;
    override attribute messageOutBackground = Color.color(0.30, 0.30, 0.30, 0.5);
    override attribute messageOutForeground = Color.WHITE;
    override attribute commentForeground = Color.BLACK;
    override attribute commentBackground = Color.color(0.70, 0.70, 0.70, 0.50);
    override attribute messageBorderColor = Color.color(0.15, 0.15, 0.15, 1.0);
    override attribute messageBorder = EmptyBorder
    {
        top: 6
        left: 8
        bottom: 6
        right: 8
    };
    override attribute commentBorder = EmptyBorder
    {
        top: 3
        left: 8
        bottom: 1
        right: 8
    };

    override attribute messageInputForeground = Color.WHITE;
    override attribute messageInputBackground = Color.color(0.50, 0.50, 0.50, 1.0);
    override attribute messageInputBorderColor = Color.color(0.25, 0.25, 0.25, 1.0);
    override attribute messageInputBorder = EmptyBorder
    {
        top: 2
        left: 0
        bottom: 1
        right: 1
    };
    override attribute messageInputAreaBorder = EmptyBorder
    {
        top: 6
        left: 8
        bottom: 6
        right: 8
    };

    override attribute fieldForeground = Color.color(0.0, 0.0, 0.0, 1.0);
    override attribute fieldBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override attribute fieldFocusColor = Color.color(0.90, 0.90, 0.90, 1.0);

    override attribute errorForeground = Color.color(1.0, 1.0, 1.0, 1.0);
    override attribute errorBackgroundInside = LinearGradient
    {
        startX: 0
        startY: 0
        endX: 0.5
        endY: 1

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
    } as AbstractColor;
    override attribute errorBackgroundOutside = Color.color(0.0, 0.0, 0.0, 0.75);
    override attribute errorBorderColor = chatFrameBackground;

    override attribute warningForeground = Color.color(1.0, 1.0, 1.0, 1.0);
    override attribute warningBackgroundInside = LinearGradient
    {
        startX: 0
        startY: 0
        endX: 0.5
        endY: 1

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
    } as AbstractColor;
    override attribute warningBackgroundOutside = errorBackgroundOutside;
    override attribute warningBorderColor = errorBorderColor;

    override attribute infoForeground = Color.color(1.0, 1.0, 1.0, 1.0);
    override attribute infoBackgroundInside = LinearGradient
    {
        startX: 0
        startY: 0
        endX: 0.5
        endY: 1

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
    } as AbstractColor;
    override attribute infoBackgroundOutside = errorBackgroundOutside;
    override attribute infoBorderColor = errorBorderColor;
};

