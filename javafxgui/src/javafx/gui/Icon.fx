/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
package javafx.gui;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.net.URL;

// PENDING_DOC_REVIEW
/**
 * A small fixed size picture, typically used to decorate components.
 */
public class Icon {

    private attribute toolkitIcon:javax.swing.Icon;

    // PENDING(shannonh) - don't like the tie-in to Swing

    public function getToolkitIcon():javax.swing.Icon { toolkitIcon }

    public static function fromToolkitIcon(value:javax.swing.Icon):Icon { 
        Icon { toolkitIcon: value } 
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the {@link Image} to be painted by this {@code Icon}. 
     */
    public attribute image:Image = null on replace {
        toolkitIcon = if (image <> null) new ImageIcon(image.getBufferedImage()) else null;
    }
    
    function getImage(): java.awt.Image  {
        if (toolkitIcon == null) null
        else if (toolkitIcon instanceof ImageIcon) (toolkitIcon as ImageIcon).getImage()
        else {
            // TBD render the toolkitIcon to an image, return that
            null
        }
    }

}
