/* test for the string literal translation
 * 
 * this is to test the "@charset" tag in the .fxproperties file.
 *
 * @test
 * @run
 */

import java.lang.System;
import java.util.*;

// save the default locale for testing
var curLoc = Locale.getDefault();

try {
    // set the default locale to Japan
    Locale.setDefault(Locale.JAPAN);

    System.out.println(##'\"@charset\" tag in .fxproperties test');

} finally {
    // restore the default locale
    Locale.setDefault(curLoc);
}
