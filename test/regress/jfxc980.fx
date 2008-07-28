/* regression test for JFXC-980
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
    // set the default locale to US
    Locale.setDefault(Locale.US);

    StringLocalizer.associate("jfxc980Resources", "", "jfxc980.fx");
    var a = StringLocalizer{ key: "hello" };
    System.out.println(a.localizedString);

    StringLocalizer.associate("resources/jfxc980", "", "jfxc980.fx");
    var b = StringLocalizer{ key: "hello" };
    System.out.println(b.localizedString);

    StringLocalizer.dissociate("", "jfxc980.fx");
    var c = StringLocalizer{ key: "hello" };
    System.out.println(c.localizedString);

} finally {
    // restore the default locale
    Locale.setDefault(curLoc);
}
