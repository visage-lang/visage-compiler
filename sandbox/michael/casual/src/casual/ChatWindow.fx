package casual;

import casual.theme.*;
import casual.ui.*;
import casual.im.*;

import javafx.ui.Canvas;
import javafx.ui.EmptyBorder;
import javafx.ui.BorderPanel;
import javafx.ui.Color;
import javafx.ui.Orientation;

import javafx.ui.canvas.VBox;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Node;
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
    override attribute minutes
        on replace {
            if (minutes%period == 0)
            {
                if (alarm == true)
                {
                    alarm = false;
                    //TODO DO LATER - this is a work around until a more permanent solution is provided
                    javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                        public function run():Void {
                            frame.addTimeMessage();
                        }
                    });
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
    
    override attribute currentHeight 
        on replace {
            if (typeOfLastMessage == MessageType.OUTGOING)
            {
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        frame.scrollFactor = 1;
                    }
                });
            }
            else if (frame.scrollFactor != 1)
            {
                var scrollFactor = (oldHeight*frame.scrollFactor)/currentHeight;
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        frame.scrollFactor = scrollFactor;
                    }
                });
            };
            oldHeight = currentHeight;
        };
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
    
    override attribute text
        on replace {
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
        };

}

public class ChatDialog extends Dialog
{
    override attribute active
        on replace {
            if (active == false)
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
        };
}

public class ChatWindow extends CasualFrame
{
    public attribute buddy: Buddy
        on replace {
            buddy.window = this;
            buddy.chatting = true;
        };
    
    attribute messages: Message[];
    attribute feedbackMsg: Message;
    attribute buddyTyping: Boolean = false;
    attribute userTyping: Boolean = false;
    
    attribute timer: ChatTimer = 
        ChatTimer {
            running: true

            frame: this
            alarm: false
            period: 5 // every 5 minutes
        }
        on replace {
            addMessage("IM with &lt;{buddy.userName}@{buddy.accountName}&gt;", MessageType.COMMENT);
            addTimeMessage();
        };

    attribute chatInput: ChatInput = ChatInput {
        frame: this
        size: Dimension { width: bind scrollPane.width }
        onKeyDown: function(e:KeyEvent)
        {
            ringStop();
            
            var k:KeyStroke = e.keyStroke;
            var modifiers = e.modifiers;
            if (k == KeyStroke.ENTER)
            {
                userTyping = false;
                
                var messageStr = chatInput.text;
                chatInput.text = "";
                if (messageStr.length() > 0)
                {
                    addMessage(messageStr, MessageType.OUTGOING);
                }
                e.source.consume();
            }
        }
    };

    attribute scrollPane: Canvas = Canvas {
        onMouseWheelMoved: function(e)
        {
	    var t = e.wheelRotation/360;
	    scrollFactor = Math.max(Math.min(1.0, scrollFactor + t), 0.0);
        }
        border: new EmptyBorder
        visible: bind showContents
        background: bind ThemeManager.getInstance().chatPanelBackground
        content: ChatScrollPane 
        {
            view: chatLines
	    scrollFactor: bind scrollFactor
            height: bind scrollPane.height
            width: bind scrollPane.width
        }
    };
    
    attribute scrollBar: ChatScrollbar = ChatScrollbar {
        var paneHeight = bind scrollPane.height
        var chatLinesHeight = bind chatLines.currentHeight

        orientation: Orientation.VERTICAL
        size: bind paneHeight
        unitScrollFactor: bind if (sizeof messages == 0) then 0 else chatLinesHeight / (sizeof messages) / paneHeight
        pageScrollFactor: bind if (chatLinesHeight == 0) then 0 else paneHeight/chatLinesHeight
        scrollFactor: bind scrollFactor
        showThumb: bind if (chatLinesHeight>0 and paneHeight>0) then (chatLinesHeight>paneHeight) else false
        showButtons: bind if (chatLinesHeight>0 and paneHeight>0) then (chatLinesHeight>paneHeight) else false
    };
    
    attribute chatLines: ChatLines = ChatLines {
        frame: this
        content: bind for (message in messages) message
    };
    
    attribute scrollFactor: Number = 1;
    
    attribute showContents: Boolean = bind (((doLiveResize==false) and (inLiveResize==true)) == false);
    
    public function receiveMessage(message:String){
    //println("ChatWindow.receiveMessage:\"{message}\"");

        if (message != null)
        {
            if (visible == false)
            {
                ringOnce();
            }

            buddyTyping = true;
            if (feedbackMsg != null)
            {
                delete feedbackMsg from messages;
                feedbackMsg = null;
            }

            addMessage(message, MessageType.INCOMING);
        }
        else if (buddyTyping == false)
        {
            buddyTyping = true;
            addMessage("...", MessageType.INCOMING);
        }
        else //if (buddyTyping == true)
        {
            buddyTyping = false;
            if (feedbackMsg != null)
            {
                delete feedbackMsg from messages;
                feedbackMsg = null;
            }
        }
    };

