/* JFXC-197:  Make sure onChanges list of JFXVar is visited in base JavafxTreeScanner visitor.
 * @test
 * @run
 */
public class ConfirmDialog {
    public attribute visible: Boolean = false on replace {
        if (visible) {
            //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                         public function run():Void {
                            var result = true;
                            if (result) { if(onYes <> null) onYes(); } 
                            else if (result) { if(onNo <> null) onNo(); }
                            else if (result) { if(onCancel <> null) onCancel(); }
                            else if (result) { if(onYes <> null) onYes(); }
                            else if (result) { if(onCancel <> null) onCancel(); }
                            visible = false;
                        }
                }
             );
        } 
    }
    public attribute onYes: function():Void;
    public attribute onNo: function():Void;
    public attribute onCancel: function():Void;
}

