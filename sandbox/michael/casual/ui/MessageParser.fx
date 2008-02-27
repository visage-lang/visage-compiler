package casual.ui;

import casual.theme.ThemeManager;

import casual.resources.CASUAL; // import this compilation unit, which will declare CASUAL_RESOURCE_URL
import javafx.ui.Color;

import java.applet.Applet as JApplet;
import java.applet.AudioClip;
import java.net.URL;
import java.lang.Object;

class Keyword
{
    public attribute id: String;
    public attribute key: String;
    public attribute rsrc: Object;
}

var casualURL = CASUAL_RESOURCE_URL:String; // root URL for casual resources

HTTP:Keyword = Keyword
{
    id: "HTTP"
    key: "http://"
};

HTTP2:Keyword = Keyword
{
    id: "HTTP2"
    key: "https://"
};

WWW:Keyword = Keyword
{
    id: "WWW"
    key: "www."
};

IMG:Keyword = Keyword
{
    id: "IMG"
    key: "[img]="
};

AUDIO:Keyword = Keyword
{   
    id: "AUDIO"
    key: "[audio]="
};

PAGE:Keyword = Keyword
{   
    id: "PAGE"
    key: "[page]="
};

RING:Keyword = Keyword
{
    var url = "{casualURL}/sounds/ring.wav"
    
    id: "RING"
    key: "[ring]"
    rsrc: JApplet.newAudioClip(new URL(url))
};

