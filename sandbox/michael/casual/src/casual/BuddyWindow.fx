package casual;

import casual.theme.*;
import casual.ui.*;
import casual.im.*;

import javafx.ui.BorderPanel;
import javafx.ui.Canvas;
import javafx.ui.Widget;
import javafx.ui.Color;
import javafx.ui.EmptyBorder;
import javafx.ui.ScrollPane;
import javafx.ui.ListBox;
import javafx.ui.ListCell;
import javafx.ui.KeyEvent;
import javafx.ui.KeyStroke;

import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;

import java.util.prefs.Preferences;

public class BuddyWindow extends Frame
{
    public operation BuddyWindow(im:InstantMessenger);
    
    public attribute im: InstantMessenger;

    attribute buddy: Buddy;
    attribute buddyIndex: Integer;
    
    attribute preferences: Preferences?;
}

attribute BuddyWindow.buddy = bind im.buddies[buddyIndex];
attribute BuddyWindow.background = bind theme:ThemeManager.windowBackground;

operation BuddyWindow.BuddyWindow(im:InstantMessenger)
{
    var frame = this;
    
    frame.im = im;
    
    preferences = Preferences.userRoot().node("Casual");
    var x = preferences.getInt("screenx", 0);
    var y = preferences.getInt("screeny", 0);
    if (x >= 0)
    {
        screenx = x;
    }
    if (y >= 0)
    {
        screeny = y;
    }
    undecorated = true;
    width = 250;
    height = 300;
    centerOnScreen = true;
    visible = true;
    
    content = Canvas
    {
        content: Group
        {
            content:
            [
                View
                {                    
                    sizeToFitCanvas: true
                    content: BorderPanel
                    {
                        var: panel

                        border: bind theme:ThemeManager.windowBorder
                        background: bind theme:ThemeManager.chatFrameBackground

                        top: Canvas
                        {
                            border: EmptyBorder
                            background: bind theme:ThemeManager.chatPanelBackground

                            content: TitleBar
                            {
                                var offsets = bind (theme:ThemeManager.windowBorder.left + theme:ThemeManager.windowBorder.right)

                                frame: frame
                                title: "CASUAL"
                                width: bind (panel.width - offsets)
                                foreground: bind theme:ThemeManager.titleBarForeground
                                background: bind theme:ThemeManager.titleBarBackground

                                onClose: operation()
                                {
                                    im.logout();

                                    <<java.lang.System>>.exit(0);
                                }
                            }
                        }

                        center: ScrollPane
                        {
                            var: panel

                            viewportBorder: EmptyBorder{}
                            scrollPaneBorder: EmptyBorder{}
                            border: EmptyBorder{}
                            verticalScrollBarPolicy: NEVER
                            horizontalScrollBarPolicy: NEVER

                            view: ListBox
                            {
                                cellBackground: new Color(0, 0, 0, 0)
                                cellForeground: bind theme:ThemeManager.uiBackground.darker()
                                selectedCellForeground: bind theme:ThemeManager.uiForeground
                                selectedCellBackground: bind theme:ThemeManager.uiBackground
                                background: bind theme:ThemeManager.chatPanelBackground
                                var: self
                        
                                enableDND: false
                                selection: bind frame.buddyIndex
                                cells: bind foreach (buddy in im.buddies) ListCell
                                {
                                    var buddyName = bind "{buddy.userName}@{buddy.accountName}"
                                    var buddyStatus = bind buddy.presence.id
                                    var string = bind "&lt;{buddyName}&gt; {buddyStatus}"

                                    border: bind theme:ThemeManager.windowInputAreaBorder
                                    text: bind "<html><div width='{panel.width}'>{string}</div></html>"
                                }
                                
                                onKeyDown: operation(e:KeyEvent)
                                {
                                    var k:KeyStroke = e.keyStroke;
                                    if (k == RIGHT:KeyStroke)
                                    {
                                        theme:ThemeManager.next();
                                    }
                                    else if (k == LEFT:KeyStroke)
                                    {
                                        theme:ThemeManager.previous();
                                    }
                                }
                                
                                action: operation()
                                {
                                    var buddy = frame.buddy;
                                    if (buddy.presence == AVAILABLE:BuddyPresence)
                                    {
                                        buddy.startChat();
                                    }
                                    else
                                    {
                                        <<java.awt.Toolkit>>.getDefaultToolkit().beep();
                                    }
                                }
                            }
                        }
                    }
                },
                // inactive rect
                Rect
                {
                    visible: bind (frame.active==false)
                    x: bind 0
                    y: bind 0
                    width: bind frame.width
                    height: bind frame.height
                    fill: bind theme:ThemeManager.windowInactive
                },
            ]
        }
    };
}

trigger on BuddyWindow.screenx = value
{
    preferences.putInt("screenx", screenx);
}

trigger on BuddyWindow.screeny = value
{
    preferences.putInt("screeny", screeny);
}
 
