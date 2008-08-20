package casual.im;

public class Account
{
    public var name: String;
    public var server: String;
    public var emailServer: String;
    public var port: Integer;

    public static var JABBER:Account = Account
    {
         name: "Jabber"
         server: "jabber.org"
         emailServer: "jabber.org"
         port: 5222
    };

    public static var GOOGLE_TALK:Account = Account
    {
         name: "GoogleTalk"
         server: "talk.google.com"
         emailServer: "gmail.com"
         port: 5222
    };

    public static var UNKNOWN:Account = Account
    {
         name: "unknown"
         server: "unknown"
         emailServer: "unknown"
         port: 5222
    };
}
   
