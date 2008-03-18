package casual.im;

import casual.AccountWindow;

public abstract class InstantMessenger
{
    public static function createIM(user:Buddy):InstantMessenger
        {
            // we only support Jabber.org at the moment
            var im = JabberIM {user: user};

            return im;
        };
    
    public abstract function login(window:AccountWindow): Void;
    public abstract function logout(): Void;

    public abstract function setPresence(): Void;
    
    public abstract function isConnected():Boolean;
    public abstract function isAuthenticated(): Boolean;
    
    public attribute user: Buddy;
    public attribute buddies: Buddy[];
}
