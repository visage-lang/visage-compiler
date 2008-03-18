/* regression test for JFXC-933
 *
 * @test
 * @run
 */

import java.lang.System;
import java.util.*;

import javafx.util.StringLocalizer;

// save the default locale for testing
var curLoc = Locale.getDefault();

// set the default locale to Japan
Locale.setDefault(Locale.JAPAN);

// Japan locale tests
var ja = new StringLocalizer();
System.out.println(ja.localizedString());
ja = StringLocalizer{ };
System.out.println(ja.localizedString());
ja = StringLocalizer{ defaultString: "defaultString" };
System.out.println(ja.localizedString());
ja = StringLocalizer{ key: "EXISTENT" defaultString: "defaultString" };
System.out.println(ja.localizedString());
ja = StringLocalizer{ key: "NON_EXISTENT" defaultString: "defaultString" };
System.out.println(ja.localizedString());


// English locale tests
var en = new StringLocalizer();
en.locale = Locale.ENGLISH;
System.out.println(en.localizedString());
en = StringLocalizer{ locale: Locale.ENGLISH };
System.out.println(en.localizedString());
en = StringLocalizer{ locale: Locale.ENGLISH 
                      defaultString: "defaultString" };
System.out.println(en.localizedString());
en = StringLocalizer{ locale: Locale.ENGLISH
                      key: "NON_EXISTENT" 
                      defaultString: "defaultString" };
System.out.println(en.localizedString());


// restore the default locale
Locale.setDefault(curLoc);
