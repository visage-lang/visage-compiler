package jfx.assortie.lang;

import jfx.assortie.system.Module;
import jfx.assortie.system.Sample;

Module {
    name: "JavaFX"
    modules: [
    "jfx.assortie.lang.api.Configuration",
    ]
    samples: [
        Sample{
            name: "New"
            className: "jfx.assortie.samples.empty.Empty"
            visible: true
        }
    ]

}
