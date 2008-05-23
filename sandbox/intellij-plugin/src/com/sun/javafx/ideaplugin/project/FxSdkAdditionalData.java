package com.sun.javafx.ideaplugin.project;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;

/**
 * @author David Kaspar
 */
public final class FxSdkAdditionalData implements SdkAdditionalData {

    private final String javaSdkName;
    private final SdkModel sdkModel;

    public FxSdkAdditionalData (String javaSdkName, SdkModel sdkModel) {
        this.javaSdkName = javaSdkName;
        this.sdkModel = sdkModel;
    }

    public String getJavaSdkName () {
        return javaSdkName;
    }

    public FxSdkAdditionalData clone () throws CloneNotSupportedException {
        return (FxSdkAdditionalData) super.clone ();
    }

    public void checkValid (SdkModel sdkModel) throws ConfigurationException {
        if (javaSdkName == null)
            throw new ConfigurationException ("No Java SDK configured");
        if (findJavaSdk (javaSdkName, sdkModel) == null)
            throw new ConfigurationException ("Cannot find Java SDK");
    }

    Sdk findSdk () {
        return findJavaSdk (javaSdkName, sdkModel);
    }

    private static Sdk findJavaSdk (String sdkName, SdkModel sdkModel) {
        for (Sdk sdk : ProjectJdkTable.getInstance ().getAllJdks ()) {
            if (sdk.getName ().equals (sdkName))
                return sdk;
        }
        if (sdkModel != null) {
            for (Sdk sdk : sdkModel.getSdks ()) {
                if (sdk.getName ().equals (sdkName))
                    return sdk;
            }
        }
        return null;
    }

}
