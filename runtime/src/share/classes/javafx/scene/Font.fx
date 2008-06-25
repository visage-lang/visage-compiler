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

package javafx.scene;

import javax.swing.plaf.UIResource;

// PENDING_DOC_REVIEW_2
/**
 * <p>The {@code Font} class represents fonts, which are used to
 * render text on screen. A {@code Font} includes the name of the
 * font (such as 'Helvetica' or 'Arial'), the style of
 * the font (bold, plain or italic), and the size of the font
 * in points (10pt, 12pt, etc.)</p>
 * 
 * <p>A font provides the information needed to map sequences of
 * <em>characters</em> to sequences of <em>glyphs</em>
 * and to render sequences of glyphs on screen.</p>
 *
 * @profile common
 * @needsreview
 */      
public /* final */ class Font {

    // PENDING_DOC_REVIEW_2
    /**
     * <p>The name of this {@code Font}. It can be a logical name
     * like 'serif' or 'sanserif' or an actual font family name 
     * like 'Helvetica' or 'Times New Roman'.  If the named
     * font cannot be found then the default font will
     * be used. <b class='editor'>Is this correct? What is the default
     * font? sanserif?</b></p>
     *
     * @profile common
     * @needsreview
     * @setonce
     */          
    public /* set-once */ attribute name: String = "Default";

    // PENDING_DOC_REVIEW_2
    /**
     * <p>The style of this {@code Font}.  The style can be
     * {@link FontStyle#PLAIN}, {@link FontStyle#BOLD},
     * {@link FontStyle#ITALIC}, or {@link FontStyle#BOLD_ITALIC}.</p>
     *
     * @profile common
     * @needsreview
     * @setonce
     */          
    public /* set-once */ attribute style: FontStyle = FontStyle.PLAIN;

    // PENDING_DOC_REVIEW_2
    /**
     * <p>The point size of this {@code Font}.</p>
     *
     * @profile common
     * @needsreview
     * @setonce
     */          
    public /* set-once */ attribute size: Integer = 12;

    private /* set-once */ attribute awtFont: java.awt.Font
                                 = new java.awt.Font(name, style.getToolkitValue(), size);

    public function getAWTFont() : java.awt.Font {
        awtFont;
    }

    /**
     * Creates a new {@code Font} from a corresponding AWT Font.
     * @needsreview
     */
    public static function fromAWTFont(f: java.awt.Font): Font {
        var name = f.getName();
        var style = FontStyle.fromToolkitValue(f.getStyle());
        var size = f.getSize();
        // PENDING(shannonh) - the commented code below is good, but it requires
        // us to fix the sync pattern of colors/fonts/etc, which requires
        // compiler support.
        // if (f instanceof UIResource) {
        //     font(name, style, size);
        // } else {
            Font {name: name, style: style, size: size, awtFont: f};
        // }
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates a new {@code Font} with the specified parameters.
     *
     * @param name the logical name for the new {@code Font}.
     * @param style the style for the new {@code Font}
     * @param size the size for the new {@code Font}
     *
     * @profile common
     */          
    public static function font(name: String, style: FontStyle, size: Integer): Font {
        Font {name: name, style: style, size: size};
    }

    // PENDING_DOC_REVIEW
    /**
     * Converts this {@code Font} object to a {@code String} representation.
     *
     * @profile common
     */          
    public function toString(): String {
        "{getClass().getName()}[name={name},style={style},size={size}]";
    }

}
