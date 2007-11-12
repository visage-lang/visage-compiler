/* JFXC-221:  compilation fails with the message 
 * "action has private access in javafx.ui.Button", even though
 * ActionWidget.action is public.
 */

import java.lang.System;
import javafx.ui.Button;

var btn = Button {
     action: function() {
        System.out.println("okay");
    }
}
