package casual.im;

import casual.AccountWindow;
import casual.BuddyWindow;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;

import java.util.Collection;
import java.lang.System;

public class JabberIM extends InstantMessenger
{
    private attribute connection: XMPPConnection;
    private attribute roster: Roster;
    private attribute chatManager: ChatManager;
    private attribute rosterListener: RosterListener;

    function login(window:AccountWindow): Void
    {
        var account = user.account;
        var configuration;
        if (account == Account.GOOGLE_TALK)
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
            // TODO DO  - this is a work around until a more permanent solution is provided
//            do
//            {
                connection.connect();
//            }
        }
        catch (any)
        {
            java.awt.Toolkit.getDefaultToolkit().beep();

           //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                public function run():Void {
                    var text = "\"{account.server}:{account.port}\" UNREACHABLE";
                    window.showErrorMessage(text, "ERROR:", true);
                }
            });

            return;
        }

        try
        {
            // TODO DO  - this is a work around until a more permanent solution is provided
//            do
//            {
                connection.login(this.user.userName, this.user.password, "Casual", true);
//            }
        }
        catch (any2)
        {
            if ((connection <> null) and (connection.isConnected() == true))
            {
                connection.disconnect();
            }

            java.awt.Toolkit.getDefaultToolkit().beep();

           //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                public function run():Void {
                    var text = "unable to connect: {user.userName}@{user.accountName}";
                    window.showErrorMessage(text, "ERROR:", true);
                }
            });

            return;
        }

        chatManager = connection.getChatManager();

        var roster = connection.getRoster();
        rosterListener = RosterListener
        {
            function entriesAdded(addresses:Collection)
            {
               //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        System.out.println("entriesAdded");
                    }
                });
            }
            function entriesDeleted(addresses:Collection)
            {
               //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        System.out.println("entriesDeleted");
                    }
                });
            }
            function entriesUpdated(addresses:Collection)
            {
               //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        System.out.println("entriesUpdated");
                    }
                });
            }
            function presenceChanged(presence:Presence)
            {
               //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        var userAddress = presence.getFrom();
                        var userName = userAddress.substring(0, userAddress.indexOf('@'));
                        for (buddy in buddies)
                        {
                            if (buddy.userName.equalsIgnoreCase(userName) == true)
                            {
                                var mode = presence.getMode();
                                if (mode == null)
                                {
                                    if (presence.isAvailable())
                                    {
                                        buddy.presence = Buddy.BuddyPresence.AVAILABLE;
                                    }
                                    else
                                    {
                                        buddy.presence = Buddy.BuddyPresence.AWAY;
                                    }
                                }
                                else if (presence.getMode() == Presence.Mode.available)
                                {
                                    buddy.presence = Buddy.BuddyPresence.AVAILABLE;
                                }
                                else
                                {
                                    buddy.presence = Buddy.BuddyPresence.AWAY;
                                }
                                break;
                            }
                        }
                   }
                });
            }
        };
        roster.addRosterListener(rosterListener);

        var groups = roster.getGroups();
        var i = groups.iterator();
        while (i.hasNext())
        {
            var rg:RosterGroup = i.next() as RosterGroup;

            var ge = rg.getEntries();
            var j = ge.iterator();
            while (j.hasNext())
            {
                var re = j.next() as RosterEntry;

                var addressXMPP = re.getUser();
                var userName = addressXMPP.substring(0, addressXMPP.indexOf('@'));
                var accountName = addressXMPP.substring(addressXMPP.indexOf('@')+1, addressXMPP.length());
                var buddy = Buddy
                {
                    type: Buddy.BuddyType.BUDDY
                    userName: userName
                    accountName: accountName
                };

                var bestPresence = roster.getPresence(addressXMPP);
                var mode = bestPresence.getMode();
                if (mode == null)
                {
                    if (bestPresence.isAvailable())
                    {
                        buddy.presence = Buddy.BuddyPresence.AVAILABLE;
                    }
                    else
                    {
                        buddy.presence = Buddy.BuddyPresence.AWAY;
                    }
                }
                else if (bestPresence.getMode() == Presence.Mode.available)
                {
                    buddy.presence = Buddy.BuddyPresence.AVAILABLE;
                }
                else
                {
                    buddy.presence = Buddy.BuddyPresence.AWAY;
                }

                buddy.chat = JabberChat {manager: chatManager, buddy: buddy};

                insert buddy into buddies;
            }
        }

        var presence = new Presence(Presence.Type.available, "available", 1, Presence.Mode.available);
        connection.sendPacket(presence);

        //TODO DO LATER - this is a work around until a more permanent solution is provided
        var im = this;
        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
            public function run():Void {
                window.hideMessage();
                window.close();
                var buddyWindow = BuddyWindow {im: im};
            }
        });
    }

    function isConnected(): Boolean
    {
        return ((connection <> null) and (connection.isConnected() == true));
    }

    function isAuthenticated(): Boolean
    {
        return ((connection <> null) and (connection.isAuthenticated() == true));
    }

    function logout(): Void
    {
        if (isConnected() == true)
        {
            roster.removeRosterListener(rosterListener);
            connection.disconnect();
            connection = null;
            roster = null;
        }
    }

    function setPresence(): Void
    {
        if (isConnected() == true)
        {
            // fix me
        }
    }
}