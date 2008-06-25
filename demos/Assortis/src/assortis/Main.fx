package assortis;

import javafx.ext.swing.Frame;
import assortis.core.*;

import java.lang.System;

/**
 * @author Alexandr Scherbatiy, Naoto Sato
 */


assortis.core.ProjectManager.setLookAndFeel();

Frame{
    title: "JavaFX Assortis"
    width:  800
    height: 600
    closeAction: function() { System.exit(0); }
    content: AssortisProject{ rootModule: "assortis.Configuration" }
    visible: true
}

