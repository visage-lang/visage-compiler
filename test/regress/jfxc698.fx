/* 
 * test for the generic localization functionality with FX properties files
 *
 * @test
 * @run
 */

import java.lang.System;
import java.util.*;

import javafx.util.StringLocalizer;

// save the default locale for testing
var curLoc = Locale.getDefault();

try {
    // set the default locale to Japan
    Locale.setDefault(Locale.JAPAN);

    // create localizer object literal with defaults
    var localizer = StringLocalizer { key: "source original string" };

    // This prints translated string in jfxc698_ja.fxproperties
    System.out.println(localizer.localizedString);

    // This prints translated string in foo/bar/FXPropTestResources_ja.fxproperties
    StringLocalizer.associate("foo.bar.FXPropTestResources", "", "jfxc698.fx");
    localizer = StringLocalizer { key: "source original string" };
    System.out.println(localizer.localizedString);

    // create another localizer object literal with defaults
    StringLocalizer.dissociate("", "jfxc698.fx");
    var localizer2 = StringLocalizer { key: "EXPLICIT_KEY", defaultString: "default string" };

    // This prints translated string in jfxc698_ja.fxproperties
    System.out.println(localizer2.localizedString);

    // This prints the 'default string'
    localizer2.key = "EXPLICIT_KEY2";
    System.out.println(localizer2.localizedString);

} finally {
    // restore the default locale
    Locale.setDefault(curLoc);
}
