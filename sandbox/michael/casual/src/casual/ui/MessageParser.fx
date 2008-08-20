package casual.ui;

import casual.theme.*;

import casual.resources.CASUAL; // import this compilation unit, which will declare CASUAL_RESOURCE_URL
import javafx.ui.Color;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.lang.Object;


class Keyword
{
    static var casualURL: String = CASUAL.getResourceURL(); // root URL for casual resources

    public var id: String;
    public var key: String;
    public var rsrc: Object;
    public static var HTTP:Keyword = Keyword
    {
        id: "HTTP"
        key: "http://"
    };

    public static var HTTP2:Keyword = Keyword
    {
        id: "HTTP2"
        key: "https://"
    };

    public static var WWW:Keyword = Keyword
    {
        id: "WWW"
        key: "www."
    };

    public static var IMG:Keyword = Keyword
    {
        id: "IMG"
        key: "[img]="
    };

    public static var AUDIO:Keyword = Keyword
    {   
        id: "AUDIO"
        key: "[audio]="
    };

    public static var PAGE:Keyword = Keyword
    {   
        id: "PAGE"
        key: "[page]="
    };

    public static var RING:Keyword = Keyword
    {
        var url = "{casualURL}/sounds/ring.wav"

        id: "RING"
        key: "[ring]"
        rsrc: Applet.newAudioClip(new URL(url))
    };

