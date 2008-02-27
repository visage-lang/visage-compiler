package casual.im;

import casual.AccountWindow;
import casual.BuddyWindow;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.packet.Presence$Type as PresenceType;
import org.jivesoftware.smack.packet.Presence$Mode as PresenceMode;

import java.util.Collection;

public class JabberIM extends InstantMessenger
{
    public operation JabberIM(user:Buddy);
    
    private attribute connection: XMPPConnection?;
    private attribute roster: Roster?;
    private attribute chatManager: ChatManager?;
    private attribute rosterListener: RosterListener?;
}

operation JabberIM.JabberIM(user:Buddy)
{
    this.user = user;
}

operation JabberIM.login(window:AccountWindow)
{
    var self = this;
    
    var account = user.account;
    var configuration;
    if (account == GOOGLE_TALK:Account)
    {
	configuration = new ConnectionConfiguration(account.server, account.port, account.emailServer);
    }
    else
    {
	configuration = new ConnectionConfiguration(account.server, account.port);
    }
    
    connection = new XMPPConnection(configuration);
    try
    {
        do
        {
            connection.connect();
        }
    }
    catch (any)
    {
        <<java.awt.Toolkit>>.getDefaultToolkit().beep();
        
        do later
        {
            var text = "\"{account.server}:{account.port}\" UNREACHABLE";
            window.showErrorMessage(text, "ERROR:", true);
        }
        
        return;
    }
    
    try
    {
        do
        {
            connection.login(this.user.userName, this.user.password, "Casual", true);
        }
    }
    catch (any)
    {
        if ((connection <> null) and (connection.isConnected() == true))
        {
            connection.disconnect();
        }
        
        <<java.awt.Toolkit>>.getDefaultToolkit().beep();
        
        do later
        {
            var text = "unable to connect: {user.userName}@{user.accountName}";
            window.showErrorMessage(text, "ERROR:", true);
        }
        
        return;
    }
    
    chatManager = connection.getChatManager();
    
    var roster = connection.getRoster();
    rosterListener = new RosterListener()
    {
        operation entriesAdded(addresses:Collection)
        {
            do later
            {
                println("entriesAdded");
            }
        }
        operation entriesDeleted(addresses:Collection)
        {
            do later
            {
                println("entriesDeleted");
            }
        }
        operation entriesUpdated(addresses:Collection)
        {
            do later
            {
                println("entriesUpdated");
            }
        }
        operation presenceChanged(presence:Presence)
        {
            do later
            {
                var userAddress = presence.getFrom();
                var userName = userAddress.substring(0, userAddress.indexOf('@'));
                for (buddy in self.buddies)
                {
                    if (buddy.userName.equalsIgnoreCase(userName) == true)
                    {
                        var mode = presence.getMode();
                        if (mode == null)
                        {
                            if (presence.isAvailable())
                            {
                                buddy.presence = AVAILABLE:BuddyPresence;
                            }
                            else
                            {
                                buddy.presence = AWAY:BuddyPresence;
                            }
                        }
                        else if (presence.getMode() == PresenceMode.available)
                        {
                            buddy.presence = AVAILABLE:BuddyPresence;
                        }
                        else
                        {
                            buddy.presence = AWAY:BuddyPresence;
                        }
                        break;
                    }
                }
            }
        }
    };
    roster.addRosterListener(rosterListener);
    
    var groups = roster.getGroups();
    var i = groups.iterator();
    while (i.hasNext())
    {
	var rg:RosterGroup = (RosterGroup)i.next();
        
	var ge = rg.getEntries();
	var j = ge.iterator();
	while (j.hasNext())
        {
	    var re = (RosterEntry)j.next();
            
            var addressXMPP = re.getUser();
	    var userName = addressXMPP.substring(0, addressXMPP.indexOf('@'));
	    var accountName = addressXMPP.substring(addressXMPP.indexOf('@')+1, addressXMPP.length());
	    var buddy = Buddy
            {
		type: BUDDY
                userName: userName
                accountName: accountName
	    };
            
	    var bestPresence = roster.getPresence(addressXMPP);
	    var mode = bestPresence.getMode();
	    if (mode == null)
            {
		if (bestPresence.isAvailable())
                {
		    buddy.presence = AVAILABLE:BuddyPresence;
		}
                else
                {
		    buddy.presence = AWAY:BuddyPresence;
		}
	    }
            else if (bestPresence.getMode() == PresenceMode.available)
            {
                buddy.presence = AVAILABLE:BuddyPresence;
            }
            else
            {
                buddy.presence = AWAY:BuddyPresence;
            }
            
            buddy.chat = new JabberChat(chatManager, buddy);
            
            insert buddy into buddies;
	}
    }
    
    var presence = new Presence(PresenceType.available, "available", 1, PresenceMode.available);
    this.connection.sendPacket(presence);
    
    do later
    {
        window.hideMessage();
        window.close();
        var buddyWindow = new BuddyWindow(this);
    }
}

operation JabberIM.isConnected()
{
    return ((connection <> null) and (connection.isConnected() == true));
}

operation JabberIM.isAuthenticated()
{
    return ((connection <> null) and (connection.isAuthenticated() == true));
}

operation JabberIM.logout()
{
    if (isConnected() == true)
    {
	roster.removeRosterListener(rosterListener);
	connection.disconnect();
	delete connection;
	delete roster;
    }
}

operation JabberIM.setPresence()
{
    if (isConnected() == true)
    {
	// fix me
    }
}
