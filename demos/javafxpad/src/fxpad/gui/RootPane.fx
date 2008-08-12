package fxpad.gui; 

import javax.swing.*;
import javafx.lang.Sequences;
import javafx.lang.FX;
import javafx.ext.swing.*;

function indexOfJMenuInJMenuBar(item: JMenu, menubar: JMenuBar): Integer {
    var children = menubar.getComponents();
    return Sequences.indexByIdentity(children, item);
}

public class RootPane extends Component, Container {

    // PENDING_DOC_REVIEW
    /**
     * Defines the array of {@link Menu}s to be used by this {@SwingFrame}.
     */
    public var menus: Menu[] on replace oldMenus[a..b] = newSlice {
        var pane = getJRootPane();
        var menubar = pane.getJMenuBar();

        if (menus == null) {
            for (menu in oldMenus[a..b]) {
                unparentFromThisContainer(menu);
            }

            pane.setJMenuBar(null);
            pane.revalidate();
            pane.repaint();
        } else if (menubar == null) {
            menubar = JMenuBar{};
            pane.setJMenuBar(menubar);
            for (menu in menus) {
                menubar.add(menu.getJMenu());
                parentToThisContainer(menu);
            }
            
            menubar.revalidate();
            menubar.repaint();
            resetMenusFromJMenuBar();
        } else {
            for (menu in oldMenus[a..b]) {
                menubar.remove(menu.getJMenu());
                unparentFromThisContainer(menu);
            }

            var index = if (a == 0) 0 else indexOfJMenuInJMenuBar(menus[a - 1].getJMenu(), menubar) + 1;
            for (menu in newSlice) {
                var oldidx = indexOfJMenuInJMenuBar(menu.getJMenu(), menubar);
                if (oldidx != -1 and oldidx < index) {
                    menubar.add(menu.getJMenu(), index - 1);
                } else {
                    menubar.add(menu.getJMenu(), index++);
                }
                parentToThisContainer(menu);
            }

            menubar.revalidate();
            menubar.repaint();
            resetMenusFromJMenuBar();
        }
    }
    
    public var content: Component = null on replace oldContent {
        if (oldContent != null) {
            unparentFromThisContainer(oldContent);
        }

        parentToThisContainer(content);

        if (content != null) {
            getJRootPane().setContentPane(content.getRootJComponent());
        } else {
            getJRootPane().setContentPane(new JPanel());
        }
    }
    

    /**
     * Called when a {@code Component} is being reparented to notify its
     * old {@code Container} that it is to be removed.
     *
     * @param component the {@code Component} to remove
     */
    override function remove(component: Component): Void {
        if (FX.isSameObject(content, component)) {
             content = null;
        }        
    }

    /**
     * Returns this {@code Container's} parent {@code Container},
     * or {@code null} if it doesn't have a parent.
     *
     * @return this {@code Container's} parent or {@code null}
     */
    override bound function getParent(): Container {
        null;
    }

    /**
     * Returns this {@code Container's} name,
     * or {@code null} if it doesn't have a name.
     * 
     * @return this {@code Container's} name or {@code null}
     */
    override bound function getName(): String {
        return null;
    }

    function resetMenusFromJMenuBar(): Void {
        var mb = getJRootPane().getJMenuBar();
        var fromJMenuBar = for (i in [0..<mb.getComponentCount()],
                                j in [Component.getComponentFor(mb.getComponent(i) as JComponent)]
                                where j instanceof Menu) j as Menu;

        if (not Sequences.isEqualByContentIdentity(menus, fromJMenuBar)) {
            // PENDING(shannonh) - want to do this without firing the trigger
            // http://openjfx.java.sun.com/jira/browse/JFXC-1007
            menus = fromJMenuBar;
        }
    }    

    override function createJComponent():javax.swing.JComponent {
        new javax.swing.JRootPane();
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns the {@link JFrame} delegate for this frame.
     */
    public /* final */ function getJRootPane(): JRootPane {
        getJComponent() as JRootPane;
    }    
}

