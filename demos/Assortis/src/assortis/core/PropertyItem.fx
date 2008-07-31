package assortis.core;

/**
 * @author Alexandr Scherbatiy
 */


import java.util.Locale;

public function createItem(localeItem: LocaleItem){
    return PropertyItem{ locale: localeItem.locale, text: localeItem.text}
}
 
public class PropertyItem {

    public attribute locale:Locale;
    public attribute text:String;
    
    public function getName(){
        return locale.getDisplayName(); 
    } 

}
