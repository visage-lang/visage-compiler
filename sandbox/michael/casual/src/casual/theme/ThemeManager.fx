package casual.theme;

import java.util.prefs.Preferences;
import java.lang.System;

public class ThemeManager extends Theme
{
    static var instance: ThemeManager = new ThemeManager;
    public static function getInstance(): ThemeManager {
        return instance;
    }
    
    var preferences: Preferences;
    
    public var theme: Theme;
    public var themes: Theme[];
    
    override var description = bind theme.description;

    override var defaultFont = bind theme.defaultFont;
    override var defaultForeground = bind theme.defaultForeground;
    override var defaultBackground = bind theme.defaultBackground;

    override var uiForeground = bind theme.uiForeground;
    override var uiBackground = bind theme.uiBackground;
    override var uiBorderColor = bind theme.uiBorderColor;
    override var uiStrokeWidth = bind theme.uiStrokeWidth;

    override var windowFont = bind theme.windowFont;
    override var windowInactive = bind theme.windowInactive;
    override var windowBackground = bind theme.windowBackground;
    override var windowBorder = bind theme.windowBorder;
    override var windowInputAreaBorder = bind theme.windowInputAreaBorder;

    override var titleBarFont = bind theme.titleBarFont;
    override var titleBarForeground = bind theme.titleBarForeground;
    override var titleBarBackground = bind theme.titleBarBackground;
    override var titleBorder = bind theme.titleBorder;
    override var titleBarCloseIconColor = bind theme.titleBarCloseIconColor;
    override var titleBarMinimizeIconColor = bind theme.titleBarMinimizeIconColor;

    override var chatPanelFont = bind theme.chatPanelFont;
    override var chatFrameBackground = bind theme.chatFrameBackground;
    override var chatPanelBackgroundLight = bind theme.chatPanelBackgroundLight;
    override var chatPanelBackgroundDark = bind theme.chatPanelBackgroundDark;
    override var chatPanelBackground = bind theme.chatPanelBackground;
    // TODO: JFXC531 Remove this function and use chatPanelBackground instead
    function getChatPanelBackground(w: Number, h: Number): javafx.ui.Color {
        return theme.getChatPanelBackground(w, h);
    }
    override var chatPanelBorder = bind theme.chatPanelBorder;

    override var messageFont = bind theme.messageFont;
    override var commentFont = bind theme.commentFont;
    override var messageInForeground = bind theme.messageInForeground;
    override var messageInBackground = bind theme.messageInBackground;
    override var messageOutForeground = bind theme.messageOutForeground;
    override var messageOutBackground = bind theme.messageOutBackground;
    override var commentForeground = bind theme.commentForeground;
    override var commentBackground = bind theme.commentBackground;
    override var messageBorderColor = bind theme.messageBorderColor;
    override var messageBorder = bind theme.messageBorder;
    override var commentBorder = bind theme.commentBorder;

    override var messageInputForeground = bind theme.messageInputForeground;
    override var messageInputBackground = bind theme.messageInputBackground;
    override var messageInputBorderColor = bind theme.messageInputBorderColor;
    override var messageInputBorder = bind theme.messageInputBorder;
    override var messageInputAreaBorder = bind theme.messageInputAreaBorder;

    override var fieldForeground = bind theme.fieldForeground;
    override var fieldBackground = bind theme.fieldBackground;
    override var fieldFocusColor = bind theme.fieldFocusColor;

    override var errorForeground = bind theme.errorForeground;
    override var errorBackgroundInside = bind theme.errorBackgroundInside;
    override var errorBackgroundOutside = bind theme.errorBackgroundOutside;
    override var errorBorderColor = bind theme.errorBorderColor;

    override var warningForeground = bind theme.warningForeground;
    override var warningBackgroundInside = bind theme.warningBackgroundInside;
    override var warningBackgroundOutside = bind theme.warningBackgroundOutside;
    override var warningBorderColor = bind theme.warningBorderColor;

    override var infoForeground = bind theme.infoForeground;
    override var infoBackgroundInside = bind theme.infoBackgroundInside;
    override var infoBackgroundOutside = bind theme.infoBackgroundOutside;
    override var infoBorderColor = bind theme.infoBorderColor;

    postinit
    {
        preferences = Preferences.userRoot().node("Casual");

        add(new DefaultTheme());
        //add(new LightTheme());
        add(new DevTheme());

        default();
    }

    function default()
    {
        theme = null;

        var defaultThemeName= new String(preferences.getByteArray("theme", themes[0].description.getBytes()));
        for (t in themes)
        {
            if (t.description.equals(defaultThemeName))
            {
                theme = t;
                break;
            }
        }

        if (theme == null)
        {
            theme = themes[0];
        }
    }

    function change(t:Theme)
    {
        System.out.println("changing themes from \"{theme.description}\" to \"{t.description}\"");
        theme = t;

        preferences.putByteArray("theme", theme.description.getBytes());
    }

    function add(t:Theme)
    {
        if (t != null)
        {
            insert t into themes;
        }
    }

    public function next()
    {
        for (t in themes)
        {
            if (t == theme)
            {
                var next = (indexof t) + 1;
                if (next >= (sizeof themes))
                {
                    next = 0;
                }
                change(themes[next]);
                break;
            }
        }
    }

    public function previous()
    {
        for (t in themes)
        {
            if (t == theme)
            {
                var previous = (indexof t) - 1;
                if (previous < 0)
                {
                    previous = (sizeof themes)-1;
                }
                change(themes[previous]);            
                break;
           }
        }
    }
}
