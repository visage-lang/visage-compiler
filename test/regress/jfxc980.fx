/* regression test for JFXC-980
 *
 * @test
 * @run
 */

import java.lang.System;
import java.util.*;

import javafx.util.StringLocalizer;

StringLocalizer.associate("jfxc980Resources", "", "jfxc980.fx");
var a = StringLocalizer{ key: "hello" locale: Locale.US };
System.out.println(a.localizedString());

StringLocalizer.associate("resources/jfxc980", "", "jfxc980.fx");
var b = StringLocalizer{ key: "hello" locale: Locale.US };
System.out.println(b.localizedString());

StringLocalizer.dissociate("", "jfxc980.fx");
var c = StringLocalizer{ key: "hello" locale: Locale.US };
System.out.println(c.localizedString());
