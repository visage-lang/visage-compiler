package casual.theme;

import java.util.prefs.Preferences;

theme:ThemeManager = new ThemeManager();

public class ThemeManager extends Theme
{
    attribute preferences: Preferences?;
    
    public attribute theme: Theme;
    public attribute themes: Theme*;
    
    public operation change(t:Theme);
    public operation default();
    public operation add(t:Theme);
    public operation next();
    public operation previous();
}

attribute ThemeManager.description = bind theme.description;

attribute ThemeManager.defaultFont = bind theme.defaultFont;
attribute ThemeManager.defaultForeground = bind theme.defaultForeground;
attribute ThemeManager.defaultBackground = bind theme.defaultBackground;

attribute ThemeManager.uiForeground = bind theme.uiForeground;
attribute ThemeManager.uiBackground = bind theme.uiBackground;
attribute ThemeManager.uiBorderColor = bind theme.uiBorderColor;
attribute ThemeManager.uiStrokeWidth = bind theme.uiStrokeWidth;

attribute ThemeManager.windowFont = bind theme.windowFont;
attribute ThemeManager.windowInactive = bind theme.windowInactive;
attribute ThemeManager.windowBackground = bind theme.windowBackground;
attribute ThemeManager.windowBorder = bind theme.windowBorder;
attribute ThemeManager.windowInputAreaBorder = bind theme.windowInputAreaBorder;

attribute ThemeManager.titleBarFont = bind theme.titleBarFont;
attribute ThemeManager.titleBarForeground = bind theme.titleBarForeground;
attribute ThemeManager.titleBarBackground = bind theme.titleBarBackground;
attribute ThemeManager.titleBorder = bind theme.titleBorder;
attribute ThemeManager.titleBarCloseIconColor = bind theme.titleBarCloseIconColor;
attribute ThemeManager.titleBarMinimizeIconColor = bind theme.titleBarMinimizeIconColor;

attribute ThemeManager.chatPanelFont = bind theme.chatPanelFont;
attribute ThemeManager.chatFrameBackground = bind theme.chatFrameBackground;
attribute ThemeManager.chatPanelBackgroundLight = bind theme.chatPanelBackgroundLight;
attribute ThemeManager.chatPanelBackgroundDark = bind theme.chatPanelBackgroundDark;
attribute ThemeManager.chatPanelBackground = bind theme.chatPanelBackground;
attribute ThemeManager.chatPanelBorder = bind theme.chatPanelBorder;

attribute ThemeManager.messageFont = bind theme.messageFont;
attribute ThemeManager.commentFont = bind theme.commentFont;
attribute ThemeManager.messageInForeground = bind theme.messageInForeground;
attribute ThemeManager.messageInBackground = bind theme.messageInBackground;
attribute ThemeManager.messageOutForeground = bind theme.messageOutForeground;
attribute ThemeManager.messageOutBackground = bind theme.messageOutBackground;
attribute ThemeManager.commentForeground = bind theme.commentForeground;
attribute ThemeManager.commentBackground = bind theme.commentBackground;
attribute ThemeManager.messageBorderColor = bind theme.messageBorderColor;
attribute ThemeManager.messageBorder = bind theme.messageBorder;
attribute ThemeManager.commentBorder = bind theme.commentBorder;

attribute ThemeManager.messageInputForeground = bind theme.messageInputForeground;
attribute ThemeManager.messageInputBackground = bind theme.messageInputBackground;
attribute ThemeManager.messageInputBorderColor = bind theme.messageInputBorderColor;
attribute ThemeManager.messageInputBorder = bind theme.messageInputBorder;
attribute ThemeManager.messageInputAreaBorder = bind theme.messageInputAreaBorder;

attribute ThemeManager.fieldForeground = bind theme.fieldForeground;
attribute ThemeManager.fieldBackground = bind theme.fieldBackground;
attribute ThemeManager.fieldFocusColor = bind theme.fieldFocusColor;

attribute ThemeManager.errorForeground = bind theme.errorForeground;
attribute ThemeManager.errorBackgroundInside = bind theme.errorBackgroundInside;
attribute ThemeManager.errorBackgroundOutside = bind theme.errorBackgroundOutside;
attribute ThemeManager.errorBorderColor = bind theme.errorBorderColor;

attribute ThemeManager.warningForeground = bind theme.warningForeground;
attribute ThemeManager.warningBackgroundInside = bind theme.warningBackgroundInside;
attribute ThemeManager.warningBackgroundOutside = bind theme.warningBackgroundOutside;
attribute ThemeManager.warningBorderColor = bind theme.warningBorderColor;

attribute ThemeManager.infoForeground = bind theme.infoForeground;
attribute ThemeManager.infoBackgroundInside = bind theme.infoBackgroundInside;
attribute ThemeManager.infoBackgroundOutside = bind theme.infoBackgroundOutside;
attribute ThemeManager.infoBorderColor = bind theme.infoBorderColor;

operation ThemeManager.ThemeManager()
{
    preferences = Preferences.userRoot().node("Casual");
    
    add(new DefaultTheme());
    //add(new LightTheme());
    add(new DevTheme());
    
    default();
}

operation ThemeManager.default()
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

operation ThemeManager.change(t:Theme)
{
    println("changing themes from \"{theme.description}\" to \"{t.description}\"");
    theme = t;
    
    preferences.putByteArray("theme", theme.description.getBytes());
}

operation ThemeManager.add(t:Theme)
{
    if (t <> null)
    {
        insert t as last into themes;
    }
}

operation ThemeManager.next()
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

operation ThemeManager.previous()
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
