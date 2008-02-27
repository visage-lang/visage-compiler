package casual;

import casual.theme.*;
import casual.ui.*;
import casual.im.*;

import javafx.ui.Canvas;
import casual.ui.ScrollPane;
import javafx.ui.EmptyBorder;
import javafx.ui.BorderPanel;
import javafx.ui.Color;

import javafx.ui.canvas.VBox;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Rect;
import javafx.ui.KeyEvent;
import javafx.ui.KeyStroke;

import java.awt.Dimension;
import java.util.prefs.Preferences;
import java.lang.Math;
import java.lang.System;

class ChatTimer extends Timer
{
    public attribute alarm: Boolean;
    public attribute period: Integer;
    public attribute frame: ChatWindow;
}

trigger on ChatTimer.minutes = value
{
    if (minutes%period == 0)
    {
        if (alarm == true)
        {
            alarm = false;
            do later
            {
                frame.addTimeMessage();
            }
        }
    }
}

class ChatTitleBar extends TitleBar
{
}

class ChatLines extends VBox
{
    public attribute typeOfLastMessage: MessageType;
    public attribute frame: ChatWindow;
    
    attribute oldHeight: Number;
}
trigger on ChatLines.currentHeight = value
{
    if (typeOfLastMessage == OUTGOING:MessageType)
    {
        do later
        {
            frame.scrollFactor = 1;
        }
    }
    else if (frame.scrollFactor <> 1)
    {
        var scrollFactor = (oldHeight*frame.scrollFactor)/currentHeight;
        do later
        {
            frame.scrollFactor = scrollFactor;
        }
    }
    oldHeight = currentHeight;
}

class ChatScrollPane extends ScrollPane
{
}

class ChatScrollbar extends ScrollBar
{
}

class ChatInput extends TextInput
{
    attribute frame:ChatWindow;
}

trigger on ChatInput.text = value
{
    if ((text.length() > 0) and (frame.userTyping == false))
    {
        frame.userTyping = true;
        
        // let the buddy know we're typying
        frame.sendMessage(null);
    }
    else if ((text.length() == 0) and (frame.userTyping == true))
    {
        frame.userTyping = false;
        
        // let the buddy know we're done typying
        frame.sendMessage(null);
    }
}

public class ChatDialog extends Dialog
{
}

trigger on ChatDialog.active = value
{
    if (value == false)
    {
        frame.visible = false;
        
        var oldWidth = frame.width;
        var oldHeight = frame.height;
        var newWidth = 350;
        var newHeight = 500;
        
        // bug in Apple's JDK16 (#5214550)
        frame.getWindow().setMinimumSize(new Dimension(200, 200));
        
        frame.screenx -= (newWidth-oldWidth)/2;
        frame.screeny -= (newHeight-oldHeight)/2;
        frame.width = newWidth;
        frame.height = newHeight;
        
        frame.visible = true;
    }
}

public class ChatWindow extends Frame
{
    public operation ChatWindow(buddy:Buddy);
    
    attribute buddy: Buddy;
    
    attribute messages: Message*;
    attribute feedbackMsg: Message?;
    attribute buddyTyping: Boolean;
    attribute userTyping: Boolean;
    
    attribute timer: ChatTimer;
    attribute chatInput: ChatInput; // default focus target
    attribute scrollPane: Canvas;
    attribute scrollBar: ChatScrollbar;
    attribute chatLines: ChatLines;
    attribute scrollFactor: Number;
    
    attribute showContents: Boolean;
    
    public operation receiveMessage(message:String);
    public operation sendMessage(message:String);
    public operation requestFocus();
    public operation ringOnce();
    public operation ringPlay();
    public operation ringStop();
    public operation resizeToMinimal();
    
    operation addMessage(messageStr:String, type:MessageType);
    operation addTimeMessage();
    public operation addComment(messageStr:String);
}

