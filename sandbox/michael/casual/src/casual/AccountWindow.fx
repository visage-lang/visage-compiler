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

import javafx.ui.canvas.View;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Transform;
import javafx.ui.canvas.HBox;
import javafx.ui.canvas.Text;

import java.util.prefs.Preferences;
import java.lang.System;

class FocusedTextField extends TextField
{
    public attribute frame: AccountWindow;

    override attribute focusable = bind frame.focusReady
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
    public attribute frame: AccountWindow;

    attribute focusable = bind frame.focusReady
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

public class AccountWindow extends Frame
{
    public attribute buddy: Buddy = Buddy { type: Buddy.BuddyType.USER }
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
                if (passwordField<>null)
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
    
    attribute preferences: Preferences = Preferences.userRoot().node("Casual");

    attribute addressRow: Row = Row {alignment: Alignment.BASELINE}
    attribute passwordRow: Row = Row {alignment: Alignment.BASELINE}
    attribute labelsColumn: Column = Column {alignment: Alignment.TRAILING}
    attribute fieldsColumn: Column = Column {alignment: Alignment.LEADING, resizable: true}
    
    attribute userField: FocusedTextField = FocusedTextField {
        frame: frame
//        horizontal: {pref: 270}
        row: firstNameRow
        column: fieldsColumn
        foreground: bind theme.fieldForeground
        background: bind theme.fieldBackground
        font: bind theme.windowFont.bold()
        border: bind theme.windowInputAreaBorder
        value: bind buddy.firstName
        onChange: function(newValue) 
        {
            save(Buddy.BuddyKey.FIRST_NAME, newValue);
        }
    };

