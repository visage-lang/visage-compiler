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
import javafx.ui.HorizontalScrollBarPolicy;
import javafx.ui.VerticalScrollBarPolicy;

import javafx.ui.canvas.Node;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;

import java.util.prefs.Preferences;
import java.lang.Math;

public class BuddyWindow extends CasualFrame
{
    public attribute im: InstantMessenger;

    attribute buddy: Buddy = bind im.buddies[buddyIndex];
    attribute buddyIndex: Integer;
    
    attribute preferences: Preferences = Preferences.userRoot().node("Casual");
    
    override attribute background = bind ThemeManager.getInstance().windowBackground;

    override attribute screenx = Math.max (0, preferences.getInt("screenx", 0))
        on replace {
            preferences.putInt("screenx", screenx);
        };

    override attribute screeny = Math.max (0, preferences.getInt("screeny", 0))
        on replace {
            preferences.putInt("screeny", screeny);
        };
        
    override attribute undecorated = true;
    override attribute width = 250;
    override attribute height = 300;
    override attribute centerOnScreen = true;
    override attribute visible = true;

    override attribute content = Canvas
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
                        border: bind ThemeManager.getInstance().windowBorder
                        background: bind ThemeManager.getInstance().chatFrameBackground

                        var top: Canvas =
                            Canvas {
                                border: new EmptyBorder
                                background: bind ThemeManager.getInstance().chatPanelBackground

                                content: TitleBar
                                {
                                    var offsets = bind (ThemeManager.getInstance().windowBorder.left + ThemeManager.getInstance().windowBorder.right)

                                    frame: this
                                    title: "CASUAL"
                                    width: bind (top.width.intValue() - offsets)
                                    foreground: bind ThemeManager.getInstance().titleBarForeground
                                    background: bind ThemeManager.getInstance().titleBarBackground

                                    onClose: function()
                                    {
                                        im.logout();

                                        java.lang.System.exit(0);
                                    }
                                }
                            }
                        top: top

                        var center: ScrollPane =
                            ScrollPane {
                                viewportBorder: EmptyBorder{}
                                scrollPaneBorder: EmptyBorder{}
                                border: EmptyBorder{}
                                verticalScrollBarPolicy: VerticalScrollBarPolicy.NEVER
                                horizontalScrollBarPolicy: HorizontalScrollBarPolicy.NEVER

                                view: ListBox
                                {
                                    cellBackground: Color.color (0, 0, 0, 0)
                                    cellForeground: bind ThemeManager.getInstance().uiBackground.darker()
                                    selectedCellForeground: bind ThemeManager.getInstance().uiForeground
                                    selectedCellBackground: bind ThemeManager.getInstance().uiBackground
                                    background: bind ThemeManager.getInstance().chatPanelBackground

                                    enableDND: false
                                    selection: bind buddyIndex
                                    cells: bind for (buddy in im.buddies) ListCell
                                    {
                                        var buddyName = bind "{buddy.userName}@{buddy.accountName}"
                                        var buddyStatus = bind buddy.presence.id
                                        var string = bind "&lt;{buddyName}&gt; {buddyStatus}"

                                        border: bind ThemeManager.getInstance().windowInputAreaBorder
                                        text: bind "<html><div width='{center.width}'>{string}</div></html>"
                                    }

                                    onKeyDown: function(e:KeyEvent)
                                    {
                                        var k:KeyStroke = e.keyStroke;
                                        if (k == KeyStroke.RIGHT)
                                        {
                                            ThemeManager.getInstance().next();
                                        }
                                        else if (k == KeyStroke.LEFT)
                                        {
                                            ThemeManager.getInstance().previous();
                                        }
                                    }

                                    action: function()
                                    {
                                        if (buddy.presence == Buddy.BuddyPresence.AVAILABLE)
                                        {
                                            buddy.startChat();
                                        }
                                        else
                                        {
                                            java.awt.Toolkit.getDefaultToolkit().beep();
                                        }
                                    }
                                }
                            }
                        center: center
                    }
                },
                // inactive rect
                Rect
                {
                    visible: bind (active==false)
                    x: bind 0
                    y: bind 0
                    width: bind width
                    height: bind height
                    fill: bind ThemeManager.getInstance().windowInactive
                } as Node,
            ]
        }
    };
    
    public function requestFocus() {}

}

