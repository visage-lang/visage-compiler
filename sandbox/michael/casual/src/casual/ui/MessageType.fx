package casual.ui;

public class MessageType
{
    public var id: String;

    public static var INCOMING:MessageType = MessageType
    {
        id: "INCOMING"
    };
    
    public static var OUTGOING:MessageType = MessageType
    {
        id: "OUTGOING"
    };
    
    public static var COMMENT:MessageType = MessageType
    {
        id: "COMMENT"
    };

}

