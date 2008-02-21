
import javafx.ui.*;
import jfx.assortie.system.*;

import java.lang.System;

/**
 * @author Alexandr Scherbatiy
 */


Frame{
    title: "JavaFX Assortie"
    width:  800
    height: 600
    onClose: function() { System.exit(0); }
    content: AssortieProject{ rootModule: "jfx.assortie.Configuration" }
    visible: true
}
