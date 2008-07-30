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
 * The {@code FontStyle} enum represents the style (plain, bold or italic) of a {@link Font} object.
 *
 * @profile common
 * @needsreview
 */      
public enum FontStyle {

    // PENDING_DOC_REVIEW
    /**
     * The plain style constant.
     *
     * @profile common
     */      
    PLAIN(java.awt.Font.PLAIN),

    // PENDING_DOC_REVIEW
    /**
     * The bold style constant.
     *
     * @profile common
     */      
    BOLD(java.awt.Font.BOLD),
    
    // PENDING_DOC_REVIEW
    /**
     * The italicized style constant.
     *
     * @profile common
     */      
    ITALIC(java.awt.Font.ITALIC),

    // PENDING_DOC_REVIEW
    /**
     * The bold italicized style constant.
     *
     * @profile common
     */      
    BOLD_ITALIC(java.awt.Font.BOLD + java.awt.Font.ITALIC);

    final int toolkitValue;

    private FontStyle(int toolkitValue) {
        this.toolkitValue = toolkitValue;
    }

    static FontStyle fromToolkitValue(int toolkitValue) {
        if (toolkitValue == PLAIN.toolkitValue) {
            return PLAIN;
        } else if (toolkitValue == BOLD.toolkitValue) {
            return BOLD;
        } else if (toolkitValue == ITALIC.toolkitValue) {
            return ITALIC;
        } else {
            return BOLD_ITALIC;
        }
    }

}
