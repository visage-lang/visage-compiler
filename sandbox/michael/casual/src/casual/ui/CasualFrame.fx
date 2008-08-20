package casual.ui;

public abstract class CasualFrame extends javafx.ui.Frame
{
    var inLiveResize: Boolean = false;
    var doLiveResize: Boolean = java.lang.System.getProperty("os.name").startsWith("Mac") == false;
    
    var dialog: Dialog;
    public abstract function requestFocus() : Void;

    public function showErrorMessage(text:String, headline:String, interactive:Boolean) {
        if (dialog != null) {
            dialog.type = Dialog.DialogType.ERROR;
            dialog.active = true;
            dialog.text = text;
            dialog.headline = headline;
            dialog.interactive = interactive;
        }
    };
    
    public function showWarningMessage(text:String, headline:String, interactive:Boolean) {
        if (dialog != null) {
            dialog.type = Dialog.DialogType.WARNING;
            dialog.active = true;
            dialog.text = text;
            dialog.headline = headline;
            dialog.interactive = interactive;
        }
    };
    
    public function showInfoMessage(text:String, headline:String, interactive:Boolean) {
        if (dialog != null) {
            dialog.type = Dialog.DialogType.INFO;
            dialog.active = true;
            dialog.text = text;
            dialog.headline = headline;
            dialog.interactive = interactive;
        }
    };
    
    public function hideMessage() {
        if (dialog != null) {
            dialog.active = false;
        }
    };

    override var disposeOnClose = true;
}
