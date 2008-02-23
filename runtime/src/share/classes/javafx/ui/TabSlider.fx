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

import java.lang.Object;
import java.awt.LayoutManager2;
import javax.swing.JPanel;
import java.awt.Dimension;
import com.sun.javafx.api.ui.LayoutNotifier;
import java.lang.Math;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.JViewport;
import java.awt.BorderLayout;
import com.sun.javafx.api.ui.ScrollablePanel;


class TabSlider extends Widget {

    override attribute focusable = false;

    // private
    attribute inUpdateSelection: Boolean;
    public function updateSelection(oldIndex:Integer, newIndex:Integer):Void {
        if (panel == null) {
            return;
        }
        var oldTab = tabs[oldIndex];
        if (oldTab <> null) {
            inSelection = true;
            oldTab.selected = false;
            inSelection = false;
        }
	var newTab = tabs[newIndex];
        if (orientation == Orientation.VERTICAL) {
            if (newTab <> null) {
                var availHeight = panel.getHeight() - sizeof tabs*23;
                var ins = panel.getInsets();
                if (ins <> null) {
                    availHeight -= ins.top + ins.bottom;
                }
                newTab.scrollable.setScrollableTracksViewportHeight(false);
                newTab.scrollable.setScrollableTracksViewportWidth(false);
                var dim = newTab.viewport.getSize();
                dim.height = availHeight;
                newTab.scrollable.setPreferredScrollableViewportSize(dim);
                if (oldTab <> null) {
                    oldTab.scrollable.setScrollableTracksViewportWidth(false);
                    oldTab.scrollable.setScrollableTracksViewportHeight(false);
                    oldTab.scrollable.setPreferredScrollableViewportSize(oldTab.viewport.getSize());
                }
                resizing = true;
                //TODO DUR
                for (i in [0..availHeight]) { // (dur slideDuration) {
                    newTab.size = i;
                    if (oldTab <> null) {
                        oldTab.size = availHeight - i;
                    }
                    panel.validate();
                    panel.doLayout();
                    panel.repaint();
                    if (i == availHeight) {
                        //TODO DO LATER - this is a work around until a more permanent solution is provided
                        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                  public function run():Void {
                                    newTab.scrollable.setScrollableTracksViewportHeight(true);
                                    newTab.scrollable.setScrollableTracksViewportWidth(true);
                                    newTab.scrollable.setPreferredScrollableViewportSize(null);
                                    oldTab.scrollable.setScrollableTracksViewportHeight(true);
                                    oldTab.scrollable.setScrollableTracksViewportWidth(true);
                                    oldTab.scrollable.setPreferredScrollableViewportSize(null);
                                    resizing = false;
                                  }
                        });
                    }
                }
                newTab.content.getNonScrollPaneComponent().requestFocus();
            } else if (oldTab <> null) {
                resizing = true;
                oldTab.scrollable.setScrollableTracksViewportHeight(false);
                oldTab.scrollable.setScrollableTracksViewportWidth(false);
                oldTab.scrollable.setPreferredScrollableViewportSize(oldTab.viewport.getSize());
                //TODO DUR
                for (i in [oldTab.size..0 step -1]){ // (dur slideDuration) {
                    oldTab.size = i;
                    panel.validate();
                    panel.doLayout();
                    panel.repaint();
                    if (i == 0) {
                        //TODO DO LATER - this is a work around until a more permanent solution is provided
                        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                  public function run():Void {
                                    oldTab.scrollable.setScrollableTracksViewportHeight(true);
                                    oldTab.scrollable.setScrollableTracksViewportWidth(true);
                                    oldTab.scrollable.setPreferredScrollableViewportSize(null);
                                    resizing = false;
                                  }
                        });
                    }
                }
            }
        } else {
            if (newTab <> null) {
                var avail = panel.getWidth() - sizeof tabs*23;
                var ins = panel.getInsets();
                var dim = newTab.viewport.getSize();
                dim.width = avail;
                if (ins <> null) {
                    avail -= ins.left + ins.right;
                }
                resizing = true;
                newTab.scrollable.setScrollableTracksViewportWidth(false);
                newTab.scrollable.setScrollableTracksViewportHeight(false);
                newTab.scrollable.setPreferredScrollableViewportSize(dim);
                if (oldTab <> null) {
                    oldTab.scrollable.setScrollableTracksViewportWidth(false);
                    oldTab.scrollable.setScrollableTracksViewportHeight(false);
                    oldTab.scrollable.setPreferredScrollableViewportSize(oldTab.viewport.getSize());
                }
                //TODO DUR
                for (i in [0..avail]){ // (dur slideDuration) {
                    newTab.size = i;
                    if (oldTab <> null) {
                        oldTab.size = avail - i;
                    }
                    panel.validate();
                    panel.doLayout();
                    panel.repaint();
                    if (i == avail) {
                        //TODO DO LATER - this is a work around until a more permanent solution is provided
                        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                  public function run():Void {
                                    newTab.scrollable.setScrollableTracksViewportHeight(true);
                                    newTab.scrollable.setScrollableTracksViewportWidth(true);
                                    newTab.scrollable.setPreferredScrollableViewportSize(null);
                                    oldTab.scrollable.setScrollableTracksViewportHeight(true);
                                    oldTab.scrollable.setScrollableTracksViewportWidth(true);
                                    oldTab.scrollable.setPreferredScrollableViewportSize(null);
                                    resizing = false;
                                  }
                        });
                    }
                }
                newTab.content.getNonScrollPaneComponent().requestFocus();
            } else if (oldTab <> null) {
                oldTab.scrollable.setScrollableTracksViewportHeight(false);
                oldTab.scrollable.setScrollableTracksViewportWidth(false);
                oldTab.scrollable.setPreferredScrollableViewportSize(oldTab.viewport.getSize());
                resizing = true;
                //TODO DUR
                for (i in [oldTab.size..0 step -1]){ // (dur slideDuration) {
                    oldTab.size = i;
                    panel.validate();
                    panel.doLayout();
                    panel.repaint();
                    if (i == 0) {
                        //TODO DO LATER - this is a work around until a more permanent solution is provided
                        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                  public function run():Void {
                                    oldTab.scrollable.setScrollableTracksViewportHeight(true);
                                    oldTab.scrollable.setScrollableTracksViewportWidth(true);
                                    oldTab.scrollable.setPreferredScrollableViewportSize(null);
                                    resizing = false;
                                  }
                        });
                    }
                }
            }
        }
    }

    attribute resizing: Boolean on replace {
       if (not resizing) {
            panel.doLayout();
            panel.repaint();
       }
    };
    attribute inSelection: Boolean;
    public attribute orientation: Orientation = Orientation.VERTICAL on replace {
        if (orientation == Orientation.VERTICAL) {
            for (i in  tabs) {
                (i.button as RotatableWidget).rotation = 0;
            }
        } else {
            for (i in  tabs) {
                (i.button as RotatableWidget).rotation = 90;
            }
        }
        panel.validate();
        panel.doLayout();
        panel.repaint();
    };
    attribute panel: LayoutNotifier;

    // public
    public attribute selectedIndex: Number = -1 on replace (old)  {
        if (not inUpdateSelection) {
            if (selectedIndex == -1 and sizeof tabs > 0) {
                inUpdateSelection = true;
                selectedIndex = sizeof tabs -1;
                inUpdateSelection = false;
            } 
            if (old <> selectedIndex) {
                updateSelection(old, selectedIndex);
            }
        }
    };
    public attribute tabs: SlideTab[] on replace oldValue[lo..hi]=newVals {
            for(n in newVals) {
                n.slider = this;
            }
        };
    public attribute slideDuration: Number = 700;

    public function createComponent():javax.swing.JComponent {
        panel = new LayoutNotifier();
        panel.setOpaque(false);
        panel.setLayoutManager(LayoutManager2 {
		public function addLayoutComponent(name : java.lang.String, comp:java.awt.Component) : Void {
                }

                public function addLayoutComponent(comp:java.awt.Component, constraint:java.lang.Object):Void { 
                } 
                public function maximumLayoutSize(container:java.awt.Container):Dimension {
                    return new Dimension(java.lang.Integer.MAX_VALUE, java.lang.Integer.MAX_VALUE);
                }
                public function minimumLayoutSize(container:java.awt.Container):Dimension {
                    return new Dimension(0, 0);
                }
                public function preferredLayoutSize(container:java.awt.Container):Dimension {
                    //TODO HACK to force a Dimension return after if statement.
                    var dim = new Dimension(0,0);
                    if (orientation == Orientation.VERTICAL) {
                        var tabCount = sizeof tabs;
                        var comp = tabs[selectedIndex.intValue()];
                        var w = 0;
                        var h = tabCount * 23;
                        for (tab in tabs) {
                            var c = tab.button.getComponent();
                            var pref = c.getPreferredSize();
                            if (pref.width > w) {
                                w = pref.width;
                            }
                            c = tab.content.getComponent();
                            pref = c.getPreferredSize();
                            if (pref.width > w) {
                                w = pref.width;
                            }
                            h += tab.size;
                        }
                        var ins = panel.getInsets();
                        if (ins <> null) {
                            w += ins.left + ins.right;
                            h += ins.top + ins.bottom;
                        } 
                        dim =  new Dimension(w, h);
                    } else {
                        var tabCount = sizeof tabs;
                        var comp = tabs[selectedIndex.intValue()];
                        var w = tabCount * 23;
                        var h = 0;
                        for (tab in tabs) {
                            var c = tab.button.getComponent();
                            var pref = c.getPreferredSize();
                            if (pref.height > h) {
                                h = pref.height;
                            }
                            c = tab.content.getComponent();
                            pref = c.getPreferredSize();
                            if (pref.height > h) {
                                h = pref.height;
                            }
                            w += tab.size;
                        }
                        var ins = panel.getInsets();
                        if (ins <> null) {
                            w += ins.left + ins.right;
                            h += ins.top + ins.bottom;
                        } 
                        dim =   new Dimension(w, h);
                    } 
                    return dim;
                }
                public function getLayoutAlignmentX(container:java.awt.Container):Number {
                    return 0.0.floatValue();
                }
                public function getLayoutAlignmentY(container:java.awt.Container):Number {
                    return 0.0.floatValue();
                }
                public function invalidateLayout(container:java.awt.Container):Void {
                }
                public function layoutContainer(container:java.awt.Container):Void {
                    if (orientation == Orientation.VERTICAL) {
                        var w = panel.getWidth();
                        var tabCount = sizeof tabs;
                        var availHeight = Math.max(panel.getHeight() - tabCount*23, 0);
                        var x = 0;
                        var y = 0;
                        var ins = panel.getInsets();
                        if (ins == null) {
                            ins = new java.awt.Insets(0, 0, 0, 0);
                        }
                        x += ins.left;
                        y += ins.top;
                        w -= ins.left + ins.right;
                        if (w < 0) {
                            w = 0;
                        }
                        availHeight -= ins.top + ins.bottom;
                        var showing = tabs[selectedIndex.intValue()];
                        if (showing <> null and not resizing) {
                            showing.size = availHeight;
                        }
                        for (tab in tabs) {
                            var c = tab.button.getComponent();
                            c.setBounds(x, y, w, 23);
                            c = tab.viewport;
                            y += 23;
                            if (not resizing) {
                                if (tab.size > 0) {
                                   var dim = new Dimension(w, tab.size.intValue());
                                   tab.viewport.getView().setSize(dim);
                                }
                            }
                            c.setBounds(x, y, w, tab.size.intValue());
                            y += tab.size;
                        }
                    } else {
                        var tabCount = sizeof tabs;
                        var avail = Math.max(panel.getWidth() - tabCount*23, 0);
                        var x = 0;
                        var y = 0;
                        var h = panel.getHeight();
                        var ins = panel.getInsets();
                        if (ins <> null) {
                            x += ins.left;
                            y += ins.top;
                            h -= ins.top + ins.bottom+1;
                        } 
                        var showing = tabs[selectedIndex.intValue()];
                        if (showing <> null and not resizing) {
                            showing.size = avail;
                        }
                        for (tab in tabs) {
                            var c = tab.button.getComponent();
                            c.setBounds(x, y, 23, h);
                            c = tab.viewport;
                            x += 23;
                            if (not resizing) {
                                if (tab.size > 0) {
                                   tab.viewport.getView().setSize(new Dimension(tab.size.intValue(), h));
                                }
                            }
                            c.setBounds(x, y, tab.size.intValue(), h);
                            x += tab.size;
                        }
                    }
                }
                public function removeLayoutComponent(comp:java.awt.Component):Void {
                }
            });
        var selection = -1;
        for (tab in tabs) {
            if (tab.button == null) {
                tab.button = new Button(); 
                if (tab.title <> null) {
                    (tab.button as Button).text = tab.title;
                }
                if (tab.icon <> null) {
                    (tab.button as Button).icon = tab.icon;
                }
                if (tab.icon <> null) {
                    (tab.button as Button).icon = tab.icon;
                }
            } 
            tab.button.toolTipText = tab.toolTipText;

            tab.button.focusable = false;
            tab.button.enabled = tab.enabled;
            var dtlistener = DropTargetAdapter  {
                    public function dragOver(e:DropTargetDragEvent):Void {
                        if (not resizing) {
                            tab.selected = true;
                        }
                    }
                    public function dragEnter(e:DropTargetDragEvent):Void {
                        if (not resizing) {
                            tab.selected = true;
                        }
                    }
                    public function drop(e:DropTargetDropEvent):Void {
                        // empty
                    }
                } as DropTargetListener;
            if (tab.button instanceof RotatableWidget) {
                var b =  tab.button as RotatableWidget;
                b.rotation = if (orientation == Orientation.VERTICAL) then 0 else 90;
            }
            if (tab.button instanceof ActionWidget) {
                var b = tab.button as ActionWidget;
                b.action = function():Void {
                    if (not resizing) {
                       tab.selected = not tab.selected;
                    }
                };
            }
            new DropTarget(tab.button.getComponent(), dtlistener);
            tab.button.opaque = true;
            panel.add(tab.button.getComponent());
            // make a non scrollable child for the viewport
            var sp = new ScrollablePanel();
            sp.setOpaque(false);
            sp.setLayout(new BorderLayout());
            sp.setScrollableTracksViewportHeight(true);
            sp.setScrollableTracksViewportWidth(true);
            sp.add(tab.content.getComponent(), BorderLayout.CENTER);
            tab.scrollable = sp;
            tab.viewport = new JViewport();
            tab.viewport.setOpaque(false);
            tab.viewport.setView(sp);
            panel.add(tab.viewport);
            if (tab.selected) {
                selection = indexof tab;
            }
        }
        //TODO DO LATER - this is a work around until a more permanent solution is provided
        javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                  public function run():Void {
                    if (selection >= 0 and selection <> selectedIndex) {
                        selectedIndex = selection;
                    } else {
                        updateSelection(-1.0, selectedIndex);
                    }
                  }
        });
        return panel;
    }

    function updateTabSelection(tab:SlideTab):Void {
         if (not inSelection) {
            if (tab.selected) {
                var i = for(x in tabs where x == this) indexof x; 
                if (sizeof i > 0) {
                    selectedIndex = i[0];
                }                
            } else {
                selectedIndex = -1;
            }
        }
    }
}



