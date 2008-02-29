package casual.im;

public class Account
{
    public attribute name: String;
    public attribute server: String;
    public attribute emailServer: String;
    public attribute port: Integer;

    public static attribute JABBER:Account = Account
    {
         name: "Jabber"
         server: "jabber.org"
         emailServer: "jabber.org"
         port: 5222
    };

    public static attribute GOOGLE_TALK:Account = Account
    {
         name: "GoogleTalk"
         server: "talk.google.com"
         emailServer: "gmail.com"
         port: 5222
    };

    public static attribute UNKNOWN:Account = Account
    {
         name: "unknown"
         server: "unknown"
         emailServer: "unknown"
         port: 5222
    };
}
   