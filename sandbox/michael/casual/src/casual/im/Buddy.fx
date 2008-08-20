package casual.im;

import casual.ChatWindow;

public class BuddyKey
{
    public var id: String;

    public static var FIRST_NAME:BuddyKey = BuddyKey
    {
        id: "FIRST_NAME"
    };
    public static var LAST_NAME:BuddyKey = BuddyKey
    {
        id: "LAST_NAME"
    };
    public static var USER_NAME:BuddyKey = BuddyKey
    {
        id: "USER_NAME"
    };
    public static var ACCOUNT_NAME:BuddyKey = BuddyKey
    {
        id: "ACCOUNT_NAME"
    };
}

public class BuddyType
{
    public var id: String;

    public static var USER:BuddyType = BuddyType
    {
        id: "USER"
    };
    public static var BUDDY:BuddyType = BuddyType
    {
        id: "BUDDY"
    };
}

public class BuddyPresence
{
    public var id: String;

    public static var AVAILABLE:BuddyPresence = BuddyPresence
    {
        id: "AVAILABLE"
    };
    public static var BUSY:BuddyPresence = BuddyPresence
    {
        id: "BUSY"
    };
    public static var AWAY:BuddyPresence = BuddyPresence
    {
        id: "AWAY"
    };
}

public class Buddy
{
    var chat: Chat;
    public var window: ChatWindow;
    
    public var type: BuddyType = BuddyType.BUDDY;
    
    public var firstName: String;
    public var lastName: String;
    public var userName: String;
    public var account: Account;
    
    public var accountName: String
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

    public var presence: BuddyPresence = BuddyPresence.AWAY
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

    
    public var password: String;
    public var chatting: Boolean = false;
    
    public function receiveMessage(message:String)
    {
        if (window == null)
        {
            window = ChatWindow {buddy: this};
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
        chat.sendMessage(message);
    }

    public function startChat()
    {
        chatting = true;
        if (window == null)
        {
            window = ChatWindow {buddy: this};
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
        if (window != null)
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
