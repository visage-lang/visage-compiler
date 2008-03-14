package casual.im;

public abstract class Chat
{
    public abstract function sendMessage(message:String): Void;
    public attribute receiveMessage: function(message:String);
}

