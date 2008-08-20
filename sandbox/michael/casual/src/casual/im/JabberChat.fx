package casual.im;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class JabberChat extends Chat
{
    var chat: org.jivesoftware.smack.Chat;
    var buddy: Buddy;
    var manager: org.jivesoftware.smack.ChatManager;
    
    postinit
    {
        var chatListener = org.jivesoftware.smack.MessageListener
        {
            function processMessage(chat, message:Message)
            {
                var messageStr = message.getBody();
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                    buddy.receiveMessage(messageStr);
                    }
                });
            };
        };

        var userName:String = "{buddy.userName}@{buddy.accountName}";

        chat = manager.createChat(userName, chatListener);
    }

    public function sendMessage(message:String): Void
    {
        try
        {
            chat.sendMessage(message);
        }
        catch (ex:XMPPException)
        {
            ex.printStackTrace();
        }
    }
}
