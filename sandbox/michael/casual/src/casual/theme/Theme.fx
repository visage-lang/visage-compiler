package casual.theme;

import javafx.ui.Color;
import javafx.ui.AbstractColor;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

// TODO: JFXC531 Remove this class as soon as LinearGradient supports 
//               proportional values
public class LinearGradientJFXC531 extends javafx.ui.LinearGradient {
    attribute width: Number;
    attribute height: Number;
}

public abstract class Theme
{
    public readonly attribute description: String;
    
    public readonly attribute defaultFont: Font;
    public readonly attribute defaultForeground: Color;
    public readonly attribute defaultBackground: AbstractColor;
    
    public readonly attribute uiForeground: Color;
    public readonly attribute uiBackground: Color;
    public readonly attribute uiBorderColor: Color;
    public readonly attribute uiStrokeWidth: Integer;
    
    public readonly attribute windowFont: Font;
    public readonly attribute windowInactive: Color;
    public readonly attribute windowBackground: Color; 
    public readonly attribute windowBorder: EmptyBorder;
    public readonly attribute windowInputAreaBorder: EmptyBorder;
    
    public readonly attribute titleBarFont: Font;
    public readonly attribute titleBarForeground: Color;
    public readonly attribute titleBarBackground: Color;
    public readonly attribute titleBorder: EmptyBorder;
    public readonly attribute titleBarCloseIconColor: Color;
    public readonly attribute titleBarMinimizeIconColor: Color;
    
    public readonly attribute chatPanelFont: Font;
    public readonly attribute chatFrameBackground: Color;
    public readonly attribute chatPanelBackgroundDark: Color;
    public readonly attribute chatPanelBackgroundLight: Color;
    public readonly attribute chatPanelBackground: Color;
    // TODO: JFXC531 Remove this function and use chatPanelBackground instead
    public abstract function getChatPanelBackground(w: Number, h: Number): Color;
    public readonly attribute chatPanelBorder: EmptyBorder;
    
    public readonly attribute messageFont: Font;
    public readonly attribute commentFont: Font;
    public readonly attribute messageInForeground: Color;
    public readonly attribute messageInBackground: Color;
    public readonly attribute messageOutForeground: Color;
    public readonly attribute messageOutBackground: Color;
    public readonly attribute commentForeground: Color;
    public readonly attribute commentBackground: Color;
    public readonly attribute messageBorderColor: Color;
    public readonly attribute messageBorder: EmptyBorder;
    public readonly attribute commentBorder: EmptyBorder;
    
    public readonly attribute messageInputForeground: Color;
    public readonly attribute messageInputBackground: Color;
    public readonly attribute messageInputBorderColor: Color;
    public readonly attribute messageInputBorder: EmptyBorder;
    public readonly attribute messageInputAreaBorder: EmptyBorder;
    
    public readonly attribute fieldForeground: Color;
    public readonly attribute fieldBackground: Color;
    public readonly attribute fieldFocusColor: Color;

    public readonly attribute errorForeground: Color;
    public readonly attribute errorBackgroundInside: AbstractColor;
    public readonly attribute errorBackgroundOutside: Color;
    public readonly attribute errorBorderColor: Color;

    public readonly attribute warningForeground: Color;
    public readonly attribute warningBackgroundInside: AbstractColor;
    public readonly attribute warningBackgroundOutside: Color;
    public readonly attribute warningBorderColor: Color;

    public readonly attribute infoForeground: Color;
    public readonly attribute infoBackgroundInside: AbstractColor;
    public readonly attribute infoBackgroundOutside: Color;
    public readonly attribute infoBorderColor: Color;
}
