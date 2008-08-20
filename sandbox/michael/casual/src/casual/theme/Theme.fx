package casual.theme;

import javafx.ui.Color;
import javafx.ui.AbstractColor;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

// TODO: JFXC531 Remove this class as soon as LinearGradient supports 
//               proportional values
public class LinearGradientJFXC531 extends javafx.ui.LinearGradient {
    var width: Number;
    var height: Number;
}

public abstract class Theme
{
    public readonly var description: String;
    
    public readonly var defaultFont: Font;
    public readonly var defaultForeground: Color;
    public readonly var defaultBackground: AbstractColor;
    
    public readonly var uiForeground: Color;
    public readonly var uiBackground: Color;
    public readonly var uiBorderColor: Color;
    public readonly var uiStrokeWidth: Integer;
    
    public readonly var windowFont: Font;
    public readonly var windowInactive: Color;
    public readonly var windowBackground: Color; 
    public readonly var windowBorder: EmptyBorder;
    public readonly var windowInputAreaBorder: EmptyBorder;
    
    public readonly var titleBarFont: Font;
    public readonly var titleBarForeground: Color;
    public readonly var titleBarBackground: Color;
    public readonly var titleBorder: EmptyBorder;
    public readonly var titleBarCloseIconColor: Color;
    public readonly var titleBarMinimizeIconColor: Color;
    
    public readonly var chatPanelFont: Font;
    public readonly var chatFrameBackground: Color;
    public readonly var chatPanelBackgroundDark: Color;
    public readonly var chatPanelBackgroundLight: Color;
    public readonly var chatPanelBackground: Color;
    // TODO: JFXC531 Remove this function and use chatPanelBackground instead
    public abstract function getChatPanelBackground(w: Number, h: Number): Color;
    public readonly var chatPanelBorder: EmptyBorder;
    
    public readonly var messageFont: Font;
    public readonly var commentFont: Font;
    public readonly var messageInForeground: Color;
    public readonly var messageInBackground: Color;
    public readonly var messageOutForeground: Color;
    public readonly var messageOutBackground: Color;
    public readonly var commentForeground: Color;
    public readonly var commentBackground: Color;
    public readonly var messageBorderColor: Color;
    public readonly var messageBorder: EmptyBorder;
    public readonly var commentBorder: EmptyBorder;
    
    public readonly var messageInputForeground: Color;
    public readonly var messageInputBackground: Color;
    public readonly var messageInputBorderColor: Color;
    public readonly var messageInputBorder: EmptyBorder;
    public readonly var messageInputAreaBorder: EmptyBorder;
    
    public readonly var fieldForeground: Color;
    public readonly var fieldBackground: Color;
    public readonly var fieldFocusColor: Color;

    public readonly var errorForeground: Color;
    public readonly var errorBackgroundInside: AbstractColor;
    public readonly var errorBackgroundOutside: Color;
    public readonly var errorBorderColor: Color;

    public readonly var warningForeground: Color;
    public readonly var warningBackgroundInside: AbstractColor;
    public readonly var warningBackgroundOutside: Color;
    public readonly var warningBorderColor: Color;

    public readonly var infoForeground: Color;
    public readonly var infoBackgroundInside: AbstractColor;
    public readonly var infoBackgroundOutside: Color;
    public readonly var infoBorderColor: Color;
}
