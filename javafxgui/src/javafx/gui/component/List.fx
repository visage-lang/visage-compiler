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

package javafx.gui.component;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.lang.Object;
import java.lang.IllegalArgumentException;
import javafx.lang.FX;

// PENDING_DOC_REVIEW
/**
 * A component that displays a list of {@link ListItem}s objects 
 * and allows the user to select one or more items. 
 */
public class List extends ScrollableComponent {

    private attribute initialized: Boolean = false;

    private attribute ignoreSelectionChanges: Boolean = false;

    private attribute listeners: ArrayList = new ArrayList();

    // PENDING_DOC_REVIEW
    /**
     * Defines the array of {@link ListItem}s objects
     * to be displayed by this {@code List}.  
     */
    public attribute items: ListItem[] on replace oldItems[a..b] = newSlice {
        var added = sizeof newSlice;

        if (a <= b) {
            if (initialized) {
                fireContentsRemoved(a, b);
            }

            for (item in oldItems[a..b]) {
                item.list = null;
                if (item.selected) {
                    selectedItem = null;
                }
            }
        }

        var index = a;

        if (added > 0) {
            if (initialized) {
                fireContentsAdded(a, added);
            }

            for (item in newSlice) {
                if (item.list <> null) {
                    delete item from item.list.items;
                }
                item.list = this;
                item.listIndex = index++;
                if (item.selected) {
                    selectedItem = item;
                }
            }
        }

        for (item in oldItems[(b + 1)..<(sizeof oldItems)]) {
            item.listIndex = index++;
        }

        updateSelectedIndexFromSelectedItem();
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the smallest selected cell index; <i>the selection</i> when only
     * a single item is selected in the list. When multiple items are selected,
     * it is simply the smallest selected index. Returns {@code -1} if there is
     * no selection.
     */
    public attribute selectedIndex: Integer = -1 on replace {
        if (selectedIndex < -1 or selectedIndex >= sizeof items) {
            selectedIndex = -1;
        }

        updateSelectedItemFromSelectedIndex();
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the {@link ListItem} for the smallest selected cell index;
     * <i>the selected value</i> when only a single item is selected in the
     * list. When multiple items are selected, it is simply the {@link ListItem} for 
     * the smallest selected index. Returns {@code null} if there is no selection.
     */
    public attribute selectedItem: ListItem = null on replace oldValue {
        if (oldValue <> null and FX.isSameObject(oldValue.list, this)) {
            oldValue.selected = false;
        }

        if (selectedItem <> null and FX.isSameObject(selectedItem.list, this)) {
            selectedItem.selected = true;
        } else {
            selectedItem = null;
        }

        updateSelectedIndexFromSelectedItem();
    }

    private function updateSelectedItemFromSelectedIndex(): Void {
        if (selectedIndex == -1) {
            selectedItem = null;
        } else {
            selectedItem = items[selectedIndex];
        }

        updateJListSelection();
    }
    
    private function updateSelectedIndexFromSelectedItem(): Void {
        if (selectedItem == null) {
            selectedIndex = -1;
        } else {
            selectedIndex = selectedItem.listIndex;
        }

        updateJListSelection();
    }

    private function updateJListSelection(): Void {
        if (not initialized) {
            return;
        }

        try {
            ignoreSelectionChanges = true;

            var list = getJList();
            if (selectedItem == null) {
                list.clearSelection();
                list.getSelectionModel().setAnchorSelectionIndex(-1);
                list.getSelectionModel().setLeadSelectionIndex(-1);
            } else {
                list.getSelectionModel().setSelectionInterval(selectedIndex, selectedIndex);
                list.ensureIndexIsVisible(selectedIndex);
            }
        } finally {
            ignoreSelectionChanges = false;
        }
    }

    init {
        installModel();
        installSelectionListener();
        initialized = true;
        updateJListSelection();
    }

    private function installSelectionListener(): Void {
        var jList = getJList();
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.getSelectionModel().addListSelectionListener(ListSelectionListener {
            public function valueChanged(lse: ListSelectionEvent): Void {
                if (ignoreSelectionChanges) {
                    return;
                }

                var sel = jList.getSelectedIndex();
                if (sel == -1) {
                    jList.setSelectedIndex(selectedIndex);
                } else {
                    selectedIndex = sel;
                }
            }
        });
    }
    
    private function installModel(): Void {
        var listModel = ListModel {
            public function getSize(): Integer {
                sizeof items;
            }

            public function getElementAt(i: Integer): Object {
                items[i];
            }

            public function addListDataListener(l: ListDataListener): Void {
                listeners.add(l);
            }

            public function removeListDataListener(l: ListDataListener): Void {
                listeners.remove(l);
            }
        };

        getJList().setModel(listModel);
    }

    function fireContentsChanged(item: ListItem): Void {
        try {
            ignoreSelectionChanges = true;
            var index = item.listIndex;
            var event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index, index);
            for (l in [(listeners.size() - 1)..0 step -1]) {
                (listeners.get(l) as ListDataListener).contentsChanged(event);
            }
        } finally {
            ignoreSelectionChanges = false;
        }
    }

    private function fireContentsRemoved(a: Integer, b: Integer): Void {
        try {
            ignoreSelectionChanges = true;
            var event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, a, b);
            for (l in [(listeners.size() - 1)..0 step -1]) {
                (listeners.get(l) as ListDataListener).intervalRemoved(event);
            }
        } finally {
            ignoreSelectionChanges = false;
        }
    }

    private function fireContentsAdded(a: Integer, size: Integer): Void {
        try {
            ignoreSelectionChanges = true;
            var event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, a, a + size - 1);
            for (l in [(listeners.size() - 1)..0 step -1]) {
                (listeners.get(l) as ListDataListener).intervalAdded(event);
            }
        } finally {
            ignoreSelectionChanges = false;
        }
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the {@link JList} delegate for this component.
     */
    public function getJList(): JList {
        getJComponent() as JList;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the specific {@link JComponent} delegate for this component.
     */
    protected /* final */ function createJComponent(): JComponent {
        new JList();
    }

}
