/*
 * Assortis.fx
 *
 * Created on Jun 10, 2008, 11:13:55 AM
 */

package assortis.core;

/**
 * @author Alexandr Scherbatiy, Naoto Sato
 */


import javafx.ext.swing.SwingApplication;
import java.lang.System;


//ProjectManager.setLookAndFeel();

SwingApplication{
    content: AssortisProject{ 
        editable: false
        rootModule: "assortis.Configuration"
    }
}

