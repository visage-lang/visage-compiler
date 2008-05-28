package assortis.sources.language.javafx.api.gui;

import assortis.core.Module;
import assortis.core.Sample;

Module {
    
    name: "GUI"
    
    samples: [
    Sample{
        name: "New"
        className: "assortis.sources.language.javafx.api.gui.localization.Empty"
        visible: true
    },
    
    Sample{
        name: "Label"
        className: "assortis.sources.language.javafx.api.gui.FXLabel"
    },
    Sample{
        name: "TextField"
        className: "assortis.sources.language.javafx.api.gui.FXTextField"
    },
    Sample{
        name: "Button"
        className: "assortis.sources.language.javafx.api.gui.FXButton"
    },
    Sample{
        name: "CheckBox"
        className: "assortis.sources.language.javafx.api.gui.FXCheckBox"
    },
    Sample{
        name: "RadioButton"
        className: "assortis.sources.language.javafx.api.gui.FXRadioButton"
    },
    Sample{
        name: "ComboBox"
        className: "assortis.sources.language.javafx.api.gui.FXComboBox"
    },
    Sample{
        name: "List"
        className: "assortis.sources.language.javafx.api.gui.FXList"
    },
    Sample{
        name: "Menus"
        className: "assortis.sources.language.javafx.api.gui.FXMenu"
    },
    Sample{
        name: "Slider"
        className: "assortis.sources.language.javafx.api.gui.FXSlider"
    },
//    Sample{
//        name: "Spinner"
//        className: "assortis.sources.language.javafx.api.gui.FXSpinner"
//    },
//    Sample{
//        name: "Table"
//        className: "assortis.sources.language.javafx.api.gui.FXTable"
//    },
    Sample{
        name: "FlowPanel"
        className: "assortis.sources.language.javafx.api.gui.FXFlowPanel"
    },
    Sample{
        name: "BorderPanel"
        className: "assortis.sources.language.javafx.api.gui.FXBorderPanel"
    },
    Sample{
        name: "Frame"
        className: "assortis.sources.language.javafx.api.gui.FXFrame"
    },
    ]
}
