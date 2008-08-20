package casual;

import casual.theme.*;
import casual.ui.*;
import casual.im.*;

import javafx.ui.BorderPanel;
import javafx.ui.Canvas;
import javafx.ui.SimpleLabel;
import javafx.ui.Widget;
import javafx.ui.Color;
import javafx.ui.EmptyBorder;
import javafx.ui.KeyEvent;
import javafx.ui.KeyStroke;
import javafx.ui.GroupPanel;
import javafx.ui.GroupLayout;
import javafx.ui.Row;
import javafx.ui.Column;
import javafx.ui.PasswordField;
import javafx.ui.TextField;
import javafx.ui.Alignment;
import javafx.ui.HorizontalAlignment;

import javafx.ui.canvas.View;
import javafx.ui.canvas.Node;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Transform;
import javafx.ui.canvas.Translate;
import javafx.ui.canvas.HBox;
import javafx.ui.canvas.Text;

import java.util.prefs.Preferences;
import java.lang.System;

class FocusedTextField extends TextField
{
    public var frame: AccountWindow;

    override var focusable = bind frame.focusReady
        on replace {
            if (focusable == true)
            {
                var bounds = getBoundsRelativeTo(null);
                frame.focusRectX = bounds.x;
                frame.focusRectY = bounds.y;
                frame.focusRectWidth = bounds.width;
                frame.focusRectHeight = bounds.height;
            }
        };
}

class FocusedPasswordField extends PasswordField
{
    public var frame: AccountWindow;

    override var focusable = bind frame.focusReady
        on replace {
            if (focusable == true)
            {
                var bounds = getBoundsRelativeTo(null);
                frame.focusRectX = bounds.x;
                frame.focusRectY = bounds.y;
                frame.focusRectWidth = bounds.width;
                frame.focusRectHeight = bounds.height;
            }
        };
}

public class AccountWindow extends CasualFrame
{
    public var buddy: Buddy = Buddy { type: Buddy.BuddyType.USER }
        on replace {
            if (buddy.type == Buddy.BuddyType.USER)
            {        
                buddy.firstName = preferences.get(Buddy.BuddyKey.FIRST_NAME.id, null);
                buddy.lastName = preferences.get(Buddy.BuddyKey.LAST_NAME.id, null);
                buddy.userName = preferences.get(Buddy.BuddyKey.USER_NAME.id, null);
                buddy.accountName = preferences.get(Buddy.BuddyKey.ACCOUNT_NAME.id, "JABBER.org");
            }
        };

    
    public function save(key:Buddy.BuddyKey, value:String) {
        if (buddy.type == Buddy.BuddyType.USER)
        {
            //println("AccountWindow.save [{key}, {value}]");
            preferences.put(key.id, value); 
        }
    };