    attribute passwordField: FocusedPasswordField = FocusedPasswordField {
        frame: this
        visible: bind (buddy.type == Buddy.BuddyType.USER)
        focusTraversalKeysEnabled: bind (buddy.type == Buddy.BuddyType.USER)
//        horizontal: {pref: 175}
        row: passwordRow
        column: fieldsColumn
        foreground: bind ThemeManager.getInstance().fieldForeground
        background: bind theme.fieldBackground
        font: bind theme.windowFont.bold()
        border: bind theme.windowInputAreaBorder
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
    
    attribute dialog: Dialog = Dialog {
        frame: this
        active: false
    };

    
    attribute focusReady: Boolean;
    attribute focusRectX: Integer;
    attribute focusRectY: Integer;
    attribute focusRectWidth: Integer;
    attribute focusRectHeight: Integer;

    override attribute background = bind theme.windowBackground;
    override attribute undecorated = true;
    override attribute width = 440;
    override attribute height = 250;
    override attribute centerOnScreen = true;

    postinit {
        requestFocus();
    };
    
    attribute buttonCancel: Button = 
        Button {
            text: "CANCEL"
            width: 60
            height: 20
            font: bind theme.windowFont.bold()
            onClick: function()
            {
                cancel();
            }
        };
    
    attribute buttonAddLogin: Button = 
        Button {
            text: bind if (buddy.type == Buddy.BuddyType.USER) then "LOGIN" else "ADD"
            width: 50
            height: 20
            font: bind theme.windowFont.bold()
            onClick: function()
            {
                addlogin();
            }
        };

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
                        var panel = this

                        border: bind theme.windowBorder
                        background: bind theme.chatFrameBackground

                        top: Canvas
                        {
                            border: EmptyBorder
                            background: bind theme.chatPanelBackground

                            content: TitleBar
                            {
                                var offsets = bind (theme.windowBorder.left + theme.windowBorder.right)

                                frame: this
                                title: "CASUAL ACCOUNTS"
                                width: bind (panel.width - offsets)
                                foreground: bind theme.titleBarForeground
                                background: bind theme.titleBarBackground

                                onClose: function()
                                {
                                    java.lang.System.exit(0);
                                }
                            }
                        }

                        center: Canvas
                        {
                            border: EmptyBorder
                            background: bind theme.chatPanelBackground

                            content: Group
                            {
                                content:
                                [
                                    View
                                    {
                                        var text = bind if (buddy.type == Buddy.BuddyType.BUDDY) then "BUDDY" else "YOUR"

                                        transform: translate(30, 20)

                                        content: SimpleLabel
                                        {
                                            font: bind theme.windowFont.bold()
                                            foreground: bind theme.messageInputForeground
                                            text: "{text} PERSONAL DETAILS"
                                        }
                                    },
                                    View
                                    {
                                        antialiasText: true
                                        transform: bind translate(panel.width-10, 40)
                                        halign: TRAILING

                                        content: GroupPanel
                                        {
                                            var firstNameRow = Row {alignment: Alignment.BASELINE}
                                            var lastNameRow = Row {alignment: Alignment.BASELINE}
                                            var labelsColumn = Column {alignment: Alignment.TRAILING}
                                            var fieldsColumn = Column {alignment: Alignment.LEADING, resizable: true}

                                            rows: [firstNameRow, lastNameRow]
                                            columns: [labelsColumn, fieldsColumn]
                                            autoCreateContainerGaps: false

                                            content:
                                            [
                                                SimpleLabel 
                                                {
                                                    row: firstNameRow
                                                    column: labelsColumn
                                                    border: bind theme.windowInputAreaBorder
                                                    font: bind theme.windowFont.bigger()
                                                    background: new Color(0,0,0,0)
                                                    foreground: bind theme.messageInputForeground
                                                    text: "first name -"

                                                },
                                                userField,
                                                SimpleLabel
                                                {
                                                    row: lastNameRow, column: labelsColumn
                                                    border: bind theme.windowInputAreaBorder
                                                    font: bind theme.windowFont.bigger()
                                                    background: new Color(0,0,0,0)
                                                    foreground: bind theme.messageInputForeground
                                                    text: "last name -"
                                                },
                                                FocusedTextField
                                                {
                                                    frame: frame
//                                                    horizontal: {pref: 270}
                                                    row: lastNameRow
                                                    column: fieldsColumn
                                                    foreground: bind theme.fieldForeground
                                                    background: bind theme.fieldBackground
                                                    font: bind theme.windowFont.bold()
                                                    border: bind theme.windowInputAreaBorder
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
                                        transform: bind translate(30, 105)
                                        content: SimpleLabel
                                        {
                                            font: bind theme.windowFont.bold()
                                            foreground: bind theme.messageInputForeground
                                            text: "INSTANT MESSENGER DETAILS"
                                        }
                                    },
                                    View
                                    {
                                        antialiasText: true
                                        transform: bind translate(panel.width-10, 125)
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
                                                    border: bind theme.windowInputAreaBorder
                                                    font: bind theme.windowFont.bigger()
                                                    background: new Color(0,0,0,0)
                                                    foreground: bind theme.messageInputForeground
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
                                                            frame: frame 
//                                                            horizontal: {pref: 130}
                                                            row: row
                                                            column: nameColumn
                                                            foreground: bind theme.fieldForeground
                                                            background: bind theme.fieldBackground
                                                            font: bind theme.windowFont.bold()
                                                            border: bind theme.windowInputAreaBorder
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
                                                            border: bind theme.windowInputAreaBorder
                                                            font: bind theme.windowFont.bigger().bold()
                                                            background: new Color(0,0,0,0)
                                                            foreground: bind theme.messageInputForeground
                                                            text: "@"
                                                        },
                                                        FocusedTextField
                                                        {
                                                            frame: frame
//                                                            horizontal: {pref: 118}
                                                            row: row
                                                            column: serverColumn
                                                            foreground: bind theme.fieldForeground
                                                            background: bind theme.fieldBackground
                                                            font: bind theme.windowFont.bold()
                                                            border: bind theme.windowInputAreaBorder
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
                                                    border: bind theme.windowInputAreaBorder
                                                    font: bind theme.windowFont.bigger()
                                                    background: new Color(0,0,0,0)
                                                    foreground: bind theme.messageInputForeground
                                                    text: "account password -"
                                                },
                                                passwordField
                                            ]
                                        }
                                    },
                                    HBox
                                    {
                                        transform: bind translate(panel.width-10, 192)
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
                    }
                },
                // focus ring
                Rect
                {
                    var strokeWidth = 1

                    visible: bind ((frame.active==true) and (frame.focusReady==true))
                    x: bind focusRectX
                    y: bind focusRectY
                    width: bind focusRectWidth-strokeWidth
                    height: bind focusRectHeight-strokeWidth
                    strokeWidth: bind strokeWidth
                    stroke: bind theme.fieldFocusColor
                },
                dialog,
                
                // inactive rect
                Rect
                {
                    visible: bind ((frame.active==false) and (frame.dialog.active == false))
                    x: bind 0
                    y: bind 0
                    width: bind frame.width
                    height: bind frame.height
                    fill: bind theme.windowInactive
                },
            ]
        }
    };

    override attribute focusReady = false;
    override attribute visible = true;
}

