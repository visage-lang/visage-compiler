/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */ 

package javafx.ui;
import javafx.ui.Widget;
import javafx.ui.ActionWidget;
import javafx.ui.KeyStroke;
import javafx.ui.Icon;
import javafx.ui.HorizontalAlignment;
import javafx.ui.VerticalAlignment;
import javafx.ui.Insets;
import javax.swing.JButton;
/**
 * An implementation of a "push" button. Encapsulates javax.swing.JButton.
 */
public class Button extends Widget, ActionWidget {
    private attribute button: JButton = bind new JButton();

    /** Determines whether this is the default button within a dialog. */
    public attribute defaultButton: Boolean on replace {
        //button.setDefault(defaultButton);        
    };

    /** Determines whether this is the default cancel button within a dialog. */
    public attribute defaultCancelButton: Boolean on replace {
	//        button.setDefaultClose(defaultCancelButton);
    };

    /** Sets this button's text. */
    public attribute text: String on replace {
         button.setText(text);
    };

    /** Sets this button's mnemonic. */
    public attribute mnemonic: KeyStroke on replace {
	if (mnemonic <> null) {
	    button.setMnemonic(mnemonic.id);
	}
    };

    /** Sets this button's default icon. */
    public attribute icon: Icon on replace {
        var jicon:javax.swing.Icon = null;
        if (icon <> null) {
            jicon = icon.getIcon();
        }
        button.setIcon(jicon);
        if (selectedIcon == null) {
            button.setSelectedIcon(jicon);
        }   
        if (icon <> null) {
            button.setIcon(icon.getIcon());
            if(selectedIcon == null) {
                button.setSelectedIcon(icon.getIcon());
            }
        }else {
            button.setIcon(null);
        }
        
    };

    /** Sets this button's selected icon. */
    public attribute selectedIcon: Icon on replace {
        var jicon:javax.swing.Icon = null;
        if (selectedIcon <> null) {
            jicon = selectedIcon.getIcon();
        }
        button.setSelectedIcon(jicon);        
    };

    /** Sets this button's pressed icon. */
    public attribute pressedIcon: Icon on replace {
        var jicon:javax.swing.Icon = null;
        if (pressedIcon <> null) {
            jicon = pressedIcon.getIcon();
        }
        button.setPressedIcon(jicon);        
    };

    /** Sets this button's rollover icon. */
    public attribute rolloverIcon: Icon on replace {
        var jicon:javax.swing.Icon = null;
        if (rolloverIcon <> null) {
            jicon = rolloverIcon.getIcon();
        }
        button.setRolloverIcon(jicon);        
    };

    /** Sets this button's selected rollover icon. */
    public attribute rolloverSelectedIcon: Icon on replace {
        var jicon:javax.swing.Icon = null;
        if (rolloverSelectedIcon <> null) {
            jicon = rolloverSelectedIcon.getIcon();
        }
        button.setRolloverSelectedIcon(jicon);        
    };

    /** Sets whether rollover effects are enabled for this button. Defaults to true. */
    public attribute rolloverEnabled: Boolean = true on replace {
        button.setRolloverEnabled(rolloverEnabled);
    }

    /** Sets this button's disabled icon. */
    public attribute disabledIcon: Icon on replace {
        var jicon:javax.swing.Icon = null;
        if (disabledIcon <> null) {
            jicon = disabledIcon.getIcon();
        }
        button.setDisabledIcon(jicon);
        if (disabledIcon <> null) {
            button.setDisabledIcon(disabledIcon.getIcon());
        }else {
            button.setDisabledIcon(null);
        }
    };

    /** Sets this button's disabled selected icon. */
    public attribute disabledSelectedIcon: Icon on replace {
        var jicon:javax.swing.Icon = null;
        if (disabledSelectedIcon <> null) {
            jicon = disabledSelectedIcon.getIcon();
        }
        button.setDisabledSelectedIcon(jicon);        
    };

