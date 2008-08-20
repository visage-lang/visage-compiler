package casual.im;

public abstract class Chat
{
    public abstract function sendMessage(message:String): Void;
    public var receiveMessage: function(message:String);
}

