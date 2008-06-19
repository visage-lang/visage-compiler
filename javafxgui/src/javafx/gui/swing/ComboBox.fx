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

package javafx.gui.swing;

import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.ComboBoxEditor;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.lang.Object;
import java.lang.IllegalArgumentException;
import javafx.lang.FX;

// PENDING_DOC_REVIEW
/**
 * A component that combines a button or editable field and a drop-down list.
 * The user can select a value from the drop-down list, which appears at the 
 * user's request. If you make the combo box editable, then the combo box
 * includes an editable field into which the user can type a value.
 */
public class ComboBox extends Component {

    private attribute initialized: Boolean = false;

    private attribute listeners: ArrayList = new ArrayList();

    private attribute modelSelected: Object;

   // PENDING_DOC_REVIEW
   /**
    * Equals {@code true} if the {@code ComboBox} is editable.
    * <p/>
    * By default, a combo box is not editable.      
    */
    public attribute editable: Boolean = false on replace {
        getJComboBox().setEditable(editable);
    }

   // PENDING_DOC_REVIEW 
   /**
    * Defines an array of this {@code ComboBox} elements.
    */
    public attribute items: ComboBoxItem[] on replace oldItems[a..b] = newSlice {
        var added = sizeof newSlice;

        if (a <= b) {
            if (initialized) {
                fireContentsRemoved(a, b);
            }

            for (item in oldItems[a..b]) {
                item.combo = null;
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
                if (item.combo <> null) {
                    delete item from item.combo.items;
                }
                item.combo = this;
                item.comboIndex = index++;
                if (item.selected) {
                    selectedItem = item;
                }
            }
        }

        for (item in oldItems[(b + 1)..<(sizeof oldItems)]) {
            item.comboIndex = index++;
        }

        updateSelectedIndexFromSelectedItem();
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the first item in the list that matches the given item.
    * The result is not always defined if the {@code ComboBox} class
    * allows selected items that are not in the list. 
    * Equals {@code -1} if there is no selected item or if the user specified
    * an item which is not in the list.
    * <p/>
    * The default value is {@code -1}.
    */
    public attribute selectedIndex: Integer = -1 on replace {
        if (selectedIndex < -1 or selectedIndex >= sizeof items) {
            selectedIndex = -1;
        }

        updateSelectedItemFromSelectedIndex();
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the selected item in the combo box display area.
     * <p/>
     * The default value is {@code null}. 
     */
    public attribute selectedItem: ComboBoxItem = null on replace oldValue {
        if (oldValue <> null and FX.isSameObject(oldValue.combo, this)) {
            oldValue.selected = false;
        }

        if (selectedItem <> null and FX.isSameObject(selectedItem.combo, this)) {
            selectedItem.selected = true;
        } else {
            selectedItem = null;
        }

        updateSelectedIndexFromSelectedItem();
    }

   // PENDING_DOC_REVIEW
   /**
    * Defines the text for the selected item in the list. 
    */
    public attribute text: String on replace {
        updateJComboBoxSelectionFromText();
    }

    private function updateSelectedItemFromSelectedIndex(): Void {
        if (selectedIndex == -1) {
            selectedItem = null;
        } else {
            selectedItem = items[selectedIndex];
        }

        updateJComboBoxSelection();
    }
    
    private function updateSelectedIndexFromSelectedItem(): Void {
        if (selectedItem == null) {
            selectedIndex = -1;
        } else {
            selectedIndex = selectedItem.comboIndex;
        }

        updateJComboBoxSelection();
    }

    private function updateJComboBoxSelection(): Void {
        if (not initialized) {
            return;
        }

        getJComboBox().setSelectedItem(selectedItem);
    }

    private function updateJComboBoxSelectionFromText(): Void {
        if (not initialized) {
            return;
        }

        getJComboBox().setSelectedItem(text);
    }

    private function setModelSelection(selection: Object): Void {
        if (FX.isSameObject(modelSelected, selection) or (selection == null and modelSelected instanceof String)) {
            return;
        }

        if (selection instanceof ComboBoxItem or selection == null) {
            var oldValue = modelSelected;
            modelSelected = selection;
            fireSelectionUpdated(oldValue, modelSelected);
            selectedItem = modelSelected as ComboBoxItem;
            text = textForSelectedItem();
            return;
        }

        if (not (selection instanceof String)) {
            return;
        }

        if (not editable) {
            text = textForSelectedItem();
            return;
        }

        var selString = selection as String;
        if (selString == modelSelected or
                (modelSelected instanceof ComboBoxItem and selString == (modelSelected as ComboBoxItem).text)) {
            return;
        }

        var sels = for (item in items where item.text == selString) item;
        var oldVal = modelSelected;
        modelSelected = if (sizeof sels > 0) sels[0] else selString;
        fireSelectionUpdated(oldVal, modelSelected);

        if (modelSelected instanceof ComboBoxItem) {
            selectedItem = modelSelected as ComboBoxItem;
            text = textForSelectedItem();
        } else {
            selectedItem = null;
            text = selString;
        }
    }

    private function textForSelectedItem(): String {
        return if (selectedItem == null) "" else selectedItem.text;
    }

    init {
        installModel();
        initialized = true;
        var txt = text;
        updateJComboBoxSelection();
        if (editable and txt <> "") {
            getJComboBox().setSelectedItem(txt);
        }
    }

    private function installModel(): Void {
        var comboModel = ComboBoxModel {
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

            public function setSelectedItem(anItem: Object) {
                setModelSelection(anItem);
            }

            public function getSelectedItem(): Object {
                return modelSelected;
            }
        };

        getJComboBox().setModel(comboModel);
    }

    private function fireSelectionUpdated(oldValue: Object, newValue: Object): Void {
        var event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, -1, -1);
        for (l in [(listeners.size() - 1)..0 step -1]) {
            (listeners.get(l) as ListDataListener).contentsChanged(event);
        }

        var impl = getJComboBox() as JComboBoxImpl;

        if (oldValue <> null ) {
            var ie = new ItemEvent(impl, ItemEvent.ITEM_STATE_CHANGED, oldValue, ItemEvent.DESELECTED);
            impl.fireItemStateChanged(ie);
        }

        if (newValue <> null) {
            var ie = new ItemEvent(impl, ItemEvent.ITEM_STATE_CHANGED, newValue, ItemEvent.SELECTED);
            impl.fireItemStateChanged(ie);
        }
    }

    function fireContentsChanged(item: ComboBoxItem): Void {
        var index = item.comboIndex;
        var event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index, index);
        for (l in [(listeners.size() - 1)..0 step -1]) {
            (listeners.get(l) as ListDataListener).contentsChanged(event);
        }
    }

