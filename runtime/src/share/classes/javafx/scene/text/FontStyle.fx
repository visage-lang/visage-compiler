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

package javafx.scene.text;

// PENDING_DOC_REVIEW
/**
 * The {@code FontStyle} class represents the style (plain, bold or italic) of a {@link Font} object.
 *
 * @profile common
 * @needsreview
 */      
public /* final */ class FontStyle {

    private /* set-once */ attribute toolkitValue: Integer = 0;

    private /* set-once */ attribute name: String = "PLAIN";

    // PENDING_DOC_REVIEW
    /**
     * The plain style constant.
     *
     * @profile common
     */      
    public static /* constant */ attribute PLAIN: FontStyle = FontStyle {};

    // PENDING_DOC_REVIEW
    /**
     * The bold style constant.
     *
     * @profile common
     */      
    public static /* constant */ attribute BOLD: FontStyle = FontStyle {toolkitValue: 1, name: "BOLD"};
    
    // PENDING_DOC_REVIEW
    /**
     * The italicized style constant.
     *
     * @profile common
     */      
    public static /* constant */ attribute ITALIC: FontStyle = FontStyle {toolkitValue: 2, name: "ITALIC"};

    // PENDING_DOC_REVIEW
    /**
     * The bold italicized style constant.
     *
     * @profile common
     */      
    public static /* constant */ attribute BOLD_ITALIC: FontStyle = FontStyle {toolkitValue: 3, name: "BOLD_ITALIC"};

    // PENDING_DOC_REVIEW
    /**
     * Converts this {@code FontStyle} object to a {@code String} representation.
     *
     * @profile common
     */      
    override function toString(): String {
        name;
    }

    function getToolkitValue(): Integer {
        toolkitValue;
    }

    static function fromToolkitValue(toolkitValue: Integer): FontStyle {
        if (toolkitValue == PLAIN.toolkitValue) PLAIN
        else if (toolkitValue == BOLD.toolkitValue) BOLD
        else if (toolkitValue == ITALIC.toolkitValue) ITALIC
        else BOLD_ITALIC;
    }

}
