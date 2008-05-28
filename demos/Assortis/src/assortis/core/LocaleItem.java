package assortis.core;

import java.util.Locale;

public class LocaleItem {

    public Locale locale;
    public String text;
    
    public LocaleItem(Locale locale, String text){
        this.locale = locale;
        this.text = text;
    }

    
    @Override
    public String toString() {
        return "[locale item] " + locale + "\n" + text;
    }
    
    
}
