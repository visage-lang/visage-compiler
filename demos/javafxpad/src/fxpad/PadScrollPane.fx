/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package fxpad;

import java.lang.System;
import javafx.ext.swing.SwingScrollPane;
import javafx.ext.swing.Component;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * @author jclarke
 */

public class PadScrollPane extends SwingScrollPane {
    
    /**
     * Adds a child that will appear as a row at the top or bottom
     * of the main view of the scrollpane depending on the current component orientation.
     */
    public attribute rowHeader:Component on replace  {
       
        if(rowHeader != null) {
            var jcomp = rowHeader.getJComponent();
            if(jcomp instanceof JScrollPane) {
                getJScrollPane().setRowHeader((jcomp as JScrollPane).getViewport());
            }else if(jcomp instanceof JViewport) {
                getJScrollPane().setRowHeader(jcomp as JViewport);
            }else {
                getJScrollPane().setRowHeaderView(jcomp);
            }
        }else {
            getJScrollPane().setRowHeaderView(null);
        }
    };

    /**
     * Adds a child that will appear as a column at the left or right
     * of the main view of the scrollpane depending on the current component orientation.
     */
    public attribute columnHeader:Component on replace  {
        if(columnHeader != null) {
            var jcomp = columnHeader.getJComponent();
            if(jcomp instanceof JScrollPane) {
                getJScrollPane().setColumnHeader((jcomp as JScrollPane).getViewport());
            }else if(columnHeader instanceof JViewport) {
                getJScrollPane().setColumnHeader(jcomp as JViewport);
            }else {
                getJScrollPane().setColumnHeaderView(jcomp);
            }
        }else { // is null
            getJScrollPane().setColumnHeaderView(null);
        }
    }
    
}