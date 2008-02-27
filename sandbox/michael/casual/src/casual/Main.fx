package casual;

if (true)
{
    import casual.AccountWindow;
    
    var win = new AccountWindow();
}
else if (true)
{
    import casual.ChatWindow;
    import casual.im.Buddy;
    
    var buddy = Buddy
    {
        firstName: "Leonardo"
        lastName: "Da Vinci"
        userName: "ldavinci"
        accountName: "JABBER.org"
    };
    var win = new ChatWindow(buddy);
    win.width = 350;
    win.height = 500;
    win.visible = true;
}
else if (true)
{

}