    public function sendMessage(message:String){
        //println("ChatWindow.sendMessage:\"{message}\"");
        buddy.sendMessage(message);
    };
    
    public function requestFocus(): Void {    
        if (dialog.active == false)
        {
            chatInput.requestFocus();
            ringStop();
        }
    };
    
    public function ringOnce()
    {
        var parser = MessageParser.getInstance();
        parser.ringPlay();
    }

    public function ringPlay()
    {
        showWarningMessage("{buddy.userName}@{buddy.accountName} is calling you.", "INCOMING", true);

        var parser = MessageParser.getInstance();
        parser.ringLoop();
    }

    public function ringStop()
    {
        var parser = MessageParser.getInstance();
        parser.ringStop();
    }

    function addMessage(messageStr:String, type:MessageType){
        //println("ChatWindow.addMessage messageStr=\"{messageStr}\"");

        var parser = new MessageParser;
        var parsedMessageStr = parser.parse(messageStr, type);

        if (parsedMessageStr != null)
        {
            var message = Message
            {
                width: bind scrollPane.width
                type: type
                message: parsedMessageStr
            };
            chatLines.typeOfLastMessage = type;

            if (type == MessageType.OUTGOING)
            {
                sendMessage(messageStr);
                sendMessage(null);
            }

            if (type != MessageType.COMMENT)
            {
                timer.alarm = true;
            }

            insert message into messages;
        }
    };
    
    function addTimeMessage() {
        addMessage(" ", MessageType.COMMENT);
        addMessage("{timer.toString()}", MessageType.COMMENT);
        addMessage(" ", MessageType.COMMENT);

        timer.alarm = false;
    };
    
    public function addComment(messageStr:String) {
        addTimeMessage();
        addMessage(messageStr, MessageType.COMMENT);
    };

    override attribute background = bind ThemeManager.getInstance().windowBackground;// gznote: should take AbstractColor

    postinit {
        requestFocus();
    };
    
    override attribute undecorated = true;
    override attribute centerOnScreen = true;
    
    private attribute scrollbarCanvas: Canvas = Canvas
    {
        visible: bind showContents
        border: new EmptyBorder
        background: bind ThemeManager.getInstance().chatPanelBackground
        content: scrollBar
    };
    
    override attribute dialog = ChatDialog {
        frame: this
        width: 300
        height: 100
        active: false
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
                    var panel: BorderPanel = BorderPanel
                    {
                        border: bind ThemeManager.getInstance().windowBorder
                        background: bind ThemeManager.getInstance().chatFrameBackground

                        top: Canvas
                        {
                            //visible: bind showContents

                            border: new EmptyBorder
                            background: bind ThemeManager.getInstance().chatPanelBackground

                            content: ChatTitleBar
                            {
                                var offsets = bind (ThemeManager.getInstance().windowBorder.left + ThemeManager.getInstance().windowBorder.right)
                                var userFullName = bind "{buddy.firstName.toUpperCase()} {buddy.lastName.toUpperCase()}"

                                frame: this
                                title: bind if ((buddy.firstName.length()>0) or (buddy.lastName.length()>0)) then userFullName else "{buddy.userName}"
                                width: bind (panel.width.intValue() - offsets)
                                foreground: bind ThemeManager.getInstance().titleBarForeground
                                background: bind ThemeManager.getInstance().titleBarBackground

                                onClose: function()
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
                            border: bind ThemeManager.getInstance().messageInputBorder

                            center: Canvas
                            {
                                visible: bind showContents

                                border: new EmptyBorder
                                background: bind ThemeManager.getInstance().chatPanelBackground

                                content: chatInput
                            }

                            right: Canvas
                            {
                                border: new EmptyBorder
                                background: bind ThemeManager.getInstance().chatPanelBackground

                                content: ResizeIcon
                                {
                                    frame: this
                                    width: bind scrollbarCanvas.width.intValue()
                                    height: bind chatInput.height
                                }
                            }
                        }
                    }
                    content: panel
                },
                dialog,
                // inactive rect
                Rect
                {
                    visible: bind (active==false)
                    x: 0
                    y: 0
                    width: bind width
                    height: bind height
                    fill: bind ThemeManager.getInstance().windowInactive
                } as Node,
            ]
        }
    };

    override attribute active
        on replace {
            if (active == true)
            {
                requestFocus();
            }
        };
}