    /**
     * Sets the amount of space between the text and the icon
     * displayed in this button.
     */
    public attribute iconTextGap: Number = -1 on replace {
	if (iconTextGap >= 0) {
	    button.setIconTextGap(iconTextGap.intValue());        
	}
    };

    /**
     * Sets whether this button will paint the content
     * area.  If you wish to have a transparent button, such as
     * an icon-only button, for example, then you should set
     * this to <code>false</code>. Defaults to <code>true</code>.
     */
    public attribute contentAreaFilled: Boolean = true on replace {
        button.setContentAreaFilled(contentAreaFilled);
        button.revalidate();
    };

    /**
     * Sets whether the focus state is painted for this button.
     * The default value for the <code>focusPainted</code> attribute
     * is <code>true</code>.
     * Some look and feels might not paint focus state;
     * they will ignore this property.
     */
    public attribute focusPainted:Boolean = true on replace {
        button.setFocusPainted(focusPainted);
        button.revalidate();
    };
    
    /**
     * Sets whether (if this button has a border) the border is painted.
     * Defaults to true.
     */
    public attribute borderPainted:Boolean = true on replace {
        button.setBorderPainted(borderPainted);
        button.revalidate();
    };

    /**
     * Sets space for margin between this button's border and
     * the label. Setting to <code>null</code> will cause this button to
     * use the default margin.  The button's default <code>Border</code>
     * object will use this value to create the proper margin.
     * However, if a non-default border is set on the button, 
     * it is that <code>Border</code> object's responsibility to create the
     * appropriate margin space (else this property will
     * effectively be ignored).
     */
    public attribute margin:Insets on replace {
	if (margin <> null) {
	    button.setMargin(margin.awtinsets);
	    button.revalidate();        
	}
    };

    /**
     * Sets the horizontal position of the text relative to the icon.<br></br>
     * One of the following values:
     * <ul>
     * <li>CENTER
     * <li>LEADING
     * <li>TRAILING (the default)
     * </ul>
     */
    public attribute horizontalTextPosition: HorizontalAlignment on replace {
	if (horizontalTextPosition <> null) {
	    button.setHorizontalTextPosition(horizontalTextPosition.id.intValue());
	    button.revalidate();
	}
    };

    /**
     * Sets the vertical position of the text relative to the icon.<br></br>
     * One of the following values:
     * <ul>
     * <li>CENTER (the default)
     * <li>TOP
     * <li>BOTTOM
     * </ul>
     */
    public attribute verticalTextPosition: VerticalAlignment on replace {
	if (verticalTextPosition <> null) {
	    button.setVerticalTextPosition(verticalTextPosition.id.intValue());
	    button.revalidate();
	}
    };

    /**
     * Sets the horizontal alignment of the the icon and text.<br></br>
     * One of the following values:
     * <ul>
     * <li>CENTER
     * <li>LEADING
     * <li>TRAILING (the default)
     * </ul>
     */
    public attribute horizontalAlignment: HorizontalAlignment on replace {
	if (horizontalAlignment <> null) {
	    button.setHorizontalAlignment(horizontalAlignment.id.intValue());
	    button.revalidate();
	}
    };

    /**
     * Sets the vertical alignment of the icon and text.<br></br>

     * One of the following values:
     * <ul>
     * <li>CENTER (the default)
     * <li>TOP
     * <li>BOTTOM
     * </ul>
     */
    public attribute verticalAlignment: VerticalAlignment on replace {
	if (verticalAlignment <> null) {
	    button.setVerticalAlignment(verticalAlignment.id.intValue());
	    button.revalidate();
	}
    };
    
    
    public function createComponent():javax.swing.JComponent { 
        return button;
    } 
    
    init {
        button.addActionListener(java.awt.event.ActionListener {
                         public function actionPerformed(e:java.awt.event.ActionEvent) {
                            //TODO JXFC-211
                            /********
                            if(action <> null) {
                                 action();
                            }
                            ***********/
                         }
                     });        
    }
}





