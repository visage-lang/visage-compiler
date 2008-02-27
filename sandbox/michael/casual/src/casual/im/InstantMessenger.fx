package casual.im;

import casual.AccountWindow;

public class InstantMessenger
{
    operation createIM(user:Buddy):InstantMessenger;
    
    public operation login(window:AccountWindow);
    public operation logout();
    
    public operation setPresence();
    
    public operation isConnected():Boolean;
    public operation isAuthenticated(): Boolean;
    
    public attribute user: Buddy;
    public attribute buddies: Buddy*;
}

operation InstantMessenger.createIM(user:Buddy):InstantMessenger
{
    // we only support Jabber.org at the moment
    var im = new JabberIM(user);
    
    return im;
}

