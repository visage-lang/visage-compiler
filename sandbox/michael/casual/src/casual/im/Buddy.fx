package casual.im;

import casual.ChatWindow;

public class BuddyKey
{
    public attribute id: String;

    public static attribute FIRST_NAME:BuddyKey = BuddyKey
    {
        id: "FIRST_NAME"
    };
    public static attribute LAST_NAME:BuddyKey = BuddyKey
    {
        id: "LAST_NAME"
    };
    public static attribute USER_NAME:BuddyKey = BuddyKey
    {
        id: "USER_NAME"
    };
    public static attribute ACCOUNT_NAME:BuddyKey = BuddyKey
    {
        id: "ACCOUNT_NAME"
    };
}

public class BuddyType
{
    public attribute id: String;

    public static attribute USER:BuddyType = BuddyType
    {
        id: "USER"
    };
    public static attribute BUDDY:BuddyType = BuddyType
    {
        id: "BUDDY"
    };
}

public class BuddyPresence
{
    public attribute id: String;

    public static attribute AVAILABLE:BuddyPresence = BuddyPresence
    {
        id: "AVAILABLE"
    };
    public static attribute BUSY:BuddyPresence = BuddyPresence
    {
        id: "BUSY"
    };
    public static attribute AWAY:BuddyPresence = BuddyPresence
    {
        id: "AWAY"
    };
}

public class Buddy
{
    attribute chat: Chat;
    attribute window: ChatWindow;
    
    public attribute type: BuddyType = BuddyType.BUDDY;
    
    public attribute firstName: String;
    public attribute lastName: String;
    public attribute userName: String;
    public attribute account: Account;
    
    public attribute accountName: String
        on replace {
            if (accountName.equalsIgnoreCase(Account.JABBER.server) == true)
            {
                account = Account.JABBER;
            }
            else if (accountName.equalsIgnoreCase(Account.GOOGLE_TALK.server) == true)
            {
                account = Account.GOOGLE_TALK;
            }
            else
            {
                account = Account.UNKNOWN;
                account.server = accountName;
            }
        };

    public attribute presence: BuddyPresence = BuddyPresence.AWAY
        on replace {
            if (presence == BuddyPresence.AVAILABLE)
            {
                window.addComment("&lt;{userName}@{accountName}&gt; reconnected");
            }
            else
            {
                window.addComment("&lt;{userName}@{accountName}&gt; disconnected");
            }
        };

    
    public attribute password: String;
    public attribute chatting: Boolean = false;
    
    public function receiveMessage(message:String)
    {
        if (window == null)
        {
            window = new ChatWindow(this);
            window.width = 300;
            window.height = 100;
            window.receiveMessage(message);
            window.visible = true;
            window.toFront();

            //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                public function run():Void {
                    window.ringPlay();
                }
            });
        }
        else
        {
            window.receiveMessage(message);
        }
    }

    public function sendMessage(message:String)
    {
        this.chat.sendMessage(message);
    }

    public function startChat()
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

    public function endChat()
    {
        chatting = false;
        if (window <> null)
        {
            window.visible = false;
            window.close();
            window = null;
        }
    }

    public function toString(): String
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
}