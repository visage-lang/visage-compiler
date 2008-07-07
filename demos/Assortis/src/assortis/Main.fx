package assortis;

import javafx.ext.swing.SwingFrame;
import assortis.core.*;

import java.lang.System;

/**
 * @author Alexandr Scherbatiy, Naoto Sato
 */


assortis.core.ProjectManager.setLookAndFeel();

SwingFrame{
    title: "JavaFX Assortis"
    width:  800
    height: 600
    closeAction: function() { System.exit(0); }
    content: AssortisProject{ rootModule: "assortis.Configuration" }
    visible: true
}

