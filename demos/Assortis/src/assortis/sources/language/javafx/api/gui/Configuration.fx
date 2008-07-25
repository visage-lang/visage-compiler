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
        name: "SwingTextField"
        className: "assortis.sources.language.javafx.api.gui.FXTextField"
    },
    Sample{
        name: "SwingButton"
        className: "assortis.sources.language.javafx.api.gui.FXButton"
    },
    Sample{
        name: "SwingCheckBox"
        className: "assortis.sources.language.javafx.api.gui.FXCheckBox"
    },
    Sample{
        name: "SwingRadioButton"
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
        name: "SwingSlider"
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
        name: "SwingFrame"
        className: "assortis.sources.language.javafx.api.gui.FXFrame"
    },
    ]
}
