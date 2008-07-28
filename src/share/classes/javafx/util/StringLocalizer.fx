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
 * <code><pre>
 * // Object creation
 * var localizer = StringLocalizer{ key: "Hello, World!" };
 *
 * // This prints localized text for "Hello, World!" for the default locale
 * System.out.println(localizer.localizedString); 
 *
 * // This prints localized text for "Duke" for the default locale
 * localizer.key = "Duke";
 * System.out.println(localizer.localizedString); 
 * </pre></code>
 *
 * By default, translations are retrieved from the JavaFX properties file
 * which has the same package and file name as the caller's script file.  For
 * example, a call from the script <code>foo.bar.Example</code> will search
 * translated strings from <code>foo/bar/Example_xx.fxproperties</code> where 'xx'
 * represents the locale. By calling <code>associate()/dissociate()</code> functions,
 * different JavaFX properties files can be used for the translation.  For example,
 * <pre><code>
 * StringLocalizer.associate("foo.bar.resources.MyResources", "foo.bar");
 * </code></pre>
 * After this call, all the translations from JavaFX script files in 
 * <code>foo.bar</code> package are searched in 
 * <code>foo/bar/resources/MyResources_xx.fxproperties</code>, where 'xx' denotes
 * the default locale.
 *
 * @needsreview
 */

public class StringLocalizer {

    /**
     * The string that represents the 'key' in the JavaFX properties file
     *
     * @needsreview
     */
    public attribute key: String;

    /**
     * The locale used for determining the JavaFX properties resource
     * bundle.  If it is not explicitly specified, Locale.getDefault()
     * is used.  
     * Note: Made this private for now, as there is no Locale class in some
     * platforms (e.g., CLDC/MIDP)
     */
    private attribute locale: Locale = Locale.getDefault();

    /**
     * The canonical base name of the JavaFX properties file.  It consists 
     * of the package name and the base bundle name of the JavaFX properties 
     * file.  For example, the canonical base name of 'MyResources_xx.fxproperties', 
     * where 'xx' denotes the locale, in 'foo.bar' package is 
     * 'foo.bar.MyResources'.  If this attribute is not explicitly specified,
     * it is synthesized from the caller's package and script file name, e.g.,
     * a JavaFX Script 'Example.fx' is in 'foo.bar' package, the synthesized
     * canonical base name will be 'foo.bar.Example'.
     *
     * @needsreview
     * @defaultvalue the caller's script file name
     */
    private attribute propertiesName: String = getDefaultPropertiesName();

    /**
     * The default string for the <code>localizedString</code>.
     *
     * @needsreview
     */
    public attribute defaultString : String;
   
    /**
     * The localized string for the given attributes.  If there
     * is no appropriate localized string found in JavaFX properties files,
     * this attribute will have the value in <code>defaultString</code> if 
     * it is specified, otherwise it will have the value in <code>key</code>.
     *
     * @needsreview
     * @readonly
     */
    public attribute localizedString : String = bind {
        if ("".equals(defaultString)) {
            StringLocalization.getLocalizedString(propertiesName, key, key, locale);
        } else {
            StringLocalization.getLocalizedString(propertiesName, key, defaultString, locale);
        }
    }

    private function getDefaultPropertiesName() : String {
        var elements  = Thread.currentThread().getStackTrace();
        var elem: StackTraceElement;
        var className: String;
        var foundMe: Boolean = false;

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
        var fileName = elem.getFileName().replaceAll("\\.[fF][xX]$", "");
        StringLocalization.getPropertiesName("{pkgName}/{fileName}");
    }
}


/**
 * Associates a JavaFX
 * package with a JavaFX properties file.  If an association is made, further
 * localizations in the source script files in the 
 * specified package will be searched within the specified JavaFX properties
 * file.
 *
 * @needsreview
 * @param properties denotes the canonical base name of the JavaFX properties 
 *     file, e.g., 'foo.bar.MyResources'.
 * @param packageName denotes the JavaFX package name, e.g., 'foo.bar'.
 */
public function associate(properties: String, packageName: String) : Void {
    associate(properties, packageName, "");
}

/**
 * Associates a JavaFX Script source file
 * with a JavaFX properties file.  If an association is made, further
 * localizations in the specified source script file
 * will be searched within the specified JavaFX properties
 * file.
 *
 * @needsreview
 * @param properties denotes the canonical base name of the JavaFX properties 
 *     file, e.g., 'foo.bar.MyResources'.
 * @param packageName denotes the JavaFX package name of the source script file, 
 *     e.g., 'foo.bar'.
 * @param scriptFileName the file name of the source script file, e.g., 'Example.fx'.
 */
public function associate(properties: String, packageName: String,
                            scriptFileName: String) : Void {
    var source: String = if (scriptFileName == "") {
            packageName;
        } else {
            "{packageName}/{scriptFileName.replaceAll("\\.[fF][xX]$", "")}";
        };
    StringLocalization.associate(source, properties);
}

/**
 * Dissociates a JavaFX
 * package from any JavaFX properties file.
 *
 * @needsreview
 * @param packageName denotes the JavaFX package name, e.g., 'foo.bar'.
 */
public function dissociate(packageName: String) : Void {
    dissociate(packageName, "");
}

/**
 * Dissociates a JavaFX Script source file
 * from any JavaFX properties file.
 *
 * @needsreview
 * @param packageName denotes the JavaFX package name, e.g., 'foo.bar'.
 * @param scriptFileName the file name of the source script file, e.g., 'Example.fx'.
 */
public function dissociate(packageName: String,
                            scriptFileName: String) : Void {
    var source: String = if (scriptFileName == "") {
            packageName;
        } else {
            "{packageName}/{scriptFileName.replaceAll("\\.[fF][xX]$", "")}";
        };
    StringLocalization.dissociate(source);
}
