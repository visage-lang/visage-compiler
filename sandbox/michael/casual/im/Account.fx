package casual.im;

public class Account
{
    public attribute name: String;
    public attribute server: String;
    public attribute emailServer: String?;
    public attribute port: Integer;
}

JABBER:Account = Account
{
     name: "Jabber"
     server: "jabber.org"
     emailServer: "jabber.org"
     port: 5222
};

GOOGLE_TALK:Account = Account
{
     name: "GoogleTalk"
     server: "talk.google.com"
     emailServer: "gmail.com"
     port: 5222
};

// gerard's local jabber server (for testing)
MOON:Account = Account
{
     name: "Moon"
     server: "moon.local"
     emailServer: "moon.local"
     port: 5222
};

UNKNOWN:Account = Account
{
     name: "unknown"
     server: "unknown"
     emailServer: "unknown"
     port: 5222
};
