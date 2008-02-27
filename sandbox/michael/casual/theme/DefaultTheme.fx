package casual.theme;

import javafx.ui.Paint;
import javafx.ui.Color;
import javafx.ui.LinearGradient;
import javafx.ui.Stop;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

public class DefaultTheme extends Theme
{
}

attribute DefaultTheme.description = "Default Casual theme (by ant)";

attribute DefaultTheme.defaultFont = new Font("sansserif", "PLAIN", 10);
attribute DefaultTheme.defaultForeground = BLACK:ThemeColor;
attribute DefaultTheme.defaultBackground = WHITE:ThemeColor;

attribute DefaultTheme.uiForeground = new Color(0.12, 0.12, 0.12, 1.0);
attribute DefaultTheme.uiBackground = new Color(0.72, 0.72, 0.72, 1.0);
attribute DefaultTheme.uiBorderColor = new Color(0.12, 0.12, 0.12, 1.0);
attribute DefaultTheme.uiStrokeWidth = 2;

attribute DefaultTheme.windowFont = new Font("sansserif", "PLAIN", 10);
attribute DefaultTheme.windowInactive = new Color(0.0, 0.0, 0.0, 0.05);
//attribute DefaultTheme.windowInactive = new Color(1, 0.5, 0.5, 0.05);
attribute DefaultTheme.windowBackground = new Color(0.72, 0.72, 0.72, 1.0);
attribute DefaultTheme.windowBorder = EmptyBorder
{
    top: 2
    left: 2
    bottom: 2
    right: 2
};
attribute DefaultTheme.windowInputAreaBorder = EmptyBorder
{
    top: 4
    left: 6
    bottom: 4
    right: 6
};

attribute DefaultTheme.titleBarFont = new Font("sansserif", "BOLD", 10);
attribute DefaultTheme.titleBarForeground = WHITE:ThemeColor;
attribute DefaultTheme.titleBarBackground = new Color(0.08, 0.08, 0.08, 1.0);
attribute DefaultTheme.titleBarCloseIconColor = new Color(0.50, 0.20, 0.20, 1.0);
attribute DefaultTheme.titleBarMinimizeIconColor = new Color(0.72, 0.72, 0.72, 1.0);
attribute DefaultTheme.titleBorder = EmptyBorder
{
    top: 8
    left: 6
    bottom: 6
    right: 6
};

attribute DefaultTheme.chatPanelFont = new Font("sansserif", "PLAIN", 12);
attribute DefaultTheme.chatFrameBackground = new Color(0.72, 0.72, 0.72, 1.0);
attribute DefaultTheme.chatPanelBackgroundDark = new Color(0.08, 0.08, 0.08, 1.0);
attribute DefaultTheme.chatPanelBackgroundLight = new Color(0.20, 0.20, 0.20, 1.0);
attribute DefaultTheme.chatPanelBackground = LinearGradient
{
    x1: 0
    y1: 0
    x2: 0.075
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
            color: chatPanelBackgroundDark
        },
        Stop
        {
            offset: 1
            color: chatPanelBackgroundLight
        }
    ]
};

attribute DefaultTheme.messageFont = new Font("sansserif", "PLAIN", 12);
attribute DefaultTheme.commentFont = new Font("sansserif", "PLAIN", 10);
attribute DefaultTheme.messageInBackground = new Color(0.90, 0.90, 0.90, 0.5);
attribute DefaultTheme.messageInForeground = BLACK:ThemeColor;
attribute DefaultTheme.messageOutBackground = new Color(0.30, 0.30, 0.30, 0.5);
attribute DefaultTheme.messageOutForeground = WHITE:ThemeColor;
attribute DefaultTheme.commentForeground = BLACK:ThemeColor;
attribute DefaultTheme.commentBackground = new Color(0.70, 0.70, 0.70, 0.50);
attribute DefaultTheme.messageBorderColor = new Color(0.15, 0.15, 0.15, 1.0);
attribute DefaultTheme.messageBorder = EmptyBorder
{
    top: 6
    left: 8
    bottom: 6
    right: 8
};
attribute DefaultTheme.commentBorder = EmptyBorder
{
    top: 3
    left: 8
    bottom: 1
    right: 8
};

attribute DefaultTheme.messageInputForeground = WHITE:ThemeColor;
attribute DefaultTheme.messageInputBackground = new Color(0.50, 0.50, 0.50, 1.0);
attribute DefaultTheme.messageInputBorderColor = new Color(0.25, 0.25, 0.25, 1.0);
attribute DefaultTheme.messageInputBorder = EmptyBorder
{
    top: 2
    left: 0
    bottom: 1
    right: 1
};
attribute DefaultTheme.messageInputAreaBorder = EmptyBorder
{
    top: 6
    left: 8
    bottom: 6
    right: 8
};

attribute ThemeManager.fieldForeground = new Color(0.0, 0.0, 0.0, 1.0);
attribute ThemeManager.fieldBackground = new Color(0.72, 0.72, 0.72, 1.0);
attribute DefaultTheme.fieldFocusColor = new Color(0.90, 0.90, 0.90, 1.0);

attribute DefaultTheme.errorForeground = new Color(1.0, 1.0, 1.0, 1.0);
attribute DefaultTheme.errorBackgroundInside = LinearGradient
{
    x1: 0
    y1: 0
    x2: 0.5
    y2: 1
    
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
attribute DefaultTheme.errorBackgroundOutside = new Color(0.0, 0.0, 0.0, 0.75);
attribute DefaultTheme.errorBorderColor = chatFrameBackground;

attribute DefaultTheme.warningForeground = new Color(1.0, 1.0, 1.0, 1.0);
attribute DefaultTheme.warningBackgroundInside = LinearGradient
{
    x1: 0
    y1: 0
    x2: 0.5
    y2: 1
    
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
attribute DefaultTheme.warningBackgroundOutside = errorBackgroundOutside;
attribute DefaultTheme.warningBorderColor = errorBorderColor;

attribute DefaultTheme.infoForeground = new Color(1.0, 1.0, 1.0, 1.0);
attribute DefaultTheme.infoBackgroundInside = LinearGradient
{
    x1: 0
    y1: 0
    x2: 0.5
    y2: 1
    
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
attribute DefaultTheme.infoBackgroundOutside = errorBackgroundOutside;
attribute DefaultTheme.infoBorderColor = errorBorderColor;


