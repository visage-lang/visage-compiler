/* JFXC-221:  compilation fails with the message 
 * "action has private access in javafx.ui.Button", even though
 * ActionWidget.action is public.
 * Still disabled... No access to the RTL classes in th etest framework. the Button class can't be found.
 */

import java.lang.System;
import javafx.ui.Button;

var btn = Button {
     action: function() {
        System.out.println("okay");
    }
}
