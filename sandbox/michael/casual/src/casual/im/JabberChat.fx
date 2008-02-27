package casual.im;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message as SmackMessage;

public class JabberChat extends Chat
{
    attribute chat: <<org.jivesoftware.smack.Chat>>;
    attribute buddy: Buddy;
    
    public operation JabberChat(manager:<<org.jivesoftware.smack.ChatManager>>, buddy:Buddy);
}

operation JabberChat.JabberChat(manager:<<org.jivesoftware.smack.ChatManager>>, buddy:Buddy)
{
    var self = this;
    self.buddy = buddy;
    var chatListener = new <<org.jivesoftware.smack.MessageListener>>()
    {
        operation processMessage(chat, message:SmackMessage)
        {
            var messageStr = message.getBody();
            do later
            {
                self.buddy.receiveMessage(messageStr);
            }
        }
    };
    
    var userName:String = "{buddy.userName}@{buddy.accountName}";
    
    chat = manager.createChat(userName, chatListener);
}

operation JabberChat.sendMessage(message:String)
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
