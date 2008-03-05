package casual.ui;

public class MessageType
{
    public attribute id: String;

    public static attribute INCOMING:MessageType = MessageType
    {
        id: "INCOMING"
    };
    
    public static attribute OUTGOING:MessageType = MessageType
    {
        id: "OUTGOING"
    };
    
    public static attribute COMMENT:MessageType = MessageType
    {
        id: "COMMENT"
    };

}

