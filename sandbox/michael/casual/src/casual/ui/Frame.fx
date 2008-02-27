package casual.ui;

import casual.ui.Dialog;
import casual.ui.DialogType;

import javafx.ui.Frame as JFXFrame;

public class Frame extends JFXFrame
{
    attribute inLiveResize: Boolean;
    attribute doLiveResize: Boolean;
    
    attribute dialog: Dialog?;
    
    public operation requestFocus();
    public operation showErrorMessage(text:String, headline:String, wait:Boolean);
    public operation showWarningMessage(text:String, headline:String, wait:Boolean);
    public operation showInfoMessage(text:String, headline:String, wait:Boolean);
    public operation hideMessage();
}

attribute Frame.inLiveResize = false;
attribute Frame.doLiveResize = (<<java.lang.System>>.getProperty("os.name").startsWith("Mac") == false);

attribute Frame.disposeOnClose = true;

operation Frame.hideMessage()
{
    if (dialog <> null)
    {
        dialog.active = false;
    }
}

operation Frame.showErrorMessage(text:String, headline:String, interactive:Boolean)
{
    if (dialog <> null)
    {
        dialog.type = ERROR:DialogType;
        dialog.active = true;
        dialog.text = text;
        dialog.headline = headline;
        dialog.interactive = interactive;
    }
}

operation Frame.showWarningMessage(text:String, headline:String, interactive:Boolean)
{
    if (dialog <> null)
    {
        dialog.type = WARNING:DialogType;
        dialog.active = true;
        dialog.text = text;
        dialog.headline = headline;
        dialog.interactive = interactive;
    }
}

operation Frame.showInfoMessage(text:String, headline:String, interactive:Boolean)
{
    if (dialog <> null)
    {
        dialog.type = INFO:DialogType;
        dialog.active = true;
        dialog.text = text;
        dialog.headline = headline;
        dialog.interactive = interactive;
    }
}


