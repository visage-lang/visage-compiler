package casual.theme;

import javafx.ui.Paint;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.LinearGradient;
import javafx.ui.Stop;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

public class DefaultTheme extends Theme {
    override var description = "Default Casual theme (by ant)";

    override var defaultFont = Font.Font("sansserif", ["PLAIN"], 10);
    override var defaultForeground = Color.BLACK;
    override var defaultBackground = Color.WHITE;

    override var uiForeground = Color.color(0.12, 0.12, 0.12, 1.0);
    override var uiBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override var uiBorderColor = Color.color(0.12, 0.12, 0.12, 1.0);
    override var uiStrokeWidth = 2;

    override var windowFont = Font.Font("sansserif", ["PLAIN"], 10);
    override var windowInactive = Color.color(0.0, 0.0, 0.0, 0.05);
    //var windowInactive = new Color(1, 0.5, 0.5, 0.05);
    override var windowBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override var windowBorder = EmptyBorder
    {
        top: 2
        left: 2
        bottom: 2
        right: 2
    };
    override var windowInputAreaBorder = EmptyBorder
    {
        top: 4
        left: 6
        bottom: 4
        right: 6
    };

    override var titleBarFont = Font.Font("sansserif", ["BOLD"], 10);
    override var titleBarForeground = Color.WHITE;
    override var titleBarBackground = Color.color(0.08, 0.08, 0.08, 1.0);
    override var titleBarCloseIconColor = Color.color(0.50, 0.20, 0.20, 1.0);
    override var titleBarMinimizeIconColor = Color.color(0.72, 0.72, 0.72, 1.0);
    override var titleBorder = EmptyBorder
    {
        top: 8
        left: 6
        bottom: 6
        right: 6
    };

    override var chatPanelFont = Font.Font("sansserif", ["PLAIN"], 12);
    override var chatFrameBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override var chatPanelBackgroundDark = Color.color(0.08, 0.08, 0.08, 1.0);
    override var chatPanelBackgroundLight = Color.color(0.20, 0.20, 0.20, 1.0);
    
    override var chatPanelBackground = LinearGradient
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

    override var messageFont = Font.Font("sansserif", ["PLAIN"], 12);
    override var commentFont = Font.Font("sansserif", ["PLAIN"], 10);
    override var messageInBackground = Color.color(0.90, 0.90, 0.90, 0.5);
    override var messageInForeground = Color.BLACK;
    override var messageOutBackground = Color.color(0.30, 0.30, 0.30, 0.5);
    override var messageOutForeground = Color.WHITE;
    override var commentForeground = Color.BLACK;
    override var commentBackground = Color.color(0.70, 0.70, 0.70, 0.50);
    override var messageBorderColor = Color.color(0.15, 0.15, 0.15, 1.0);
    override var messageBorder = EmptyBorder
    {
        top: 6
        left: 8
        bottom: 6
        right: 8
    };
    override var commentBorder = EmptyBorder
    {
        top: 3
        left: 8
        bottom: 1
        right: 8
    };

    override var messageInputForeground = Color.WHITE;
    override var messageInputBackground = Color.color(0.50, 0.50, 0.50, 1.0);
    override var messageInputBorderColor = Color.color(0.25, 0.25, 0.25, 1.0);
    override var messageInputBorder = EmptyBorder
    {
        top: 2
        left: 0
        bottom: 1
        right: 1
    };
    override var messageInputAreaBorder = EmptyBorder
    {
        top: 6
        left: 8
        bottom: 6
        right: 8
    };

    override var fieldForeground = Color.color(0.0, 0.0, 0.0, 1.0);
    override var fieldBackground = Color.color(0.72, 0.72, 0.72, 1.0);
    override var fieldFocusColor = Color.color(0.90, 0.90, 0.90, 1.0);

    override var errorForeground = Color.color(1.0, 1.0, 1.0, 1.0);
    override var errorBackgroundInside = LinearGradient
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
    override var errorBackgroundOutside = Color.color(0.0, 0.0, 0.0, 0.75);
    override var errorBorderColor = chatFrameBackground;

    override var warningForeground = Color.color(1.0, 1.0, 1.0, 1.0);
    override var warningBackgroundInside = LinearGradient
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
    override var warningBackgroundOutside = errorBackgroundOutside;
    override var warningBorderColor = errorBorderColor;

    override var infoForeground = Color.color(1.0, 1.0, 1.0, 1.0);
    override var infoBackgroundInside = LinearGradient
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
    override var infoBackgroundOutside = errorBackgroundOutside;
    override var infoBorderColor = errorBorderColor;
};

