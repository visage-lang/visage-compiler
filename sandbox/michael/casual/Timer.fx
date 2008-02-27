package casual;

import java.util.Date;

class Timer
{
    attribute tick: Number;
    attribute running: Boolean;
    
    attribute hours: Number;
    attribute minutes: Number;
    attribute seconds: Number;
    
    public operation toString(): String;
}

attribute Timer.tick = bind
if running 
    then [1..20] dur 1000 linear
        while running 
        continue if true 
    else 0;

trigger on Timer.tick = value
{
    var now = new Date();
    minutes = now.getMinutes();
    seconds = now.getSeconds() + (now.getTime() % 1000)/1000;
    hours = now.getHours();        
}

operation Timer.toString(): String
{
    var h:Integer = hours;
    var m:Integer = minutes;
    var s:Integer = seconds;
    
    var pmamStr:String = "AM";
    var hourStr:String = "{h}";
    if (h >= 12)
    {
        h = h-12;
        pmamStr = "PM";
    }
    if (h == 0)
    {
        hourStr = "12";
    }
    else
    {
        hourStr = "{h}";
    }
    
    var minuteStr:String = "{m}";
    if (m < 10)
    {
        minuteStr = "0{m}";
    }
    
    var secondStr:String = "{s}";
    if (s < 10)
    {
        secondStr = "0{s}";
    }
    
    return "{hourStr}:{minuteStr} {pmamStr}";
}

