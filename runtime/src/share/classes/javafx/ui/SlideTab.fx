/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.
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


import javax.swing.JViewport;
import com.sun.javafx.api.ui.ScrollablePanel;

public class SlideTab {
    // private
    attribute scrollable: ScrollablePanel;
    attribute viewport: JViewport;
    attribute size: Number;
    attribute slider: TabSlider;


    // public
    public attribute enabled: Boolean = true on replace {
        if (button instanceof Button) {
            button.enabled = enabled;
        }
    };
    public attribute button: Widget;
    public attribute title: String on replace {
        if (button instanceof Button) {
            (button as Button).text = title;
        }
    };
    public attribute toolTipText: String on replace {
        if (button instanceof Button) {
            (button as Button).toolTipText = toolTipText;
        }        
    };
    public attribute icon: Icon on replace {
        if (button instanceof Button) {
            (button as Button).icon = icon;
        } 
    };
    public attribute content: Widget;
    public attribute selected: Boolean on replace {
        if(slider <> null) {
            slider.updateTabSelection(this);
        }
    };
    public attribute opaque: Boolean = true on replace {
        if (button instanceof Button) {
            button.opaque = opaque;
        }
    };
}


