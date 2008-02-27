package casual.ui;

public class MessageType
{
    public attribute id: String;
}

INCOMING:MessageType = MessageType
{
    id: "INCOMING"
};
OUTGOING:MessageType = MessageType
{
    id: "OUTGOING"
};
COMMENT:MessageType = MessageType
{
    id: "COMMENT"
};


