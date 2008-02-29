package casual.theme;

import java.util.prefs.Preferences;
import java.lang.System;

var theme: ThemeManager = new ThemeManager();

public class ThemeManager extends Theme
{
    attribute preferences: Preferences;
    
    public attribute theme: Theme;
    public attribute themes: Theme[];
    
    override attribute description = bind theme.description;

    override attribute defaultFont = bind theme.defaultFont;
    override attribute defaultForeground = bind theme.defaultForeground;
    override attribute defaultBackground = bind theme.defaultBackground;

    override attribute uiForeground = bind theme.uiForeground;
    override attribute uiBackground = bind theme.uiBackground;
    override attribute uiBorderColor = bind theme.uiBorderColor;
    override attribute uiStrokeWidth = bind theme.uiStrokeWidth;

    override attribute windowFont = bind theme.windowFont;
    override attribute windowInactive = bind theme.windowInactive;
    override attribute windowBackground = bind theme.windowBackground;
    override attribute windowBorder = bind theme.windowBorder;
    override attribute windowInputAreaBorder = bind theme.windowInputAreaBorder;

    override attribute titleBarFont = bind theme.titleBarFont;
    override attribute titleBarForeground = bind theme.titleBarForeground;
    override attribute titleBarBackground = bind theme.titleBarBackground;
    override attribute titleBorder = bind theme.titleBorder;
    override attribute titleBarCloseIconColor = bind theme.titleBarCloseIconColor;
    override attribute titleBarMinimizeIconColor = bind theme.titleBarMinimizeIconColor;

    override attribute chatPanelFont = bind theme.chatPanelFont;
    override attribute chatFrameBackground = bind theme.chatFrameBackground;
    override attribute chatPanelBackgroundLight = bind theme.chatPanelBackgroundLight;
    override attribute chatPanelBackgroundDark = bind theme.chatPanelBackgroundDark;
    override attribute chatPanelBackground = bind theme.chatPanelBackground;
    override attribute chatPanelBorder = bind theme.chatPanelBorder;

    override attribute messageFont = bind theme.messageFont;
    override attribute commentFont = bind theme.commentFont;
    override attribute messageInForeground = bind theme.messageInForeground;
    override attribute messageInBackground = bind theme.messageInBackground;
    override attribute messageOutForeground = bind theme.messageOutForeground;
    override attribute messageOutBackground = bind theme.messageOutBackground;
    override attribute commentForeground = bind theme.commentForeground;
    override attribute commentBackground = bind theme.commentBackground;
    override attribute messageBorderColor = bind theme.messageBorderColor;
    override attribute messageBorder = bind theme.messageBorder;
    override attribute commentBorder = bind theme.commentBorder;

    override attribute messageInputForeground = bind theme.messageInputForeground;
    override attribute messageInputBackground = bind theme.messageInputBackground;
    override attribute messageInputBorderColor = bind theme.messageInputBorderColor;
    override attribute messageInputBorder = bind theme.messageInputBorder;
    override attribute messageInputAreaBorder = bind theme.messageInputAreaBorder;

    override attribute fieldForeground = bind theme.fieldForeground;
    override attribute fieldBackground = bind theme.fieldBackground;
    override attribute fieldFocusColor = bind theme.fieldFocusColor;

    override attribute errorForeground = bind theme.errorForeground;
    override attribute errorBackgroundInside = bind theme.errorBackgroundInside;
    override attribute errorBackgroundOutside = bind theme.errorBackgroundOutside;
    override attribute errorBorderColor = bind theme.errorBorderColor;

    override attribute warningForeground = bind theme.warningForeground;
    override attribute warningBackgroundInside = bind theme.warningBackgroundInside;
    override attribute warningBackgroundOutside = bind theme.warningBackgroundOutside;
    override attribute warningBorderColor = bind theme.warningBorderColor;

    override attribute infoForeground = bind theme.infoForeground;
    override attribute infoBackgroundInside = bind theme.infoBackgroundInside;
    override attribute infoBackgroundOutside = bind theme.infoBackgroundOutside;
    override attribute infoBorderColor = bind theme.infoBorderColor;

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
        if (t <> null)
        {
            insert t into themes;
        }
    }

    function next()
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

    function previous()
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
