/* regression test for JFXC-1179
 *
 * @test
 * @run
 */

import java.util.Locale;
import javafx.util.StringLocalizer;

var locale = Locale.getDefault();
try {
    Locale.setDefault(Locale.US);
    var a = StringLocalizer { key: "NOTFOUND" };
} finally {
    Locale.setDefault(locale);
}
