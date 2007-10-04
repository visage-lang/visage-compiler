/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;

/**
 * A small fixed size picture, typically used to decorate components.
 * Encapsulates javax.swing.Icon.
 *
 */
public abstract class Icon {
    protected attribute icon: javax.swing.Icon;
    
    public abstract function getImage(): java.awt.Image ;
    public function getIcon(): javax.swing.Icon {
        if (icon == null) then {
            icon = this.createIcon();
        };
        return icon;
    }
    protected abstract function createIcon(): javax.swing.Icon;

}