attribute ChatWindow.showContents = bind (((doLiveResize==false) and (inLiveResize==true)) == false);
attribute ChatWindow.buddyTyping = false;
attribute ChatWindow.userTyping = false;
attribute ChatWindow.background = bind theme:ThemeManager.windowBackground;// gznote: should take AbstractColor

operation ChatWindow.ChatWindow(buddy:Buddy)
{
    var frame = this;
    
    // bug in Apple's JDK16 (#5214550)
    frame.getWindow().setMinimumSize(new Dimension(100, 100));
    
    frame.undecorated = true;
    frame.centerOnScreen = true;
    
    frame.scrollFactor = 1;
    
    frame.buddy = buddy;
    frame.buddy.window = frame;
    frame.buddy.chatting = true;
    
    timer = ChatTimer
    {
        running: bind true
        
        frame: frame
        alarm: false
        period: 5 // every 5 minutes
    };
    
    onOpen = operation()
    {
       frame.requestFocus();
    };
    
    chatLines = ChatLines
    {
        frame: frame
        content: bind foreach (message in messages) message
    };
    
    scrollPane = Canvas
    {
        onMouseWheelMoved: operation(e)
        {
	    var t = e.wheelRotation/360;
	    scrollFactor = Math.max(Math.min(1.0, scrollFactor + t), 0.0);
        }
        var: canvas
        border: null
        visible: bind showContents
        background: bind theme:ThemeManager.chatPanelBackground
        content: ChatScrollPane 
        {
            view: chatLines
	    scrollFactor: bind scrollFactor
            height: bind canvas.height
            width: bind canvas.width
        }
    };
    
    var scrollbarCanvas = Canvas
    {
        visible: bind showContents
        border: EmptyBorder
        background: bind theme:ThemeManager.chatPanelBackground
        content: ChatScrollbar
        {
            attribute: scrollBar
            var paneHeight = bind scrollPane.height
            var chatLinesHeight = bind chatLines.currentHeight
            
            orientation: VERTICAL
            size: bind paneHeight
            unitScrollFactor: bind if sizeof messages == 0 then 0 else select avgMessageHeight/paneHeight from avgMessageHeight in chatLinesHeight/sizeof messages
            pageScrollFactor: bind if chatLinesHeight == 0 then 0 else paneHeight/chatLinesHeight
            scrollFactor: bind scrollFactor
            showThumb: bind if (chatLinesHeight>0 and paneHeight>0) then (chatLinesHeight>paneHeight) else false
            showButtons: bind if (chatLinesHeight>0 and paneHeight>0) then (chatLinesHeight>paneHeight) else false
        }
    };
    
    chatInput = ChatInput
    {
        var: self
        
        frame: frame
        size: bind select {width:w} from w in scrollPane.width
        onKeyDown: operation(e:KeyEvent)
        {
            ringStop();
            
            var k:KeyStroke = e.keyStroke;
            var modifiers:KeyStroke* = e.modifiers;
            if (k == ENTER:KeyStroke)
            {
                userTyping = false;
                
                var messageStr = self.text;
                self.text = "";
                if (messageStr.length() > 0)
                {
                    addMessage(messageStr, OUTGOING:MessageType);
                }
                e.source.consume();
            }
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
                            //visible: bind showContents

                            border: EmptyBorder
                            background: bind theme:ThemeManager.chatPanelBackground

                            content: ChatTitleBar
                            {
                                var offsets = bind (theme:ThemeManager.windowBorder.left + theme:ThemeManager.windowBorder.right)
                                var userFullName = bind "{buddy.firstName.toUpperCase()} {buddy.lastName.toUpperCase()}"

                                frame: frame
                                title: bind if ((buddy.firstName.length()>0) or (buddy.lastName.length()>0)) then userFullName else "{buddy.userName}"
                                width: bind (panel.width - offsets)
                                foreground: bind theme:ThemeManager.titleBarForeground
                                background: bind theme:ThemeManager.titleBarBackground

                                onClose: operation()
                                {
                                    ringStop();

                                    buddy.endChat();
                                }
                            }
                        }

                        center: scrollPane

                        right: scrollbarCanvas

                        bottom: BorderPanel
                        {
                            var: panel

                            border: bind theme:ThemeManager.messageInputBorder

                            center: Canvas
                            {
                                visible: bind showContents

                                border: EmptyBorder
                                background: bind theme:ThemeManager.chatPanelBackground

                                content: chatInput
                            }

                            right: Canvas
                            {
                                border: EmptyBorder
                                background: bind theme:ThemeManager.chatPanelBackground

                                content: ResizeIcon
                                {
                                    frame: frame
                                    width: bind scrollbarCanvas.width
                                    height: bind chatInput.height
                                }
                            }
                        }
                    }
                },
                // dialog window
                ChatDialog
                {
                    attribute: dialog
                    frame: bind frame
                    width: 300
                    height: 100
                    active: false
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

trigger on ChatWindow.active = value
{
    if (value == true)
    {
        requestFocus();
    }
}

trigger on ChatWindow.timer = value
{
    addMessage("IM with &lt;{buddy.userName}@{buddy.accountName}&gt;", COMMENT:MessageType);
    addTimeMessage();
}

operation ChatWindow.requestFocus()
{    
    if (dialog.active == false)
    {
        chatInput.requestFocus();
        ringStop();
    }
}

operation ChatWindow.addTimeMessage()
{
    addMessage(" ", COMMENT:MessageType);
    addMessage("{timer.toString()}", COMMENT:MessageType);
    addMessage(" ", COMMENT:MessageType);
    
    timer.alarm = false;
}

operation ChatWindow.addComment(messageStr:String)
{
    addTimeMessage();
    addMessage(messageStr, COMMENT:MessageType);
}

operation ChatWindow.addMessage(messageStr:String, type:MessageType)
{
    //println("ChatWindow.addMessage messageStr=\"{messageStr}\"");
    
    var parser = parser:MessageParser;
    var parsedMessageStr = parser.parse(messageStr, type);
    
    if (parsedMessageStr <> null)
    {
        var message = Message
        {
            width: bind scrollPane.width
            type: type
            message: parsedMessageStr
        };
        chatLines.typeOfLastMessage = type;
        
        if (type == OUTGOING:MessageType)
        {
            sendMessage(messageStr);
            sendMessage(null);
        }
        
        if (type <> COMMENT:MessageType)
        {
            timer.alarm = true;
        }
        
        insert message as last into messages;
        return message;
    }
    else
    {
        return null;
    }
}

operation ChatWindow.receiveMessage(message:String)
{
//println("ChatWindow.receiveMessage:\"{message}\"");
    
    if (message <> null)
    {
        if (visible == false)
        {
            ringOnce();
        }
        
        buddyTyping = true;
        if (feedbackMsg <> null)
        {
            delete messages[m|m == feedbackMsg];
            feedbackMsg = null;
        }

        addMessage(message, INCOMING:MessageType);
    }
    else if (buddyTyping == false)
    {
        buddyTyping = true;
        feedbackMsg = addMessage("...", INCOMING:MessageType);
    }
    else //if (buddyTyping == true)
    {
        buddyTyping = false;
        if (feedbackMsg <> null)
        {
            delete messages[m|m==feedbackMsg];
            feedbackMsg = null;
        }
    }
}

operation ChatWindow.sendMessage(message:String)
{
//println("ChatWindow.sendMessage:\"{message}\"");
   buddy.sendMessage(message);
}

operation ChatWindow.ringOnce()
{
    var parser = parser:MessageParser;
    parser.ringPlay();
}

operation ChatWindow.ringPlay()
{
    showWarningMessage("{buddy.userName}@{buddy.accountName} is calling you.", "INCOMING", true);
    
    var parser = parser:MessageParser;
    parser.ringLoop();
}

operation ChatWindow.ringStop()
{
    var parser = parser:MessageParser;
    parser.ringStop();
}