SMILE:Keyword = Keyword
{    
    var url = "{casualURL}/images/smile.png"
    
    id: "SMILE"
    key: ":-)"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

SMILE2:Keyword = Keyword
{
    id: "SMILE2"
    key: ":)"
    rsrc: bind SMILE.rsrc
};

WINK:Keyword = Keyword
{
    var url = "{casualURL}/images/wink.png"
    
    id: "WINK"
    key: ";-)"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

WINK2:Keyword = Keyword
{
    id: "WINK2"
    key: ";)"
    rsrc: bind WINK.rsrc
};

FROWN:Keyword = Keyword
{
    var url = "{casualURL}/images/frown.png"
    
    id: "FROWN"
    key: ":-("
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

FROWN2:Keyword = Keyword
{
    id: "FROWN2"
    key: ":("
    rsrc: bind FROWN.rsrc
};

UNDECIDED:Keyword = Keyword
{
    var url = "{casualURL}/images/undecided.png"
    
    id: "UNDECIDED"
    key: ":-/"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

UNDECIDED2:Keyword = Keyword
{
    id: "UNDECIDED2"
    key: ":-\\"
    rsrc: bind UNDECIDED.rsrc
};

GASP:Keyword = Keyword
{
    var url = "{casualURL}/images/gasp.png"
    
    id: "GASP"
    key: ":-o"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

GASP2:Keyword = Keyword
{
    id: "GASP2"
    key: ":-O"
    rsrc: bind GASP.rsrc
};

GASP3:Keyword = Keyword
{
    id: "GASP3"
    key: ":-0"
    rsrc: bind GASP.rsrc
};

GASP4:Keyword = Keyword
{
    id: "GASP4"
    key: "=-o"
    rsrc: bind GASP.rsrc
};

LAUGH:Keyword = Keyword
{
    var url = "{casualURL}/images/laugh.png"
    
    id: "LAUGH"
    key: ":-D"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

LAUGH2:Keyword = Keyword
{
    id: "LAUGH2"
    key: ":D"
    rsrc: bind LAUGH.rsrc
};

KISS:Keyword = Keyword
{
    var url = "{casualURL}/images/kiss.png"
    
    id: "KISS"
    key: ":-*"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

TONQUE:Keyword = Keyword
{
    var url = "{casualURL}/images/tonque.png"
    
    id: "TONQUE"
    key: ":-p"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

TONQUE2:Keyword = Keyword
{
    id: "TONQUE2"
    key: ":p"
    rsrc: bind TONQUE.rsrc
};

TONQUE3:Keyword = Keyword
{
    id: "TONQUE3"
    key: ":-P"
    rsrc: bind TONQUE.rsrc
};

TONQUE4:Keyword = Keyword
{
    id: "TONQUE4"
    key: ":P"
    rsrc: bind TONQUE.rsrc
};

EMBARRASED:Keyword = Keyword
{
    var url = "{casualURL}/images/embarrassed.png"
    
    id: "EMBARRASED"
    key: ":-["
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

FOOT:Keyword = Keyword
{
    var url = "{casualURL}/images/foot.png"
    
    id: "FOOT"
    key: ":-!"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

COOL:Keyword = Keyword
{
    var url = "{casualURL}/images/cool.png"
    
    id: "COOL"
    key: "8-)"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

ANGRY:Keyword = Keyword
{
    var url = "{casualURL}/images/angry.png"
    
    id: "ANGRY"
    key: ">:-o"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

ANGRY2:Keyword = Keyword
{
    id: "ANGRY2"
    key: ">:-O"
    rsrc: bind ANGRY.rsrc
};

ANGRY3:Keyword = Keyword
{
    id: "ANGRY3"
    key: ">:-0"
    rsrc: bind ANGRY.rsrc
};

ANGRY4:Keyword = Keyword
{
    id: "ANGRY4"
    key: ">:o"
    rsrc: bind ANGRY.rsrc
};

ANGRY5:Keyword = Keyword
{
    id: "ANGRY5"
    key: ">:O"
    rsrc: bind ANGRY.rsrc
};

ANGRY6:Keyword = Keyword
{
    id: "ANGRY6"
    key: ">:0"
    rsrc: bind ANGRY.rsrc
};

INNOCENT:Keyword = Keyword
{
    var url = "{casualURL}/images/innocent.png"
    
    id: "INNOCENT"
    key: "o:-)"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

INNOCENT2:Keyword = Keyword
{
    id: "INNOCENT2"
    key: "O:-)"
    rsrc: bind INNOCENT.rsrc
};

INNOCENT3:Keyword = Keyword
{
    id: "INNOCENT3"
    key: "0:-)"
    rsrc: bind INNOCENT.rsrc
};

CRY:Keyword = Keyword
{
    var url = "{casualURL}/images/cry.png"
    
    id: "CRY"
    key: ":'("
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

CRY2:Keyword = Keyword
{
    id: "CRY2"
    key: ":^("
    rsrc: bind CRY.rsrc
};

CRY3:Keyword = Keyword
{
    id: "CRY3"
    key: ":`("
    rsrc: bind CRY.rsrc
};

SEALED:Keyword = Keyword
{
    var url = "{casualURL}/images/sealed.png"
    
    id: "SEALED"
    key: ":-x"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

SEALED2:Keyword = Keyword
{
    id: "SEALED2"
    key: ":-X"
    rsrc: bind SEALED.rsrc
};

MONEY:Keyword = Keyword
{
    var url = "{casualURL}/images/money.png"
    
    id: "MONEY"
    key: ":-$"
    rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
};

public class MessageParser
{
    public operation MessageParser();
    public operation parse(string:String, type:MessageType): String;

    public operation ringPlay();
    public operation ringLoop();
    public operation ringStop();
}

operation MessageParser.ringPlay()
{
    var audioClip:AudioClip = (AudioClip)RING:Keyword.rsrc;
    audioClip.stop();
    audioClip.play();
}

operation MessageParser.ringLoop()
{
    var audioClip:AudioClip = (AudioClip)RING:Keyword.rsrc;
    audioClip.stop();
    audioClip.loop();
}

operation MessageParser.ringStop()
{
    var audioClip:AudioClip = (AudioClip)RING:Keyword.rsrc;
    audioClip.stop();
}

operation MessageParser.parse(string:String, type:MessageType): String
{
//println("MessageParser.parse string=\"{string}\"");
    var result:String = "";
    
    var tokens = string.split("\\s");
    var tokensSize = sizeof tokens;
    var tokensIndex = 0;
    for (token in tokens)
    {        
        var part:String = null;
        
        if (token.equals(RING:Keyword.key) == true)
        {
            if (type == INCOMING:MessageType)
            {
                ringPlay();
                return null;
            }
            else
            {
                return token;
            }
        }
        else if ((token.startsWith(HTTP:Keyword.key) == true) or (token.startsWith(HTTP2:Keyword.key) == true))
        {
            var color:Color = null;
            if (type == INCOMING:MessageType)
            {
                color = theme:ThemeManager.messageOutForeground;
            }
            else
            {
                color = theme:ThemeManager.messageInForeground;
            }
            part = new String("<a text-decoration='none' style='color:{color.htmlRef()}' href='{token}'>{token}</a>");
        }
        else if (token.startsWith(WWW:Keyword.key) == true)
        {
            var color:Color = null;
            if (type == INCOMING:MessageType)
            {
                color = theme:ThemeManager.messageOutForeground;
            }
            else
            {
                color = theme:ThemeManager.messageInForeground;
            }
            part = new String("<a text-decoration='none' style='color:{color.htmlRef()}' href='http://{token}'>{token}</a>");
        }
        else if (token.startsWith(IMG:Keyword.key) == true)
        {
            var url = token.substring(IMG:Keyword.key.length(), token.length());
            if ((url.startsWith("\"") == true) or (url.startsWith("<") == true))
            {
                url = url.substring(1, url.length()-1);
            }
            part = new String("<img src='{url}'></img>");
        }
        else if ((token.equals(SMILE:Keyword.key) == true) or (token.equals(SMILE2:Keyword.key) == true))
        {
            part = (String)SMILE:Keyword.rsrc;
        }
        else if ((token.equals(WINK:Keyword.key) == true) or (token.equals(WINK2:Keyword.key) == true))
        {
            part = (String)WINK:Keyword.rsrc;
        }
        else if ((token.equals(FROWN:Keyword.key) == true) or (token.equals(FROWN2:Keyword.key) == true))
        {
            part = (String)FROWN:Keyword.rsrc;
        }
        else if ((token.equals(UNDECIDED:Keyword.key) == true) or (token.equals(UNDECIDED2:Keyword.key) == true))
        {
            part = (String)UNDECIDED:Keyword.rsrc;
        }
        else if ((token.equals(GASP:Keyword.key) == true) or (token.equals(GASP2:Keyword.key) == true) or (token.equals(GASP3:Keyword.key) == true) or (token.equals(GASP4:Keyword.key) == true))
        {
            part = (String)GASP:Keyword.rsrc;
        }
        else if ((token.equals(LAUGH:Keyword.key) == true) or (token.equals(LAUGH2:Keyword.key) == true))
        {
            part = (String)LAUGH:Keyword.rsrc;
        }
        else if (token.equals(KISS:Keyword.key) == true)
        {
            part = (String)KISS:Keyword.rsrc;
        }
        else if ((token.equals(TONQUE:Keyword.key) == true) or (token.equals(TONQUE2:Keyword.key) == true) or (token.equals(TONQUE3:Keyword.key) == true) or (token.equals(TONQUE4:Keyword.key) == true))
        {
            part = (String)TONQUE:Keyword.rsrc;
        }
        else if (token.equals(EMBARRASED:Keyword.key) == true)
        {
            part = (String)EMBARRASED:Keyword.rsrc;
        }
        else if (token.equals(FOOT:Keyword.key) == true)
        {
            part = (String)FOOT:Keyword.rsrc;
        }
        else if (token.equals(COOL:Keyword.key) == true)
        {
            part = (String)COOL:Keyword.rsrc;
        }
        else if ((token.equals(ANGRY:Keyword.key) == true) or (token.equals(ANGRY2:Keyword.key) == true) or (token.equals(ANGRY3:Keyword.key) == true))
        {
            part = (String)ANGRY:Keyword.rsrc;
        }
        else if ((token.equals(ANGRY4:Keyword.key) == true) or (token.equals(ANGRY4:Keyword.key) == true) or (token.equals(ANGRY6:Keyword.key) == true))
        {
            part = (String)ANGRY:Keyword.rsrc;
        }
        else if ((token.equals(INNOCENT:Keyword.key) == true) or (token.equals(INNOCENT2:Keyword.key) == true) or (token.equals(INNOCENT3:Keyword.key) == true))
        {
            part = (String)INNOCENT:Keyword.rsrc;
        }
        else if ((token.equals(CRY:Keyword.key) == true) or (token.equals(CRY2:Keyword.key) == true)or (token.equals(CRY3:Keyword.key) == true))
        {
            part = (String)CRY:Keyword.rsrc;
        }
        else if ((token.equals(SEALED:Keyword.key) == true) or (token.equals(SEALED2:Keyword.key) == true))
        {
            part = (String)SEALED:Keyword.rsrc;
        }
        else if (token.equals(MONEY:Keyword.key) == true)
        {
            part = (String)MONEY:Keyword.rsrc;
        }
        else
        {
            part = token;
        }
        
        result = result.concat(part);
        tokensIndex++;
        if (tokensIndex <= tokensSize)
        {
            result = result.concat(" ");
        }
    }
    
//println("   result=\"{result}\"");
    return result;
}

parser:MessageParser = new MessageParser();