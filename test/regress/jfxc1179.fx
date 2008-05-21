/* regression test for JFXC-1179
 *
 * @test
 * @run
 */

import java.util.Locale;
import javafx.util.StringLocalizer;

Locale.setDefault(Locale.US);
var a = StringLocalizer { key: "NOTFOUND" };
