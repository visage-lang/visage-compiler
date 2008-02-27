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

import javafx.ui.canvas.View;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Transform;
import javafx.ui.canvas.HBox;
import javafx.ui.canvas.Text;

import java.util.prefs.Preferences;

class FocusedTextField extends TextField
{
    public attribute frame: AccountWindow;
}
attribute FocusedTextField.focusable = bind frame.focusReady;

trigger on FocusedTextField.focused = value
{
    if (value == true)
    {
        var bounds = getBoundsRelativeTo(null);
        frame.focusRectX = bounds.x;
        frame.focusRectY = bounds.y;
        frame.focusRectWidth = bounds.width;
        frame.focusRectHeight = bounds.height;
    }
}

class FocusedPasswordField extends PasswordField
{
    public attribute frame: AccountWindow;
}
attribute FocusedPasswordField.focusable = bind frame.focusReady;

trigger on FocusedPasswordField.focused = value
{
    if (value == true)
    {
        var bounds = getBoundsRelativeTo(null);
        frame.focusRectX = bounds.x;
        frame.focusRectY = bounds.y;
        frame.focusRectWidth = bounds.width;
        frame.focusRectHeight = bounds.height;
    }
}

public class AccountWindow extends Frame
{
    public attribute buddy: Buddy;
    
    public operation save(key:BuddyKey, value:String);
    public operation addlogin();
    public operation cancel();
    public operation requestFocus();
    
    attribute preferences: Preferences?;

    attribute userField: FocusedTextField;
    attribute passwordField: FocusedPasswordField;
    
    attribute focusReady: Boolean;
    attribute focusRectX: Integer;
    attribute focusRectY: Integer;
    attribute focusRectWidth: Integer;
    attribute focusRectHeight: Integer;
}

attribute AccountWindow.background = bind theme:ThemeManager.windowBackground;

