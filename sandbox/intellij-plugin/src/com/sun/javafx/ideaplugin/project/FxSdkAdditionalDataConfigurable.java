package com.sun.javafx.ideaplugin.project;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.util.Comparing;

import javax.swing.*;
import java.awt.*;

/**
 * @author David Kaspar
 */
public final class FxSdkAdditionalDataConfigurable implements AdditionalDataConfigurable {

    private final SdkModel sdkModel;
    private final SdkModel.Listener listener;
    private final DefaultComboBoxModel model;
    private final FxSdkAdditionalDataPanel panel;
    private Sdk fxSdk;

    public FxSdkAdditionalDataConfigurable (SdkModel sdkModel) {
        this.sdkModel = sdkModel;

        model = new DefaultComboBoxModel ();

        panel = new FxSdkAdditionalDataPanel ();
        panel.javaSdkCombo.setModel (model);
        panel.javaSdkCombo.setRenderer (new DefaultListCellRenderer() {
            public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent (list, value instanceof ProjectJdk ? ((ProjectJdk) value).getName (): value, index, isSelected, cellHasFocus);
            }
        });

        listener = new SdkModel.Listener() {
            public void sdkAdded (Sdk sdk) {
                reloadModel ();
            }

            public void beforeSdkRemove (Sdk sdk) {
                reloadModel ();
            }

            public void sdkChanged (Sdk sdk, String previousName) {
                reloadModel ();
            }

            public void sdkHomeSelected (Sdk sdk, String newSdkHome) {
                reloadModel ();
            }
        };
    }

    private void reloadModel () {
        model.removeAllElements ();
        Object previouslySelected = model.getSelectedItem ();
        for (Sdk sdk : sdkModel.getSdks ()) {
            SdkType sdkType = sdk.getSdkType ();
            if (Comparing.equal (sdkType, JavaSdk.getInstance ()) || "IDEA JDK".equals (sdkType.getName ()))
                model.addElement (sdk);
        }
        model.setSelectedItem (previouslySelected);
    }

    public void setSdk (Sdk sdk) {
        fxSdk = sdk;
    }

    public JComponent createComponent () {
        sdkModel.addListener (listener);
        reloadModel ();
	reset ();
        apply ();
        return panel.panel;
    }

    public boolean isModified () {
        FxSdkAdditionalData additionalData = (FxSdkAdditionalData) fxSdk.getSdkAdditionalData();
        Sdk selectedSdk = (Sdk) model.getSelectedItem ();
        return additionalData == null  ||  ! Comparing.equal(selectedSdk != null ? selectedSdk.getName () : null, additionalData.getJavaSdkName());
    }

    public void apply () {
        final SdkModificator modificator = fxSdk.getSdkModificator ();
        Sdk selectedSdk = (Sdk) model.getSelectedItem ();
        modificator.setSdkAdditionalData (new FxSdkAdditionalData (selectedSdk != null ? selectedSdk.getName () : null, sdkModel));
        ApplicationManager.getApplication ().runWriteAction (new Runnable() {
            public void run () {
                modificator.commitChanges ();
            }
        });
        fxSdk.getSdkType ().setupSdkPaths (fxSdk);
    }

    public void reset () {
        FxSdkAdditionalData data = (FxSdkAdditionalData) fxSdk.getSdkAdditionalData ();
        if (data != null) {
            Sdk sdk = data.findSdk ();
            if (sdk != null)
                model.setSelectedItem (sdk);
        }
    }

    public void disposeUIResources () {
        sdkModel.removeListener (listener);
    }

}

