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

import javax.swing.JTabbedPane;

public class TabbedPane extends Widget {
    protected attribute tabbedpane: javax.swing.JTabbedPane =  UIElement.context.createTabbedPane();
    private attribute changeListener: javax.swing.event.ChangeListener;
    public attribute selectedIndex: Number = -1 on replace {
        if (component <> null and selectedIndex <> -1) {
            var model = tabbedpane.getModel();
            model.removeChangeListener(changeListener);
            var tab = tabs[selectedIndex.intValue()];
            if (tab <> null) {
                tab.selectTab();
                model.setSelectedIndex(selectedIndex.intValue());
            }
            model.addChangeListener(changeListener);
        }
    };
    public attribute onSelectionChange: function(
            oldSelection:SingleSelection, newSelection:SingleSelection):Void;
    //TODO JFXC-267
    public attribute selection: SingleSelection /************** on replace (old) {
        if (component <> null) {
            selectedIndex = selection.anchorIndex;
            tabs[selection.anchorIndex.intValue()].selectTab();
            if(onSelectionChange <> null) {
                onSelectionChange(old, selection);
            }
        }        
    }**********/;
    public attribute tabPlacement: TabPlacement on replace {
        if (tabPlacement <> null) {
            tabbedpane.setTabPlacement(tabPlacement.id.intValue());
        } 
    };
    public attribute tabLayout: TabLayout on replace {
        if (tabLayout == TabLayout.WRAP) {
            tabbedpane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        } else {
            tabbedpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        }
    };
    public attribute tabs: Tab[]
        on insert [ndx] (tab) {
            tab.tabbedPane = this;
            var sicon:javax.swing.Icon = null;
            if (tab.icon <> null) {
                sicon = tab.icon.getIcon();
            }
            tabbedpane.insertTab(tab.title, sicon, tab.panel, tab.toolTipText, ndx);
            if (ndx == selectedIndex) {
                tabbedpane.setSelectedIndex(selectedIndex.intValue());
                selection = SingleSelection {anchorIndex: selectedIndex};
            }
        }

        on delete [ndx] (tab) {
            tabbedpane.removeTabAt(ndx);
        }

        on replace [ndx] (old) { 
            var tab = tabs[ndx];
            tab.tabbedPane = this;
            var sicon:javax.swing.Icon = null;
            if (tab.icon <> null) {
                sicon = tab.icon.getIcon();
            }
            var i = ndx;
            tabbedpane.setTitleAt(i, tab.title);
            tabbedpane.setIconAt(i, sicon);
            tabbedpane.setToolTipTextAt(i, tab.toolTipText);
            tabbedpane.setComponentAt(i, tab.panel);
        }


    public attribute opaque: Boolean = true;
    public attribute focusable: Boolean = false;
    public function createComponent():javax.swing.JComponent{
        if(changeListener == null) {
            tabbedpane.setOpaque(false);
            changeListener = javax.swing.event.ChangeListener {
                public function stateChanged(e:javax.swing.event.ChangeEvent):Void {
                    selection = WidgetInitiatedMultiSelection {
                            widget: tabbedpane,
                            anchorIndex: tabbedpane.getSelectedIndex()
                   } as SingleSelection;
                }
            };
            tabbedpane.getModel().addChangeListener(changeListener);
        }

        if (selectedIndex >= 0 and selectedIndex < tabbedpane.getTabCount()) {
            for (i in tabs) {
                i.selectTab();
            }
            tabbedpane.setSelectedIndex(selectedIndex.intValue());
        }
        return tabbedpane;
    }
}