operation AccountWindow.AccountWindow()
{
    var frame = this;
    var strokeWidth = 1;
    
    preferences = Preferences.userRoot().node("Casual");
    
    buddy = Buddy
    {
        type: USER:BuddyType
    };
    
    undecorated = true;
    width = 440;
    height = 250;
    centerOnScreen = true;
    onOpen = operation()
    {
        do later
        {
            requestFocus();
        }
    };
    
    var buttonCancel = Button
    {
        text: "CANCEL"
        width: 60
        height: 20
        font: bind theme:ThemeManager.windowFont.bold()
        onClick: operation()
        {
            cancel();
        }
    };
    
    var buttonAddLogin = Button
    {
        text: bind if (buddy.type == USER:BuddyType) then "LOGIN" else "ADD"
        width: 50
        height: 20
        font: bind theme:ThemeManager.windowFont.bold()
        onClick: operation()
        {
            addlogin();
        }
    };
    
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
                                title: "CASUAL ACCOUNTS"
                                width: bind (panel.width - offsets)
                                foreground: bind theme:ThemeManager.titleBarForeground
                                background: bind theme:ThemeManager.titleBarBackground

                                onClose: operation()
                                {
                                    <<java.lang.System>>.exit(0);
                                }
                            }
                        }
                        
                        center: Canvas
                        {
                            border: EmptyBorder
                            background: bind theme:ThemeManager.chatPanelBackground
                            
                            content: Group
                            {
                                content:
                                [
                                    View
                                    {
                                        var text = bind if (buddy.type == BUDDY:BuddyType) then "BUDDY" else "YOUR"
                                        
                                        transform: translate(30, 20)

                                        content: SimpleLabel
                                        {
                                            font: bind theme:ThemeManager.windowFont.bold()
                                            foreground: bind theme:ThemeManager.messageInputForeground
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
                                            var firstNameRow = Row {alignment: BASELINE}
                                            var lastNameRow = Row {alignment: BASELINE}
                                            var labelsColumn = Column {alignment: TRAILING}
                                            var fieldsColumn = Column {alignment: LEADING, resizable: true}
                                            
                                            rows: [firstNameRow, lastNameRow]
                                            columns: [labelsColumn, fieldsColumn]
                                            autoCreateContainerGaps: false

                                            content:
                                            [
                                                SimpleLabel 
                                                {
                                                    row: firstNameRow
                                                    column: labelsColumn
                                                    border: bind theme:ThemeManager.windowInputAreaBorder
                                                    font: bind theme:ThemeManager.windowFont.bigger()
                                                    background: new Color(0,0,0,0)
                                                    foreground: bind theme:ThemeManager.messageInputForeground
                                                    text: "first name -"

                                                },
                                                FocusedTextField
                                                {
                                                    attribute: userField
                                                    frame: frame
                                                    horizontal: {pref: 270}
                                                    row: firstNameRow
                                                    column: fieldsColumn
                                                    foreground: bind theme:ThemeManager.fieldForeground
                                                    background: bind theme:ThemeManager.fieldBackground
                                                    font: bind theme:ThemeManager.windowFont.bold()
                                                    border: bind theme:ThemeManager.windowInputAreaBorder
                                                    value: bind buddy.firstName
                                                    onChange: operation(newValue) 
                                                    {
                                                        save(FIRST_NAME:BuddyKey, newValue);
                                                    }
                                                },
                                                SimpleLabel
                                                {
                                                    row: lastNameRow, column: labelsColumn
                                                    border: bind theme:ThemeManager.windowInputAreaBorder
                                                    font: bind theme:ThemeManager.windowFont.bigger()
                                                    background: new Color(0,0,0,0)
                                                    foreground: bind theme:ThemeManager.messageInputForeground
                                                    text: "last name -"
                                                },
                                                FocusedTextField
                                                {
                                                    frame: frame
                                                    horizontal: {pref: 270}
                                                    row: lastNameRow
                                                    column: fieldsColumn
                                                    foreground: bind theme:ThemeManager.fieldForeground
                                                    background: bind theme:ThemeManager.fieldBackground
                                                    font: bind theme:ThemeManager.windowFont.bold()
                                                    border: bind theme:ThemeManager.windowInputAreaBorder
                                                    value: bind buddy.lastName
                                                    onChange: operation(newValue) 
                                                    {
                                                        save(LAST_NAME:BuddyKey, newValue);
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
                                            font: bind theme:ThemeManager.windowFont.bold()
                                            foreground: bind theme:ThemeManager.messageInputForeground
                                            text: "INSTANT MESSENGER DETAILS"
                                        }
                                    },
                                    View
                                    {
                                        antialiasText: true
                                        transform: bind translate(panel.width-10, 125)
                                        halign: TRAILING

                                        content: GroupPanel
                                        {
                                            var addressRow = Row {alignment: BASELINE}
                                            var passwordRow = Row {alignment: BASELINE}
                                            var labelsColumn = Column {alignment: TRAILING}
                                            var fieldsColumn = Column {alignment: LEADING, resizable: true}
                                            
                                            rows: [addressRow, passwordRow]
                                            columns: [labelsColumn, fieldsColumn]
                                            autoCreateContainerGaps: false
                                            content:
                                            [
                                                SimpleLabel
                                                {
                                                    row: addressRow, column: labelsColumn
                                                    border: bind theme:ThemeManager.windowInputAreaBorder
                                                    font: bind theme:ThemeManager.windowFont.bigger()
                                                    background: new Color(0,0,0,0)
                                                    foreground: bind theme:ThemeManager.messageInputForeground
                                                    text: "account address -"

                                                },
                                                GroupPanel
                                                {
                                                    var row = Row {alignment: BASELINE}
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
                                                            horizontal: {pref: 130}
                                                            row: row
                                                            column: nameColumn
                                                            foreground: bind theme:ThemeManager.fieldForeground
                                                            background: bind theme:ThemeManager.fieldBackground
                                                            font: bind theme:ThemeManager.windowFont.bold()
                                                            border: bind theme:ThemeManager.windowInputAreaBorder
                                                            value: bind buddy.userName
                                                            onChange: operation(newValue) 
                                                            {
                                                                 save(USER_NAME:BuddyKey, newValue);
                                                            }
                                                        },
                                                        SimpleLabel
                                                        {
                                                            horizontal: {pref: 10}
                                                            row: row
                                                            column: atColumn
                                                            border: bind theme:ThemeManager.windowInputAreaBorder
                                                            font: bind theme:ThemeManager.windowFont.bigger().bold()
                                                            background: new Color(0,0,0,0)
                                                            foreground: bind theme:ThemeManager.messageInputForeground
                                                            text: "@"
                                                        },
                                                        FocusedTextField
                                                        {
                                                            frame: frame
                                                            horizontal: {pref: 118}
                                                            row: row
                                                            column: serverColumn
                                                            foreground: bind theme:ThemeManager.fieldForeground
                                                            background: bind theme:ThemeManager.fieldBackground
                                                            font: bind theme:ThemeManager.windowFont.bold()
                                                            border: bind theme:ThemeManager.windowInputAreaBorder
                                                            value: bind buddy.accountName
                                                            onChange: operation(newValue) 
                                                            {
                                                                 save(ACCOUNT_NAME:BuddyKey, newValue);
                                                            }
                                                        }
                                                    ]
                                                },
                                                SimpleLabel
                                                {
                                                    visible: bind (buddy.type == USER:BuddyType)
                                                    row: passwordRow
                                                    column: labelsColumn
                                                    border: bind theme:ThemeManager.windowInputAreaBorder
                                                    font: bind theme:ThemeManager.windowFont.bigger()
                                                    background: new Color(0,0,0,0)
                                                    foreground: bind theme:ThemeManager.messageInputForeground
                                                    text: "account password -"
                                                },
                                                FocusedPasswordField
                                                {
                                                    attribute: passwordField
                                                    frame: frame
                                                    visible: bind (buddy.type == USER:BuddyType)
                                                    focusTraversalKeysEnabled: bind (buddy.type == USER:BuddyType)
                                                    horizontal: {pref: 175}
                                                    row: passwordRow
                                                    column: fieldsColumn
                                                    foreground: bind theme:ThemeManager.fieldForeground
                                                    background: bind theme:ThemeManager.fieldBackground
                                                    font: bind theme:ThemeManager.windowFont.bold()
                                                    border: bind theme:ThemeManager.windowInputAreaBorder
                                                    value: bind buddy.password
                                                    onKeyDown: operation(e:KeyEvent)
                                                    {
                                                        if (e.keyStroke == ENTER:KeyStroke)
                                                        {
                                                            e.source.consume();
                                                            
                                                            buttonAddLogin.doClick();
                                                        }
                                                    }
                                                }
                                            ]
                                        }
                                    },
                                    HBox
                                    {
                                        transform: bind translate(panel.width-10, 192)
                                        halign: TRAILING

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
                    stroke: bind theme:ThemeManager.fieldFocusColor
                },
                // dialog
                Dialog
                {
                    attribute: dialog
                    frame: bind frame
                    active: false
                },
                // inactive rect
                Rect
                {
                    visible: bind ((frame.active==false) and (frame.dialog.active == false))
                    x: bind 0
                    y: bind 0
                    width: bind frame.width
                    height: bind frame.height
                    fill: bind theme:ThemeManager.windowInactive
                },
            ]
        }
    };
    
    focusReady = false;
    visible = true;
}

