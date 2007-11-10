/*
 * UIContext.java
 * 
 * Created on Oct 28, 2007, 4:16:36 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.javafx.api.ui;

import javax.swing.JPanel;

/**
 *
 * @author jclarke
 */
public interface UIContext {

    public JPanel createPanel();
    
    public XButton createButton();


}