    private function fireContentsRemoved(a: Integer, b: Integer): Void {
        var event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, a, b);
        for (l in [(listeners.size() - 1)..0 step -1]) {
            (listeners.get(l) as ListDataListener).intervalRemoved(event);
        }
    }

    private function fireContentsAdded(a: Integer, size: Integer): Void {
        var event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, a, a + size - 1);
        for (l in [(listeners.size() - 1)..0 step -1]) {
            (listeners.get(l) as ListDataListener).intervalAdded(event);
        }
    }

   // PENDING_DOC_REVIEW
   /**
    * Creates the specific {@link JComboBox} delegate for this component.
    * 
    * @see javax.swing.JComboBox 
    */
    public function getJComboBox(): JComboBox {
        getJComponent() as JComboBox;
    }

    protected /* final */ function createJComponent(): JComponent {
        JComboBoxImpl{};
    }

}

class JComboBoxImpl extends JComboBox {

    public function setModel(aModel: ComboBoxModel): Void {
        var oldModel = dataModel;
        dataModel = aModel;
        firePropertyChange("model", oldModel, dataModel);
    }

    public function setSelectedItem(anObject: Object): Void {
        dataModel.setSelectedItem(anObject);
    }

    public function getSelectedIndex(): Integer {
        var selectedItem = getSelectedItem();
        return if (selectedItem instanceof ComboBoxItem) (selectedItem as ComboBoxItem).comboIndex else -1;
    }

    // overridden to make accessible from this module
    protected function fireItemStateChanged(e: ItemEvent): Void {
        JComboBox.fireItemStateChanged(e);
    }

    public function configureEditor(anEditor: ComboBoxEditor, anItem: Object): Void {
        if (anItem == null) {
            anEditor.setItem("");
        } else {
            anEditor.setItem(anItem);
        }
    }

}
