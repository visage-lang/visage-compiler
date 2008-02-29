package casual.im;

import casual.AccountWindow;

public class InstantMessenger
{
    function createIM(user:Buddy):InstantMessenger
        {
            // we only support Jabber.org at the moment
            var im = JabberIM {user: user};

            return im;
        };
    
    public function login(window:AccountWindow);
    public function logout();
    
    public function setPresence();
    
    public function isConnected():Boolean;
    public function isAuthenticated(): Boolean;
    
    public attribute user: Buddy;
    public attribute buddies: Buddy[];
}
