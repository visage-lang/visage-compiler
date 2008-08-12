package assortis.core;

/**
 * @author Alexandr Scherbatiy
 */


import java.util.Locale;

public function createItem(localeItem: LocaleItem){
    return PropertyItem{ locale: localeItem.locale, text: localeItem.text}
}
 
public class PropertyItem {

    public var locale:Locale;
    public var text:String;
    
    public function getName(){
        return locale.getDisplayName(); 
    } 

}
