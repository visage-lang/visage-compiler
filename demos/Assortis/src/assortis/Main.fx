package assortis;

import javafx.gui.*;
import assortis.core.*;

import java.lang.System;

/**
 * @author Alexandr Scherbatiy, Naoto Sato
 */

javax.swing.UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceNebulaLookAndFeel");
 
Frame{
    title: "JavaFX Assortis"
    width:  800
    height: 600
    closeAction: function() { System.exit(0); }
    content: AssortisProject{ rootModule: "assortis.Configuration" }
    visible: true
}

