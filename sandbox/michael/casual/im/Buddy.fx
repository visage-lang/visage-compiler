package casual.im;

import casual.ChatWindow;

public class BuddyKey
{
    public attribute id: String;
}

FIRST_NAME:BuddyKey = BuddyKey
{
    id: "FIRST_NAME"
};
LAST_NAME:BuddyKey = BuddyKey
{
    id: "LAST_NAME"
};
USER_NAME:BuddyKey = BuddyKey
{
    id: "USER_NAME"
};
ACCOUNT_NAME:BuddyKey = BuddyKey
{
    id: "ACCOUNT_NAME"
};

public class BuddyType
{
    public attribute id: String;
}

USER:BuddyType = BuddyType
{
    id: "USER"
};
BUDDY:BuddyType = BuddyType
{
    id: "BUDDY"
};

public class BuddyPresence
{
    public attribute id: String;
}

AVAILABLE:BuddyPresence = BuddyPresence
{
    id: "AVAILABLE"
};
BUSY:BuddyPresence = BuddyPresence
{
    id: "BUSY"
};
AWAY:BuddyPresence = BuddyPresence
{
    id: "AWAY"
};

class Buddy
{
    attribute chat: Chat;
    attribute window: ChatWindow;
    
    public attribute type: BuddyType;
    
    public attribute firstName: String;
    public attribute lastName: String;
    public attribute userName: String;
    public attribute accountName: String;
    public attribute account: Account;
    public attribute presence: BuddyPresence;
    
    public attribute password: String?;
    public attribute chatting: Boolean;
    
    public operation sendMessage(message:String);
    public operation receiveMessage(message:String);
    
    public operation startChat();
    public operation endChat();
    public operation toString(): String;
}

Buddy.type = BUDDY;
Buddy.presence = AWAY;
Buddy.chatting = false;

operation Buddy.receiveMessage(message:String)
{
    if (window == null)
    {
        window = new ChatWindow(this);
        window.width = 300;
        window.height = 100;
        window.receiveMessage(message);
        window.visible = true;
        window.toFront();
        
        do later
        {
            window.ringPlay();
        }
    }
    else
    {
        window.receiveMessage(message);
    }
}

operation Buddy.sendMessage(message:String)
{
    this.chat.sendMessage(message);
}

operation Buddy.startChat()
{
    chatting = true;
    if (window == null)
    {
        window = new ChatWindow(this);
        window.width = 350;
        window.height = 500;
        window.visible = true;
        window.toFront();
    }
    else
    {
        if (window.visible == false)
        {
            window.visible = true;
        }
        window.toFront();
    }
}

operation Buddy.endChat()
{
    chatting = false;
    if (window <> null)
    {
        window.visible = false;
        window.close();
        window = null;
    }
}

trigger on Buddy.presence = value
{
    if (presence == AVAILABLE:BuddyPresence)
    {
        window.addComment("&lt;{userName}@{accountName}&gt; reconnected");
    }
    else
    {
        window.addComment("&lt;{userName}@{accountName}&gt; disconnected");
    }
}

trigger on Buddy.accountName = value
{
    if (accountName.equalsIgnoreCase(JABBER:Account.server) == true)
    {
        account = JABBER:Account;
    }
    else if (accountName.equalsIgnoreCase(GOOGLE_TALK:Account.server) == true)
    {
        account = GOOGLE_TALK:Account;
    }
    else if (accountName.equalsIgnoreCase(MOON:Account.server) == true)
    {
        account = MOON:Account;
    }
    else
    {
        account = UNKNOWN:Account;
        account.server = accountName;
    }
}

function Buddy.toString(): String
{
    return
"
Buddy:
    firstName = {firstName}
    lastName = {lastName}
    userName = {userName}
    accountName = {accountName}
    account = {account.name}
    presence = {presence.id}
";
}
