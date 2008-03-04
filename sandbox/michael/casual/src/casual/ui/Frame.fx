package casual.ui;

import casual.ui.Dialog;
import casual.ui.DialogType;

public class Frame extends javafx.ui.Frame
{
    attribute inLiveResize: Boolean = false;
    attribute doLiveResize: Boolean = java.lang.System.getProperty("os.name").startsWith("Mac") == false;
    
    attribute dialog: Dialog;
    
    public function requestFocus();
    
    public function showErrorMessage(text:String, headline:String, wait:Boolean) {
        if (dialog <> null) {
            dialog.type = DialogType.ERROR;
            dialog.active = true;
            dialog.text = text;
            dialog.headline = headline;
            dialog.interactive = interactive;
        }
    };
    
    public function showWarningMessage(text:String, headline:String, wait:Boolean) {
        if (dialog <> null) {
            dialog.type = DialogType.WARNING;
            dialog.active = true;
            dialog.text = text;
            dialog.headline = headline;
            dialog.interactive = interactive;
        }
    };
    
    public function showInfoMessage(text:String, headline:String, wait:Boolean) {
        if (dialog <> null) {
            dialog.type = DialogType.INFO;
            dialog.active = true;
            dialog.text = text;
            dialog.headline = headline;
            dialog.interactive = interactive;
        }
    };
    
    public function hideMessage() {
        if (dialog <> null) {
            dialog.active = false;
        }
    };
    
    override attribute disposeOnClose = true;
}
