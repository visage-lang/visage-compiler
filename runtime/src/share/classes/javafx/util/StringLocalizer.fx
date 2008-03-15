package javafx.util;

import java.lang.Class;
import java.lang.System;
import java.lang.Thread;
import java.lang.StackTraceElement;
import java.util.Locale;
import com.sun.javafx.runtime.util.StringLocalization;

/**
 * Utility class for localizing strings using JavaFX properties files.
 *
 * An object of this class is used to find the appropriate localized
 * strings for the given attributes.  For example,
 * <code>
 * // Object creation
 * var localizer = StringLocalizer{ key: "Hello, World!" };
 *
 * // This prints localized text for "Hello, World!" for the default locale
 * System.out.println(localizer.localizedString()); 
 *
 * // This prints localized text for "Duke" for the default locale
 * localizer.key = "Duke";
 * System.out.println(localizer.localizedString()); 
 *
 * // This prints localized text for "Duke" for the French locale
 * localizer.locale = Locale.FRENCH;
 * System.out.println(localizer.localizedString()); 
 *
 * // This prints localized text for "Duke", from 
 * // the FX properties file "foo/bar/MyResources_fr.fxproperties
 * localizer.propertiesName = "foo.bar.MyResources";
 * System.out.println(localizer.localizedString()); 
 * </code>
 */
public class StringLocalizer {

    /**
     * The string that represents the 'key' in the JavaFX properties file
     */
    public attribute key: String;

    /**
     * The locale used for determining the JavaFX properties resource
     * bundle.  If it is not explicitly specified, Locale.getDefault()
     * is used.
     */
    public attribute locale: Locale = Locale.getDefault();

    /**
     * The canonical base name of the JavaFX properties file.  It consists 
     * of the package name and the base bundle name of the JavaFX properties 
     * file.  For example, the canonical base name of 'MyResources_xx.fxproperties', 
     * where 'xx' denotes the target locale, in 'foo.bar' package is 
     * 'foo.bar.MyResources'.  If this attribute is not explicitly specified,
     * it is synthesized from the caller's package and script file name, e.g.,
     * a JavaFX Script 'Example.fx' is in 'foo.bar' package, the synthesized
     * canonical base name will be 'foo.bar.Example'.
     */
    public attribute propertiesName: String = getDefaultPropertiesName();

    /**
     * The default string returned from localizedString() method.
     */
    public attribute defaultString : String;
   
    /**
     * This function returns the localized string for the given attributes.
     * The return value is bound to each of the attributes.  If there
     * is no appropriate localized string found in JavaFX properties files,
     * this function returns 'defaultString' if it is specified, otherwise
     * 'key' is returned.
     */
    public bound function localizedString() : String {
        localizedStr;
    }

    /**
     * A static function to associate a JavaFX Script source file, or a JavaFX
     * package to a JavaFX properties file.  If an association is made, further
     * localizations in the specified source file, or source files in the 
     * specified package will be searched within the specified JavaFX properties
     * file.
     *
     * 'packageName' denotes the JavaFX package, e.g., 'foo.bar', and 'scriptFileName
     * is the file name source script file, e.g., 'Example.fx'.  If the 
     * 'scriptFileName' is null, all the JavaFX scripts in the package designated by 
     * 'packageName' are associated with the specified JavaFX properties file.
     * 'properties' denotes the canonical base name of the JavaFX properties file, e.g.,
     * 'foo.bar.MyResources'.
     */
    public static function associate(packageName: String,
                                scriptFileName: String, properties: String) : Void {
        var source: String = 
            if (scriptFileName == null) packageName 
            else packageName + "." + scriptFileName.replaceAll("\\.[fF][xX]$", "");
        StringLocalization.associate(source, properties);
    }

    private attribute localizedStr : String = bind {
        if (defaultString <> null) {
            StringLocalization.getLocalizedString(propertiesName, key, defaultString, locale);
        } else {
            StringLocalization.getLocalizedString(propertiesName, key, key, locale);
        }
    }

    private function getDefaultPropertiesName() : String {
        var elements  = Thread.currentThread().getStackTrace();
        var elem: StackTraceElement;
        var className: String;
        var foundMe: Boolean = false;

//      JFXC-555
//      for (elem in elements) {
        for (i in [0..<sizeof elements]) {
            elem = elements[i];
            className = elem.getClassName();

            if (className.startsWith("javafx.util.StringLocalizer")) {
                foundMe = true;
            } else if (foundMe) {
                // this should be the caller's stack
                break;
            }
        }

        var pkgName = className.replaceAll("\\.?[^\\.]+$", "");
        StringLocalization.getPropertiesName(
            (if (pkgName <> "") pkgName + "." else "") +
            elem.getFileName().replaceAll("\\.[fF][xX]$", ""));
    }
}