trigger on AccountWindow.buddy = value
{
    if (buddy.type == USER:BuddyType)
    {        
        buddy.firstName = preferences.get(FIRST_NAME:BuddyKey.id, null);
        buddy.lastName = preferences.get(LAST_NAME:BuddyKey.id, null);
        buddy.userName = preferences.get(USER_NAME:BuddyKey.id, null);
        buddy.accountName = preferences.get(ACCOUNT_NAME:BuddyKey.id, "JABBER.org");
    }
}

operation AccountWindow.save(key:BuddyKey, value:String)
{
    if (buddy.type == USER:BuddyType)
    {
        //println("AccountWindow.save [{key}, {value}]");
        preferences.put(key.id, value); 
    }
}

operation AccountWindow.addlogin()
{
    if (buddy.type == USER:BuddyType)
    {
        if ((buddy.userName.length()>0) and (buddy.accountName.length()>0) and (buddy.password.length()>0))
        {
            showInfoMessage("contacting server; please wait.", "CONNECTING", false);
            
            do later
            {
                var im = new InstantMessenger().createIM(buddy);
                im.login(this);
            }
        }
        else
        {
            <<java.awt.Toolkit>>.getDefaultToolkit().beep();
        }
    }
    else
    {
        if ((buddy.userName.length()>0) and (buddy.accountName.length()>0))
        {
            println("add {buddy.toString()}");
        }
        else
        {
            <<java.awt.Toolkit>>.getDefaultToolkit().beep();
        }
    }
}

operation AccountWindow.cancel()
{
    if (buddy.type == USER:BuddyType)
    {
        <<java.lang.System>>.exit(0);
    }
    else
    {
        println("cancel");
    }
}

operation AccountWindow.requestFocus()
{
    if (dialog.active == false)
    {
        focusReady = true;

        if ((buddy.userName.length() > 0) and (buddy.accountName.length() > 0) and (buddy.type == USER:BuddyType))
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
}