    public static var SMILE:Keyword = Keyword
    {    
        var url = "{casualURL}/images/smile.png"

        id: "SMILE"
        key: ":-)"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var SMILE2:Keyword = Keyword
    {
        id: "SMILE2"
        key: ":)"
        rsrc: bind SMILE.rsrc
    };

    public static var WINK:Keyword = Keyword
    {
        var url = "{casualURL}/images/wink.png"

        id: "WINK"
        key: ";-)"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var WINK2:Keyword = Keyword
    {
        id: "WINK2"
        key: ";)"
        rsrc: bind WINK.rsrc
    };

    public static var FROWN:Keyword = Keyword
    {
        var url = "{casualURL}/images/frown.png"

        id: "FROWN"
        key: ":-("
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var FROWN2:Keyword = Keyword
    {
        id: "FROWN2"
        key: ":("
        rsrc: bind FROWN.rsrc
    };

    public static var UNDECIDED:Keyword = Keyword
    {
        var url = "{casualURL}/images/undecided.png"

        id: "UNDECIDED"
        key: ":-/"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var UNDECIDED2:Keyword = Keyword
    {
        id: "UNDECIDED2"
        key: ":-\\"
        rsrc: bind UNDECIDED.rsrc
    };

    public static var GASP:Keyword = Keyword
    {
        var url = "{casualURL}/images/gasp.png"

        id: "GASP"
        key: ":-o"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var GASP2:Keyword = Keyword
    {
        id: "GASP2"
        key: ":-O"
        rsrc: bind GASP.rsrc
    };

    public static var GASP3:Keyword = Keyword
    {
        id: "GASP3"
        key: ":-0"
        rsrc: bind GASP.rsrc
    };

    public static var GASP4:Keyword = Keyword
    {
        id: "GASP4"
        key: "=-o"
        rsrc: bind GASP.rsrc
    };

    public static var LAUGH:Keyword = Keyword
    {
        var url = "{casualURL}/images/laugh.png"

        id: "LAUGH"
        key: ":-D"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var LAUGH2:Keyword = Keyword
    {
        id: "LAUGH2"
        key: ":D"
        rsrc: bind LAUGH.rsrc
    };

    public static var KISS:Keyword = Keyword
    {
        var url = "{casualURL}/images/kiss.png"

        id: "KISS"
        key: ":-*"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var TONQUE:Keyword = Keyword
    {
        var url = "{casualURL}/images/tonque.png"

        id: "TONQUE"
        key: ":-p"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var TONQUE2:Keyword = Keyword
    {
        id: "TONQUE2"
        key: ":p"
        rsrc: bind TONQUE.rsrc
    };

    public static var TONQUE3:Keyword = Keyword
    {
        id: "TONQUE3"
        key: ":-P"
        rsrc: bind TONQUE.rsrc
    };

    public static var TONQUE4:Keyword = Keyword
    {
        id: "TONQUE4"
        key: ":P"
        rsrc: bind TONQUE.rsrc
    };

    public static var EMBARRASED:Keyword = Keyword
    {
        var url = "{casualURL}/images/embarrassed.png"

        id: "EMBARRASED"
        key: ":-["
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var FOOT:Keyword = Keyword
    {
        var url = "{casualURL}/images/foot.png"

        id: "FOOT"
        key: ":-!"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var COOL:Keyword = Keyword
    {
        var url = "{casualURL}/images/cool.png"

        id: "COOL"
        key: "8-)"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var ANGRY:Keyword = Keyword
    {
        var url = "{casualURL}/images/angry.png"

        id: "ANGRY"
        key: ">:-o"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var ANGRY2:Keyword = Keyword
    {
        id: "ANGRY2"
        key: ">:-O"
        rsrc: bind ANGRY.rsrc
    };

    public static var ANGRY3:Keyword = Keyword
    {
        id: "ANGRY3"
        key: ">:-0"
        rsrc: bind ANGRY.rsrc
    };

    public static var ANGRY4:Keyword = Keyword
    {
        id: "ANGRY4"
        key: ">:o"
        rsrc: bind ANGRY.rsrc
    };

    public static var ANGRY5:Keyword = Keyword
    {
        id: "ANGRY5"
        key: ">:O"
        rsrc: bind ANGRY.rsrc
    };

    public static var ANGRY6:Keyword = Keyword
    {
        id: "ANGRY6"
        key: ">:0"
        rsrc: bind ANGRY.rsrc
    };

    public static var INNOCENT:Keyword = Keyword
    {
        var url = "{casualURL}/images/innocent.png"

        id: "INNOCENT"
        key: "o:-)"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var INNOCENT2:Keyword = Keyword
    {
        id: "INNOCENT2"
        key: "O:-)"
        rsrc: bind INNOCENT.rsrc
    };

    public static var INNOCENT3:Keyword = Keyword
    {
        id: "INNOCENT3"
        key: "0:-)"
        rsrc: bind INNOCENT.rsrc
    };

    public static var CRY:Keyword = Keyword
    {
        var url = "{casualURL}/images/cry.png"

        id: "CRY"
        key: ":'("
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var CRY2:Keyword = Keyword
    {
        id: "CRY2"
        key: ":^("
        rsrc: bind CRY.rsrc
    };

    public static var CRY3:Keyword = Keyword
    {
        id: "CRY3"
        key: ":`("
        rsrc: bind CRY.rsrc
    };

    public static var SEALED:Keyword = Keyword
    {
        var url = "{casualURL}/images/sealed.png"

        id: "SEALED"
        key: ":-x"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };

    public static var SEALED2:Keyword = Keyword
    {
        id: "SEALED2"
        key: ":-X"
        rsrc: bind SEALED.rsrc
    };

    public static var MONEY:Keyword = Keyword
    {
        var url = "{casualURL}/images/money.png"

        id: "MONEY"
        key: ":-$"
        rsrc: "<img align='top' hspace='1' vspace='0' src='{url}'></img>"
    };
}

public class MessageParser
{
    static var instance: MessageParser = new MessageParser;
    public static function getInstance(): MessageParser {
        return instance;
    }
    
    public function parse(string:String, type:MessageType): String {
    //println("MessageParser.parse string=\"{string}\"");
        var result:String = "";

        var tokens = string.split("\\s");
        var tokensSize = sizeof tokens;
        var tokensIndex = 0;
        for (token in tokens)
        {        
            var part:String = null;

            if (token.equals(Keyword.RING.key) == true)
            {
                if (type == MessageType.INCOMING)
                {
                    ringPlay();
                    return null;
                }
                else
                {
                    return token;
                }
            }
            else if ((token.startsWith(Keyword.HTTP.key) == true) or (token.startsWith(Keyword.HTTP2.key) == true))
            {
                var color:Color = null;
                if (type == MessageType.INCOMING)
                {
                    color = ThemeManager.getInstance().messageOutForeground;
                }
                else
                {
                    color = ThemeManager.getInstance().messageInForeground;
                }
                part = new String("<a text-decoration='none' style='color:{color.htmlRef()}' href='{token}'>{token}</a>");
            }
            else if (token.startsWith(Keyword.WWW.key) == true)
            {
                var color:Color = null;
                if (type == MessageType.INCOMING)
                {
                    color = ThemeManager.getInstance().messageOutForeground;
                }
                else
                {
                    color = ThemeManager.getInstance().messageInForeground;
                }
                part = new String("<a text-decoration='none' style='color:{color.htmlRef()}' href='http://{token}'>{token}</a>");
            }
            else if (token.startsWith(Keyword.IMG.key) == true)
            {
                var url = token.substring(Keyword.IMG.key.length(), token.length());
                if ((url.startsWith("\"") == true) or (url.startsWith("<") == true))
                {
                    url = url.substring(1, url.length()-1);
                }
                part = new String("<img src='{url}'></img>");
            }
            else if ((token.equals(Keyword.SMILE.key) == true) or (token.equals(Keyword.SMILE2.key) == true))
            {
                part = Keyword.SMILE.rsrc as String;
            }
            else if ((token.equals(Keyword.WINK.key) == true) or (token.equals(Keyword.WINK2.key) == true))
            {
                part = Keyword.WINK.rsrc as String;
            }
            else if ((token.equals(Keyword.FROWN.key) == true) or (token.equals(Keyword.FROWN2.key) == true))
            {
                part = Keyword.FROWN.rsrc as String;
            }
            else if ((token.equals(Keyword.UNDECIDED.key) == true) or (token.equals(Keyword.UNDECIDED2.key) == true))
            {
                part = Keyword.UNDECIDED.rsrc as String;
            }
            else if ((token.equals(Keyword.GASP.key) == true) or (token.equals(Keyword.GASP2.key) == true) or (token.equals(Keyword.GASP3.key) == true) or (token.equals(Keyword.GASP4.key) == true))
            {
                part = Keyword.GASP.rsrc as String;
            }
            else if ((token.equals(Keyword.LAUGH.key) == true) or (token.equals(Keyword.LAUGH2.key) == true))
            {
                part = Keyword.LAUGH.rsrc as String;
            }
            else if (token.equals(Keyword.KISS.key) == true)
            {
                part = Keyword.KISS.rsrc as String;
            }
            else if ((token.equals(Keyword.TONQUE.key) == true) or (token.equals(Keyword.TONQUE2.key) == true) or (token.equals(Keyword.TONQUE3.key) == true) or (token.equals(Keyword.TONQUE4.key) == true))
            {
                part = Keyword.TONQUE.rsrc as String;
            }
            else if (token.equals(Keyword.EMBARRASED.key) == true)
            {
                part = Keyword.EMBARRASED.rsrc as String;
            }
            else if (token.equals(Keyword.FOOT.key) == true)
            {
                part = Keyword.FOOT.rsrc as String;
            }
            else if (token.equals(Keyword.COOL.key) == true)
            {
                part = Keyword.COOL.rsrc as String;
            }
            else if ((token.equals(Keyword.ANGRY.key) == true) or (token.equals(Keyword.ANGRY2.key) == true) or (token.equals(Keyword.ANGRY3.key) == true))
            {
                part = Keyword.ANGRY.rsrc as String;
            }
            else if ((token.equals(Keyword.ANGRY4.key) == true) or (token.equals(Keyword.ANGRY4.key) == true) or (token.equals(Keyword.ANGRY6.key) == true))
            {
                part = Keyword.ANGRY.rsrc as String;
            }
            else if ((token.equals(Keyword.INNOCENT.key) == true) or (token.equals(Keyword.INNOCENT2.key) == true) or (token.equals(Keyword.INNOCENT3.key) == true))
            {
                part = Keyword.INNOCENT.rsrc as String;
            }
            else if ((token.equals(Keyword.CRY.key) == true) or (token.equals(Keyword.CRY2.key) == true)or (token.equals(Keyword.CRY3.key) == true))
            {
                part = Keyword.CRY.rsrc as String;
            }
            else if ((token.equals(Keyword.SEALED.key) == true) or (token.equals(Keyword.SEALED2.key) == true))
            {
                part = Keyword.SEALED.rsrc as String;
            }
            else if (token.equals(Keyword.MONEY.key) == true)
            {
                part = Keyword.MONEY.rsrc as String;
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
    };

    public function ringPlay() {
        var audioClip:AudioClip = Keyword.RING.rsrc as AudioClip;
        audioClip.stop();
        audioClip.play();
    };
    
    public function ringLoop() {
        var audioClip:AudioClip = Keyword.RING.rsrc as AudioClip;
        audioClip.stop();
        audioClip.loop();
    };
    
    public function  ringStop()
    {
        var audioClip:AudioClip = Keyword.RING.rsrc as AudioClip;
        audioClip.stop();
    }
}
