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

package javafx.ext.swing;

import javax.swing.JPanel;
import java.awt.GridLayout;

// PENDING_DOC_REVIEW
/**
 * Creates a panel applying the grid layout that 
 * lays out components in a rectangular grid. 
 * The panel is divided into equal-sized rectangles, 
 * and one component is placed in each rectangle.
 */
public class GridPanel extends SwingPanel {

    // columns needs to appear before (and therefore be initialized before) rows.
    // The GridLayout is created with rows=1, cols=0. GridLayout does not support
    // rows and cols both being 0. So in order to support {rows: 0 cols: n} we
    // need columns to be set to n before rows is set to 0.

   // PENDING_DOC_REVIEW
   /**
    * This is the number of columns specified for the grid.  The number
    * of columns can be changed at any time.
    * This value should be a non negative integer, where {@code 0} means
    * 'any number' meaning that the number of Columns in that
    * dimension depends on the other dimension. 
    */
    public attribute columns: Integer = getGridLayout().getColumns() on replace {
        getGridLayout().setColumns(columns);
        getJPanel().revalidate();
        getJPanel().repaint();
    }

   // PENDING_DOC_REVIEW
   /**
    * This is the number of rows specified for the grid.  The number
    * of rows can be changed at any time.
    * This value should be a non negative integer, where {@code 0} means
    * 'any number' meaning that the number of Rows in that
    * dimension depends on the other dimension.
    */
    public attribute rows: Integer = getGridLayout().getRows() on replace {
        getGridLayout().setRows(rows);
        getJPanel().revalidate();
        getJPanel().repaint();
    };

   // PENDING_DOC_REVIEW
   /**
    * This is the horizontal gap (in pixels) which specifies the space
    * between columns.  They can be changed at any time.
    * This value should be a non-negative integer. 
    */
    public attribute hgap: Integer = getGridLayout().getHgap() on replace {
        getGridLayout().setHgap(hgap);
        getJPanel().revalidate();
        getJPanel().repaint();
    }

   // PENDING_DOC_REVIEW
   /**
    * This is the vertical gap (in pixels) which specifies the space
    * between rows.  They can be changed at any time.
    * This value should be a non negative integer. 
    */
    public attribute vgap: Integer = getGridLayout().getVgap() on replace {
        getGridLayout().setVgap(vgap);
        getJPanel().revalidate();
        getJPanel().repaint();
    }

    protected /* final */ function configureJPanel(jPanel: JPanel): Void {
        jPanel.setLayout(new GridLayout());
    }

    private function getGridLayout(): GridLayout {
        return getJPanel().getLayout() as GridLayout;
    }

}
