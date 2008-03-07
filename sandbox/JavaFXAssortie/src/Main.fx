
import javafx.ui.*;
import jfx.assortie.system.*;

import java.lang.System;

/**
 * @author Alexandr Scherbatiy
 */

javax.swing.UIManager.setLookAndFeel(ProjectManager.getDefaultLookAndFeel());

Frame{
    title: "JavaFX Assortie"
    width:  800
    height: 600
    onClose: function() { System.exit(0); }
    content: AssortieProject{ rootModule: "jfx.assortie.Configuration" }
    //lookAndFeel: ProjectManager.getDefaultLookAndFeel()

    visible: true
}