    public function addlogin() {
        if (buddy.type == Buddy.BuddyType.USER)
        {
            if ((buddy.userName.length()>0) and (buddy.accountName.length()>0) and (buddy.password.length()>0))
            {
                showInfoMessage("contacting server; please wait.", "CONNECTING", false);
                
                var frame = this;

                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        var im = InstantMessenger.createIM(buddy);
                        im.login(frame);
                    }
                });
            }
            else
            {
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }
        else
        {
            if ((buddy.userName.length()>0) and (buddy.accountName.length()>0))
            {
                System.out.println("add {buddy.toString()}");
            }
            else
            {
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }
    };

    public function cancel() {
        if (buddy.type == Buddy.BuddyType.USER)
        {
            java.lang.System.exit(0);
        }
        else
        {
            System.out.println("cancel");
        }
    };

    public function requestFocus()    {
        if (dialog.active == false)
        {
            focusReady = true;

            if ((buddy.userName.length() > 0) and (buddy.accountName.length() > 0) and (buddy.type == Buddy.BuddyType.USER))
            {
                if (passwordField!=null)
                {
                    passwordField.requestFocus();
                }
            }
            else
            {
                userField.requestFocus();
            }
        }
    };
    
    var preferences: Preferences = Preferences.userRoot().node("Casual");

    var addressRow: Row = Row {alignment: Alignment.BASELINE}
    var passwordRow: Row = Row {alignment: Alignment.BASELINE}
    var firstNameRow: Row = Row {alignment: Alignment.BASELINE}
    var lastNameRow: Row = Row {alignment: Alignment.BASELINE}
    var labelsColumn: Column = Column {alignment: Alignment.TRAILING}
    var fieldsColumn: Column = Column {alignment: Alignment.LEADING, resizable: true}

    var userField: FocusedTextField = FocusedTextField {
        frame: this
//        horizontal: {pref: 270}
        row: firstNameRow
        column: fieldsColumn
        foreground: bind ThemeManager.getInstance().fieldForeground
        background: bind ThemeManager.getInstance().fieldBackground
        font: bind ThemeManager.getInstance().windowFont.bold()
        border: bind ThemeManager.getInstance().windowInputAreaBorder
        value: bind buddy.firstName
        onChange: function(newValue) 
        {
            save(Buddy.BuddyKey.FIRST_NAME, newValue);
        }
    };

    var passwordField: FocusedPasswordField = FocusedPasswordField {
        frame: this
        visible: bind (buddy.type == Buddy.BuddyType.USER)
        focusTraversalKeysEnabled: bind (buddy.type == Buddy.BuddyType.USER)
//        horizontal: {pref: 175}
        row: passwordRow
        column: fieldsColumn
        foreground: bind ThemeManager.getInstance().fieldForeground
        background: bind ThemeManager.getInstance().fieldBackground
        font: bind ThemeManager.getInstance().windowFont.bold()
        border: bind ThemeManager.getInstance().windowInputAreaBorder
        value: bind buddy.password
        onKeyDown: function(e:KeyEvent)
        {
            if (e.keyStroke == KeyStroke.ENTER)
            {
                e.source.consume();

                buttonAddLogin.doClick();
            }
        }
    };
    
    override var dialog = Dialog {
        frame: this
        active: false
    };

    
    var focusReady: Boolean;
    var focusRectX: Integer;
    var focusRectY: Integer;
    var focusRectWidth: Integer;
    var focusRectHeight: Integer;

    override var background = bind ThemeManager.getInstance().windowBackground;
    override var undecorated = true;
    override var width = 440;
    override var height = 250;
    override var centerOnScreen = true;

    postinit {
        requestFocus();
    };
    
    var buttonCancel: Button = 
        Button {
            text: "CANCEL"
            width: 60
            height: 20
            font: bind ThemeManager.getInstance().windowFont.bold()
            onClick: function()
            {
                cancel();
            }
        };
    
    var buttonAddLogin: Button = 
        Button {
            text: bind if (buddy.type == Buddy.BuddyType.USER) then "LOGIN" else "ADD"
            width: 50
            height: 20
            font: bind ThemeManager.getInstance().windowFont.bold()
            onClick: function()
            {
                addlogin();
            }
        };

    override var content = Canvas
    {
        content: Group
        {
            content:
            [
                View
                {                    
                    sizeToFitCanvas: true
                    var panel: BorderPanel = 
                        BorderPanel {
                            border: bind ThemeManager.getInstance().windowBorder
                            background: bind ThemeManager.getInstance().chatFrameBackground

                            var top: Canvas =
                                Canvas {
                                    border: new EmptyBorder
                                    
                                    // TODO JFXC531: Replace this hack
                                    background: bind ThemeManager.getInstance().getChatPanelBackground(top.width, top.height)
                                    
                                    

                                    content: TitleBar
                                    {
                                        var offsets = bind (ThemeManager.getInstance().windowBorder.left + ThemeManager.getInstance().windowBorder.right)

                                        frame: this
                                        title: "CASUAL ACCOUNTS"
                                        width: bind (panel.width.intValue() - offsets)
                                        foreground: bind ThemeManager.getInstance().titleBarForeground
                                        background: bind ThemeManager.getInstance().titleBarBackground

                                        onClose: function()
                                        {
                                            java.lang.System.exit(0);
                                        }
                                    }
                                }
                            top: top

                            var center: Canvas =
                                Canvas {
                                    border: new EmptyBorder
                                    background: bind ThemeManager.getInstance().getChatPanelBackground (center.width, center.height)

                                    content: Group
                                    {
                                        content:
                                        [
                                            View
                                            {
                                                var text = bind if (buddy.type == Buddy.BuddyType.BUDDY) then "BUDDY" else "YOUR"

                                                transform: Translate {x: 30, y: 20}

                                                content: SimpleLabel
                                                {
                                                    font: bind ThemeManager.getInstance().windowFont.bold()
                                                    foreground: bind ThemeManager.getInstance().messageInputForeground
                                                    text: "{text} PERSONAL DETAILS"
                                                }
                                            },
                                            View
                                            {
                                                antialiasText: true
                                                transform: bind Translate {x: panel.width-10, y: 40}
                                                halign: HorizontalAlignment.TRAILING

                                                content: GroupPanel
                                                {
                                                    rows: [firstNameRow, lastNameRow]
                                                    columns: [labelsColumn, fieldsColumn]
                                                    autoCreateContainerGaps: false

                                                    content:
                                                    [
                                                        SimpleLabel 
                                                        {
                                                            row: firstNameRow
                                                            column: labelsColumn
                                                            border: bind ThemeManager.getInstance().windowInputAreaBorder
                                                            font: bind ThemeManager.getInstance().windowFont.bigger()
                                                            background: Color.color(0,0,0,0)
                                                            foreground: bind ThemeManager.getInstance().messageInputForeground
                                                            text: "first name -"

                                                        },
                                                        userField,
                                                        SimpleLabel
                                                        {
                                                            row: lastNameRow, column: labelsColumn
                                                            border: bind ThemeManager.getInstance().windowInputAreaBorder
                                                            font: bind ThemeManager.getInstance().windowFont.bigger()
                                                            background: Color.color(0,0,0,0)
                                                            foreground: bind ThemeManager.getInstance().messageInputForeground
                                                            text: "last name -"
                                                        },
                                                        FocusedTextField
                                                        {
                                                            frame: this
        //                                                    horizontal: {pref: 270}
                                                            row: lastNameRow
                                                            column: fieldsColumn
                                                            foreground: bind ThemeManager.getInstance().fieldForeground
                                                            background: bind ThemeManager.getInstance().fieldBackground
                                                            font: bind ThemeManager.getInstance().windowFont.bold()
                                                            border: bind ThemeManager.getInstance().windowInputAreaBorder
                                                            value: bind buddy.lastName
                                                            onChange: function(newValue) 
                                                            {
                                                                save(Buddy.BuddyKey.LAST_NAME, newValue);
                                                            }
                                                        }
                                                    ]
                                                }
                                            },
                                            View
                                            {
                                                transform: bind Translate {x: 30, y: 105}
                                                content: SimpleLabel
                                                {
                                                    font: bind ThemeManager.getInstance().windowFont.bold()
                                                    foreground: bind ThemeManager.getInstance().messageInputForeground
                                                    text: "INSTANT MESSENGER DETAILS"
                                                }
                                            },
                                            View
                                            {
                                                antialiasText: true
                                                transform: bind Translate {x: panel.width-10, y: 125}
                                                halign: HorizontalAlignment.TRAILING

                                                content: GroupPanel
                                                {
                                                    rows: [addressRow, passwordRow]
                                                    columns: [labelsColumn, fieldsColumn]
                                                    autoCreateContainerGaps: false
                                                    content:
                                                    [
                                                        SimpleLabel
                                                        {
                                                            row: addressRow, column: labelsColumn
                                                            border: bind ThemeManager.getInstance().windowInputAreaBorder
                                                            font: bind ThemeManager.getInstance().windowFont.bigger()
                                                            background: Color.color(0,0,0,0)
                                                            foreground: bind ThemeManager.getInstance().messageInputForeground
                                                            text: "account address -"

                                                        },
                                                        GroupPanel
                                                        {
                                                            var row = Row {alignment: Alignment.BASELINE}
                                                            var nameColumn = Column {}
                                                            var atColumn = Column {}
                                                            var serverColumn = Column {}

                                                            rows: row
                                                            columns: [nameColumn, atColumn, serverColumn]
                                                            autoCreateGaps: false
                                                            autoCreateContainerGaps: false
                                                            row: addressRow, column: fieldsColumn
                                                            content:
                                                            [
                                                                FocusedTextField
                                                                {
                                                                    frame: this 
        //                                                            horizontal: {pref: 130}
                                                                    row: row
                                                                    column: nameColumn
                                                                    foreground: bind ThemeManager.getInstance().fieldForeground
                                                                    background: bind ThemeManager.getInstance().fieldBackground
                                                                    font: bind ThemeManager.getInstance().windowFont.bold()
                                                                    border: bind ThemeManager.getInstance().windowInputAreaBorder
                                                                    value: bind buddy.userName
                                                                    onChange: function(newValue) 
                                                                    {
                                                                         save(Buddy.BuddyKey.USER_NAME, newValue);
                                                                    }
                                                                },
                                                                SimpleLabel
                                                                {
        //                                                            horizontal: {pref: 10}
                                                                    row: row
                                                                    column: atColumn
                                                                    border: bind ThemeManager.getInstance().windowInputAreaBorder
                                                                    font: bind ThemeManager.getInstance().windowFont.bigger().bold()
                                                                    background: Color.color(0,0,0,0)
                                                                    foreground: bind ThemeManager.getInstance().messageInputForeground
                                                                    text: "@"
                                                                },
                                                                FocusedTextField
                                                                {
                                                                    frame: this
        //                                                            horizontal: {pref: 118}
                                                                    row: row
                                                                    column: serverColumn
                                                                    foreground: bind ThemeManager.getInstance().fieldForeground
                                                                    background: bind ThemeManager.getInstance().fieldBackground
                                                                    font: bind ThemeManager.getInstance().windowFont.bold()
                                                                    border: bind ThemeManager.getInstance().windowInputAreaBorder
                                                                    value: bind buddy.accountName
                                                                    onChange: function(newValue) 
                                                                    {
                                                                         save(Buddy.BuddyKey.ACCOUNT_NAME, newValue);
                                                                    }
                                                                }
                                                            ]
                                                        },
                                                        SimpleLabel
                                                        {
                                                            visible: bind (buddy.type == Buddy.BuddyType.USER)
                                                            row: passwordRow
                                                            column: labelsColumn
                                                            border: bind ThemeManager.getInstance().windowInputAreaBorder
                                                            font: bind ThemeManager.getInstance().windowFont.bigger()
                                                            background: Color.color(0,0,0,0)
                                                            foreground: bind ThemeManager.getInstance().messageInputForeground
                                                            text: "account password -"
                                                        },
                                                        passwordField
                                                    ]
                                                }
                                            },
                                            HBox
                                            {
                                                transform: bind Translate {x: panel.width-10, y: 192}
                                                halign: HorizontalAlignment.TRAILING

                                                content:
                                                [
                                                    buttonCancel,
                                                    buttonAddLogin
                                                ]
                                            }
                                        ]
                                    }
                                }                    
                            center: center
                        }
                    content: panel
                },
                // focus ring
                Rect
                {
                    var strokeWidth = 1

                    visible: bind ((active==true) and (focusReady==true))
                    x: bind focusRectX
                    y: bind focusRectY
                    width: bind focusRectWidth-strokeWidth
                    height: bind focusRectHeight-strokeWidth
                    strokeWidth: bind strokeWidth
                    stroke: bind ThemeManager.getInstance().fieldFocusColor
                } as Node,
                dialog,
                
                // inactive rect
                Rect
                {
                    visible: bind ((active==false) and (dialog.active == false))
                    x: bind 0
                    y: bind 0
                    width: bind width
                    height: bind height
                    fill: bind ThemeManager.getInstance().windowInactive
                } as Node,
            ]
        }
    };

    override var focusReady = false;
    override var visible = true;
}

