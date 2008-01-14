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

import com.sun.javafx.api.ui.JBookPanel;
import com.sun.javafx.api.ui.JBookPanel.BookPanelViewModel;
import java.awt.Rectangle;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JComponent;
import java.lang.System;

public class BookPanel extends Widget {
    private attribute bookPanel: JBookPanel;
    public attribute pages: Widget[];
    public attribute leftPageIndex: Integer on replace {
        if(bookPanel <> null) {
            bookPanel.setLeftPageIndex(leftPageIndex);
        }
    }
    public attribute pageBounds: Rectangle = new Rectangle (70, 80, 210, 342);
    public attribute refreshSpeed: Number = 20;
    public attribute darkShadowColor: Color;
    public attribute neutralShadowColor: Color;
    public attribute shadowWidth: Number;
    public attribute softClipping: Boolean;
    public attribute borderLinesVisible: Boolean = false;
    public function nextPage() { bookPanel.nextPage(); }
    public function previousPage() {bookPanel.previousPage();}
    public function createComponent():javax.swing.JComponent {
        bookPanel = JBookPanel{};
        bookPanel.setOpaque(false);
        bookPanel.setModel(JBookPanel.BookPanelViewModel {
                public function getPageCount():Integer {
                    return sizeof pages;
                }
                public function getPage(i:Integer):JComponent {
                    if(i < 0) i = 0;
                    if(i >= sizeof pages) i = sizeof pages - 1;
                    return pages[i].getComponent();
                }
            });
        bookPanel.addSelectionListener(ChangeListener {
                public function stateChanged(e:ChangeEvent):Void {
                    leftPageIndex = bookPanel.getLeftPageIndex();
                }
            });
        if (pageBounds <> null) {
            bookPanel.setMargins(pageBounds.x, pageBounds.y, pageBounds.width, pageBounds.height);
        }
        bookPanel.setRefreshSpeed(refreshSpeed.intValue());
        bookPanel.setBorderLinesVisible(borderLinesVisible);
        bookPanel.setLeftPageIndex(leftPageIndex);
        return bookPanel;
    }   
    public attribute background: AbstractColor = Color.WHITE;
    public attribute focusable:Boolean = false;
    
}

